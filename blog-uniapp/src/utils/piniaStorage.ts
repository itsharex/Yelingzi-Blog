import { storage } from './storage'

export const piniaStorage = {
  getItem(key: string) {
    return storage.getItem(key)
  },
  setItem(key: string, value: string) {
    storage.setItem(key, value)
  },
}