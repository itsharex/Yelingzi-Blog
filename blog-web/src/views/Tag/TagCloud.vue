<template>
  <CommonLayout :title="t('tag')" :bgImg="bgImg" />
  <div class="bg">
    <div class="page-container">
      <div class="tag-header">
        <div class="tag-cloud" v-if="tagList.length > 0">
          <div class="tag-item" v-for="tag in tagList" :key="tag.id" @click="changeTags(tag)">
            <el-badge :value="tag.articleCount" :type="typeList.at(tag.id % typeList.length)">
              <el-tag :type="typeList.at(tag.id % typeList.length)" :effect="tag.id === tagData.id ? 'dark' : 'light'">
                <SvgIcon name="icon-youhuiquan" class="tag-icon" />
                {{ tag.tagName }}
              </el-tag>
            </el-badge>
          </div>
        </div>
        <div v-else>
          <Empty :loading="loading">
            <div class="empty-action pointer">
              <proButton v-if="!loading" info="重新加载" before="#ed6ea0" after="#9cd0ed" width="120px" @click="getTagList">
              </proButton>
            </div>
          </Empty>
        </div>
        <DividerLine symbol="2" margin="20" />
      </div>

      <Tag :list="articleList" :loading="tagLoading" @update:tag="changeTags"></Tag>
    </div>
  </div>
</template>

<script setup lang="ts">
import bgImg from '@/assets/images/bg-article.jpg'
import CommonLayout from '@/components/Layout/CommonLayout.vue';
import Tag from './Tag.vue';
import DividerLine from '@/components/Hr/DividerLine.vue';
import { onMounted, ref } from "vue";
import { getArticleListByTagService, getTagListService } from '@/api/article';
import { useBlogStore } from "@/stores";
import Empty from '@/components/Empty/Empty.vue';
import { t } from '@/utils/i18n'
import type { ArticleCondition } from '@/types/tag';
import proButton from '@/components/Button/proButton.vue';

const loading = ref(true)
const blog = useBlogStore()
const tagId = blog.tagId
const tagLoading = ref(true)
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
  tagData.value.id = tag.id
  tagData.value.tagName = tag.tagName
  getArticleConditionList()
}

const getTagList = async () => {
  loading.value = true
  tagLoading.value = true
  try {
    const res = await getTagListService()
    tagList.value = res.data.data
    if (tagId === 0 && tagList.value.length > 0) {
      tagData.value.id = tagList.value.at(0)!.id
      tagData.value.tagName = tagList.value.at(0)!.tagName
      getArticleConditionList()
    } else {
      tagData.value.id = tagId
    }
    blog.tagId = 0
  } catch {
    loading.value = false
    tagLoading.value = false
  }

}

const getArticleConditionList = async () => {
  try {
    const res = await getArticleListByTagService(tagData.value.id)
    articleList.value = res.data.data
    console.log(articleList.value)
  } catch {
    tagLoading.value = false
  }

}

onMounted(() => {
  getTagList()
});
</script>

<style lang="scss" scoped>
.page-container {
  min-height: 700px;
  position: relative;
}

.tag-header {
  position: sticky;
  top: 74px;
  z-index: 100;
  background-color: var(--grey-1);
}

.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 20px;
  padding: 20px;
}

.tag-item {
  transition: all 0.3s;

  &:hover {
    transform: scale(1.1);
  }
}

.empty-action {
  display: flex;
  justify-content: center;
  margin-bottom: 24px;
}
</style>
