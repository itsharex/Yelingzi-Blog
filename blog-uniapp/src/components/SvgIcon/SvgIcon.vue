<template>
  <text
    class="iconfont"
    :class="iconClass"
    :style="{ color: color, fontSize: sizeWithUnit }"
  />
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps({
  name: { type: String, required: true },
  color: { type: String, default: 'currentColor' },
  size: { type: [String, Number], default: '1em' }
})

/* 1. 图标类名 */
const iconClass = computed(() => `${props.name}`)

/* 2. 尺寸自动补 rpx */
const sizeWithUnit = computed(() => {
  const s = props.size
  if (typeof s === 'number') return s + 'rpx'
  if (typeof s === 'string') {
    // 已带单位（px、rpx、upx、%、em、rem…）直接返回
    if (/^\d+(px|rpx|upx|%|em|rem)$/i.test(s)) return s
    // 纯数字字符串
    if (/^\d+$/.test(s)) return s + 'rpx'
  }
  return s // 其他情况原样返回
})
</script>

<style scoped>
.iconfont {
  display: inline-block;
  font-style: normal;
  line-height: 1;
  vertical-align: middle;
}
</style>