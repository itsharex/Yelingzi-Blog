<template>
  <div ref="brandRef" class="brand-container">
    <div class="brand">
      <!-- 标题 -->
      <p class="artboard" :class="{ active: artboard }">{{ t('blogName') }}</p>
      <!-- 打字机 -->
      <Typeit v-if="yiYan.hitokotoList.length > 0" :data="yiYan.hitokotoList" :speed="100" :delete-speed="50"
        :pause-time="3000" />
      <div v-else class="loading">正在加载每日一言...</div>
    </div>
    <!-- 波浪 -->
    <WaveOne></WaveOne>
    <!-- 向下按钮 -->
    <SvgIcon name="icon-xiala" class="arrow-down pointer" size="120" @click="scrollDown" />

  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, inject, type Ref, nextTick } from 'vue'
import { t } from '@/utils/i18n'
import Typeit from "@/components/Typeit/Typeit.vue"
import WaveOne from "@/components/Waves/WaveOne.vue"
import { useYiYanStore } from '@/stores/modules/yiyan'

const yiYan = useYiYanStore()
const brandRef = ref<HTMLElement>()
const artboard = ref(false)

// 生命周期
// 初始加载和定期刷新
onMounted(async () => {
  await yiYan.fetchHitokoto()

  // 每15秒尝试获取一次
  const interval = setInterval(async () => {
    await yiYan.fetchHitokoto()

    // 当天达到10条或已失败时清除定时器
    if (yiYan.dailyCount >= 10 || yiYan.failedToday) {
      clearInterval(interval)
    }
  }, 15000)
})

const scrollDown = () => {
  if (!brandRef.value) return

  const element = brandRef.value
  const elementRect = element.getBoundingClientRect()
  const absoluteBottom = elementRect.bottom + window.scrollY

  window.scrollTo({
    behavior: "smooth",
    top: absoluteBottom - 30
  })
}

onMounted(() => {
  nextTick(() => artboard.value = true)
})

</script>


<style lang="scss" scoped>
@use '/src/assets/styles/mixin.scss' as m;

.brand-container {
  @include m.flex;
  flex-direction: column;
  position: relative;
  width: 100%;
  height: 70vh;
  min-height: 10rem;
  color: var(--header-text-color);
}

.brand {
  @include m.flex;
  flex-direction: column;
  position: fixed;
  z-index: -1;
  pointer-events: none;

  .artboard {
    font-family: 'CustomFont', "Fredericka the Great", Mulish, -apple-system, "PingFang SC", "Microsoft YaHei",
      sans-serif;
    font-size: 5em;
    line-height: 1.2;
    color: var(--color-pink);
    letter-spacing: -1em;
    opacity: 0;
    // animation: titleScale 1s;
  }

  .title {
    letter-spacing: 0.1em;
  }
}

.active {
  animation: unfoldSpacing 1.2s ease-out forwards;
}

.easy-typed-cursor {
  margin-left: 0.625rem;
  opacity: 1;
  -webkit-animation: blink 0.7s infinite;
  -moz-animation: blink 0.7s infinite;
  animation: blink 0.7s infinite;
}

.arrow-down {
  position: absolute;
  bottom: 70px;
  -webkit-animation: arrow-shake 1.5s ease-out infinite;
  animation: arrow-shake 1.5s ease-out infinite;
  z-index: 8;
  color: var(--lightRed);
}

@media (max-width: 767px) {
  .brand-container {
    padding: 3rem 0 0;
  }

  .brand .artboard {
    font-size: 3.5em;
  }
}

@keyframes arrow-shake {
  0% {
    opacity: 1;
    transform: translateY(0);
  }

  30% {
    opacity: 0.5;
    transform: translateY(25px);
  }

  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes blink {
  0% {
    opacity: 0;
  }

  100% {
    opacity: 1;
  }
}

@keyframes unfoldSpacing {
  0% {
    letter-spacing: -1em;
    opacity: 0;
  }

  100% {
    letter-spacing: normal;
    opacity: 1;
  }
}
</style>
