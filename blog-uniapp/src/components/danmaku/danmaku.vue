<!-- 仅弹幕部分，其余页面代码不变 -->
<template>
  <view class="danmaku-box">

    <!-- 只渲染 4 个节点，复用 -->
    <view
      v-for="(item, idx) in pool"
      :key="idx"
      class="danmaku-item"
      :style="{ top: item.top + '%', visibility: item.show ? 'visible' : 'hidden' }"
      :animation="item.anim"
    >
      <YlAvatar class="dm-avatar" :src="item.data?.userAvatar" :size="40" />
      <text class="dm-text">{{ item.data?.nickname }}: {{ item.data?.content }}</text>
    </view>
	
  </view>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, ref } from 'vue'
import YlAvatar from '@/components/Image/YlAvatar.vue'
import ImageWithFallback from '@/components/Image/ImageWithFallback.vue';

interface Danmu { id: number; nickname: string; userAvatar: string; content: string }

const props = defineProps<{ list: Danmu[] }>()

/* 4 条轨道 */
const TRACK = 4
const TRACK_TOP = [20, 40, 60, 80]   // 百分比

/* 对象池 */
const pool = ref(Array.from({ length: TRACK }, () => ({
  show: false,
  top: 0,
  data: null as Danmu | null,
  anim: {} as any
})))

let running = true
let timer: any = null
let idx = 0                 // 取弹幕下标

/* 根据文字长度计算滚动时间（px / 速度） */
function calcDuration(text: string) {
  // 中文字≈24rpx，屏幕宽度 750rpx，这里简单按 0.5 s / 汉字 估算
  const charCount = text.replace(/[\x00-\xff]/g, 'aa').length
  return Math.max(4000, Math.min(8000, charCount * 180))
}

/* 发射一条弹幕 */
function shoot() {
  if (!running || !props.list.length) return
  const track = Math.floor(Math.random() * TRACK)
  const slot = pool.value[track]

  // 如果当前槽位还在跑，直接放弃，避免重叠
  if (slot.show) return

  const dm = props.list[idx++ % props.list.length]
  slot.data = dm
  slot.top = TRACK_TOP[track]
  slot.show = true

  const duration = calcDuration(dm.content)

  /* 从右到左 */
  const ani = uni.createAnimation({
    duration,
    timingFunction: 'linear',
    delay: 0
  })
  ani.translateX('-100%').step()
  slot.anim = ani.export()

  /* 动画结束复位 */
  setTimeout(() => {
    const reset = uni.createAnimation({ duration: 0 })
    reset.translateX('100vw').step()
    slot.anim = reset.export()
    slot.show = false
  }, duration)
}

function run() {
  timer = setInterval(shoot, 600)   // 600 ms 一发，可自行调整密度
}

onMounted(() => {
  // 等组件挂载后先初始化 4 条，避免空屏
  for (let i = 0; i < TRACK; i++) setTimeout(shoot, i * 200)
  run()
})

onUnmounted(() => {
  running = false
  clearInterval(timer)
})
</script>

<style lang="scss" scoped>
.danmaku-box {
  width: 100%;
  height: 100%;
  position: relative;
  overflow: hidden;
}
.danmaku-item {
  position: absolute;
  right: 0;
  display: flex;
  align-items: center;
  white-space: nowrap;
  background: rgba(0, 0, 0, 0.3);
  border-radius: 30rpx;
  padding: 6rpx 30rpx;
  color: #fff;
  font-size: 24rpx;
  transform: translateX(100vw);   /* 初始放到屏幕外 */
}
.dm-avatar {
  margin-right: 10rpx;
}

</style>