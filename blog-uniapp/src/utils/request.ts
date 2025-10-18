// utils/request.ts
import { useUserStore } from '@/stores'
import { APP_BASE_URL } from './config'

/* 全局刷新控制 */
let isRefreshing = false
let requests : ((token : string) => void)[] = []

const BASE_URL = APP_BASE_URL

/**
 * 核心请求函数：返回 Promise<T>
 */
function request<T = any>(options : UniApp.RequestOptions) : Promise<T> {
	return new Promise((resolve, reject) => {
		const user = useUserStore()

		/* 1. 组装 header */
		const header = {
			'Content-Type': 'application/json',
			...(options.header || {}),
		}
		if (user.accessToken) header.Authorization = user.accessToken

		/* 2. 真正发请求 */
		uni.request({
			...options,
			url: /^https?:\/\//.test(options.url!)
				? options.url
				: `${BASE_URL}${options.url}`,
			header,
			success: (res : any) => {

				const { code, data } = res.data as any

				/* 2xx 都算成功 */
				if (code == 2000) {
					resolve(res.data as T)
				} else {
					reject(new RequestError(code, data))
				}

			},
			fail: (err : any) => reject(err),
		})
	})
}

/**
 * 自定义错误类，方便拦截器统一处理
 */
class RequestError extends Error {
	constructor(
		public status : number,
		public data : any,
	) {
		super(data?.msg || data?.message || '请求异常')
	}
}

/**
 * 业务拦截器包装
 */
async function requestWithInterceptor<T = any>(
	options : UniApp.RequestOptions,
) : Promise<T> {
	try {
		return await request<T>(options)
	} catch (err) {
		const user = useUserStore()

		/* 仅处理 RequestError */
		if (!(err instanceof RequestError)) throw err

		const { status, data } = err

		/* 短 token 过期 */
		if (status === 401 && data?.msg === 'accessTokenExpired') {
			if (!user.refreshToken) {
				user.removeToken()
				uni.reLaunch({ url: '/pages/login/index' })
				throw err
			}

			/* 并发控制 */
			if (!isRefreshing) {
				isRefreshing = true
				try {
					const refreshData = await request<{
						data : { accessToken : string }
					}>({
						url: '/api/auth/refresh',
						method: 'POST',
						header: { 'x-refresh-token': user.refreshToken },
					})
					const { accessToken } = refreshData.data
					user.setTokens(accessToken, user.refreshToken)

					/* 重放等待队列 */
					requests.forEach((cb) => cb(accessToken))
					requests = []

					/* 重放当前请求 */
					options.header = {
						...options.header,
						Authorization: accessToken,
					}
					return await request<T>(options)
				} catch (refreshErr : any) {
					/* refresh 也 401 */
					if (refreshErr instanceof RequestError && refreshErr.status === 401) {
						uni.showToast({ title: '登录已过期', icon: 'none' })
						user.removeToken()
						uni.reLaunch({ url: '/pages/login/index' })
					}
					requests.forEach((cb) => cb(''))
					throw refreshErr
				} finally {
					isRefreshing = false
				}
			} else {
				/* 排队 */
				return new Promise<T>((resolve, reject) => {
					requests.push((token : string) => {
						options.header = { ...options.header, Authorization: token }
						request<T>(options).then(resolve).catch(reject)
					})
				})
			}
		}

		/* 其它 401 */
		if (status === 401 && data?.msg === 'accessTokenInvalid') {
			user.removeToken()
			uni.reLaunch({ url: '/pages/login/index' })
			throw err
		}

		/* 统一错误提示 */
		uni.showToast({ title: data?.msg || '服务异常', icon: 'none' })
		throw err
	}
}

/* 导出 GET / POST / upload 等常用语法糖 */
export const http = {
	get<T = any>(url : string, params ?: any, header ?: any) {
		return requestWithInterceptor<T>({ url, method: 'GET', data: params, header })
	},
	post<T = any>(url : string, data ?: any, header ?: any) {
		return requestWithInterceptor<T>({ url, method: 'POST', data, header })
	},
	put<T = any>(url : string, data ?: any, header ?: any) {
		return requestWithInterceptor<T>({ url, method: 'PUT', data, header })
	},
	delete<T = any>(url : string) {
		return requestWithInterceptor<T>({ url, method: 'DELETE' })
	},
	upload<T = any>(
		url : string,
		filePath : string,
		formData ?: any,
		header : AnyObject = {},
		fileType ?: string
	) {
		return new Promise<T>((resolve, reject) => {
			const user = useUserStore()
			uni.uploadFile({
				url: `${BASE_URL}${url}`,
				filePath,
				name: fileType || 'file',        
				formData,
				header: {
					Authorization: user.accessToken || '',
					...header                
				},
				success: (res) => {
					try {
						resolve(JSON.parse(res.data))
					} catch {
						resolve(res.data as T)
					}
				},
				fail: reject
			})
		})
	}
}

export const buildQuery = (url : string, obj : Record<string, any>) => {
	const qs = Object.keys(obj)
		.map(k => `${k}=${encodeURIComponent(obj[k])}`)
		.join('&');
	return qs ? `${url}?${qs}` : url;
}

/* 默认导出兼容 axios 写法的 instance（可选） */
export default {
	request: requestWithInterceptor,
	get: http.get,
	post: http.post,
	put: http.put,
	delete: http.delete,
	upload: http.upload,
}