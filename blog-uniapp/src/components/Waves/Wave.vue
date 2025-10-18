<template>
  <view class="wave-box">
    <!-- 浅层浪 -->
    <view class="wave wave1" :style="{backgroundImage:`url(${wave1})`}" />
    <!-- 深层浪 -->
    <view class="wave wave2" :style="{backgroundImage:`url(${wave2})`}" />
  </view>
</template>

<script setup lang="ts">
// 图片路径用import，打包后不会丢
import wave1 from '@/static/images/wave1.png'
import wave2 from '@/static/images/wave2.png'
</script>

<style scoped>
.wave-box{
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 200rpx;          /* 可自行调整 */
  overflow: hidden;
  z-index: 1;
}
.wave{
  position: absolute;
  left: 0;
  bottom: 0;
  width: 200%;              /* 关键：两倍宽，用来循环 */
  height: 100%;
  background-repeat: repeat-x;
  background-size: 50% 100%;/* 一张图占50%，这样左右拼接无缝 */
  animation: move linear infinite;
}
.wave1{
  animation-duration: 8s;
  opacity: 0.6;
}
.wave2{
  animation-duration: 12s;
  opacity: 0.9;
  transform: translateX(-25%);/* 让两层错开 */
}
@keyframes move{
  0%  { transform: translateX(0); }
  100%{ transform: translateX(-50%); }/* 刚好一张图宽度 */
}
</style>