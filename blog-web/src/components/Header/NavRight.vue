<template>
  <div class="header-right">

    <div class="settings-container">
      <div @click="openSet" class="icon-settings pointer" v-pio="{ text: `这里可以切换语言和主题哦` }">
        <SvgIcon size="30" style="color:#fecfef;" name="icon-setting" />
      </div>
      <div class="settings-menu" :class="{ active: openSetting }">
        <Settings @close="openSet"></Settings>
      </div>
    </div>

    <div class="search-btn" @click="handleSearch" v-pio="{ text: `点击这里搜索文章` }">
      <SvgIcon name="icon-Search" />
      <span class="search-text">{{ t('search') }}</span>
    </div>

    <!-- 用户信息 -->
    <div class="user-container" @mouseleave="handleMouseLeaveUser" @mouseenter="handleMouseEnterUser">
      <div v-if="userStore.getIsLogin()" class="user-info">
        <div class="avatar" @mouseenter="showDropdown = true">
          <YlAvatar :src="userInfo.userAvatar"></YlAvatar>
        </div>
        <div class="user-menu" :class="{ active: isDropdownOpenUser }">
          <div class="dropdown-header">
            <div class="header-item" v-if="userInfo.userAvatar">
              <YlAvatar :size="80" :src="userInfo.userAvatar" />
            </div>
            <div class="header-item" v-if="userInfo.nickname">
              昵称：{{ userInfo.nickname }}
            </div>
          </div>
          <div class="dropdown-content">
            <div class="dropdown-item pointer" @click="handleRouter">
              <SvgIcon name="icon-geren" />
              {{ t('personalCenter') }}
            </div>
            <div class="dropdown-item pointer" @click="handleLogout">
              <SvgIcon name="icon-chexiao" />
              {{ t('logOut') }}
            </div>
          </div>
        </div>
      </div>
      <div v-else class="avatar pointer" @click="handleLogin">
        <el-avatar>{{ t('login') }}</el-avatar>
      </div>
    </div>

    <div class="mobile-search-btn" @click="handleSearch" v-pio="{ text: `点击这里搜索文章` }">
      <SvgIcon name="icon-Search" size="30" />
    </div>

  </div>
</template>

<script lang="ts" setup>
import { useUserStore } from '@/stores';
import { useChatStore } from '@/stores/modules/chat';
import { t } from '@/utils/i18n';
import { ElMessage } from 'element-plus';
import { computed, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import Settings from '../Settings/Settings.vue';

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const chatStore = useChatStore()

const userInfo = computed(() => userStore.userInfo)
const showDropdown = ref(false)
const hoverTimerUser = ref(0);
const isDropdownOpenUser = ref(false)
const openSetting = ref(false)
const emit = defineEmits(['search'])

//打开设置
const openSet = () => {
  openSetting.value = !openSetting.value
}

const handleSearch = () => {
  emit('search')
}

const handleMouseLeaveUser = () => {
  hoverTimerUser.value = setTimeout(() => {
    isDropdownOpenUser.value = false;
  }, 300);
}

const handleMouseEnterUser = () => {
  clearTimeout(hoverTimerUser.value);
  isDropdownOpenUser.value = true;
}

const handleRouter = () => {
  router.push('/user/info')
}

const handleLogin = () => {
  userStore.setLastShowWeb(route.path)
  router.push('/login')
}

const handleLogout = () => {
  userStore.removeToken()
  chatStore.removeChat()
  ElMessage.success('已退出登录')
  showDropdown.value = false
}
</script>

<style lang="scss" scoped>
@use '/src/assets/styles/variables.scss' as *;

.header-right {
  display: flex;
  align-items: center;
  gap: 30px;
  margin-left: auto;
}

.mobile-search-btn {
  visibility: hidden;
  padding: $spacing-sm;
  font-size: 1.2em;
  color: var(--color-black);
  transition: color 0.3s ease;

}

.settings-container {
  position: relative;

  .settings-menu {
    position: absolute;
    top: calc(100% + 21px);
    left: 50%;
    transform: translateX(-50%) translateY(50px);
    background: var(--glass-bg);
    border-radius: 8px;
    box-shadow: 12px;
    width: max-content;
    min-width: 120px;
    opacity: 0;
    visibility: hidden;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    pointer-events: none;
    z-index: 100;
  }

  .settings-menu.active {
    opacity: 1;
    visibility: visible;
    transform: translateX(-50%) translateY(0);
    pointer-events: auto;
  }

  .icon-settings {
    &:hover {
      animation: spin 2s linear infinite;
    }
  }


}


.search-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 24px;
  height: 20px;
  background: var(--hover-bg);
  border-radius: 20px;
  color: var(--color-black);
  transition: all 0.3s ease;
  border: 1px solid var(--color-grey);

  .search-text {
    font-size: 0.9em;
    font-weight: 500;
  }

  &:hover {
    background: var(--color-white);
    color: $primary;
    border-color: $primary;
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba($primary, 0.1);
  }

}

.user-container {
  position: relative;
  display: flex;
  align-items: center;
  margin-right: 20px;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  transition: transform 0.2s;
  display: flex;
  align-items: center;
  border: 2px solid $primary;

  &:hover {
    transform: scale(1.05);
    background: var(--hover-bg);
  }

}

.user-info {
  max-width: 240px;

  .user-menu {
    position: absolute;
    top: calc(100% + 21px);
    transform: translateX(-45%);
    background: var(--color-white);
    border-radius: $border-radius-md;
    box-shadow: $shadow-lg;
    width: 240px;
    visibility: hidden;
    opacity: 0;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    padding: 0;
  }

  .user-menu.active {
    visibility: visible;
    opacity: 1;
    transform: translateX(-50%);
    pointer-events: auto;
  }



  .dropdown-header {
    display: flex;
    flex-direction: column;
    gap: 10px;
    padding: 20px;
    border-radius: $border-radius-md $border-radius-md 0 0;
    border-bottom: 1px solid var(--color-grey);

    .header-item {
      display: flex;
      justify-content: center;
      width: fit-content;
      font-size: 16px;
      color: var(--color-pink);
      margin: 0 auto;
    }

  }

  .dropdown-content {
    border-radius: 0 0 $border-radius-md $border-radius-md;
  }

  .dropdown-item {
    display: flex;
    align-items: center;
    gap: $spacing-sm;
    padding: 8px $spacing-md;
    height: 36px;
    color: var(--color-black);
    transition: all 0.3s ease;
    font-size: 16px;
    border-radius: $border-radius-md;

    &:hover {
      color: var(--color-white);
      background-color: var(--hover-primary);
    }
  }

}

@media (max-width: $bp-md) {
  .mobile-search-btn {
    visibility: visible;
  }

  .settings-container {
    visibility: hidden;
  }

  .search-btn {
    visibility: hidden;
  }

  .user-container {
    visibility: hidden;
  }
}

/* 定义旋转动画 */
@keyframes spin {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}
</style>
