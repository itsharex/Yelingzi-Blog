<template>

  <!-- 使用 CSS Grid 布局 -->
  <div class="article-grid">
    <div class="article-item" v-for="article of props.list" :key="article.id">
      <div class="article-cover">
        <router-link :to="`/article/${article.id}`">
          <ImageWithFallback class="cover" :src="article.articleCover" />
        </router-link>
      </div>
      <div class="article-info">
        <div class="article-title">
          <router-link :to="`/article/${article.id}`">{{ article.title }}</router-link>
        </div>
        <div class="article-content">
          {{ article.content }}
        </div>
        <div class="tag-info">
          <div @click="emitTagChange(tag)" class="article-tag pointer" v-for="tag in article.tagList" :key="tag.id">
            <SvgIcon name="icon-youhuiquan" />
            {{ tag.tagName }}
          </div>
        </div>
        <div class="article-meta">

          <div @click="router.push('/category')" class="category">
            <SvgIcon name="icon-fenlei1" style="color: #FFD43B;" />
            {{ article.category.categoryName }}
          </div>
          <span class="time">
            <SvgIcon name="icon-rili1" />
            {{ formatDate(article.createTime) }}
          </span>
        </div>

      </div>
    </div>
  </div>

</template>

<script setup lang="ts">
import { type PropType } from 'vue';
import { formatDate } from '@/utils/common'
import ImageWithFallback from '@/components/Image/ImageWithFallback.vue';
import { useRouter } from 'vue-router';
import type { Tag } from '@/types/article';
import type { ArticleCondition } from '@/types/tag';

const router = useRouter()

const props = defineProps({
  list: {
    type: Array as PropType<ArticleCondition[]>,
    required: true
  }
})
const emit = defineEmits(['update:tag'])

// 触发事件通知父组件
const emitTagChange = (tag: Tag) => {
  emit('update:tag', tag)
}



</script>

<style lang="scss" scoped>
.article-grid {
  display: grid;
  gap: 15px;
  grid-template-columns: repeat(1, 1fr);

  @media (min-width: 600px) {
    grid-template-columns: repeat(2, 1fr);
  }

  @media (min-width: 900px) {
    grid-template-columns: repeat(3, 1fr);
  }
}

.article-item {
  display: flex;
  flex-direction: column;
  background: var(--card-bg);
  border-radius: 0.625rem;
  overflow: hidden;
  box-shadow: 0 0.625rem 1.875rem -0.9375rem var(--box-bg-shadow);
  transition: all 0.2s ease-in-out;
  animation: zoomIn 1s both;

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 0 2rem var(--box-bg-shadow);
  }
}

.article-cover {
  width: 100%;
  height: 12rem;
  overflow: hidden;

  .cover {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: all 0.5s;
  }

  &:hover .cover {
    transform: scale(1.1);
    filter: brightness(0.9);
  }
}

.article-info {
  padding: 8px 16px;
}

.article-title {
  font-size: 1.3rem;
  font-weight: 500;
  margin-bottom: 0.5rem;

  a {
    color: var(--color-blue);
    text-decoration: none;

    &:hover {
      color: var(--color-red);
    }
  }
}

.article-content {
  height: 60px;
  font-size: 14px;
}

.article-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.9rem;
  color: var(--text-secondary);
  margin: 5px 0;
}

.tag-info {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin: 0;

  .article-tag {
    display: flex;
    align-items: center;
    padding: 0.3rem 0;
    background: var(--tag-bg);
    border-radius: 4px;
    font-size: 0.8rem;
    color: var(--color-orange);
    text-decoration: none;

    &:hover {
      background: var(--tag-hover-bg);
    }
  }
}

.time {
  display: flex;
  align-items: center;
}

.category {
  &:hover {
    color: var(--color-red);
  }
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
