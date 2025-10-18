<template>
  <view class="history-panel">
    <!-- 顶部 Logo + 图标 -->
    <view class="history-header">
      <ImageWithFallback class="logo" src="/image/logo.jpg" />
      <SvgIcon name="icon-sidebarcebianlan" size="72"  @tap="onHideHistory" />
    </view>

    <!-- 开启新对话 -->
    <button class="new-btn" @tap="onPushChaiAI">
	  <SvgIcon class="btn-icon" name="icon-jia" size="32"></SvgIcon>
      <text>开启新对话</text>
    </button>

    <!-- 历史会话标题 -->
    <view class="sub-title">
	  <SvgIcon class="btn-icon" name="icon-jishi" size="28"></SvgIcon>
      <text>历史会话</text>
    </view>

    <!-- 历史列表 -->
    <scroll-view class="history-list" scroll-y>
      <view
        v-for="item in sessionList"
        :key="item.sessionId"
        class="history-item"
        :class="{ active: item.sessionId === sessionId }"
        hover-class="item-hover"
        @tap="onPushSession(item.sessionId)"
      >
        <text>{{ item.description }}</text>
      </view>
    </scroll-view>
  </view>
</template>

<script lang="ts" setup>
import { defineProps, defineEmits } from 'vue'
import type { PropType } from 'vue'
import ImageWithFallback from '@/components/Image/ImageWithFallback.vue';

interface SessionHistoryView {
  sessionId: string
  description: string
}

const props = defineProps({
  sessionList: {
    type: Array as PropType<SessionHistoryView[]>,
    required: true
  },
  sessionId: {
    type: String,
    required: true
  }
})

const emit = defineEmits<{
  hideHistory: [],
  updateSessionId: [ sessionId: string]
}>()

function onPushSession(sessionId: string) {
  console.log(sessionId)
  emit('updateSessionId', sessionId)
}

function onPushChaiAI() {
  emit('updateSessionId', '')
}

function onHideHistory() {
	console.log('子组件')
  emit('hideHistory')
}
</script>

<style lang="scss" scoped>
.history-panel {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 32rpx;
  box-sizing: border-box;
  font-size: 28rpx;
  color: #343434;
  background: #fafafa;
}

.history-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 40rpx;
  .logo {
    width: 72rpx;
    height: 72rpx;
    border-radius: 12rpx;
  }
  .header-icon {
    width: 40rpx;
    height: 40rpx;
  }
}

.new-btn {
  width: 100%;
  height: 80rpx;
  border: 1rpx solid #dcdfe6;
  border-radius: 24rpx;
  background: #f5f7fa;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 12rpx;
  font-size: 32rpx;
  color: #606266;
}

.sub-title {
  margin: 40rpx 0 24rpx;
  display: flex;
  align-items: center;
  gap: 12rpx;
  font-weight: 500;
}

.history-list {
  flex: 1;
  overflow: hidden;
  .history-item {
    padding: 20rpx 24rpx;
    margin-bottom: 16rpx;
    border-radius: 12rpx;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    background: #fff;
    &.active {
      background: #aee1ff;
      color: #fff;
    }
  }
  .item-hover {
    background: #ecf5ff;
    color: #409eff;
  }
}
</style>