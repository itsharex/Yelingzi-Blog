<template>
  <view class="container">
    <!-- 图片 -->
    <view class="image-box">
      <image
        class="carousel-image"
        src="https://www/yeling.top/images/common/images/404.jpg"
        mode="widthFix"
      />
    </view>

    <!-- 文字 -->
    <view class="text-box">
      <text class="title">页面找不到了!!!</text>
    </view>

    <!-- 按钮 -->
    <view class="text-box">
      <button class="home-btn" @tap="goHome">返回首页</button>
    </view>
  </view>
</template>

<script setup lang="ts">
/* 返回首页 */
const goHome = () => {
  uni.switchTab({ url: '/pages/index/index' });   // 如果首页是 tabBar 页面
  // uni.reLaunch({ url: '/pages/index/index' }); // 如不是 tabBar，用 reLaunch
}
</script>

<style lang="scss" scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  text-align: center;
}

.image-box {
  margin-bottom: 40rpx;
}

.carousel-image {
  width: 600rpx;          /* 按需调整 */
  height: auto;
}

.text-box {
  margin-bottom: 40rpx;
}

.title {
  font-size: 36rpx;
  color: #333;
}

.home-btn {
  width: 320rpx;
  height: 80rpx;
  line-height: 80rpx;
  font-size: 30rpx;
  color: #fff;
  background-color: #007aff;
  border-radius: 10rpx;
}
</style>