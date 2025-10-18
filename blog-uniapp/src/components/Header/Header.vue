<template>
  <view class="site-header" :class="{ flutter: isScrolled }">
    <view class="navbar">
      <!-- 移动端菜单按钮 -->
      <button class="menu-btn" @tap="handleOpenMobileMenu">
			 <SvgIcon name="icon-list" size="40" />
      </button>

      <!-- 中间 Logo -->
      <view class="nav-left" @tap="goHome">
        <image class="logo-img" :src="blogInfo.logo" mode="aspectFill" />
        <text class="logo-text">{{ t('blogName') }}</text>
      </view>

      <!-- 右侧搜索 -->
      <view class="nav-right">
		<view class="mobile-search-btn" @click="handleOpenSearch" >
		  <SvgIcon name="icon-Search" size="50" color="#ed6ea0" />
		</view>
		
      </view>
    </view>
  </view>

  <!-- 抽屉菜单 -->
  <view class="mobile-menu-mask" :class="{ show: isMobi }" @tap="handleOpenMobileMenu">
    <view class="mobile-menu-panel" @tap.stop>
      <mobile-menu @close="handleOpenMobileMenu" />
    </view>
  </view>

  <!-- 搜索弹层 -->
  <view class="search-mask" :class="{ show: isShowSearch }" @tap="handleCloseSearch">
    <view class="search-panel" @tap.stop>
      <search-dialog :show="isShowSearch"  @close="handleCloseSearch" />
    </view>
  </view>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue'
import { useBlogStore } from '@/stores'
import { t } from '@/utils/i18n'
import MobileMenu from '@/components/Header/MobilleMenu.vue'
import SearchDialog from '@/components/Search/Search.vue'
import ImageWithFallback from '../Image/ImageWithFallback.vue'
const props = defineProps({
  scrollTop: { type: Number, default: 0 },
})
const blogStore = useBlogStore()
const blogInfo = computed(() => blogStore.blogInfo)
const isScrolled = computed(() => props.scrollTop > 200)

/* 菜单 & 搜索 */
const isMobi = ref(false)
const isShowSearch = ref(false)

function handleOpenMobileMenu() {
  isMobi.value = !isMobi.value
}
function handleOpenSearch() {
  isShowSearch.value = true
}
function handleCloseSearch() {
	console.log(222)
  isShowSearch.value = false
}
function goHome() {
  uni.reLaunch({ url: '/pages/index/index' })
}
</script>

<style lang="scss" scoped>
/* 玻璃拟态：纯色+透明度 */
%glass {
  background: rgba(255, 255, 255, 0.85);
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
}

.site-header {
  position: fixed;
  left: 0;
  right: 0;
  top: 0;
  z-index: 998;
  height: 88rpx;
  padding: 0 30rpx;
  @extend %glass;
  transition: all 0.3s;
  transform: translateY(20rpx) scale(0.95);
  border-radius: 16rpx;
  border: 2rpx solid transparent;
}
.site-header.flutter {
  transform: translateY(0) scale(1);
  border-radius: 0;
  border-color: rgba(0, 0, 0, 0.05);
}

.navbar {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.nav-left {
  display: flex;
  align-items: center;
  gap: 12rpx;
}
.logo-img {
  width: 56rpx;
  height: 56rpx;
  border-radius: 8rpx;
}
.logo-text {
  font-size: 34rpx;
  font-weight: 700;
  background: linear-gradient(135deg, #007aff, #5856d6);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.menu-btn {
  background: none;
  border: none;
  padding: 0;
  margin: 0;
  line-height: 1;
}

/* 抽屉 */
.mobile-menu-mask {
  position: fixed;
  inset: 0;
  z-index: 999;
  background: rgba(0, 0, 0, 0.45);
  opacity: 0;
  visibility: hidden;
  transition: all 0.3s;
}
.mobile-menu-mask.show {
  opacity: 1;
  visibility: visible;
}
.mobile-menu-panel {
  position: absolute;
  right: 0;
  top: 0;
  bottom: 0;
  width: 560rpx;
  background: #fff;
  transform: translateX(100%);
  transition: transform 0.3s;
}
.mobile-menu-mask.show .mobile-menu-panel {
  transform: translateX(0);
}

/* 搜索 */
.search-mask {
  position: fixed;
  inset: 0;
  z-index: 1000;
  background: rgba(0, 0, 0, 0.45);
  opacity: 0;
  visibility: hidden;
  transition: all 0.3s;
}
.search-mask.show {
  opacity: 1;
  visibility: visible;
}
.search-panel {
  background: #fff;
  border-radius: 24rpx 24rpx 0 0;
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  transform: translateY(-150%);
  transition: transform 0.6s;
}
.search-mask.show .search-panel {
  transform: translateY(0);
}

.mobile-search-btn {
  padding: 5rpx;
  color: var(--color-black);
}
</style>