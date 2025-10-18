<template>
	<view class="mobile-menu">
		<!-- 关闭按钮 -->
		<view class="close" @tap="$emit('closeMenu')">
			<svg-icon name="icon-close1" />
		</view>

		<!-- 滚动区 -->
		<scroll-view scroll-y class="menu-scroll">
			<view class="menu-header" @tap="onUserInfoClick">
				<yl-avatar :size="80" :src="userInfo.userAvatar"></yl-avatar>
				<text class="logo-text">{{ userInfo.nickname }}</text>
			</view>
			<view class="menu-content">
				<view v-for="(item,idx) in menuItems" :key="idx" class="menu-item">
					<view class="nav-link" :class="{active:isActive(item)}"
						@tap="handleSelectMenu(item);handleDropdownItemClick(item)">
						<text class="iconfont" :class="item.icon"></text>
						<text class="name">{{ t(item.name) }}</text>
						<text v-if="item.children" class="iconfont icon-xiala dropdown-icon"
							:class="{open:selectMenu===item.name}"></text>

					</view>
					<view class="expand-wrapper" :style="{ height: item.children && selectMenu === item.name ? `${menuHeight}px` : '0' }">
						<view class="dropdown-menu" :id="`menu-${item.name}`">
							<view v-for="(child,cidx) in item.children" :key="cidx" class="dropdown-item"
								:class="{active:isChildActive(child)}" @tap.stop="handleDropdownItemClick(child)">
								<text class="iconfont" :class="child.icon"></text>
								<text class="name">{{ t(child.name) }}</text>
							</view>
						</view>
					</view>
				</view>

			</view>
		</scroll-view>
		<!-- 底部版本 -->
		<view class="menu-footer">
			<text v-if="ver" class="app-version">版本号：{{ ver }}</text>
		</view>

	</view>
</template>

<script lang="ts" setup>
	import {
		computed,
		ref,
		getCurrentInstance,
		nextTick
	} from 'vue'
	import {
		useUserStore
	} from '@/stores'
	import {
		getMenuItems, type MenuItem
	} from '@/types/menu'
	import YlAvatar from '@/components/Image/YlAvatar.vue'
	import {
		t
	} from '@/utils/i18n'
	import {
		onLoad,
		onShow
	} from '@dcloudio/uni-app'

	/* ----------------- 基础数据 ----------------- */
	const emit = defineEmits(['closeMenu'])
	const userStore = useUserStore()
	const userInfo = computed(() => userStore.userInfo)
	const ver = __APP_FULL_VERSION__
	const menuHeight = ref(0)
	const ins = ref(getCurrentInstance())

	/* ----------------- 菜单 ----------------- */
	const selectMenu = ref('')
	// const menuItems = computed(() =>
	// 	getMenuItems().map(item => ({
	// 		...item,
	// 		name: t(item.name),
	// 		children: item.children?.map(c => ({
	// 			...c,
	// 			name: t(c.name)
	// 		}))
	// 	}))
	// )
	const menuItems = getMenuItems()

	function handleSelectMenu(item: { name: string }) {
		if (selectMenu.value === item.name) {
			selectMenu.value = ''
		} else {
			selectMenu.value = item.name
			toggleHeight()
		}
	}

	const toggleHeight = async () => {
		await nextTick()
		
		menuHeight.value = 0       
		let q = uni.createSelectorQuery()
		// #ifndef H5
		if (ins != null) q = q.in(ins.value)
		// #endif
		/* -------------------------------- */
		console.log(selectMenu.value)
		q.select(`#menu-${selectMenu.value}`)
		 .boundingClientRect((rect: any) => {
				if (rect) menuHeight.value = rect.height
			})
		 .exec()
	}

	function handleDropdownItemClick(item: any) {
		if (item.external) {
			// #ifdef H5
			window.open(item.path, '_blank')
			// #endif
			// #ifndef H5
			uni.setClipboardData({
				data: item.path
			})
			// #endif
			return
		}
		if (item.children) return

		emit('closeMenu')
		if (item.isTabBar) {
			uni.switchTab({
				url: item.path
			})
		} else {
			uni.navigateTo({
				url: item.path
			})
		}
	}

	function isActive(item: MenuItem) {
		if (item.children) return item.children.some(c => isChildActive(c))
		return currentPath.value === item.path
	}

	function isChildActive(child: MenuItem) {
		return currentPath.value === child.path
	}

	/* ----------------- 用户信息弹层 ----------------- */
	function onUserInfoClick() {
		if (userStore.accessToken) {
			toUserInfoPage()
		} else {
			emit('closeMenu')
			uni.navigateTo({
				url: '/pages/login/index'
			})
		}
	}

	function toUserInfoPage() {
		emit('closeMenu')
		uni.switchTab({
			url: '/pages/user/info'
		})
	}

	/* ----------------- 当前页面路径 ----------------- */
	const currentPath = ref('')
	// 页面显示时更新
	onShow(() => {
		const pages = getCurrentPages()
		currentPath.value = '/' + (pages[pages.length - 1]?.route || '')
	})
	onLoad(() => {
	})
</script>

<style scoped lang="scss">
	.mobile-menu {
		height: 100vh;
		background: var(--grey-3, #f8f8f8);
		display: flex;
		flex-direction: column;
	}

	.close {
		position: absolute;
		top: 24rpx;
		right: 24rpx;
		z-index: 10;
		padding: 12rpx;
	}

	.menu-scroll {
		flex: 1;
		height: 0;
	}

	.menu-header {
		display: flex;
		align-items: center;
		padding: 40rpx;
		border-bottom: 1rpx solid var(--border-color, #eee);
	}

	.logo-text {
		margin-left: 20rpx;
		font-size: 32rpx;
	}

	.menu-content {
		padding: 16rpx 0;
	}

	.menu-item {
		border-bottom: 1rpx solid var(--border-color, #eee);
	}

	.nav-link {
		display: flex;
		align-items: center;
		padding: 28rpx 40rpx;
		font-size: 32rpx;
		gap: 18rpx;
		color: var(--text-primary, #333);
	}

	.nav-link.active {
		background: var(--primary-color, #007aff);
		color: #fff;
	}

	.dropdown-icon {
		margin-left: auto;
		transition: transform .3s;
	}

	.dropdown-icon.open {
		transform: rotate(180deg);
	}

	.expand-wrapper {
		overflow: hidden;
		transition: height 0.35s ease;
	}

	.dropdown-menu {
		background: var(--grey-2, #f5f5f5);
		overflow: hidden;
		transition: height .4s;
	}

	.dropdown-item {
		display: flex;
		align-items: center;
		padding: 24rpx 40rpx 24rpx 80rpx;
		font-size: 30rpx;
		gap: 18rpx;
		color: var(--text-secondary, #555);
	}

	.dropdown-item.active {
		color: var(--primary-color);
		font-weight: 600;
	}

	.menu-footer {
		padding: 32rpx;
		text-align: center;
		font-size: 24rpx;
		color: var(--text-secondary, #999);
		border-top: 1rpx solid var(--border-color, #eee);
	}

	.user-wrap {
		position: fixed;
		width: 640rpx;
		height: 100vh;
		right: 0;
		top: 0;
		z-index: 9999;
		background: var(--grey-3, #f8f8f8);
		transform: translateX(100%);
		transition: transform .6s;
	}

	.user-wrap.show {
		transform: translateX(0);
	}
</style>