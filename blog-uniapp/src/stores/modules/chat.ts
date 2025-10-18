import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Chat } from '../../types/chatType'

export const useChatStore = defineStore(
  'useChatStore',
  () => {

    /* ------------ 状态 ------------ */
    const chatList = ref<Chat[]>([
      {
        id: '6', route: 'linkme', chatType: 'single', chatMessageList: [], nickname: '叶玲子(管理员)', avatar: '/images/common/images/avatar2.jpg', unReadChat: 0,
        lastMessage: {
          messageType: '',
          message: '',
          id: 0,
          userId: 0,
          nickname: '',
          userAvatar: '',
          createTime: '',
        }
      },
      {
        id: 'chatroom', route: 'chatroom', chatType: 'group', chatMessageList: [], nickname: '聊天室', avatar: '/images/common/images/chatroom.jpg', unReadChat: 0,
        lastMessage: {
          messageType: '',
          message: '',
          id: 0,
          userId: 0,
          nickname: '',
          userAvatar: '',
          createTime: '',
        }
      }
    ])

    const removeChat = () => {
      chatList.value.splice(0, chatList.value.length)
      chatList.value.push({
        id: '6', route: 'linkme', chatType: 'single', chatMessageList: [], nickname: '叶玲子(管理员)', avatar: '/images/common/images/avatar2.jpg', unReadChat: 0,
        lastMessage: {
          messageType: '',
          message: '',
          id: 0,
          userId: 0,
          nickname: '',
          userAvatar: '',
          createTime: '',
        }
      })
      chatList.value.push({
        id: 'chatroom', route: 'chatroom', chatType: 'group', chatMessageList: [], nickname: '聊天室', avatar: '/images/common/images/chatroom.jpg', unReadChat: 0,
        lastMessage: {
          messageType: '',
          message: '',
          id: 0,
          userId: 0,
          nickname: '',
          userAvatar: '',
          createTime: '',
        }
      })
    }

    return {
      chatList,
      removeChat
    }
  },
  { persist: true }
)
