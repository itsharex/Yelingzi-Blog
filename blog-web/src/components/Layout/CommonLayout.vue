<template>
  <BgImage :src="props.bgImg" :transition-duration="1000" />
  <div ref="brandRef" class="brand-container">
    <div class="brand">
      <!-- 标题 -->
      <p class="artboard">{{ props.title }}</p>
    </div>
    <!-- 波浪 -->
    <WaveOne></WaveOne>
    <!-- 向下按钮 -->
    <SvgIcon name="icon-xiala" class="arrow-down pointer" size="120" @click="scrollDown" />
  </div>
  <div class="bottom"></div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import WaveOne from "@/components/Waves/WaveOne.vue"
import BgImage from '@/components/Image/BgImage.vue';
const brandRef = ref<HTMLElement>()
const props = defineProps(['title', 'bgImg'])

const scrollDown = () => {
  if (!brandRef.value) return

  const element = brandRef.value
  const elementRect = element.getBoundingClientRect()
  const absoluteBottom = elementRect.bottom + window.scrollY

  window.scrollTo({
    behavior: "smooth",
    top: absoluteBottom
  })

}
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
  color: var(--color-pink);
  overflow-x: hidden;
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
    font-size: 3.5em;
    line-height: 1.2;
    animation: titleScale 1s;
  }

}


.arrow-down {
  position: absolute;
  bottom: 70px;
  -webkit-animation: arrow-shake 1.5s ease-out infinite;
  animation: arrow-shake 1.5s ease-out infinite;
  z-index: 8;
  color: var(--color-yellow);
}

.bottom {
  height: 80px;
  background-color: var(--grey-1);
  //解决滚动时出现缝隙问题
  margin-top: -20px;

}

@media (max-width: 767px) {
  .brand-container {
    padding: 3rem 0 0;
  }
}

@media (min-width: 760px) {
  .title {
    font-size: 1.5rem;
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
</style>
