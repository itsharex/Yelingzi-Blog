<!-- pages/tag/index.vue -->
<template>
	<page-header :title="t('tag')" bg-img="/image/common/images/bg-article.jpg" />
  <view class="tag-page">
    <!-- 自定义页头 -->
    
    <view class="bg">
      <view class="page-container">
        <!-- 标签云 -->
        <view class="tag-header">
          <view class="tag-cloud">
            <view
              v-for="tag in tagList"
              :key="tag.id"
              class="tag-item"
              @tap="changeTags(tag)"
            >
              <!-- 角标 -->
              <view class="badge-box">
                <text class="badge-text">{{ tag.articleCount }}</text>
              </view>

              <!-- 标签 -->
              <view
                class="tag-tag"
                :class="[
                  'tag-' + typeList[tag.id % typeList.length],
                  tag.id === tagData.id ? 'tag-dark' : 'tag-light'
                ]"
              >
                <SvgIcon name="icon-youhuiquan" class="tag-icon" />
                <text class="tag-name">{{ tag.tagName }}</text>
              </view>
            </view>
          </view>

          <!-- 分割线 -->
          <divider-line symbol="2" margin="20" />
        </view>

        <!-- 文章列表 -->
        <article-list :list="articleList" @update:tag="changeTags" />
      </view>
    </view>
  </view>
</template>


<script setup lang="ts">
import PageHeader from '@/components/Layout/PageHeader.vue'
import ArticleList from '@/components/Article/ArticleList.vue';
import DividerLine from '@/components/Hr/DividerLine.vue';
import { onMounted, ref } from "vue";
import { getArticleListByTagService, getTagListService } from '@/api/article';
import { useBlogStore } from "@/stores";
import { t } from '@/utils/i18n'
import type { ArticleCondition } from '@/types/tag';

interface Tag {
  id: number
  tagName: string
  articleCount: number
}

const tagData = ref({
  id: 0,
  tagName: ''
})
const tagList = ref<Tag[]>([])
const typeList = ref(['primary', 'success', 'info', 'warning', 'danger'])
const articleList = ref<ArticleCondition[]>([])

const changeTags = (tag: Tag) => {
  tagData.value = { id: tag.id, tagName: tag.tagName }
  getArticleConditionList()
}

const getTagList = async () => {
  const res = await getTagListService()
  tagList.value = res.data
  if(!tagList.value) return
  tagData.value = { id: tagList.value[0].id, tagName: tagList.value[0].tagName }
  getArticleConditionList()
}

const getArticleConditionList = async () => {
  const res = await getArticleListByTagService(tagData.value.id)
  articleList.value = res.data

}

onMounted(() => {
  getTagList()
});
</script>

<style lang="scss" scoped>
.tag-page {
  min-height: 100vh;
  background-color: #f5f5f5;
}
.page-container {
  padding: 0 30rpx;
}
.tag-header {
  position: sticky;
  top: 0;
  z-index: 100;
  background: #fff;
  padding-bottom: 20rpx;
}
.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 20rpx;
  padding: 20rpx;
}
.tag-item {
  position: relative;
  transition: transform 0.3s;
  &:active {
    transform: scale(1.1);
  }
}
.badge-box {
  position: absolute;
  right: -10rpx;
  top: -10rpx;
  background: #fa5151;
  color: #fff;
  font-size: 20rpx;
  padding: 2rpx 8rpx;
  border-radius: 50%;
  z-index: 2;
}
.tag-tag {
  display: inline-flex;
  align-items: center;
  padding: 8rpx 16rpx;
  border-radius: 8rpx;
  font-size: 28rpx;
  line-height: 1;
}
.tag-icon {
  margin-right: 6rpx;
}
.tag-name {
  white-space: nowrap;
}

/* 颜色主题 */
.tag-primary.tag-light {
  background: #e6f7ff;
  color: #1890ff;
  border: 1px solid #91d5ff;
}
.tag-primary.tag-dark {
  background: #1890ff;
  color: #fff;
}
.tag-success.tag-light {
  background: #f6ffed;
  color: #52c41a;
  border: 1px solid #b7eb8f;
}
.tag-success.tag-dark {
  background: #52c41a;
  color: #fff;
}
.tag-info.tag-light {
  background: #f4f4f5;
  color: #909399;
  border: 1px solid #d3d4d6;
}
.tag-info.tag-dark {
  background: #909399;
  color: #fff;
}
.tag-warning.tag-light {
  background: #fffbe6;
  color: #faad14;
  border: 1px solid #ffe58f;
}
.tag-warning.tag-dark {
  background: #faad14;
  color: #fff;
}
.tag-danger.tag-light {
  background: #fff1f0;
  color: #f5222d;
  border: 1px solid #ffa39e;
}
.tag-danger.tag-dark {
  background: #f5222d;
  color: #fff;
}
</style>
