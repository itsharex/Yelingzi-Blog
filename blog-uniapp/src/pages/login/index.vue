<template>
  <!-- 小程序整页滑动必须加 page-meta -->
  <page-meta :page-style="pageStyle" />

  <view class="login-container">
    <!-- 三页共用一条 swiper-item，性能最好 -->
    <swiper
      class="swiper"
      :current="currentIndex"
      @change="onSwipe"
      :duration="300"
      circular
    >
      <swiper-item>
        <Login @change-mode="goMode" />
      </swiper-item>

      <swiper-item>
        <Regist @change-mode="goMode" />
      </swiper-item>

      <swiper-item>
        <CodeLogin @change-mode="goMode" />
      </swiper-item>
    </swiper>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import Login from '@/components/Login/Login.vue'
import Regist from '@/components/Login//Regist.vue'
import CodeLogin from '@/components/Login//CodeLogin.vue'

/* 1 = 账号登录  2 = 注册  3 = 验证码登录 */
const mode = ref('login')
const currentIndex = computed(() =>
  mode.value === 'login' ? 0 : mode.value === 'regist' ? 1 : 2
)

/* 禁止下拉/回弹，避免滑动冲突 */
const pageStyle = 'overflow: hidden;'

/* 统一事件：子组件只需传目标模式 */
function goMode (target) {
  mode.value = target
}

/* 用户手动滑动也同步状态 */
function onSwipe (e) {
  const idx = e.detail.current
  const map = ['login', 'regist', 'code']
  mode.value = map[idx]
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  background: #fff;
}
.swiper {
  height: 100%;
}
</style>