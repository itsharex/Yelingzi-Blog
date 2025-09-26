// utils/refreshToken.ts
import { useUserStore } from '@/stores'
import { ElMessage } from 'element-plus'
import router from '@/router'

/**
 * 刷新 Token 的通用逻辑
 */
export async function refreshToken(): Promise<string | null> {
  const userStore = useUserStore()
  const refreshToken = userStore.refreshToken

  if (!refreshToken) {
    userStore.removeToken()
    router.push('/login')
    return null
  }

  try {
    const refreshRes = await fetch(new URL('/api/auth/refresh', import.meta.env.VITE_API_BASE_URL).toString(), {
      method: 'POST',
      headers: {
        'x-refresh-token': refreshToken
      }
    })

    if (!refreshRes.ok) {
      throw new Error('刷新 Token 失败')
    }

    const refreshData = await refreshRes.json()
    const newAccessToken = refreshData.data?.accessToken

    if (newAccessToken) {
      userStore.setTokens(newAccessToken, refreshToken)
      return newAccessToken
    } else {
      userStore.removeToken()
      router.push('/login')
      return null
    }
  } catch (err) {
    ElMessage.error('网络异常，请重新登录')
    userStore.removeToken()
    router.push('/login')
    return null
  }
}
