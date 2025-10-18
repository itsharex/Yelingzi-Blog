<template>
	<FixedLayoutVue :scroll-top="scrollTop" :scroll-height="scrollHeight">
		<view class="home-warp">
			<PageHeader :title="t('category')" bg-img="/image/common/images/bg-article.jpg" />
			<view class="page-container">
				<view class="categories-page">
					<!-- 分类导航 -->
					<view class="categories-nav">
						<view v-for="category in categories" :key="category.id" class="category-tab pointer"
							:class="{ active: activeCategory === category.id }" @tap="scrollToCategory(category.id)">
							<SvgIcon name="icon-fenlei" />
							<span>{{ category.categoryName }}</span>
						</view>
					</view>
			
					<!-- 分类列表 -->
					<view v-for="item in categories" :key="item.categoryName">
						<!-- 这里把 :ref 去掉，改用 id 标识 -->
						<view class="category-group" :data-category="item.categoryName" :id="`cg-${item.categoryName}`">
							<h2 class="category-name">
								<SvgIcon name="icon-fenlei" />
								{{ item.categoryName }}
								<span class="post-count">{{ item.posts.length }} 篇文章</span>
							</h2>
			
							<!-- 分割线同样用 id 标记 -->
							<view :id="'line-' + item.id">
								<DividerLine margin="0px" symbol="2" />
							</view>
			
							<view class="posts-list">
								<view v-for="post in item.posts" :key="post.id" class="post-item" @tap="goToPost(post.id)">
									<view class="post-date">
										<span
											class="month">{{ getYear(post.createTime) }}.{{ getMonth(post.createTime) }}</span>
										<span class="day">{{ getDate(post.createTime) }}</span>
									</view>
									<view class="post-info">
										<h3 class="post-title">{{ post.title }}</h3>
									</view>
								</view>
							</view>
						</view>
					</view>
			
					<!-- 加载状态 -->
					<view v-if="loading" class="loading-state">
						加载中...
					</view>
				</view>
			</view>
		</view>		
	</FixedLayoutVue>
	
</template>

<script lang="ts" setup>
	import { ref, reactive, onMounted, onBeforeUnmount, getCurrentInstance, nextTick } from 'vue'
	import PageHeader from '@/components/Layout/PageHeader.vue'
	import DividerLine from '@/components/Hr/DividerLine.vue'
	import { getDate, getMonth, getYear } from '@/utils/common'
	import { getArticleListFromCategoryService } from '@/api/article'
	import type { Archives } from '@/types/article'
	import { t } from '@/utils/i18n'
	import { onPageScroll, onLoad, onUnload } from '@dcloudio/uni-app'
	import FixedLayoutVue from '@/components/Layout/FixedLayout.vue';
		const scrollTop = ref(0)
		const windowHeight = ref(uni.getSystemInfoSync().windowHeight)
		const scrollHeight = ref(windowHeight.value + 1)
	

	interface Category {
		id : number
		categoryName : string
		posts : Archives[]
	}

	const categories = ref<Category[]>([])
	const activeCategory = ref<number>(0)
	const loading = ref(false)
	const ins = getCurrentInstance()

	/* --------- 跨端跳转 --------- */
	const goToPost = (id : number) => {
		uni.navigateTo({ url: `/pages/article/detail?id=${id}` })
	}

	/* ---------------- 滚动到指定分类 ---------------- */
const scrollToCategory = async (name: number) => {
  activeCategory.value = name

  uni.createSelectorQuery()
    .in(ins)                       // 作用域
    .select(`#line-${name}`)
    .boundingClientRect()
    .selectViewport().scrollOffset() // 拿到当前已滚距离
    .exec(([rect, scroll]: any[]) => {
      if (!rect) return
      uni.pageScrollTo({
        scrollTop: scroll.scrollTop + rect.top - 90, // 90 = 顶部悬浮区高度
        duration: 300
      })
    })
}

	/* 滚动时高亮当前分类 */
	let scrollTimer : number | null = null
	onPageScroll((e) => {
		scrollTop.value = e.scrollTop
			
		uni.createSelectorQuery()
			.select('.home-warp')
			.boundingClientRect(rect => {
				scrollHeight.value = rect?.height - windowHeight.value
			})
			.exec();
		if (scrollTimer) clearTimeout(scrollTimer)
		scrollTimer = setTimeout(() => {
			const query = uni.createSelectorQuery().in(ins)
			categories.value.forEach(c => {
				query.select(`#line-${c.id}`).boundingClientRect()
			})
			query.exec((res : any[]) => {
				const idx = res.findIndex(v => v && v.top > 90)
				const cur = idx === -1 ? categories.value.at(-1) : categories.value[idx - 1]
				if (cur) activeCategory.value = cur.id
			})
		}, 50)
	})



	/* --------- 获取数据 --------- */
	const fetchCategories = async () => {
		if (categories.value.length) categories.value.splice(0)
		loading.value = true
		try {
			const res = await getArticleListFromCategoryService()
			for (const d of res.data) {
				categories.value.push({
					id: d.id,
					categoryName: d.categoryName,
					posts: d.simpleArticleList
				})
			}
		} finally {
			loading.value = false
		}
		await nextTick()
	}

	/* --------- 生命周期 --------- */
	/* 页面加载时拉数据 */
	onLoad(async () => {
		await fetchCategories()
	})
</script>

<style lang="scss">
	.page-container {
		padding: 20rpx 30rpx;
		background-color: #fafafa;
		width: auto;
	}

	.categories-page {
		width: 100%;
	}

	.category-group {
		opacity: 1 !important; // 强制显示初始状态
		transform: none !important;

		// 动画执行前保持隐藏
		&.gsap-animating {
			opacity: 0;
			transform: translateY(50px);
		}
	}

	.posts-list {
		position: relative;
		z-index: 1;
	}


	.categories-nav {
		position: sticky;
		top: 64px;
		margin: -1rem;
		margin-bottom: 1rem;
		padding: 0.5rem 1rem;
		z-index: 10;
		background: #fafafa;
		border-radius: 30rpx;
		display: flex;
		gap: 1rem;
		justify-content: center;
		flex-wrap: wrap;

		.category-tab {
			padding: 10rpx 20rpx;
			border-radius: 30rpx;
			background: var(--hover-bg);
			color: var(--grey-8);
			transition: all 0.3s ease;
			white-space: nowrap;
			display: flex;
			align-items: center;
			gap: 12rpx;
			font-size: 0.9em;

			&.active {
				background: #49b1f5;
				color: #fafafa;
			}
		}
	}

	.category-group {
		margin-bottom: 3rem;
		scroll-margin-top: 100px;
	}

	.post-item {
		display: flex;
		align-items: center;
		gap: 1.5rem;
		padding: 1rem;
		transition: all 0.3s ease;
		color: var(--grey-8);

		.post-date {
			display: flex;
			flex-direction: column;
			align-items: center;
			padding: 8rpx;
			border: 1px solid var(--grey-8);
			border-radius: 10rpx;
			transition: all 0.3s ease;
			min-width: 60px;

			.month {
				font-size: 0.9em;
				color: var(--grey-8);
			}

			.day {
				font-size: 1.2em;
				font-weight: 600;
				color: var(--grey-8);
			}

			.post-info {
				flex: 1;
				min-width: 0;

			}
		}
	}

	.loading-state {
		text-align: center;
		padding: 2rem;
		color: var(--text-secondary);
	}

	.category-name {
		display: flex;
		align-items: center;
		/* 垂直居中 */
		gap: 8px;
		/* 元素间距 */
		color: var(--color-blue);
	}

	.post-count {
		margin-left: auto;
		font-size: 0.8em;
		opacity: 0.8;
		font-weight: normal;
		color: #ed6ea0;
	}
</style>