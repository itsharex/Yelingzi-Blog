<!-- components/custom-hr/custom-hr.vue -->
<template>
  <view
    class="custom-hr"
    :style="{ marginTop: margin, marginBottom: margin }"
  >
    <!-- 横线 -->
    <view class="line" :style="{ borderColor: color }">
      <!-- 小符号 -->
      <text
        class="symbol"
        :style="{ left: symbolLeft + '%', color: color }"
        @tap="onTap"
      >
        {{ showSymbol }}
      </text>
    </view>

    <!-- 右侧文字 -->
    <text class="hr-text" :style="{ color: color }">{{ content }}</text>
  </view>
</template>

<script lang="ts" setup>
import { ref } from 'vue'

/* 小程序 pages.json 里需打开 "usingComponents": true 且使用 Vue3 编译模式 */
const props = defineProps({
  margin: { type: String, default: '40px' },
  symbol: { type: String, default: '1' },
  content: { type: String, default: '' },
  color:   { type: String, default: '#409eff' }   /* 默认用 uni-blue */
})

/* 符号映射表 */
const symbolMap = {
  '1': '❄',
  '2': '❀',
  '3': '☯',
  '4': '♕',
  '5': '𝄞'
}

/* 当前要显示的符号 */
const showSymbol = symbolMap[props.symbol] || symbolMap['1']

/* 符号距离左边的百分比，点击后滑到 95% */
let symbolLeft = ref(5)
function onTap() {
  symbolLeft.value = 95
}
</script>

<style scoped>
/* 最外层 */
.custom-hr {
  display: flex;
  flex-direction: column;
  padding: 0 20rpx;
}

/* 横线容器 */
.line {
  position: relative;
  height: 2rpx;
  border-style: dashed;
  border-width: 2rpx 0 0 0;
  margin: 20rpx 0;
}

/* 小符号 */
.symbol {
  position: absolute;
  top: -14rpx;
  font-size: 30rpx;
  line-height: 1;
  transition: left 1s ease-in-out;
}

/* 右侧文字 */
.hr-text {
  margin-left: 5%;
  font-size: 30rpx;
  font-style: italic;
  font-weight: bold;
  margin-bottom: 20rpx;
}
</style>