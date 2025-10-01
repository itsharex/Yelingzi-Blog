<template>
  <div class="container">
    <!-- 移动端 进度条 -->
    <div class="m-progress-warp" v-show="!showPcProgress">
      <div class="progress" :style="{ width: `${Math.round(currentScrollProgress)}%` }"></div>
    </div>
    <Transition name="slide-up">
      <div v-show="!showPcProgress && Math.round(currentScrollProgress) > 30" class="m-progress-buuton-top pointer"
        @click="handleTop">
        <SvgIcon name="icon-xiala" class="m-svg-top" size="24" />
      </div>
    </Transition>
    <!-- PC 进度条 -->
    <div class="progress-warp" ref="progressRef" v-show="showPcProgress">
      <div class="progress" :style="{ height: `calc(${currentScrollProgress}%)` }"></div>
      <div ref="progressIconRef" class="progress-icon pointer" :style="{
        opacity: currentScrollProgress !== 0 ? 1 : 0,
        bottom: `calc(${currentScrollProgress}% - ${Math.min(currentScrollProgress * 0.4, 20)}px)`
      }" @click="handleTop">
        <span class="progress-text">{{ Math.round(currentScrollProgress) }}%</span>
      </div>
    </div>
  </div>

</template>

<script lang="ts" setup>
import { ref, onMounted, onUnmounted, reactive, computed } from 'vue'
const props = defineProps({
  disabledSmooth: {
    type: Boolean,
    required: true,
  },
});

// DOM Refs
const progressRef = ref<HTMLElement | null>(null)
const progressIconRef = ref<HTMLElement | null>(null)

// 响应式状态
const currentScrollProgress = ref(0)
const state = reactive({
  isDragging: false,
  dragTimer: -1,
})

// 计算属性
const showPcProgress = computed(() => {
  return window.innerWidth > 768;
});

// 处理置顶
const handleTop = () => {
  if (state.isDragging) return
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  })
}

// 滚动处理
const handleScroll = () => {
  const el = document.documentElement
  const scrollTop = el.scrollTop || document.body.scrollTop
  const scrollHeight = el.scrollHeight - el.clientHeight
  const progress = (scrollTop / scrollHeight) * 100
  currentScrollProgress.value = Math.min(100, Math.max(0, progress))
}

// 拖动处理
const handleProgressDrag = (emit: any) => {
  const progressEl = progressRef.value
  const iconEl = progressIconRef.value
  if (!progressEl || !iconEl) return

  const DRAG_THRESHOLD = 10

  let startY = 0
  let hasMoved = false

  const handleMouseDown = (e: MouseEvent) => {
    if (e.cancelable && (e.type === 'mousedown' || e.type === 'touchstart')) {
      // 仅在拖拽开始时阻止
      e.preventDefault()
    }
    e.stopPropagation()
    startY = e.clientY
    hasMoved = false

    const handleMouseMove = (moveE: MouseEvent) => {
      const deltaY = Math.abs(moveE.clientY - startY)
      if (deltaY > DRAG_THRESHOLD) {
        hasMoved = true
      }

      if (!state.isDragging && hasMoved) {
        state.isDragging = true
        emit('update:disabledSmooth', !props.disabledSmooth)
      }

      if (!hasMoved) return

      const rect = progressEl.getBoundingClientRect()
      const yPos = moveE.clientY - rect.top
      const progress = Math.min(100, Math.max(0, (yPos / rect.height) * 100))

      currentScrollProgress.value = progress
      const scrollHeight = document.documentElement.scrollHeight - window.innerHeight
      window.scrollTo(0, (progress / 100) * scrollHeight)
    }

    const handleMouseUp = () => {
      document.removeEventListener('mousemove', handleMouseMove)
      document.removeEventListener('mouseup', handleMouseUp)

      if (!hasMoved) {
        // 视为点击，执行置顶
        handleTop()
      } else {
        emit('update:disabledSmooth', !props.disabledSmooth)
        state.dragTimer = window.setTimeout(() => {
          state.isDragging = false
        }, 10)
      }
    }

    document.addEventListener('mousemove', handleMouseMove)
    document.addEventListener('mouseup', handleMouseUp)
  }

  iconEl.addEventListener('mousedown', handleMouseDown)
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
  handleProgressDrag((emit: any) => emit)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
  if (state.dragTimer) clearTimeout(state.dragTimer)
})

</script>

<style lang="scss" scoped>
.container {
  max-width: 100%;
  position: relative;
}

.m-progress-warp {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 2px;
  z-index: 9999;

  .progress {
    background: linear-gradient(-90deg, #f03e07, #c15594, #23a6d5, #23d5ab);
    background-size: 100%;
    height: 100%;
  }
}

.m-progress-buuton-top {
  position: fixed;
  bottom: 200px;
  right: 32px;
  width: 40px;
  height: 30px;
  z-index: 9999;
  background-color: var(--color-pink-light);
  align-items: center;
  justify-content: center;
  display: flex;
  border-radius: 8px;

}

.m-svg-top {
  transform: rotate(180deg);
  color: var(--color-pink);
}

.progress-warp {
  position: fixed;
  top: 0px;
  right: 6px;
  width: 50px;
  height: 80vh;
  z-index: 998;
  pointer-events: none;

  .progress {
    background-image: url('/images/progress.jpg');
    width: 100%;
    background-size: 100% 115%;
  }

  .progress-icon {
    width: 50px;
    height: 50px;
    pointer-events: all;
    background-image: url('/images/progress.jpg');
    background-size: 100% 870%;
    background-position: 100% 100%;
    transition: opacity 1s ease;
    display: flex;
    justify-content: center;

    // 添加底部边界限制
    &::after {
      content: '';
      position: absolute;
      bottom: -30px; // 底部保留30px空间
      height: 30px;
      width: 1px;
      background: transparent;
    }

    .progress-text {
      background-color: #ffffffd6;
      transform: scale(0.9);
      margin-top: 4px;
      display: inline-block;
      text-align: center;
      line-height: 14px;
      color: #7d4842;
      text-shadow: var(--text-shadow);
      user-select: none;
      font-size: 12px;
      height: 14px;
      border-radius: var(--border-radius-1);
    }
  }
}

@keyframes animation {
  0% {
    background-position: 0% 50%;
  }

  50% {
    background-position: 100% 50%;
  }

  100% {
    background-position: 0% 50%;
  }
}

.slide-up-enter-active {
  animation: slideUp 0.35s cubic-bezier(0.18, 0.89, 0.32, 1.28);
}

.slide-up-leave-active {
  animation: slideUp 0.25s ease-in reverse;
}

@keyframes slideUp {
  0% {
    transform: translateY(100%);
    opacity: 0;
  }

  100% {
    transform: translateY(0);
    opacity: 1;
  }
}
</style>
