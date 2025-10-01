<template>
  <!-- 第一行: 标题 -->
  <h1 class="article-title">{{ article.title }}</h1>

  <!-- 第二行: 分类和标签 -->
  <div class="meta-row category-tag-row">
    <div v-if="article.isOriginal === 1" class="original">
      <el-tag type="success">
        原创
      </el-tag>
    </div>
    <div class="category-group">
      <span class="category">
        <el-tag type="warning">
          <SvgIcon name="icon-fenlei1" size="12" />
          {{ article.category.categoryName }}
        </el-tag>
      </span>
    </div>
    <div class="tag-group">
      <span v-for="tag of article.tagList" class="tag">
        <el-tag type="primary">
          <SvgIcon name="icon-youhuiquan" size="12" />
          {{ tag.tagName }}
        </el-tag>
      </span>
    </div>
  </div>

  <!-- 第三行: 时间信息和字数统计 -->
  <div class="meta-row time-stats-row">
    <div class="stats-group">
      <el-statistic :value="article.content.length">
        <template #title>
          <SvgIcon name="icon-bianji" size="12" />
          字数统计
        </template>
      </el-statistic>
    </div>
    <div class="time-group" v-if="article.updateTime">
      <el-statistic :value="article.readCount">
        <template #title>
          <SvgIcon name="icon-read" size="16" style="vertical-align: -0.3em;" />
          浏览量
        </template>
      </el-statistic>
    </div>
    <div class="time-group" v-if="article.updateTime">
      <el-statistic :value="formatDate(article.updateTime)">
        <template #title>
          <SvgIcon name="icon-rili1" size="12" class="icon" />
          更新于
        </template>
      </el-statistic>
    </div>
  </div>
</template>

<script lang="ts" setup>
import type { Article } from '@/types/article';
import type { PropType } from 'vue';
import { formatDate } from '@/utils/common'

const props = defineProps({
  article: {
    type: Object as PropType<Article>,
    required: true
  }
})
</script>

<style lang="scss" scoped>
.article-title {
  font-size: 2.8rem;
  margin-bottom: 1.5rem;
}

.meta-row {
  display: flex;
  align-items: center;
  margin-bottom: 1rem;
  gap: 1rem;

  &.category-tag-row {
    justify-content: center;
    flex-wrap: wrap;

    .original {
      display: flex;
      align-items: center;
      gap: 0.8rem;
    }

    .category-group {
      display: flex;
      align-items: center;
      gap: 0.8rem;
    }

    .tag-group {
      display: flex;
      align-items: center;
      gap: 0.8rem;
    }
  }

  &.time-stats-row {
    display: flex;
    justify-content: center;
    width: 100%;
    gap: 2rem;

    .stats-group,
    .time-group {
      display: flex;
      align-items: center;
    }
  }
}
</style>
