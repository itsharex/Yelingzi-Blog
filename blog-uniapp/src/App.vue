<template>
  <I18nProvider>
	  
  </I18nProvider>
</template>

<script setup lang="ts">
import { onLaunch, onShow, onHide } from "@dcloudio/uni-app";
import I18nProvider from './components/Language/I18nProvider.vue';
import { openWs, closeWs } from '@/utils/websocket'
import { ref, provide, type InjectionKey } from "vue";

onLaunch(() => {
	console.log(__APP_FULL_VERSION__)
  console.log("App Launch");
    openWs()
});

onShow(() => {
  console.log("App Show");
});

uni.onAppHide(() => {
  console.log("App truly hidden");
  closeWs();
});

// 真正回到前台时才重新连接
uni.onAppShow(() => {
  console.log("App truly shown");
  openWs();
});

onHide(() => {
  console.log("App Hide");
});
</script>

<style lang="scss" scoped>

</style>
