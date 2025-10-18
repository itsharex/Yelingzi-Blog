<template>
  <view class="layout-warp">
    <!-- 顶部进度条 -->
   <ProgressBar
      :disabledSmooth="disabledSmooth"
	      :scroll-top="scrollTop"
	      :scroll-height="scrollHeight"
      @update:disabledSmooth="updateDisabledSmooth"
    />

    <!-- 内容区 -->
    <view class="main">
      <slot />
    </view>

  </view>

  
</template>

<script lang="ts" setup>
/* -------------------- 1. 依赖引入 -------------------- */
import { ref, onMounted } from 'vue'
import ProgressBar from '@/components/Progress/ProgressBar.vue'
import { userInfoService, viewService } from '@/api/login'
import { onShow } from '@dcloudio/uni-app'

const props = defineProps({
  scrollTop: { type: Number, default: 0 },
  scrollHeight: { type: Number, default: 1 }
})

const disabledSmooth = ref(true) 

/* 每次进入页面（含返回）都滚到顶部 */
onShow(() => {
  uni.pageScrollTo({ scrollTop: 0, duration: 300 })
})

const updateDisabledSmooth = (val: boolean) => {
  disabledSmooth.value = val
}
</script>

<style lang="scss" scoped>
.layout-warp {
  width: 100%;
  -webkit-overflow-scrolling: touch;
}
.footer {
  width: 100%;
  overflow-x: hidden;
}
</style>