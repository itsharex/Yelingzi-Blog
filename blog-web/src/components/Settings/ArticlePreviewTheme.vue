<template>
  <el-select v-model="previewTheme" placeholder="请选择主题" :popper-append-to-body="false" style="width: 120px;">
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
import { ref, watch } from 'vue';

const userStore = useUserStore()
const previewTheme = ref(userStore.previewTheme)


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
