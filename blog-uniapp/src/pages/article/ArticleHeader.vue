<template>
  <!-- 第一行: 标题 -->
  <view class="article-title">{{ article.title }}</view>

  <!-- 第二行: 分类和标签 -->
  <view class="meta-row category-tag-row">
    <!-- 原创 -->
    <view v-if="article.isOriginal === 1" class="original">
      <text class="tag success">原创</text>
    </view>

    <!-- 分类 -->
    <view class="category-group">
      <text class="tag warning">
        <SvgIcon name="icon-fenlei1" size="24" />
        {{ article.category.categoryName }}
      </text>
    </view>

    <!-- 标签 -->
    <view class="tag-group">
      <text v-for="tag in article.tagList" :key="tag.tagId" class="tag primary">
        <SvgIcon name="icon-youhuiquan" size="24" />
        {{ tag.tagName }}
      </text>
    </view>
  </view>

  <!-- 第三行: 时间信息和字数统计 -->
  <view class="meta-row time-stats-row">
    <view class="stats-group">
      <text class="label">
        <SvgIcon name="icon-bianji" size="24" />
        字数统计
      </text>
      <text class="value">{{ article.content.length }}</text>
    </view>

    <view class="time-group">
      <text class="label">
        <SvgIcon name="icon-read" size="28" />
        浏览量
      </text>
      <text class="value">{{ article.readCount }}</text>
    </view>

    <view v-if="article.updateTime" class="time-group">
      <text class="label">
        <SvgIcon name="icon-rili1" size="24" />
        更新于
      </text>
      <text class="value">{{ formatDate(article.updateTime) }}</text>
    </view>
  </view>
</template>

<script lang="ts" setup>
import type { PropType } from 'vue'
import type { Article } from '@/types/article'
import { formatDate } from '@/utils/common'

/* props */
defineProps({
  article: {
    type: Object as PropType<Article>,
    required: true
  }
})
</script>

<style lang="scss" scoped>
/* 标题 */
.article-title {
  font-size: 48rpx;
  font-weight: 600;
  text-align: center;
  margin-bottom: 30rpx;
}

/* 通用行 */
.meta-row {
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
  gap: 16rpx;
}

/* 第二行：分类/标签 */
.category-tag-row {
  justify-content: center;
  flex-wrap: wrap;

  .original,
  .category-group,
  .tag-group {
    display: flex;
    align-items: center;
    gap: 12rpx;
  }
}

/* 第三行：统计/时间 */
.time-stats-row {
  justify-content: center;
  gap: 40rpx;

  .stats-group,
  .time-group {
    display: flex;
    align-items: center;
    gap: 8rpx;
  }
}

/* 标签样式 */
.tag {
  display: inline-flex;
  align-items: center;
  gap: 6rpx;
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
  color: #fff;

  &.success {
    background: #67c23a;
  }
  &.warning {
    background: #e6a23c;
  }
  &.primary {
    background: #409eff;
  }
}

/* 统计文字 */
.label {
  font-size: 24rpx;
  color: #666;
}
.value {
  font-size: 24rpx;
  color: #333;
  margin-left: 6rpx;
}
</style>