<template>
  <!-- 设置 -->
  <div class="container">
    <div class="header">
      <h2 class="title">{{ t('setting') }}</h2>
      <SvgIcon name="icon-close1" class="icon-shezhi pointer" @click="handleClose" />
    </div>
  </div>
  <div class="setting-item">
    <span>{{ t('language') }}</span>
    <LanguageSwitchaer />
  </div>
  <div class="setting-item">
    <span>{{ t('theme') }}</span>
    <ThemeSwitcher />
  </div>
  <div class="setting-item">
    <span>{{ t('articleTheme') }}</span>
    <ArticlePreviewTheme v-pio="{ text: `这里可以切换文章展示主题哦` }" />
  </div>

</template>

<script lang="ts" setup>
import SvgIcon from '../SvgIcon/SvgIcon.vue';
import ThemeSwitcher from './ThemeSwitcher.vue';
import LanguageSwitchaer from './LanguageSwitchaer.vue';
import ArticlePreviewTheme from '@/components/Settings/ArticlePreviewTheme.vue';
import { t } from '@/utils/i18n'

const emit = defineEmits(['close']);
const handleClose = () => {
  emit('close')
}

</script>

<style lang="scss" scoped>
$radius: 8px;
$gap: 16px;

/* 整个弹窗容器 */
.container {
  width: 320px;
  border-radius: $radius;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  overflow: hidden;

}

/* 顶部标题栏 */
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: $gap $gap * 1.5;
  border-bottom: 1px solid var(--color-grey);

  .title {
    margin: 0;
    font-size: 18px;
    font-weight: 500;
    color: var(--color-black);
  }

  /* 关闭图标 */
  .icon-shezhi {
    color: var(--color-pink);
    transition: fill 0.2s;

    &:hover {
      color: var(--color-blue);
    }
  }
}

/* 单个设置项 */
.setting-item {
  display: flex;
  align-items: center;
  padding: 14px $gap * 1.5;
  font-size: 15px;
  color: var(--color-black);

  &:last-child {
    border-bottom: none;
  }

  span {
    width: 130px;
    // flex: 1;
    // letter-spacing: 0.5px;
  }

  /* 语言/主题切换组件本身 */
  >*:last-child {
    margin-left: $gap;
  }
}
</style>
