<template>
  <view class="page">
    <!-- 背景图组件 -->
    <BgImageVue :src="bgImg" :duration="1000" />

    <!-- 品牌区 -->
    <view ref="brandRef" class="brand-container">
      <view class="brand">
        <text class="artboard">{{ title }}</text>
      </view>

      <!-- 波浪 -->
      <WaveVue />
    </view>
  </view>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import BgImageVue from '../Image/BgImage.vue';
import WaveVue from '../Waves/Wave.vue';

// 接收页面参数（或父组件传参）
const props = defineProps({
  title: { type: String, default: '默认标题' },
  bgImg: { type: String, default: '' }
})

const brandRef = ref(null)
</script>

<style>
/* 简单重置 */
page {
  background-color: #f5f5f5;
}

.brand-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: relative;
  width: 100%;
  height: 400rpx;          /* 小程序用 rpx 代替 vh */
  min-height: 200rpx;
  color: #ff5777;           /* 原来的 var(--color-pink) */
  overflow-x: hidden;
}

.brand {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: fixed;
  z-index: -1;
  pointer-events: none;
}

.artboard {
  font-size: 80rpx;
  line-height: 1.2;
  animation: titleScale 1s;
}

/* 箭头 / 底部多余样式可继续保留，这里省略 */

@keyframes titleScale {
  0% {
    transform: scale(0.8);
    opacity: 0;
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}
</style>