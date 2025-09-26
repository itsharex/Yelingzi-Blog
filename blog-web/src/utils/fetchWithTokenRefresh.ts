// utils/fetchWithTokenRefresh.ts
import { useRouter } from 'vue-router'
import { refreshToken } from './refreshToken'
import { useUserStore } from '@/stores'
import { ElMessage } from 'element-plus'

const router = useRouter()
export type FetchFunction = () => Promise<Response>

/**
 * 包装 fetch 请求，自动处理 token 失效并重试
 * @param fetchFn 实际的 fetch 请求函数
 */
export async function fetchWithTokenRefresh(fetchFn: FetchFunction): Promise<Response> {
  const response = await fetchFn()

  // 如果 token 失效，尝试刷新
  if (response.status === 401) {
    const errorData = await response.json().catch(() => ({}))
    if (errorData.msg === 'accessTokenExpired') {
      const newToken = await refreshToken()
      if (newToken) {
        // 用新 token 重试一次
        const userStore = useUserStore()
        const retryResponse = await fetchFn()
        return retryResponse
      } else {
        throw new Error('登录已过期')
      }
    } else {
      const userStore = useUserStore()
      userStore.removeToken()
      ElMessage.error('用户身份已过期，请重新登录')
      router.push('/login')
    }
  }

  return response
}
