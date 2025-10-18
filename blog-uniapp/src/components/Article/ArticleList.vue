<!-- components/ArticleGrid/ArticleGrid.vue -->
<template>
  <view class="article-grid">
    <view
      class="article-item"
      v-for="article in list"
      :key="article.id"
      @tap="goDetail(article.id)"
    >
      <view class="article-cover">
        <ImageWithFallback
          class="cover"
          :src="article.articleCover"
          mode="aspectFill"
        />
      </view>

      <view class="article-info">
        <view class="article-title">{{ article.title }}</view>
        <view class="article-content">{{ article.content }}</view>

        <view class="tag-info">
          <view
            v-for="tag in article.tagList"
            :key="tag.id"
            class="article-tag"
            @tap.stop="emitTagChange(tag)"
          >
            <SvgIcon name="icon-youhuiquan" />
            {{ tag.tagName }}
          </view>
        </view>

        <view class="article-meta">
          <view class="category" @tap.stop="goCategory">
            <SvgIcon name="icon-fenlei1" color="#FFD43B" />
            {{ article.category.categoryName }}
          </view>
          <view class="time">
            <SvgIcon name="icon-rili1" />
            {{ formatDate(article.createTime) }}
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { defineProps, defineEmits } from 'vue'
import { formatDate } from '@/utils/common'
import type { ArticleCondition } from '@/types/tag'
import type { Tag } from '@/types/article'
import ImageWithFallback from '@/components/Image/ImageWithFallback.vue';
const props = defineProps<{
  list: ArticleCondition[]
}>()

const emit = defineEmits<{
  (e: 'update:tag', tag: Tag): void
}>()

/* 跳转到文章详情 */
const goDetail = (id: string | number) => {
  uni.navigateTo({ url: `/pages/article/detail?id=${id}` })
}

/* 跳转到分类页 */
const goCategory = () => {
  uni.navigateTo({ url: '/pages/category/index' })
}

/* 通知父组件 tag 变化 */
const emitTagChange = (tag: Tag) => {
  emit('update:tag', tag)
}

</script>

<style scoped lang="scss">
.article-grid {
  display: grid;
  gap: 30rpx;
  grid-template-columns: 1fr;

  @media (min-width: 600rpx) {
    grid-template-columns: repeat(2, 1fr);
  }
  @media (min-width: 900rpx) {
    grid-template-columns: repeat(3, 1fr);
  }
}

.article-item {
  background: var(--card-bg, #fff);
  border-radius: 10rpx;
  overflow: hidden;
  box-shadow: 0 10rpx 30rpx -15rpx rgba(0, 0, 0, 0.15);
  animation: zoomIn 1s both;
}

.article-cover {
  width: 100%;
  height: 240rpx;
  overflow: hidden;
  .cover {
    width: 100%;
    height: 100%;
    transition: transform 0.5s;
  }
  &:active .cover {
    transform: scale(1.05);
    filter: brightness(0.9);
  }
}

.article-info {
  padding: 16rpx 32rpx;
}

.article-title {
  font-size: 32rpx;
  font-weight: 500;
  margin-bottom: 10rpx;
  color: var(--color-blue, #2d8cf0);
}

.article-content {
  height: 120rpx;
  font-size: 28rpx;
  color: #666;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.tag-info {
  display: flex;
  flex-wrap: wrap;
  gap: 10rpx;
  margin: 10rpx 0;
  .article-tag {
    display: flex;
    align-items: center;
    padding: 6rpx 12rpx;
    background: var(--tag-bg, #fff5e6);
    border-radius: 4rpx;
    font-size: 24rpx;
    color: var(--color-orange, #ff9800);
  }
}

.article-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 26rpx;
  color: var(--text-secondary, #999);
  margin: 10rpx 0;
}

@keyframes zoomIn {
  from {
    opacity: 0;
    transform: scale(0.9);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}
</style>