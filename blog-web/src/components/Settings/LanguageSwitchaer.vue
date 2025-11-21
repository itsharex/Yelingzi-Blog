<template>
  <div v-pio="{ text: `这里可以切换语言哦` }" class="i18n pointer" @mouseleave="handleMouseLeaveI18n"
    @mouseenter="handleMouseEnterI18n">

    <!-- <component :is="i18nIcon" color="#000" class="i18n-icon " v-if="i18nIcon" /> -->
    <span class="current-lang">
      {{currentLangList.find(lang => lang.id === useI18n.currentLang)?.text}}
    </span>

    <div v-if="currentLangList" class="i18n-menu" :class="{ active: isDropdownOpen }">
      <div v-for="currentLang in currentLangList" :key="currentLang.id" class="i18n-item pointer"
        @click="handleSwitch(currentLang.id)">
        {{ currentLang.text }}
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { useLangChange } from '@/constants/useLangChange';
import { useI18nStore } from '@/stores';
import { handleI18n } from '@/utils/i18n';
import { ref } from 'vue';

const useI18n = useI18nStore()
const isDropdownOpen = ref(false);
const hoverTimer = ref(0);
const currentLangList = ref([
  { id: 'zh-CN', text: '简体中文' },
  { id: 'en-US', text: 'English' }
])
// 逻辑可提取到 useHeader 组合式函数
const handleMouseLeaveI18n = () => {
  hoverTimer.value = setTimeout(() => {
    isDropdownOpen.value = false;
  }, 300); // 延迟关闭防止鼠标移出时立即消失
}

const handleMouseEnterI18n = () => {
  clearTimeout(hoverTimer.value);
  isDropdownOpen.value = true;
}
const handleSwitch = async (lang: string) => {
  await handleI18n(lang)
}
useLangChange(() => {
  // 当语言变化时，这里会重新执行
  console.log('语言已切换至', useI18n.currentLang)
})

</script>

<style lang="scss" scoped>
$spacing-sm: 8px;
$spacing-md: 16px;
$primary: var(--color-primary);
$hover-bg: var(--hover-bg);

.i18n {
  position: relative;
  display: flex;
  align-items: center;

  .i18n-menu {
    position: absolute;
    top: calc(100% + 14px);
    transform: translateX(50%) translateY(0px);
    background: var(--color-cyan-light);
    border-radius: 8px;
    box-shadow: 12px;
    width: max-content;
    min-width: 120px;
    opacity: 0;
    visibility: hidden;
    transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
    pointer-events: none;
    z-index: 1000;
  }

  .i18n-menu.active {
    opacity: 1;
    visibility: visible;
    transform: translateX(-25%) translateY(0);
    pointer-events: auto;
  }

  .i18n-item {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: $spacing-sm;
    padding: 8px $spacing-md;
    height: 36px;
    color: var(--color-black);
    text-decoration: none;
    transition: all 0.3s ease;
    white-space: nowrap;
    font-size: 0.9em;
    border-radius: 8px;

    &:hover {
      background: var(--hover-primary);
      color: $primary;

    }
  }
}
</style>
