	<template>
		<Layout :scroll-top="scrollTop" :scroll-height="scrollHeight">
			<div class="home-warp">
				<!--背景轮播-->
				<BackgroundImages></BackgroundImages>
				<!-- 品牌 -->
				<Brand></Brand>

				<div class="home-bg">
					<div class="home-header"></div>
					<div class="home-main-container">
						<div class="home-left-container">
							<!-- 说说 -->
							<Talk></Talk>
							<!-- 推荐文章 -->
							<Recommend></Recommend>
							<!-- 文章列表 -->
							<ArticleItem></ArticleItem>
						</div>
					</div>
				</div>
			</div>
		</Layout>
	</template>

	<script setup lang="ts">
	import BackgroundImages from '@/components/home/BackgroundImages.vue';
	import Brand from '@/components/home/Brand.vue';
	import Talk from '@/components/home/Talk.vue';
	import Recommend from '@/components/home/Recommend.vue';
	import SideBar from '@/components/SideBar/SideBar.vue';
	import ArticleItem from '@/components/Article/ArticleItem.vue';
	import Layout from '@/components/Layout/Layout.vue'
	import { onPageScroll, onReady } from '@dcloudio/uni-app';
	import { ref } from 'vue';
	const scrollTop = ref(0)
	const windowHeight = ref(uni.getSystemInfoSync().windowHeight)
	const scrollHeight = ref(windowHeight.value + 1)


	onPageScroll((e) => {
		scrollTop.value = e.scrollTop
		
		uni.createSelectorQuery()
			.select('.home-warp')       
			.boundingClientRect(rect => {
				scrollHeight.value =  rect?.height - windowHeight.value
			})
			.exec();
	})
	</script>

	<style lang="scss">
		.home-warp {
			box-sizing: border-box;
		}

		.home-bg {
			width: 100%;
			position: relative;
			z-index: 1;
			transition: margin-top 0.3s ease;
			background-color: var(--grey-1);
		}

		.home-header {
			height: 20px;
		}



		.home-main-container {
			width: 100%;
			flex-direction: column;
		}

		.home-left-container {
			width: 100%;
		}

		.home-right-container {
			position: absolute;
			left: -9999px;
			visibility: hidden;
			pointer-events: none;
			width: 18rem;
			height: 100%;
			overflow: hidden;
		}
	</style>