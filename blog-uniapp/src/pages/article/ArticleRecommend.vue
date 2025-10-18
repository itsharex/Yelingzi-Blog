<!-- pages/index/index.vue -->
<template>
  <view class="section" v-if="articleRecommend.length">
    <view class="h3">
      <text class="iconfont icon-star" />
      推荐文章
    </view>

    <view class="post-list">
      <view
        v-for="(article, idx) in articleRecommend"
        :key="article.id"
        class="post-item"
        :style="{ '--delay': idx * 100 + 'ms' }"
        @tap="goDetail(article.id)"
      >
        <image
          :src="article.articleCover"
          mode="aspectFill"
          class="art-img"
          @error="onImgError($event, idx)"
        />
        <view class="post-meta">
          <text class="post-title">{{ article.title }}</text>
          <text class="post-time">
            <text class="iconfont icon-rili1" />
            {{ article.createTime }}
          </text>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { getRecommendArticleListService } from '@/api/article'
import type { ArticleRecommend } from '@/types/article'

const articleRecommend = ref<ArticleRecommend[]>([])

const getArticleRecommend = async () => {
  articleRecommend.value = []
  const res = await getRecommendArticleListService()
  articleRecommend.value = res.data.data
}

/* 跳转详情页 */
const goDetail = (id: string | number) => {
  uni.navigateTo({ url: `/pages/article/detail?id=${id}` })
}

/* 图片加载失败兜底 */
const onImgError = (e: any, idx: number) => {
  articleRecommend.value[idx].articleCover = '/static/images/default.jpg'
}

onMounted(() => {
  getArticleRecommend()
})
</script>

<style lang="scss" scoped>
/* 如原来项目里有 SCSS 变量，可手动抄一份到本文件顶部 */
$spacing-lg: 20rpx;
$border-radius-lg: 12rpx;

.section {
  margin: 20rpx 0 $spacing-lg;
  background: #f5f5f7; // var(--grey-1)
  border-radius: $border-radius-lg;
  padding: 24rpx;

  .h3 {
    font-size: 34rpx;
    font-weight: 600;
    color: #6366f1;
    margin: 0 0 24rpx;
    padding-bottom: 16rpx;
    border-bottom: 2rpx solid rgba(99, 102, 241, 0.1);
    display: flex;
    align-items: center;
    gap: 10rpx;

    &::before {
      content: '';
      width: 8rpx;
      height: 36rpx;
      background: linear-gradient(to bottom, #6366f1, #8b5cf6);
      border-radius: 4rpx;
      margin-right: 16rpx;
    }
  }

  .post-list {
    display: flex;
    flex-direction: column;
    gap: 40rpx;

    .post-item {
      display: flex;
      gap: 32rpx;
      align-items: center;
      transition: all 0.3s ease;

      &:active {
        transform: translateX(8rpx);
      }

      .art-img {
        width: 200rpx;
        height: 140rpx;
        border-radius: 12rpx;
        flex-shrink: 0;
        box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.08);
      }

      .post-meta {
        flex: 1;
        min-width: 0;
        display: flex;
        flex-direction: column;
        justify-content: space-between;

        .post-title {
          font-size: 30rpx;
          color: #333; // var(--grey-9)
          line-height: 1.4;
          overflow: hidden;
          text-overflow: ellipsis;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          margin-bottom: 12rpx;
        }

        .post-time {
          font-size: 24rpx;
          color: #8b8b8b;
          display: flex;
          align-items: center;
          gap: 8rpx;
        }
      }
    }
  }
}
</style>