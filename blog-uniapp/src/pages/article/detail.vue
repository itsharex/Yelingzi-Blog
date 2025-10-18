<template>
	<FixedLayoutVue :scroll-top="scrollTop" :scroll-height="scrollHeight">
		<view class="article-bg">
		  <view class="article-header">
		    <ArticleHeader :article="article"></ArticleHeader>
		  </view>
		
		  <!-- 内容解析 -->
		  <view class="article-main">
			  <mp-html :content="htmlContent" />
		  </view>
		  
		  <view class="article-footer">
			  <ArticleFooter :article="article" @like="handleParentLike"></ArticleFooter>
		  </view>
		
		  <!-- 目录侧边栏 -->
		
		</view>
	</FixedLayoutVue>


</template>

<script lang="ts" setup>
	import { ref, onMounted, onUnmounted, watch, inject, type Ref, computed } from 'vue'
	import type { Article } from '@/types/article'
	import { numberToBoolean } from '@/utils/common'
	import snowHr from '@/components/Hr/DividerLine.vue'
	import ArticleFooter from './ArticleFooter.vue'
	import ArticleRecommend from './ArticleRecommend.vue'
	import { addArticleLikeService, delArticleLikeService, getArticleByIdService, getArticleLikeService } from '@/api/article'
	import { useUserStore, useBlogStore } from '@/stores';
	import ArticleHeader from './ArticleHeader.vue';
	import { onLoad, onUnload } from '@dcloudio/uni-app';
	import { marked } from 'marked'
	import mpHtml from 'mp-html/dist/uni-app/components/mp-html/mp-html'
	import FixedLayoutVue from '@/components/Layout/FixedLayout.vue';
		import { onPageScroll } from '@dcloudio/uni-app'
		const scrollTop = ref(0)
		const windowHeight = ref(uni.getSystemInfoSync().windowHeight)
		const scrollHeight = ref(windowHeight.value + 1)
	
	
		onPageScroll((e) => {
			scrollTop.value = e.scrollTop
	
			uni.createSelectorQuery()
				.select('.article-bg')
				.boundingClientRect(rect => {
					scrollHeight.value = rect?.height - windowHeight.value
				})
				.exec();
		})

	const blogStore = useBlogStore();
	const isDark = computed(() => blogStore.blogInfo.isDark);
	const userStore = useUserStore()
	const id = ref<number>(0)
	// 响应式数据
	const article = ref<Article>({
		id: 0,
		title: '',
		category: { id: 0, categoryName: '' },
		tagList: [],
		likeCount: 0,
		starCount: 0,
		createTime: '',
		updateTime: '',
		content: '',
		isOriginal: 0,
		userId: 0,
		originalUrl: '',
		nickname: '',
		readCount: 0,
		commentCount: 0,
		userAvatar: '',
		isTop: 0,
		state: 0,
		articleCover: '',
		isLike: false,
		isStar: false
	})

	const showShareMenu = ref(false)
	const loading = ref(false)
	const showSidebar = ref(false)
	const previewTheme = computed(() => userStore.previewTheme)
	const isRequesting = ref(false)
	const readProgress = ref(0)
	const totalHeadings = ref(0);
	const layoutRef = ref(null)
	const scrollElement = computed(() => {
			return layoutRef.value || undefined;
		});
	const htmlContent = ref('')
		
	/* --------------- 生命周期 --------------- */
	onLoad((query : any) => {
		id.value = Number(query.id)
		getArticle()
	})
	onUnload(() => { })
	/* --------------- 数据 --------------- */
	const getArticle = async () => {
		loading.value = true
		try {
			const res = await getArticleByIdService(id.value)
			if (res.code === 404) {
				uni.redirectTo({ url: '/pages/404/404' })
				return
			}
			article.value = res.data
			 htmlContent.value = await marked(article.value.content)
			if (userStore.accessToken) await getArticleLike()
		} finally {
			loading.value = false
		}
	}

	const getArticleLike = async () => {
		const res = await getArticleLikeService(article.value.id)
		article.value.isLike = numberToBoolean(res.data)
	}

	const handleParentLike = async () => {

		if (isRequesting.value) return;
		if (!userStore.getIsLogin()) {
			uni.showToast({ title: '请先登录', icon: 'none' })
			return;
		}
		isRequesting.value = true;
		try {
			if (article.value.isLike) {
				await delArticleLikeService(article.value.id);
				article.value.likeCount -= 1;
			} else {
				await addArticleLikeService(article.value.id);
				article.value.likeCount += 1;
			}
			article.value.isLike = !article.value.isLike;
		} finally {
			setTimeout(() => {
				isRequesting.value = false;
			}, 600);
		}
	};

	// 侧边栏切换
	const toggleSidebar = () => {
		showSidebar.value = !showSidebar.value
	}

	/**
	 * 分享
	 */
	const toggleShareMenu = () => {
		showShareMenu.value = !showShareMenu.value
	}
	/**
	 * 关闭分享
	 */
	const closeShareMenu = () => {
		showShareMenu.value = false
	}

	/* --------------- 复制链接 --------------- */
	const copyLink = () => {
		const pages = getCurrentPages()
		const cur = pages[pages.length - 1]
		const url = `/${cur?.route}?id=${id.value}`
		uni.setClipboardData({
			data: url,
			success: () => uni.showToast({ title: '链接已复制' })
		})
	}
</script>

<style lang="scss" scoped>
	.article-bg {
		background-color: var(--article-bg);
		z-index: -1;
	}

	.article-header {
		height: 20vh;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		text-align: center;
		min-height: 180px;
	}
	
	.article-main{
		box-sizing: border-box;
		padding: 0 25px;
	}
	
	.article-footer{
		padding: 20px;
	}



</style>