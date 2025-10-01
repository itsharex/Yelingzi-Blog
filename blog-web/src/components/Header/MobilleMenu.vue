<template>
  <div class="mobile-menu">

    <div class="close">
      <SvgIcon name="icon-close1" class="pointer" @click="$emit('closeMenu')" v-pio="{ text: `关闭菜单` }" />
    </div>

    <div class="menu-scroll">
      <div class="menu-header pointer" @click="onUserInfoCilck" v-pio="{ text: `查看个人信息` }">
        <YlAvatar :size="40" :src="userInfo.userAvatar" />
        <div class="logo-text">{{ userInfo.nickname }}</div>
      </div>

      <div class="menu-content">
        <div v-for="item in menuItems" :key="item.path" class="menu-item">
          <div class="nav-link pointer" :class="{ 'active': isActive(item) }"
            v-pio="{ text: `${item.name}`, type: 'link' }"
            @click="handleSelectMenu(item.name); handleDropdownItemClick(item)">
            <SvgIcon :name="item.icon" class="svg-icon" />
            {{ item.name }}
            <SvgIcon name="icon-xiala" v-if="item.children" class="dropdown-icon svg-icon" />
          </div>
          <Transition name="expand" @enter="startTransition" @leave="endTransition">
            <div v-if="item.children && item.name === selectMenu" class="dropdown-menu">
              <a href="javascript:void(0)" v-for="child in item.children" :key="child.path" class="dropdown-item"
                :class="{ 'active': isChildActive(child) }" @click="handleDropdownItemClick(child)"
                v-pio="{ text: `${child.name}`, type: 'link' }">
                <SvgIcon :name="child.icon" class="svg-icon" />
                {{ child.name }}
              </a>
            </div>
          </Transition>
        </div>
      </div>
    </div>
    <div class="menu-footer">
      <div v-if="ver != ''" class="app-version">版本号：{{ ver }}</div>
    </div>
  </div>

  <div class="user-warp" :class="{ 'is-show-user-info': isShowUserinfo }">
    <MobilleUserInfo :userInfo="userInfo" v-on:close-userinfo="toggleUserinfo" v-on:to-user-info="toggleUserInfoPage" />
  </div>

</template>

<script lang="ts" setup>
import { useUserStore } from '@/stores';
import { getMenuItems, type MenuItem } from '@/types/menu';
import { t } from '@/utils/i18n';
import { computed, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import YlAvatar from '../Image/YlAvatar.vue';
import MobilleUserInfo from '../User/MobilleUserInfo.vue';

const router = useRouter();
const route = useRoute();
const userStore = useUserStore()
const userInfo = computed(() => userStore.userInfo)
const ver = import.meta.env.VITE_APP_FULL_VERSION
const emit = defineEmits(['closeMenu'])
const selectMenu = ref('')
const isShowUserinfo = ref(false)

const menuItems = computed(() =>
  getMenuItems().map(item => ({
    ...item,
    name: t(item.name),
    children: item.children?.map(child => ({
      ...child,
      name: t(child.name)
    }))
  }))
)

const handleSelectMenu = (name: string) => {
  if (selectMenu.value === name) {
    selectMenu.value = ''
    return
  }
  selectMenu.value = name
}

const handleDropdownItemClick = (item: MenuItem) => {
  if (item.external) {
    const newWindow = window.open(item.path, '_blank')
    newWindow!.opener = null;
    return
  } if (item.children) {
    return
  }
  emit('closeMenu')
  router.push(item.path)
}
const isActive = (item: MenuItem): boolean => {
  if (item.children) {
    return item.children.some(child => isChildActive(child))
  }
  return route.path === item.path
}

const isChildActive = (child: MenuItem): boolean => {
  return route.path === child.path
}

const startTransition = (element: Element) => {
  const el = element as HTMLElement
  el.style.height = 'auto'
  const height = el.scrollHeight
  el.style.height = '0px'
  el.offsetHeight
  el.style.height = `${height}px`
}

const endTransition = (element: Element) => {
  const el = element as HTMLElement
  el.style.height = `${el.scrollHeight}px`
  el.offsetHeight
  el.style.height = '0px'
}

const toggleUserinfo = () => {
  isShowUserinfo.value = !isShowUserinfo.value
}

const onUserInfoCilck = () => {
  if (userStore.accessToken) {
    toggleUserinfo()
  } else {
    emit('closeMenu')
    router.push('/login')
  }
}

const toggleUserInfoPage = () => {
  emit('closeMenu')
  router.push('/user/info')
}

</script>

<style lang="scss" scoped>
.mobile-menu {
  height: 100%;
  background: var(--grey-3);
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  position: relative;
  overflow-y: auto;
}

.close {
  position: absolute;
  top: 12px;
  right: 12px;
  z-index: 10;
  padding: 6px;
  line-height: 0;
}

.close .svg-icon {
  color: var(--color-pink);
}

.menu-scroll {
  flex: 1 1 auto;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
}

.menu-header {
  padding: 20px;
  border-bottom: 1px solid var(--border-color);
  align-items: center;
  display: flex;
  justify-content: center;
  gap: 12px;
}

.menu-content {
  flex: 1;
  overflow-y: auto;
  padding: 8px 0;
  -webkit-overflow-scrolling: touch;
}

/* 导航项 */
.menu-item {
  border-bottom: 1px solid var(--border-color, #eee);
}

.nav-link {
  display: flex;
  align-items: center;
  padding: 14px 20px;
  font-size: 1rem;
  color: var(--text-primary-color, #333);
  transition: background 0.2s ease;
}

.nav-link.active {
  background: var(--primary-color, #007bff);
  color: #fff;
}

.nav-link:active,
.dropdown-item:active {
  background: rgba(0, 0, 0, 0.05);
}

.svg-icon {
  width: 20px;
  height: 20px;
  margin-right: 12px;
  flex-shrink: 0;
}

.dropdown-icon {
  margin-left: auto;
  transition: transform 0.3s ease;
}

.nav-link.active .dropdown-icon {
  transform: rotate(180deg);
}

/* 下拉菜单 */
.dropdown-menu {
  overflow: hidden;
  background: var(--grey-2, #f7f7f7);
  padding-left: 20px;
}

.dropdown-item {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  font-size: 0.95rem;
  color: var(--text-secondary-color, #555);
  text-decoration: none;
}

.dropdown-item.active {
  color: var(--primary-color, #007bff);
  font-weight: 600;
}

/* 过渡动画优化 */
.expand-enter-active,
.expand-leave-active {
  transition: height 0.4s ease;
  overflow: hidden;
}

.expand-enter-from,
.expand-leave-to {
  height: 0;
}

/* 版本号样式微调 */
.app-version {
  font-size: 0.8rem;
  color: var(--text-secondary-color, #999);
}


.menu-footer {
  flex-shrink: 0;
  padding: 16px;
  text-align: center;
  color: var(--text-secondary-color, #666);
  font-size: 0.9rem;
  border-top: 1px solid var(--border-color, #eee);
}

.user-warp {
  position: fixed;
  width: 320px;
  height: 100vh;
  right: 0;
  top: 0;
  z-index: 9999;
  background-color: var(--grey-3);
  transition: transform 0.6s ease;
  transform: translateX(320px);
  will-change: transform;
  backface-visibility: hidden;
}

.is-show-user-info {
  transform: translateX(0) !important;
}
</style>
