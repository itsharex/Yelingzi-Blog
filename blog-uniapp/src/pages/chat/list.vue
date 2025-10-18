<template>
  <view v-for="(chat, index) in chatlist" :key="index" class="chat-list" @click="pushChating(chat)">
    
    <view class="avatar-wrapper">
      <YlAvatar class="avatar" :src="chat.avatar" mode="aspectFill" />
      <view v-if="chat.unReadChat > 0" class="unread-badge">
        {{ chat.unReadChat < 100 ? chat.unReadChat : '99+' }}
      </view>
    </view>

    <view class="chat-content">
      <view class="chat-info">
        <text class="chat-name">{{ chat.nickname }}</text>
        <text v-if="chat.lastMessage.createTime !== ''" class="chat-time">
          {{ formatChatListDisplayTime(chat.lastMessage.createTime) }}
        </text>
      </view>

      <view class="message-preview">
        <view class="message-content">
          <block v-if="chat.lastMessage">
            <text v-if="chat.lastMessage.nickname !== '' && chat.chatType === 'group'" class="sender-name">
              {{ chat.lastMessage.nickname }}:
            </text>
            <text v-if="chat.lastMessage.messageType === 'text'">{{ chat.lastMessage.message }}</text>
            <text v-else-if="chat.lastMessage.messageType === 'image'">[图片]</text>
            <text v-else-if="chat.lastMessage.messageType === 'file'">[文件]</text>
            <text v-else-if="chat.lastMessage.messageType === 'emoji'">[表情]</text>
          </block>
        </view>
      </view>
    </view>
  </view>
</template>

<script lang="ts" setup>
import { computed, defineProps } from 'vue'
import { useChatStore } from '@/stores/modules/chat'
import YlAvatar from '@/components/Image/YlAvatar.vue'
import { formatChatListDisplayTime } from '@/utils/common'
import type { Chat, WsMsg } from '@/types/chatType'
import { onLoad, onUnload } from '@dcloudio/uni-app'
import { addWsMessageHandler, removeWsMessageHandler } from '@/utils/websocket'


const chatStore = useChatStore()

const chatlist = computed(() => chatStore.chatList)

const pushChating = (chat: Chat) => {
  uni.navigateTo({
    url: `/pages/chat/index?chatType=${chat.route}`
  })
}

</script>

<style lang="scss" scoped>
.chat-list {
  display: flex;
  align-items: center;
  padding: 16rpx 24rpx;
  background-color: #fff;
  border-bottom: 1rpx solid #f0f0f0;

}

.avatar-wrapper {
  position: relative;
  margin-right: 20rpx;
}

.avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
}

.unread-badge {
  position: absolute;
  top: -8rpx;
  right: -8rpx;
  background-color: #ff4d4f;
  color: #fff;
  font-size: 20rpx;
  padding: 2rpx 8rpx;
  border-radius: 50%;
  min-width: 32rpx;
  text-align: center;
  line-height: 1;
}

.chat-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.chat-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8rpx;
}

.chat-name {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  flex: 1;
}

.chat-time {
  font-size: 24rpx;
  color: #999;
  margin-left: 16rpx;
}

.message-preview {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.message-content {
  font-size: 26rpx;
  color: #666;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  flex: 1;
}

.sender-name {
  color: #555;
  font-weight: 500;
}
</style>