<template>
	<view>
		<view
		  class="article-item"
		  v-for="(article, index) in articleList"
		  :key="article.id"
		  :class="index % 2 === 1 ? 'even' : ''"
		>
			<!-- 封面图 -->
			<view class="article-cover" @tap="toArticle(article.id)">
				<image class="cover" :src="getImageUrl(article.articleCover)" mode="aspectFill" @error="onImageError" />
			</view>

			<!-- 信息区 -->
			<view class="article-info">
				<view class="article-meta">
					<text class="meta-item">
						<text v-if="article.isTop == 1" class="top iconfont icon-paihang" style="color:#ed6ea0" />
						<text v-if="article.isTop == 1">置顶</text>
					</text>
					<text class="meta-item">
						<text class="iconfont icon-rili1 icon" style="color:#f6416c" />
						{{ formatDate(article.updateTime) }}
					</text>
					<text class="meta-item" v-for="tag in article.tagList" :key="tag.id" @tap="toTag(tag)">
						<text class="iconfont icon-youhuiquan icon" style="color:#00b8a9" />
						{{ tag.tagName }}
					</text>
				</view>

				<!-- 标题 -->
				<view class="article-title" @tap="toArticle(article.id)">
					{{ article.title }}
				</view>

				<!-- 内容 -->
				<view class="article-content">{{ article.content }}</view>
				<view class="article-bottom">
					<!-- 分类 -->
					<view class="article-category" @tap="toCategory(article.category)">
						<text class="iconfont icon-fenlei1 icon" style="color:#ffde7d" />
						{{ article.category.categoryName }}
					</view>

					<!-- 阅读按钮 -->
					<view class="article-btn" @tap="toArticle(article.id)">
						more...
					</view>
				</view>

			</view>
		</view>

		<!-- 加载更多 -->
		<view class="pagination">
			<proButton v-if="loadingArticle" :info="t('loadMore') + '...'" width="120px" before="#ed6ea0"
				after="#9cd0ed" @tap="nextPage" />
			<view v-else style="text-align:center;">{{ t('loadEnd') }}</view>
		</view>
	</view>
</template>

<script lang="ts" setup>
	import { ref, onMounted } from 'vue';
	import { formatDate } from '@/utils/common';
	import { getArticleListByPageService } from '@/api/article';
	import { t } from '@/utils/i18n';
	import { getImageUrl } from '@/utils/image'

	const articleList = ref<any[]>([]);
	const page = ref(1);
	const pageSize = ref(5);
	const total = ref(0);
	const loadingArticle = ref(true);

	const getArticle = async () => {
		const res = await getArticleListByPageService(page.value, pageSize.value);
		const data = res.data;
		total.value = data.total;
		articleList.value.push(...data.data);
		if (page.value * pageSize.value >= total.value) {
			loadingArticle.value = false;
		}
	};

	const nextPage = () => {
		page.value++;
		getArticle();
	};

	const toArticle = (id : number) => {
		uni.navigateTo({ url: `/pages/article/detail?id=${id}` });
	};

	const toTag = (tag : any) => {
		uni.setStorageSync('tagId', tag.id);
		uni.navigateTo({ url: '/pages/tag/tag' });
	};

	const toCategory = (category : any) => {
		uni.setStorageSync('categoryId', category.id);
		uni.navigateTo({ url: '/pages/category/category' });
	};

	const onImageError = (e : any) => {
		e.target.src = '/static/images/default-cover.jpg'; // 占位图
	};

	onMounted(() => {
		getArticle();
	});
</script>

<style lang="scss">
	.article-item {
		display: flex;
		margin: 20rpx 0;
		padding: 0 10rpx;
		border-radius: 10rpx;
		box-shadow: 0 5rpx 30rpx -15rpx rgba(0, 0, 0, 0.1);
		overflow: hidden;
		flex-direction: column;

	}

	.article-cover {
		width: 100%;
		height: 14rem;
		margin: auto;
		clip-path: polygon(0 0, 100% 0, 100% 92%, 0 100%);
		border-radius: 0.625rem 0.625rem 0 0;
	}

	.cover {
		width: 100%;
		height: 100%;
	}

	.article-info {
		padding: 10rpx 0 30rpx 0;
		display: flex;
		flex-direction: column;
		gap: 30rpx;
	}

	.article-meta {
		font-size: 24rpx;
		color: #999;
		display: flex;
		gap: 15rpx;
		justify-content: flex-end;
	}

	.article-title {
		font-size: 32rpx;
		font-weight: bold;
		margin: 10rpx auto;
		color: var(--color-pink);
	}

	.article-content {
		font-size: 28rpx;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 3;
		overflow: hidden;
	}
.article-bottom{
	position: relative;
}
	.article-category {
		display: flex;
		gap: 15rpx;
		justify-content: flex-start;
		align-items: center;
	}

	.article-btn {
		position: absolute;
		bottom: 0rpx;
		right: 0rpx;
		padding: 6rpx 20rpx;
		border-radius: 20rpx 0 20rpx 0;
		background: linear-gradient(to right, #ff6a88, #ff8e53);
		color: #fff;
		font-size: 24rpx;
	}

	.pagination {
		text-align: center;
		margin-top: 20rpx;
	}

	.even .article-meta {
		justify-content: flex-start;
	}

	.even .article-category {
		justify-content: flex-end;
	}

	.even .article-btn {
		right: auto;
		left: 0;
		border-radius: 0 20rpx 0 20rpx;
	}
</style>