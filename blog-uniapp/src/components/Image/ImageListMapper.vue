<template>
  <view class="image-list-mapper">
    <view v-for="(value, key) in filteredMap" :key="key" class="image-item pointer" @tap="handleClick(key)">			
		<ImageWithFallback :lazy="false" :src="value" :preview="false" class="image-preview" mode="aspectFit" />

    </view>
  </view>
</template>

<script setup lang="ts">
import { computed, defineEmits, onMounted } from 'vue';
import { textToImageMap } from '@/utils/mapping';
import ImageWithFallback from './ImageWithFallback.vue';
import { APP_BASE_URL } from '@/utils/config';

const emit = defineEmits<{
  (e: 'select', value: string): void;
}>()

const basePath = `${APP_BASE_URL}/images/emoji/`;

const filteredMap = computed(() => {
  const result: Record<string, string> = {};

  Object.entries(textToImageMap).forEach(([key, value]) => {
    if (key && value && key.trim() && value.trim()) {
      result[key] = `${basePath}${value}.jpg`
    }
  });

  return result;
});


// 点击处理
const handleClick = (key: string): void => {
  emit('select', key);
};
onMounted(() => {

});
</script>

<style scoped>
.image-list-mapper {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120rpx, 1fr));
  gap: 16rpx;
}

.image-item {
  transition: all 0.2s ease-in-out;
  width: 120rpx;
  height: 120rpx;
}

.image-item:hover {
  transform: scale(1.05);
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
}

.image-preview {
  width: 100%;
  height: 100%;
}

</style>
