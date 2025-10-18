<template>
  <view class="reply-box">
    <view class="chat-input-contain">
      <!-- 关键：style.height 绑定计算出的 px 值 -->
      <textarea
        v-model="content"
        class="input-textarea"
        :show-confirm-bar="false"
        auto-height
		adjust-position
      />
      <view>
        <button
          class="send-btn"
          :class="{ active: content.length > 0 }"
          @tap="send"
        >
          发送
        </button>
      </view>
    </view>
  </view>
</template>

<script lang="ts" setup>
import { ref, nextTick, getCurrentInstance } from 'vue'

const content = ref('')
const textareaHeight = ref(40) // 初始 1 行高度（单位 px）
const ins = getCurrentInstance()

/* 发送 */
const emit = defineEmits<{
  send: [text: string]
}>()

const send = () => {
  const text = content.value.trim()
  if (!text) return
  emit('send', text)
  content.value = ''
}
</script>

<style lang="scss">
.reply-box {
  display: flex;
  flex-direction: column;
}
.chat-input-contain {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  gap: 30rpx;
  width: 100%;
  box-sizing: border-box;
}

.input-textarea {
  min-height: 40px;
  border-radius: 16rpx;
  background-color: #fff;
  font-size: 28rpx;
  padding: 0 10rpx;
  box-sizing: border-box;
  flex: 1;
  overflow-y: auto; 
  max-height: 240rpx;
}

.send-btn {
  border-radius: 16rpx;
  width: 150rpx;
  height: 70rpx;
  background: #98d0ff;
  color: #e2e2e2;
  font-size: 28rpx;
  line-height: 70rpx;
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
}
.send-btn.active {
  background: #61cdff;
  color: #fff;
}
</style>