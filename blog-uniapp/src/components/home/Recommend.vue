<template>
  <view class="album-swiper" v-if="list.length">
    <!-- 小程序原生轮播 -->
    <swiper
      class="swiper-box"
      indicator-dots
      indicator-active-color="#fff"
      autoplay
      circular
      interval="4000"
      duration="300"
    >
      <swiper-item
        v-for="item in list"
        :key="item.id"
        class="slide-item"
        @tap="toAlbum(item.id)"
      >
        <!-- 背景图 -->
        <image class="bg-img" :src="getImageUrl(item.albumCover)" mode="aspectFill" />
        <!-- 遮罩 -->
        <view class="mask" />
        <!-- 文字 -->
        <view class="slide-content">
          <text class="slide-title">{{ item.albumName }}</text>
          <text class="slide-desc">简介：{{ item.albumDesc }}</text>
        </view>

        <!-- 左上角角标 -->
        <view class="corner">相册</view>
      </swiper-item>
    </swiper>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getSimpleAlbumOfPhotoCountService } from '@/api/album'
import type { Album } from '@/types/album'
import { getImageUrl } from '@/utils/image'

const list = ref<Album[]>([])

const getList = async () => {
  const res = await getSimpleAlbumOfPhotoCountService()
  list.value = res.data || []
}

const toAlbum = (id: number) => {
  uni.navigateTo({ url: `/pages/album/detail?id=${id}` })
}

onLoad(() => {
  getList()
})
</script>

<style scoped>
.album-swiper {
  margin: 32rpx 16rpx;
  border-radius: 24rpx;
  overflow: hidden;
}

.swiper-box {
  width: 100%;
  height: 422rpx; /* 16:9 近似值 */
  border-radius: 24rpx;
}

.slide-item {
  position: relative;
  width: 100%;
  height: 100%;
}

.bg-img {
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
}

.mask {
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.35);
}

.slide-content {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #fff;
  z-index: 1;
}

.slide-title {
  font-size: 64rpx;
  margin-bottom: 8rpx;
}

.slide-desc {
  font-size: 28rpx;
}

.corner {
  position: absolute;
  left: 0;
  top: 0;
  background: linear-gradient(90deg, #ffd100, #ff8a00);
  color: #fff;
  font-size: 30rpx;
  letter-spacing: 6rpx;
  width: 130rpx;
  height: 56rpx;
  line-height: 56rpx;
  text-align: center;
  border-radius: 0 0 24rpx 0;
}
</style>