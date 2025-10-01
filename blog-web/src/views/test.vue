<template>
  <div class="ll">
    <MdPreview :id="'article'" v-model="article.content" ref="preview" />
  </div>
</template>

<script lang="ts" setup>
import { getArticleByIdService } from '@/api/article';
import { ref } from 'vue';
import 'md-editor-v3/lib/preview.css';
import { MdPreview } from 'md-editor-v3'
const article = ref({
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

const getArticle = async () => {
  try {
    const res = await getArticleByIdService(5);


    article.value = res.data.data;

  } catch (error) {

  }
};
getArticle()
</script>

<style lang="scss" scoped>
.ll {
  overflow: visible;
}
</style>
