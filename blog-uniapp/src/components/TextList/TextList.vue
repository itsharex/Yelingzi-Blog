<template>
  <view class="message-container">
    <view class="header">
      <text class="title">{{ title }}</text>
      <text v-if="isCopy" class="copy-btn" @tap="copyItems">{{ t('copy') }}</text>
    </view>

    <view class="main">
      <view class="blockquote">
        <text
          v-for="(item, index) in items"
          :key="index"
          class="item"
          :selectable="isCopy"  
        >
          {{ item }}
        </text>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { defineProps } from 'vue'
import { t } from '@/utils/i18n'

/* props 定义 */
const props = defineProps<{
  items: string[]
  title: string
  isCopy?: boolean
}>()

/* 复制逻辑 */
const copyItems = () => {
  const text = props.items.join('\n')
  uni.setClipboardData({
    data: text,
    success: () => {
      uni.showToast({ title: '已复制到剪贴板', icon: 'success' })
    },
    fail: () => {
      uni.showToast({ title: '复制失败，请手动选择', icon: 'none' })
    }
  })
}
</script>

<style lang="scss">
/* 注意：scoped 在小程序无效，直接写类名即可 */
.message-container {
  padding: 0 30rpx;
}

.title{
	color: #ed6ea0;
}

.header {
  margin-top: 40rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 40rpx;
  font-weight: bold;

  .copy-btn {
    padding: 10rpx 24rpx;
    font-size: 28rpx;
    color: #ed6ea0;
    background: #ffe6fa;
    border-radius: 8rpx;
    transition: all 0.3s;

    &:active {
      transform: scale(0.95);
      background: #ed6ea0;
      color: #fff;
    }
  }
}

.main {
  margin: 30rpx 0;
  padding: 20rpx 30rpx;
  border-left: 6rpx solid #ed6ea0;
  background-color: #ffe6fa;
  border-radius: 8rpx;
}

.blockquote {
  line-height: 2;
  color: #000;

  .item {
    display: block;   /* 每条占一行 */
  }
}
</style>