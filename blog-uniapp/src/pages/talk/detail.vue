<template>
	<!-- 页面头部 -->
	<page-header :title="title" bg-img="/image/common/images/talks-bg.jpg" />
	<view class="container">

		<view class="bg">
			<view class="page-container">
				<!-- 说说卡片 -->
				<view class="talk-item">
					<!-- 头像 -->
					<view class="talk-meta">
						<yl-avatar class="user-avatar" :size="120" :src="talk.userAvatar" mode="aspectFill" />
					</view>
					<view class="talk-info">
						<text class="talk-user-name">{{ talk.nickname }}</text>
						<text class="talk-time">{{ getDateDiff(talk.createTime) }}</text>
					</view>

				</view>

				<!-- 右侧内容区 -->
				<view class="talk-content-wrap">

					<!-- 文本内容 -->
					<rich-text class="title" :nodes="talk.title" />
					<rich-text class="talk-content" :nodes="talk.content" />

					<!-- 图片列表 -->
					<view v-if="talk.imageUrl && talk.imageUrl.length" class="talk-image">
						<view v-if="talk.imageUrl.length === 1" class="one">
							<image-with-fallback class="single" :src="talk.imageUrl.at(0)" mode="aspectFill" />
						</view>

						<!-- 多于 1 张：3 列，最多 9 -->
						<view v-else class="multi">
							<view v-for="(url, idx) in talk.imageUrl.slice(0, 9)" :key="idx" class="pic">
								<image-with-fallback :src="url" mode="aspectFill" />
							</view>
						</view>

						<!-- 点赞 / 评论 -->
						<view class="info-bar">
							<view class="info talk-like" :class="{ active: isLike }" @click="like">
								<svg-icon name="icon-dianzan_kuai" class="icon" :size="36" />
								<text>{{ talk.likeCount }}</text>
							</view>
							<view class="info talk-comment">
								<svg-icon name="icon-xiaoxi" class="icon" :size="36" />
								<text>{{ talk.commentCount }}</text>
							</view>
						</view>
					</view>
				</view>

				<!-- 评论区（按需引入） -->
				<!-- <comment v-if="talk.id" :talk-id="talk.id" /> -->

			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref,
		onMounted
	} from 'vue';
	import {
		onLoad
	} from '@dcloudio/uni-app';
	import {
		useUserStore
	} from '@/stores';
	import {
		getTalkByIdService,
		getTalkLikeService,
		addTalkLikeService,
		delTalkLikeService
	} from '@/api/talk';
	import {
		getDateDiff,
		numberToBoolean
	} from '@/utils/common';

	/* 组件（easycom 自动引入） */
	import PageHeader from '@/components/Layout/PageHeader.vue';
	import YlAvatar from '@/components/Image/YlAvatar.vue';
	import ImageWithFallback from '@/components/Image/ImageWithFallback.vue';
	// import Comment from '@/components/comment/comment.vue';

	/* 响应式数据 */
	const userStore = useUserStore();
	const isLike = ref(false);
	const isRequesting = ref(false);
	const talk = ref({
		id: 0,
		nickname: '',
		userId: 0,
		userAvatar: '',
		title: '',
		content: '',
		imageUrl: [],
		isTop: 0,
		likeCount: 0,
		commentCount: 0,
		createTime: ''
	});

	/* 页面参数 */
	const talkId = ref(0);

	/* 国际化（如用 vue-i18n 可保留，这里简化） */
	const title = ref('说说详情');

	/* 生命周期 */
	onLoad((query) => {
		talkId.value = Number(query.id || 0);
		if (!talkId.value) {
			uni.showToast({
				title: '参数错误',
				icon: 'none'
			});
			setTimeout(() => uni.navigateBack(), 1500);
			return;
		}
		getTalkById();
	});

	onMounted(() => {
		// 如仍需 vue 生命周期可保留
	});

	/* 方法 */
	async function getTalkById() {
		try {
			const res = await getTalkByIdService(talkId.value);
			const data = res.data;
			// 过滤无效图片
			data.imageUrl = (data.imageUrl || []).filter(
				(url) => url && url.trim() && url !== 'null' && url !== 'undefined'
			);
			talk.value = data;
			await getIsLike();
		} catch (e) {
			console.error(e);
			uni.showToast({
				title: '获取失败',
				icon: 'none'
			});
			setTimeout(() => uni.navigateTo({
				url: '/pages/404/404'
			}), 1500);
		}
	}

	async function getIsLike() {
		if (!userStore.isLogin) return;
		try {
			const res = await getTalkLikeService(talk.value.id);
			isLike.value = numberToBoolean(res.data);
		} catch {}
	}

	async function like() {
		if (isRequesting.value) return;
		if (!userStore.isLogin) {
			uni.showToast({
				title: '请先登录',
				icon: 'none'
			});
			return;
		}
		isRequesting.value = true;
		try {
			if (isLike.value) {
				await delTalkLikeService(talk.value.id);
				talk.value.likeCount--;
			} else {
				await addTalkLikeService(talk.value.id);
				talk.value.likeCount++;
			}
			isLike.value = !isLike.value;
		} finally {
			setTimeout(() => (isRequesting.value = false), 800);
		}
	}
</script>

<style lang="scss" scoped>
	/* 容器 */
	.container {
		min-height: 100vh;
		background-color: #f5f5f5;
	}

	.bg {
		padding: 20rpx;
	}

	.page-container {
		background: #fff;
		border-radius: 16rpx;
		padding: 24rpx;
	}

	/* 说说卡片 */
	.talk-item {
		display: flex;
	}

	.talk-meta {
		flex-shrink: 0;
	}

	.user-avatar {
		width: 100rpx;
		height: 100rpx;
		border-radius: 50%;
	}

	.talk-content-wrap {
		flex: 1;
		margin-left: 20rpx;
	}

	.talk-info {
		display: flex;
		flex-direction: column;
		margin-left: 30rpx;
	}

	.talk-user-name {
		font-size: 48rpx;
		font-weight: 600;
		color: #ef89c6;
	}

	.talk-time {
		font-size: 32rpx;
		color: #9499a0;
		margin-top: 4rpx;
	}

	.title {
		font-size: 48rpx;
		font-weight: bold;
	}

	.talk-content {
		margin-top: 16rpx;
		font-size: 32rpx;
		line-height: 1.6;
		word-break: break-all;
		white-space: pre-line;
	}

	.talk-image {
		margin-top: 16rpx;
	}

	.one {
		width: 360rpx;
		height: 360rpx;
		border-radius: 8rpx;
	}

	.multi {
		display: grid;
		grid-template-columns: repeat(2, 1fr);
		gap: 8rpx;
	}

	.pic {
		width: 100%;
		padding-bottom: 100%;
		/* 1:1 */
		position: relative;
	}

	.pic image-with-fallback {
		position: absolute;
		left: 0;
		top: 0;
		width: 100%;
		height: 100%;
		border-radius: 8rpx;
	}

	/* 底部互动栏 */
	.info-bar {
		display: flex;
		align-items: center;
		margin-top: 20rpx;
		gap: 40rpx;
	}

	.info {
		display: flex;
		align-items: center;
		justify-content: center;
		gap: 10rpx;
		font-size: 36rpx;
		color: #9499a0;
	}

	.talk-like.active {
		color: #ff2442;
	}

	/* 点赞动画 */
	@keyframes like {
		0% {
			transform: scale(1);
		}

		50% {
			transform: scale(1.4);
		}

		100% {
			transform: scale(1.1);
		}
	}

	.active .icon {
		animation: like 0.8s ease;
	}
</style>