// main.js
import { createSSRApp } from 'vue'
import { createPinia } from 'pinia'
import { type StorageLike, createPersistedState } from 'pinia-plugin-persistedstate'
import App from './App.vue'
import directive from './utils/directive'
import { useI18nStore } from './stores'
import '@/static/styles/index.scss'
import '@/static/iconfont/iconfont.css'


export function createApp() {
  const app = createSSRApp(App)

  // Pinia 配置
  const pinia = createPinia()
const wechatStorage: StorageLike = {
  getItem(key: string) {
    return uni.getStorageSync(key) ?? null
  },
  setItem(key: string, value: string) {
    uni.setStorageSync(key, value)
  }
  // 不要写 removeItem
}

pinia.use(createPersistedState({
  storage: wechatStorage,
  auto: true
}))

  app.use(pinia)
  directive(app)

  // 初始化语言
  const i18nStore = useI18nStore()
  i18nStore.initAppLang()

  return {
    app
  }
}