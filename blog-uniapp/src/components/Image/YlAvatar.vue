<template>
  <view
    class="avatar"
    :style="{ width: sizePx, height: sizePx, borderRadius: sizePx }"
  >
    <!-- 网络头像 -->
    <image
      v-if="showImg"
      class="img"
      :src="getImageUrl(displaySrc)"
      mode="aspectFill"
      @error="onError"
    />

    <!-- 兜底图标 -->
    <text v-else class="iconfont icon-geren" :style="{ fontSize: sizePx }"></text>
  </view>
</template>

<script lang="ts" setup>
import { ref, watch, computed } from 'vue';
import { getImageUrl } from '@/utils/image'

const props = defineProps({
  src:  { type: String, default: '' },
  size: { type: Number, default: 80 }          // rpx
});

const sizePx = computed(() => props.size + 'rpx');

/* 失败标记 */
const isError = ref(false);

/* 显示图片条件 */
const showImg = computed(() => !isError.value && displaySrc.value);

/* 兜底地址 */
const displaySrc = ref(props.src || 'https://www.yeling.top/image/common/images/avatar.jpg');

watch(() => props.src, (val) => {
  isError.value = false;
  displaySrc.value = val || 'https://www.yeling.top/image/common/images/avatar.jpg';
});

const onError = () => {
  isError.value = true;
};
</script>

<style lang="scss" scoped>
.avatar {
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  background: #f5f5f5;
}

.img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
}

/* 使用 uni-icons 或自定义字体图标均可 */
.icon-geren {
  color: #ccc;
}
</style>