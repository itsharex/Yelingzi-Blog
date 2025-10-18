<template>
  <view class="imgs">
    <image
      v-for="(item, i) in carouselList"
      :key="item.id"
      :src="getImageUrl(item.url)"
      mode="aspectFill"
      class="item"
      :style="{ animationDelay: i * 6 + 's' }"
    />
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getBgListService } from '@/api/album'
import { getImageUrl } from '@/utils/image'

interface Carousel { id: number; url: string }
const carouselList = ref<Carousel[]>([])

onLoad(async () => {
  const res = await getBgListService()
  carouselList.value = res.data || []
})
</script>

<style scoped>
.imgs {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 70vh;
  z-index: -9;
  background: #363636;
  pointer-events: none;
}

.item {
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  opacity: 0;
  animation: imageAnimation 30s linear infinite;
}

@keyframes imageAnimation {
  0%   { opacity: 0; }
  2%   { opacity: 1; }
  8%   { opacity: 1; transform: scale(1.05); }
  17%  { opacity: 1; transform: scale(1.1); }
  25%  { opacity: 0; transform: scale(1.1); }
  100% { opacity: 0; }
}
</style>