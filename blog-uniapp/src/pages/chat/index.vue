<template>
	<view class="container">
		<view class="chat-header">
			<view class="chat-header-left">
				<YlAvatar class="chat-avatar" :src="userInfo.userAvatar" />
				<view class="chat-user-info">
					<text class="chat-nickname">{{ userInfo.nickname }}</text>
					<text class="chat-online">在线</text>
				</view>
			</view>
			<view class="chat-header-righet">
				<SvgIcon name="icon-list"></SvgIcon>
			</view>
		</view>
		<view class="chat-list">
			<ChatList />
		</view>

	</view>
</template>

<script setup lang="ts">
import type { Chat, ChatMessage, WsMsg } from '@/types/chatType'
import { reactive, toRefs, watch, ref, computed, nextTick } from 'vue'
import { onLoad, onUnload, onPageScroll } from '@dcloudio/uni-app'
import { useUserStore } from '@/stores'
import { useChatStore } from '@/stores/modules/chat'
import { getChatListService, getNewChatCountByGroupService, getNewChatCountBySingleService } from '@/api/chat'
import ChatList from './list.vue'
import YlAvatar from '@/components/Image/YlAvatar.vue'
import { addWsMessageHandler, removeWsMessageHandler } from '@/utils/websocket'

/* ---------------- stores ---------------- */
const userStore = useUserStore()
const chatStore = useChatStore()
const userInfo = computed(() => userStore.userInfo)



/* ---------------- 工具函数 ---------------- */
function mergeMessages(oldList: ChatMessage[], newList: ChatMessage[]) {
  const map = new Map<number, ChatMessage>()
  oldList.forEach(m => map.set(m.id, m))
  newList.forEach(m => map.set(m.id, m))
  return Array.from(map.values()).sort((a, b) => a.id - b.id)
}

const setDefaultMessage = () => ({
  messageType: 'text',
  message: '',
  id: 0,
  userId: 0,
  nickname: '',
  userAvatar: '',
  createTime: ''
})

const handleMyMessage = () => {
  chatStore.chatList.forEach(chat => {
    chat.chatMessageList = chat.chatMessageList.filter(Boolean)
    chat.lastMessage = chat.lastMessage || setDefaultMessage()
  })
}

/* ---------------- 业务 ---------------- */
const refreshNewCount = async () => {
  let maxSingleId = 0
  let maxGroupId = 0
  chatStore.chatList.forEach(c => {
    const ids = [
      ...c.chatMessageList.map(m => m.id),
      c.lastMessage?.id ?? 0
    ]
    const maxId = Math.max(...ids)
    if (c.chatType === 'single' && maxId > maxSingleId) maxSingleId = maxId
    if (c.chatType === 'group' && maxId > maxGroupId) maxGroupId = maxId
  })
  const [singleChatData, groupChatData] = await Promise.all([
    getNewChatCountBySingleService({ device: userStore.deviceId, id: maxSingleId }),
    getNewChatCountByGroupService({ device: userStore.deviceId, id: maxGroupId })
  ])
  for (const brief of singleChatData.data) {
    const exist = chatStore.chatList.find(c => c.id === brief.info.id && c.chatType === brief.info.type)
    if (exist && brief.messages && brief.messages.length > 0) {
      exist.chatMessageList = [...exist.chatMessageList, ...brief.messages]
      exist.unReadChat += brief.messages.length
      if (brief.messages[brief.messages.length - 1].id > (exist.lastMessage?.id || 0)) {
        exist.lastMessage = brief.messages[brief.messages.length - 1]
      }
    }
  }
  for (const brief of groupChatData.data) {
    const exist = chatStore.chatList.find(c => c.id === brief.info.id && c.chatType === 'group')
    if (exist) {
      exist.unReadChat += brief.count
      if (brief.lastMessage && brief.lastMessage.id > (exist.lastMessage?.id || 0)) {
        exist.lastMessage = brief.lastMessage
      }
    }
  }
}

function onWsMessage({ receiver, message }: WsMsg) {
  const target = chatStore.chatList.find(c => c.id === receiver)
  if (!target) return
  if (target.chatMessageList.some(m => m.id === message.id)) return 

  target.lastMessage = message
  target.unReadChat++
  
  if(target.lastMessage.id === target.chatMessageList.at(-1)?.id){
  	  target.chatMessageList.push(message)
  }
}

/* ---------------- 生命周期 ---------------- */
onLoad(() => {
  addWsMessageHandler('chat', onWsMessage)
  handleMyMessage()
  refreshNewCount()

})

onUnload(() => {
  removeWsMessageHandler('chat', onWsMessage)
})

/* 如果实现了 scroll-view 的自定义下拉，可在此触发 onLoadMore */
onPageScroll(() => {
  /* 空着，按需实现 */
})
</script>

<style lang="scss" scoped>
/* ========== 公共变量 ========== */
$avatar-size: 80rpx;
$header-height: 96rpx;
$gap-avatar-text: 16rpx;
$color-text: #333;

/* ========== 基础布局（H5 / App / 小程序通用） ========== */
.container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f5f5f5;
}

.chat-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: $header-height;
  padding: 30rpx 32rpx;
  background-color: rgb(235.9,245.3,255);
  border-bottom: 1rpx solid #e6e6e6;
}

.chat-header-left {
  display: flex;
  align-items: center;
  gap: $gap-avatar-text;
}


.chat-avatar {
  width: $avatar-size;
  height: $avatar-size;
  border-radius: 50%; 
  flex-shrink: 0;
}

.chat-user-info {
  display: flex;
  flex-direction: column;
}
.chat-nickname {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  flex: 1;
}

.chat-online{
	font-size: 24rpx;
	color: #444;
}

.chat-header-righet {

  min-width: 60rpx;
}

.chat-list {
  flex: 1; 
  overflow: hidden;
  padding-bottom: env(safe-area-inset-bottom); /* 底部安全区 */
}
</style>