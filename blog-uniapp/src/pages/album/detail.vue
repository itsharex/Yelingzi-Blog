<template>
	<FixedLayoutVue :scroll-top="scrollTop" :scroll-height="scrollHeight">
		<view class="home-warp">
			<PageHeader :title="photoInfo.albumName" :bg-img="photoInfo.albumCover"> </PageHeader>
			<scroll-view scroll-y class="page-container">
				<!-- 图片列表 -->
				<view v-if="imageList.length" class="image-list">
					<view v-for="(src, idx) in imageList" :key="idx" class="img-box">
						<ImageWithFallback class="img" :src="src" mode="widthFix" :list="imageList" />
					</view>
				</view>

				<!-- 空态 -->
				<EmptyData v-else text="当前相册为空" />
			</scroll-view>
		</view>
	</FixedLayoutVue>


</template>

<script lang="ts" setup>
	import {
		ref,
		onMounted, inject
	} from 'vue';
	import PageHeader from '@/components/Layout/PageHeader.vue'
	import EmptyData from '@/components/Empty/Empty.vue';
	import {
		getAlbumDataService
	} from '@/api/album'
	import ImageWithFallback from '@/components/Image/ImageWithFallback.vue';
	import FixedLayoutVue from '@/components/Layout/FixedLayout.vue';
	import { onPageScroll } from '@dcloudio/uni-app'
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

	/* -------------------- 获取页面参数 -------------------- */
	const id = ref(0);
	onMounted(() => {
		const pages = getCurrentPages();
		const curPage = pages[pages.length - 1];
		id.value = Number(curPage.options.id || 0);
		getImageList();
	});

	/* -------------------- 数据 -------------------- */
	const photoInfo = ref({
		id: 0,
		albumCover: '',
		albumName: '',
		photoList: []
	});
	const imageList = ref([]); // 纯 url 数组


	/* -------------------- 请求 -------------------- */
	const getImageList = async () => {
		uni.showLoading({
			title: '加载中'
		});
		const res = await getAlbumDataService(id.value)
		photoInfo.value = res.data
		imageList.value = photoInfo.value.photoList.map(item => item.photoUrl);
		uni.hideLoading()
	};
</script>

<style lang="scss" scoped>
	.page-container {
		background-color: #fff;
	}

	.image-list {
		padding: 20rpx;
	}

	.img-box {
		margin-bottom: 16rpx;
		border-radius: 12rpx;
		overflow: hidden;
	}

	.img {
		width: 100%;
		height: 422rpx;
		display: block;
	}
</style>