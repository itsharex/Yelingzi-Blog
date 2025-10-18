<template>
  <view class="layout-warp">
    <!-- 顶部进度条 -->
   <ProgressBar
      :disabledSmooth="disabledSmooth"
	      :scroll-top="scrollTop"
	      :scroll-height="scrollHeight"
      @update:disabledSmooth="updateDisabledSmooth"
    />

    <!-- 头部 -->
    <MyHeader :scroll-top="scrollTop" />

    <!-- 内容区 -->
    <view class="main">
      <slot />
    </view>

    <!-- 底部 -->
    <view class="footer">
      <MyFooter />
    </view>
  </view>
  
  
</template>

<script lang="ts" setup>
/* -------------------- 1. 依赖引入 -------------------- */
import { ref, onMounted } from 'vue'
import MyHeader from '@/components/Header/Header.vue'
import MyFooter from '@/components/Footer/Footer.vue'
import ProgressBar from '@/components/Progress/ProgressBar.vue'
import { useUserStore } from '@/stores'
import { userInfoService, viewService } from '@/api/login'
import { onShow } from '@dcloudio/uni-app'

const props = defineProps({
  scrollTop: { type: Number, default: 0 },
  scrollHeight: { type: Number, default: 1 }
})

/* -------------------- 2. 响应式数据 -------------------- */
const userStore = useUserStore()
const disabledSmooth = ref(false) // 控制进度条是否禁用过渡

/* -------------------- 3. 工具函数 -------------------- */
const todayStr = () => new Date().toISOString().split('T')[0]

/* -------------------- 4. 每日首次访问上报 -------------------- */
const isReportedToday = ref(userStore.viewDate === todayStr())

const handleDailyFirstView = async () => {
  if (isReportedToday.value) return

  await viewService(userStore.deviceId) // 自己封装的 uni.request
  userStore.setViewDate(todayStr() || '')
  isReportedToday.value = true
}

/* -------------------- 5. 登录态刷新 -------------------- */
const checkLogin = async () => {
  if (!userStore.getIsLogin()) return

  const { data: userRes } = await userInfoService() // 自己封装的 uni.request
  userStore.setUserState(userRes.data)
}

/* -------------------- 6. 生命周期 -------------------- */
onMounted(() => {
  checkLogin()
  handleDailyFirstView()
  userStore.checkIsFirstView()
})

/* 每次进入页面（含返回）都滚到顶部 */
onShow(() => {
  uni.pageScrollTo({ scrollTop: 0, duration: 300 })
})

/* -------------------- 7. 子组件事件 -------------------- */
const updateDisabledSmooth = (val: boolean) => {
  disabledSmooth.value = val
}
</script>

<style lang="scss" scoped>
.layout-warp {
  width: 100%;
  -webkit-overflow-scrolling: touch;
}
.footer {
  width: 100%;
  overflow-x: hidden;
}
</style>