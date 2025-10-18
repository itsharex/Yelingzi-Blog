<template>
	<FixedLayoutVue :scroll-top="scrollTop" :scroll-height="scrollHeight">
		<view class="home-warp">
			<PageHeader :title="pageTitle" bg-img="/image/common/images/album-bg.jpg"> </PageHeader>
			<view class="bg">
				<scroll-view scroll-y class="page-container">
					<view class="album-container">
						<!-- 有数据 -->
						<view v-if="albumList.length" v-for="album in albumList" :key="album.id"
							:hover-class="'album-hover'" :class="['album-item']" @tap="goDetail(album.id)">
							<!-- 封面图 -->
							<ImageWithFallback class="album-cover" :src="album.albumCover" :preview="false" />
							<!-- 文字信息 -->
							<view class="album-info">
								<text class="album-name">{{ album.albumName }}</text>
								<text class="album-desc">{{ album.albumDesc }}</text>
							</view>
						</view>

						<!-- 空态 -->
						<EmptyData v-else text="相册为空" />
					</view>
				</scroll-view>
			</view>
		</view>
	</FixedLayoutVue>

</template>

<script setup>
	import {
		ref,
		computed,
		onMounted
	} from 'vue';
	import PageHeader from '@/components/Layout/PageHeader.vue'
	import ImageWithFallback from '@/components/Image/ImageWithFallback.vue';
	import EmptyData from '@/components/Empty/Empty.vue';
	import {
		t
	} from '@/utils/i18n';
	import {
		getAlbumListService
	} from '@/api/album'
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

	const pageTitle = computed(() => t('album'));

	const albumList = ref([]);

	// 获取列表
	const getAlbumList = async () => {
		const {
			data
		} = await getAlbumListService();
		albumList.value = data;
	};

	// 跳转详情
	const goDetail = (id) => {
		uni.navigateTo({
			url: `/pages/album/detail?id=${id}`
		});
	};

	onMounted(() => {
		getAlbumList();
	});
</script>

<style lang="scss" scoped>
	.bg {
		min-height: 100vh;
		background-color: #f5f5f5;
	}

	.page-container {
		height: calc(100vh - 88rpx);
		/* 88rpx 为自定义导航栏高度，按需调整 */
	}

	.album-container {
		display: flex;
		flex-wrap: wrap;
	}

	.album-item {
		position: relative;
		width: 100%;
		height: 422rpx;
		margin: 12rpx 8rpx;
		border-radius: 16rpx;
		overflow: hidden;
		background: #000;
	}

	.album-hover {
		opacity: 0.85;
	}

	.album-cover {
		position: absolute;
		width: 100%;
		height: 100%;
		opacity: 0.8;
	}

	.album-info {
		position: absolute;
		left: 0;
		right: 0;
		bottom: 0;
		padding: 36rpx 40rpx;
		color: #fff;
		background: linear-gradient(to top, rgba(0, 0, 0, 0.7), transparent);
	}

	.album-name {
		display: block;
		font-size: 36rpx;
		font-weight: 700;
		padding: 14rpx 0;
		position: relative;
		overflow: hidden;
		white-space: nowrap;
		text-overflow: ellipsis;
	}

	.album-name::after {
		content: '';
		position: absolute;
		bottom: 0;
		left: 0;
		width: 100%;
		height: 4rpx;
		background: #fff;
		transform: translateX(-100%);
		transition: transform 0.35s;
	}

	.album-hover .album-name::after {
		transform: translateX(0);
	}

	.album-desc {
		display: block;
		font-size: 28rpx;
		line-height: 1.5;
		padding-top: 16rpx;
		opacity: 0;
		transform: translateX(100%);
		transition: all 0.35s;
	}

	.album-hover .album-desc {
		opacity: 1;
		transform: translateX(0);
	}
</style>