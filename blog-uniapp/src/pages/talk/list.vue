<template>
	<FixedLayoutVue :scroll-top="scrollTop" :scroll-height="scrollHeight">
		<view class="home-warp">
			<page-header :title="t('talk')" bg-img="/image/common/images/talks-bg.jpg" />
			<view class="page">
				<!-- 自定义头部 -->

				<view class="bg">
					<!-- 列表 -->
					<view v-if="talkList.length" class="page-container">
						<view v-for="talk in talkList" :key="talk.id" class="talk-item">
							<!-- 用户信息 -->
							<view class="talk-meta">
								<YlAvatarVue class="avatar" :src="talk.userAvatar" mode="aspectFill" />
								<view class="talk-info">
									<text class="name">{{ talk.nickname }}</text>
									<view class="row">
										<view v-if="talk.isTop === 1" class="top">
											<SvgIcon name="icon-paihang" class="talk-top"></SvgIcon>
											置顶
										</view>
										<text class="time">{{ formatDateWithRelativeTime(talk.createTime) }}</text>
									</view>
								</view>
							</view>

							<!-- 内容 -->
							<view class="content-box" @tap="toDetail(talk.id)">
								<rich-text class="title" :nodes="talk.title" />
								<rich-text class="content" :nodes="talk.content" />
							</view>

							<!-- 图片 -->
							<view v-if="talk.imageUrl.length" class="img-box">
								<!-- 只有 1 张 -->
								<view v-if="talk.imageUrl.length === 1" class="one">
									<image-with-fallback class="single" :src="talk.imageUrl.at(0)" mode="aspectFill" />
								</view>

								<!-- 多于 1 张：3 列，最多 9 -->
								<view v-else class="multi">
									<view v-for="(url, idx) in talk.imageUrl.slice(0, 9)" :key="idx" class="pic">
										<image-with-fallback :src="url" mode="aspectFill" />
									</view>
								</view>
							</view>
						</view>

						<!-- 加载更多 -->
						<view class="load-wrap">
							<view v-if="loadingTalk" class="load-btn" @tap="nextPage">
								{{ t('loadMore') }}...
							</view>
							<view v-else class="load-end">{{ t('loadEnd') }}</view>
						</view>
					</view>

					<!-- 空态 -->
					<my-empty v-else :description="t('emptyTalk')" />
				</view>
			</view>
		</view>
	</FixedLayoutVue>


</template>

<script setup>
	import {
		ref,
		reactive,
		onMounted
	} from 'vue'
	import {
		t
	} from '@/utils/i18n'
	import {
		formatDateWithRelativeTime
	} from '@/utils/common'
	import {
		getTalkListByPageService
	} from '@/api/talk'
	import ImageWithFallback from '@/components/Image/ImageWithFallback.vue';
	import PageHeader from '@/components/Layout/PageHeader.vue'
	import MyEmpty from "@/components/Empty/Empty.vue";
	import YlAvatarVue from '@/components/Image/YlAvatar.vue'
	import FixedLayoutVue from '@/components/Layout/FixedLayout.vue';
	import {
		onPageScroll
	} from '@dcloudio/uni-app'
	const scrollTop = ref(0)
	const windowHeight = ref(uni.getSystemInfoSync().windowHeight)
	const scrollHeight = ref(windowHeight.value + 1)


	onPageScroll((e) => {
		scrollTop.value = e.scrollTop

		uni.createSelectorQuery()
			.select('.home-warp')
			.boundingClientRect(rect => {
				scrollHeight.value = rect?.height - windowHeight.value
			})
			.exec();
	})

	const talkList = ref([])
	const loadingTalk = ref(true)
	const data = reactive({
		total: 100,
		page: 1,
		pageSize: 5
	})

	const getList = async () => {
		const res = await getTalkListByPageService(data.page, data.pageSize)
		data.total = res.data.total
		const newTalks = res.data.data.map(t => ({
			...t,
			imageUrl: (t.imageUrl || []).filter(u => u && u.trim() && u !== 'null')
		}))
		talkList.value = [...talkList.value, ...newTalks]
		if (data.page * data.pageSize >= data.total) loadingTalk.value = false
	}

	const nextPage = () => {
		data.page += 1
		getList()
	}

	const toDetail = id => {
		uni.navigateTo({
			url: `/pages/talk/detail?id=${id}`
		})
	}

	onMounted(() => {
		talkList.value = []
		getList()
	})
</script>

<style lang="scss" scoped>
	.page {
		min-height: 100vh;
		background: #f6f6f6;
	}

	.bg {
		padding: 24rpx;
	}

	.page-container {
		padding-bottom: 40rpx;
	}

	.talk-item {
		background: #fff;
		border-radius: 16rpx;
		padding: 24rpx;
		margin-bottom: 24rpx;
		box-shadow: 0 6rpx 20rpx 0 rgba(0, 0, 0, .06);
	}

	.talk-meta {
		display: flex;
		align-items: center;
	}

	.avatar {
		width: 80rpx;
		height: 80rpx;
		border-radius: 10rpx;
	}

	.talk-info {
		margin-left: 16rpx;
		flex: 1;
	}

	.name {
		font-size: 32rpx;
		font-weight: 600;
		color: #ef89c6;
	}

	.row {
		display: flex;
		align-items: center;
		margin-top: 4rpx;
	}

	.top {
		color: #ee6b8b;
		font-size: 24rpx;
		margin-right: 10rpx;
	}

	.time {
		font-size: 24rpx;
		color: #9499a0;
	}

	.content-box {
		margin-top: 16rpx;
	}

	.title {
		font-size: 36rpx;
		font-weight: bold;
	}

	.content {
		font-size: 30rpx;
		line-height: 1.6;
		margin-top: 8rpx;
	}

	.img-box {
		margin-top: 16rpx;
	}

	.one {
		width: 360rpx;
		height: 360rpx;
		border-radius: 8rpx;
	}

	.multi {
		display: grid;
		grid-template-columns: repeat(3, 1fr);
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

	.load-wrap {
		text-align: center;
		padding: 30rpx 0;
	}

	.load-btn {
		display: inline-block;
		padding: 12rpx 40rpx;
		font-size: 28rpx;
		color: #fff;
		border-radius: 30rpx;
		background: linear-gradient(90deg, #ed6ea0, #9cd0ed);
	}

	.load-end {
		font-size: 28rpx;
		color: #999;
	}
</style>