<template>
  <view class="mobile-user-info">

    <!-- 用户信息 -->
    <view class="user-info">
      <view class="info-item">
        <YlAvatar class="info-avatar" :size="260" :src="userInfo.userAvatar" />
      </view>
      <text class="info-item" v-if="userInfo.nickname">昵称：{{ userInfo.nickname }}</text>
      <text class="info-item" v-if="userInfo.email">邮箱：{{ userInfo.email }}</text>
    </view>

    <!-- 菜单 -->
    <view class="menu">
		<view v-if="!userStore.refreshToken" class="menu-item pointer" @tap="handleLogin">
		  <SvgIcon name="icon-geren" class="svg-icon" />
		  <text>{{ t('login') }}</text>
		</view>
      <view class="menu-item pointer" @tap="handleLogout">
        <SvgIcon name="icon-chexiao" class="svg-icon" />
        <text>{{ t('logOut') }}</text>
      </view>
    </view>
  </view>
</template>

<script lang="ts" setup>
/* -------------- 逻辑层 -------------- */
import YlAvatar from '@/components/Image/YlAvatar.vue'
import { t } from '@/utils/i18n'
import { useUserStore } from '@/stores'
import { useChatStore } from '@/stores/modules/chat'
import { computed } from 'vue'

const userStore = useUserStore()
const chatStore = useChatStore()

const userInfo = computed(() => userStore.userInfo)

const props = defineProps({
  userInfo: {
    type: Object,
    default: () => ({})
  }
})

const emit = defineEmits(['closeUserinfo', 'toUserInfo'])

const toggleUserinfo = () => emit('toUserInfo')

const handleLogout = () => {
  userStore.removeToken()
  chatStore.removeChat()

}
const handleLogin = () => {
	uni.reLaunch({ url: '/pages/login/index' })
}
</script>

<style lang="scss">
/* -------------- 样式层 -------------- */
.mobile-user-info {
  position: relative;
}

.close {
  position: absolute;
  top: 24rpx;
  right: 24rpx;
  z-index: 10;
  padding: 12rpx;
  line-height: 0;
  .svg-icon {
    color: #ff5a7f; // 对应原来的 --color-pink
  }
}

.info-avatar{
	display: flex;
	justify-content: center;
}
.user-info {
  padding: 40rpx;
  text-align: center;
  justify-content: center;
  .info-item {
    display: block;
    margin-bottom: 30rpx;
    font-size: 32rpx;
    color: #666; // 对应原来的 --grey-7
  }
}

.menu {
  border-top: 1rpx solid #eee; // 对应 --grey-9-a1
  .menu-item {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 28rpx 40rpx;
    font-size: 32rpx;
    color: #333;
    border-bottom: 1rpx solid #eee;
    .svg-icon {
      margin-right: 20rpx;
      font-size: 38rpx;
      color: #999; // 对应 --grey-6
    }
  }
}
</style>