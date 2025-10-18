<template>
	<!-- 版权声明提示 -->
	<view class="copyright-notice">
		<view class="notice-content">
			<svg-icon name="icon-tishi1" style="color: #74C0FC;" />
			<view class="notice-text">
				<text v-if="article.isOriginal" selectable>
					本文为原创文章，遵循
					<text class="link" @click="openCC">CC BY-NC-SA 4.0</text>
					版权协议，转载请注明出处。
				</text>
				<text v-else selectable>
					本文转载自 {{ article.originalUrl || '未知来源' }}，版权归原作者所有。如有侵权，请联系删除。
				</text>
			</view>
		</view>
	</view>

	<!-- 操作按钮部分 -->
	<view class="article-actions">
		<button class="action-btn like" :class="{ active: article.isLike }" @tap="handleLikeClick">
			<svg-icon name="icon-shoucang" />
			<text>{{ article.likeNum }}</text>
		</button>

		<!-- 分享 -->
		<view class="share-dropdown">
			<button class="action-btn share" @tap="toggleShareMenu">
				<SvgIcon name="icon-fenxiang" />
				分享
			</button>

			<!-- 分享菜单 -->
			<view v-if="showShareMenu" class="share-mask" @tap="closeShareMenu" />
			<view v-show="showShareMenu" class="share-menu">
				<button class="share-item" @tap="copyLink">
					<svg-icon name="icon-fenxiang" />
					复制链接
				</button>
			</view>
		</view>
	</view>
</template>

<script lang="ts" setup>
	import { ref } from 'vue'

	/* -------------------- 接收参数 & 事件 -------------------- */
	const props = defineProps({
		article: { type: Object, required: true },
		showShare: { type: Boolean, default: false }
	})
	const emit = defineEmits(['share', 'copy', 'like'])

	/* -------------------- 响应式数据 -------------------- */
	const showShareMenu = ref(props.showShare)

	/* -------------------- 方法 -------------------- */
	// 点赞
	const handleLikeClick = () => emit('like')

	// 分享菜单
	const toggleShareMenu = () => {
		showShareMenu.value = !showShareMenu.value
		emit('share', showShareMenu.value)
	}
	const closeShareMenu = () => {
		showShareMenu.value = false
		emit('share', false)
	}

	// 复制链接
	const copyLink = () => {
		let copyUrl : string

		// #ifdef H5
		copyUrl = `${location.origin}${location.pathname}${location.search}`
		// #endif

		// #ifndef H5
		const pages = getCurrentPages()
		const curPage = pages?.at(-1)     
		if (!curPage) {
			uni.showToast({ title: '获取页面信息失败', icon: 'none' })
			return
		}
		const route = (curPage as any).$page?.fullPath || (curPage as any).route || ''
		const domain = 'https://www.yeling.top.com'  
		copyUrl = domain + route
		// #endif

		uni.setClipboardData({
			data: copyUrl,
			success: () => emit('copy'),
			fail: (e) => console.error('复制链接失败:', e)
		})
	}
	// H5 端打开协议链接（小程序/App 端可在对应平台配置 web-view）
	const openCC = () => {
		// #ifdef H5
		window.open('https://creativecommons.org/licenses/by-nc-sa/4.0/')
		// #endif
	}
</script>

<style scoped>
	/* 变量统一写死，方便三端兼容 */
	.copyright-notice {
		border-radius: 16rpx;
		background: rgba(24, 144, 255, 0.05);
		border: 1px solid rgba(24, 144, 255, 0.1);
		margin: 0 30rpx;
		transition: all 0.3s;
	}

	.copyright-notice:hover {
		background: rgba(24, 144, 255, 0.08);
		transform: translateY(-1rpx);
		box-shadow: 0 4rpx 16rpx rgba(24, 144, 255, 0.1);
	}

	.notice-content {
		padding: 24rpx;
		display: flex;
		align-items: flex-start;
		gap: 16rpx;
	}

	.notice-text {
		flex: 1;
		color: #666;
		font-size: 28rpx;
		line-height: 1.6;
	}

	.link {
		color: #1890ff;
		text-decoration: underline;
	}

	/* 操作按钮 */
	.article-actions {
		display: flex;
		justify-content: center;
		align-items: center;
		gap: 40rpx;
		margin: 40rpx auto;
		height: 60rpx;
		width: 400rpx;
	}

	.action-btn {
		display: flex;
		align-items: center;
		justify-content: center;
		gap: 12rpx;
		padding: 12rpx 40rpx;
		border: none;
		border-radius: 40rpx;
		font-size: 28rpx;
		background: #fff;
		height: 60rpx;
	}

	.action-btn.like {
		border: 1px solid #d9d9d9;
		color: #666;
	}

	.action-btn.like.active {
		background: #ff4d4f;
		color: #fff;
		border: none;
	}

	.action-btn.share {
		background: #1890ff;
		color: #fff;
	}

	/* 分享下拉 */
	.share-dropdown {
		position: relative;
	}

	.share-mask {
		position: fixed;
		left: 0;
		top: 0;
		right: 0;
		bottom: 0;
		z-index: 9;
	}

	.share-menu {
		position: absolute;
		bottom: calc(100% + 16rpx);
		left: 50%;
		transform: translateX(-50%);
		background: #fff;
		border-radius: 16rpx;
		box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.1);
		width: 300rpx;
		z-index: 10;
	}

	.share-item {
		padding: 16rpx 32rpx;
		border: none;
		background: none;
		font-size: 28rpx;
		display: flex;
		align-items: center;
		gap: 16rpx;
	}

	.share-item:hover {
		background: #f5f5f5;
	}
</style>