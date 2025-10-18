<template>
  <view class="custom-image-container">
    <!-- 真实图片 -->
    <image
       v-show="status === 'loaded'"
      class="img"
      :src="path"
      :mode="props.mode"
      :lazy-load="props.lazy"
      @load="onLoad"
      @error="onError"
	  @tap="onPreview"
	  :style="isbindload ? { width: finalW + 'px', height: finalH + 'px' } : {}"
    />

    <!-- 加载中 -->
    <view v-if="status === 'loading'" class="placeholder">
      <div class="image-slot">Loading<span class="dot">...</span></div>
    </view>

    <!-- 加载失败 -->
    <view v-else-if="status === 'error'" class="placeholder">
      <image class="placeholder-img" :src="props.fallback" mode="aspectFill" />
    </view>
  </view>
</template>

<script lang="ts" setup>
import { computed, onMounted, ref, watch, type PropType } from 'vue';
import { getImageUrl, getImageUrls } from '@/utils/image'
const props = defineProps({
  src:       { type: String, required: true },
  list:      { type: Array as PropType<string[]>, default: () => [] },
  fallback:  { type: String, default: 'https://www.yeling.top/image/common/images/404.gif' },
  lazy:      { type: Boolean, default: true },
  alt:       { type: String, default: 'image' },
  preview:   { type: Boolean, default: true },
  mode:   	 { type: String, default: 'aspectFill' },
  isbindload:{ type: Boolean, default: false },
  maxwidth:  { type: Number, default: 1000 },
  maxheight: { type: Number, default: 1000 }
});
const path =  getImageUrl(props.src)
const status = ref('loading');   // loading | loaded | error
const emit = defineEmits<{ onload: [] }>()
const finalW = ref(0)
const finalH = ref(0)
// 加载成功
const onLoad = (event: any) => {
  status.value = 'loaded'
  if(!props.isbindload) return
  console.log(event.target.width + '：' + event.target.height)
  const { width, height } = event.target
  const maxW = props.maxwidth
  const maxH = props.maxheight
  const scale = Math.min(maxW / width, maxH / height, 1)
  finalW.value = width * scale
  finalH.value = height * scale
  emit('onload')
}
// 加载失败
const onError = () => {
  status.value = 'error'
}

function onPreview() {
  if (status.value !== 'loaded' || !props.preview) return
  uni.previewImage({
    urls: props.list ? getImageUrls(props.list) : [getImageUrl(props.src)],
    current: props.src, 
	fail: err => console.error('预览失败:', err)
  })
}

// 当外部 src 变化后重置状态
watch(
  () => props.src,
  (newVal, oldVal) => {
    const isValid = (v: any) => typeof v === 'string' && v.trim() !== '';
    // 第一次进来就有效，也要重新走 loading → loaded 流程
    if ((oldVal === undefined && isValid(newVal)) || (!isValid(oldVal) && isValid(newVal))) {
      status.value = 'loading';
    }
  },
  { immediate: true }
);
</script>

<style >
.custom-image-container {
  width: 100%;
  height: 100%;
  position: relative;
}

.img {
  width: 100%;
  height: 100%;
  display: block !important;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  font-size: 14px;
}

.dot {
  animation: dot 2s infinite steps(3, start);
  overflow: hidden;
}

.placeholder {
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  position: absolute;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
}

.placeholder-img {
  width: 100%;
  height: 100%;
}
</style>