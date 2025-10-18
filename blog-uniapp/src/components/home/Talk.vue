<template>
  <view v-if="list.length" class="talk-swiper">
    <!-- 消息图标 -->
    <text class="iconfont icon-xiaoxi"></text>

    <!-- 文案 -->
    <text class="talk-text">{{ t('talk') }}</text>

    <!-- 垂直轮播 -->
    <swiper
      class="swiper-box"
      vertical
      circular
      autoplay
      duration="300"
      interval="3000"
      @change="onChange"
    >
      <swiper-item
        v-for="(talk, idx) in list"
        :key="talk.id"
        class="slide-item"
        @tap="toTalk(talk.id)"
      >
        <view class="slide-content">{{ talk.content }}</view>
      </swiper-item>
    </swiper>

    <!-- 右侧箭头动画 -->
    <view class="arrow-box">
      <view class="arrow" />
      <view class="arrow" />
      <view class="arrow" />
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getTopTalkListService } from '@/api/talk'
import type { SimpleTalk } from '@/types/talk'
import { t } from '@/utils/i18n'

/* -------------------- data -------------------- */
const list = ref<SimpleTalk[]>([])

/* -------------------- methods -------------------- */
const getTopTalkList = async () => {
  const res = await getTopTalkListService()
  list.value = res.data || []
}

const toTalk = (id: number) => {
  uni.navigateTo({ url: `/pages/talk/detail?id=${id}` })
}

const onChange = (e: any) => {
  // 如需索引可在这里拿 e.detail.current
}

/* -------------------- life cycle -------------------- */
onLoad(() => {
  getTopTalkList()
})
</script>

<style scoped>
/* 外层 flex 横向排列 */
.talk-swiper {
  display: flex;
  align-items: center;
  margin: 0 20rpx;
  padding: 24rpx 32rpx;
  font-size: 28rpx;
  border-radius: 16rpx;
  background: #fff;
  box-shadow: 0 10rpx 60rpx -30rpx rgba(0, 0, 0, 0.08);
}

.talk-icon {
  margin-right: 8rpx;
}

.talk-text {
  white-space: nowrap;
  margin-right: 16rpx;
  margin-left: 16rpx;
}

/* 轮播区域 */
.swiper-box {
  flex: 1;
  height: 60rpx;
  line-height: 60rpx;
}

.slide-item {
  display: flex;
  align-items: center;
}

.slide-content {
  width: 100%;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  color: #333;
}

/* 右侧箭头 */
.arrow-box {
  display: flex;
  flex-direction: row;
  margin-left: 16rpx;
}

.arrow {
  width: 0;
  height: 0;
  border: 10rpx solid transparent;
  border-left: 10rpx solid #999;
  margin-left: -6rpx;
  animation: passing 1.5s infinite;
}

.arrow:nth-child(2) {
  animation-delay: 0.2s;
}

.arrow:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes passing {
  0% {
    transform: translateX(-50%);
    opacity: 0;
  }
  50% {
    transform: translateX(0);
    opacity: 1;
  }
  100% {
    transform: translateX(50%);
    opacity: 0;
  }
}
</style>