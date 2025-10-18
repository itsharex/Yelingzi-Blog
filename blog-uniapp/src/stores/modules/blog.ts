// stores/blog.ts
import { defineStore } from 'pinia'
import { ref, watch } from 'vue'

/* 静态资源 */
const logo   = 'https://www.yeling.top/image/logo.jpg'
const avatar = '/image/common/images/avatar2.jpg'

interface BlogState {
  nickname: string
  avatar: string
  logo: string
  isDark: boolean
}

export const useBlogStore = defineStore(
  'useBlogStore',
  () => {
    /* 状态 */
    const blogInfo = ref<BlogState>({
      nickname: '叶玲子',
      avatar,
      logo,
      isDark: false,
    })

    const tagId = ref(0)

    /* 持久化 key */
    const STORAGE_KEY = 'blog_theme'

    /* 初始化主题：先从缓存读，再同步到全局导航栏 */
    function loadTheme() {
      try {
        const cache = uni.getStorageSync(STORAGE_KEY)
        if (cache === 'dark' || cache === 'light') {
          blogInfo.value.isDark = cache === 'dark'
        }
      } catch {}
      syncThemeToNative(blogInfo.value.isDark)
    }

    /* 切换主题 */
    function toggleTheme(isDark?: boolean) {
      const newVal = isDark ?? !blogInfo.value.isDark
      blogInfo.value.isDark = newVal
      syncThemeToNative(newVal)
      try {
        uni.setStorageSync(STORAGE_KEY, newVal ? 'dark' : 'light')
      } catch {}
    }

    /* 同步主题到小程序导航栏、TabBar */
    function syncThemeToNative(dark: boolean) {
      /* #ifdef H5 */
      // H5 仍可用 class 方式（可选）
      const html = document.documentElement
      dark ? html.classList.add('dark') : html.classList.remove('dark')
      /* #endif */

      /* #ifndef H5 */
      // 原生小程序导航栏
      uni.setNavigationBarColor({
        frontColor: dark ? '#ffffff' : '#000000',
        backgroundColor: dark ? '#1e1e1e' : '#ffffff',
        animation: { duration: 300 },
      })
      // TabBar 颜色
      uni.setTabBarStyle({
        color: dark ? '#969799' : '#7A7E83',
        selectedColor: dark ? '#f08300' : '#3cc51f',
        backgroundColor: dark ? '#1e1e1e' : '#ffffff',
        borderStyle: dark ? 'white' : 'black',
      })
      /* #endif */
    }


    /* 监听状态变化，落库 */
    watch(
      () => blogInfo.value.isDark,
      (newVal) => toggleTheme(newVal),
      { immediate: true }
    )

    /* 启动时读一次 */
    loadTheme()

    return {
      blogInfo,
      toggleTheme,
      tagId,
    }
  },
  { persist: false } // 小程序端用 uni.setStorageSync 自行控制，不用 pinia 插件
)