// utils/storage.ts
const isBrowser = typeof window !== 'undefined' && !!window.localStorage

export const storage = {
  getItem(key: string): string | null {
    if (isBrowser) return localStorage.getItem(key)
    // @ts-ignore
    if (typeof wx !== 'undefined' && wx.getStorageSync) {
      // @ts-ignore
      return wx.getStorageSync(key) || null
    }
    return null
  },
  setItem(key: string, value: string): void {
    if (isBrowser) return localStorage.setItem(key, value)
    // @ts-ignore
    if (typeof wx !== 'undefined' && wx.setStorageSync) {
      // @ts-ignore
      wx.setStorageSync(key, value)
    }
  },
}