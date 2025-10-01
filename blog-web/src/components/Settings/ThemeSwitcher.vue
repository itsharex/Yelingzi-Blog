<template>
  <el-tooltip content="切换主题" placement="bottom" effect="light" v-pio="{ text: `这里可以切换主题哦` }">
    <div class="pointer" @click="handleToggleTheme">
      <SvgIcon v-if="blogInfo.isDark" size="32" style="color:#c2d7e9;" name="icon-Moon-Star" />
      <SvgIcon v-else name="icon-Sun" size="32" style="color:#fbc90d;" />
    </div>
  </el-tooltip>
</template>

<script lang="ts" setup>
import { useBlogStore } from '@/stores';

// 逻辑可提取到 useHeader 组合式函数
const blogStore = useBlogStore()
const blogInfo = blogStore.blogInfo


const handleToggleTheme = () => {
  blogStore.toggleTheme()
}
</script>

<style lang="scss" scoped></style>
