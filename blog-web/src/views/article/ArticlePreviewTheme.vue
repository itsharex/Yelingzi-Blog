<template>

  <el-text style="align-items: center;font-size: 16px;">文章展示主题：</el-text>
  <el-select v-model="previewTheme" placeholder="请选择主题" style="width: 200px;margin-right: 20px;"
    :popper-append-to-body="false">
    <el-option label="默认" value="default" />
    <el-option label="github" value="github" />
    <el-option label="vuepress" value="vuepress" />
    <el-option label="mk-cute" value="mk-cute" />
    <el-option label="smart-blue" value="smart-blue" />
    <el-option label="cyanosis" value="cyanosis" />
  </el-select>

</template>


<script lang="ts" setup>
import { useUserStore } from '@/stores';
import { computed, ref, watch } from 'vue';

const userStore = useUserStore()
const previewTheme = ref('default')


watch(
  () => previewTheme.value,
  (newValue) => {
    userStore.setPreviewTheme(newValue)
  }
);

</script>

<style lang="scss" scoped>
:deep(.el-select__wrapper) {
  cursor: url('/src/assets/cursors/up.cur'), pointer;
}
</style>
