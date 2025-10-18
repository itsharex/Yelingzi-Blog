<template>
  <view class="bg-wrap">
    <!-- 正常图 -->
    <image
      v-if="!isError"
      class="img puffIn"
      :src="getImageUrl(src)"
      mode="aspectFill"
      @error="onError"
    />

    <!-- 兜底图 -->
    <ImageWithFallback
      v-else
      class="img fallback"
      :src="fallback"
      mode="aspectFill"
    />
  </view>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { getImageUrl } from '@/utils/image'
import ImageWithFallback from './ImageWithFallback.vue'


const props = defineProps({
  src: { type: String, required: true },
  fallback: {
    type: String,
    default: '/image/common/images/404.gif'
  }
})

const isError = ref(false)

function onError() {
  isError.value = true
}
</script>

<style>
.bg-wrap {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 350rpx;     
  z-index: -2;          
  overflow: hidden;
}

.img {
  width: 100%;
  height: 100%;
  pointer-events: none;
  animation-duration: 1s;
  animation-fill-mode: both;
}

.fallback {
  filter: grayscale(80%) blur(1px);
  opacity: 0.9;
}

/* 动画 */
@keyframes puffIn {
  0% {
    opacity: 0;
    transform: scale(2, 2);
    filter: blur(2px);
  }
  100% {
    opacity: 1;
    transform: scale(1, 1);
    filter: blur(0);
  }
}

.puffIn {
  animation-name: puffIn;
}
</style>