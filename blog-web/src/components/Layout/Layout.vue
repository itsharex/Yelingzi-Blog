<template>
  <div class="layout-warp">
    <!-- 滚动条 -->
    <ProgressBar :disabledSmooth="disabledSmooth" @update:disabledSmooth="updateDisabledSmooth"></ProgressBar>


    <!-- 头部菜单 -->
    <MyHeader></MyHeader>

    <!-- main -->
    <main>
      <router-view></router-view>
    </main>
    <div class="footer">
      <MyFooter></MyFooter>
    </div>
    <!-- 尾部 -->

  </div>
</template>

<script lang="ts" setup>
import { dayjs } from 'element-plus'
import { onMounted, ref, watch } from 'vue';
import MyHeader from '@/components/Header/Header.vue';
import MyFooter from '@/components/Footer/Footer.vue'
import ProgressBar from '@/components/Progress/ProgressBar.vue';
import { useRoute } from 'vue-router';
import { useUserStore } from '@/stores';
import { userInfoService, viewService } from '@/api/login';

const userStore = useUserStore()
const disabledSmooth = ref(true)
const route = useRoute()

// 监听路由变化
watch(
  () => route.path,
  () => {
    window.scrollTo({
      top: 0,
      behavior: 'auto'
    })
  }
)

// 今天 YYYY-MM-DD
const todayStr = () => dayjs().format('YYYY-MM-DD')

const isReportedToday = ref(
  userStore.viewDate === todayStr()
)

// 发送访问请求（只会在今天首次调用时真正执行）
const handleDailyFirstView = async () => {
  if (isReportedToday.value) return

  await viewService(userStore.deviceId)

  userStore.setViewDate(todayStr())
  isReportedToday.value = true
}

onMounted(() => {
  checkLogin()
  handleDailyFirstView()
  userStore.checkIsFirstView()
});

// 定义一个方法，用于更新 disabledSmooth
const updateDisabledSmooth = (value: boolean) => {
  disabledSmooth.value = value;
};

const checkLogin = async () => {
  if (userStore.getIsLogin()) {
    const { data: userRes } = await userInfoService()

    userStore.setUserState({
      ...userRes.data,
    })

  }
}



</script>

<style lang="scss" scoped>
.layout-warp {
  width: 100%;
  user-select: none;
  -webkit-overflow-scrolling: touch;
}

.footer {
  overflow-x: hidden;
  width: 100%;
}
</style>
