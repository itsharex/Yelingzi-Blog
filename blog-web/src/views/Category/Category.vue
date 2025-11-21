<template>
  <CommonLayout :title="t('category')" :bg-img="bgImg" />
  <div class="bg">
    <div class="page-container">

      <div v-if="categories.length > 0">
        <!-- 分类导航 -->
        <div class="categories-nav">
          <div v-for="category in categories" :key="category.categoryName" class="category-tab pointer"
            :class="{ active: activeCategory === category.categoryName }"
            @click="scrollToCategory(category.categoryName)">
            <SvgIcon name="icon-fenlei" />
            <span>{{ category.categoryName }}</span>
          </div>
        </div>

        <!-- 分类列表 -->
        <div v-for="item in categories">
          <div :key="item.categoryName" class="category-group" :data-category="item.categoryName"
            :ref="(el) => { if (el) categoryRefs[item.categoryName] = el as HTMLElement }">

            <h2 class="category-name">
              <SvgIcon name="icon-fenlei" />
              {{ item.categoryName }}
              <span class="post-count">{{ item.posts.length }} 篇文章</span>
            </h2>

            <div :ref="(el) => { if (el) dividerRefs[item.categoryName] = el as HTMLElement }">
              <DividerLine margin="0px" symbol="2"></DividerLine>
            </div>

            <div class="posts-list">
              <div v-for="post in item.posts" :key="post.id" class="post-item" @click="goToPost(post.id)"
                v-pio="{ text: `${post.title}`, type: 'read' }">
                <div class="post-date">
                  <span class="month">{{ getYear(post.createTime) }}.{{ getMonth(post.createTime) }}</span>
                  <span class="day">{{ getDate(post.createTime) }}</span>
                </div>
                <div class="post-info">
                  <h3 class="post-title">{{ post.title }}</h3>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 加载状态 -->
      <div v-else class="loading-state">
        <Empty :loading="loading">
          <div class="empty-action pointer">
            <proButton v-if="!loading" info="重新加载" before="#ed6ea0" after="#9cd0ed" width="120px"
              @click="fetchCategories">
            </proButton>
          </div>
        </Empty>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted, onBeforeUnmount } from 'vue'
import CommonLayout from '@/components/Layout/CommonLayout.vue'
import bgImg from '@/assets/images/bg-article.jpg'
import DividerLine from '@/components/Hr/DividerLine.vue'
import { getDate, getMonth, getYear } from '@/utils/common'
import { getArticleListFromCategoryService } from '@/api/article'
import type { Archives } from '@/types/article'
import { t } from '@/utils/i18n'
import Empty from '@/components/Empty/Empty.vue'
import proButton from '@/components/Button/proButton.vue'

interface Category {
  id: number
  categoryName: string
  posts: Archives[]
}


const categories = reactive<Category[]>([])
const activeCategory = ref<string | null>(null)
const loading = ref(true)
const categoryRefs = reactive<Record<string, HTMLElement>>({})
const dividerRefs = reactive<Record<string, HTMLElement>>({})

// 跳转到文章
const goToPost = (id: number) => {
  window.open(`/article/${id}`, '_blank');
}

// 滚动到分类
const scrollToCategory = (categoryName: string) => {
  activeCategory.value = categoryName;

  const divider = dividerRefs[categoryName];
  if (!divider) return;

  const rect = divider.getBoundingClientRect();
  const scrollTop = window.scrollY + rect.bottom - 200;

  window.scrollTo({
    top: scrollTop,
    behavior: 'smooth',
  });
};

// 处理滚动
const handleScroll = () => {
  // 获取当前滚动位置（容器或窗口）
  const scrollPosition = window.scrollY + 200;

  // 遍历所有分类检测激活状态
  for (const category of categories) {
    const element = categoryRefs[category.categoryName];
    if (!element) continue;

    // 计算元素位置
    let elementTop = 0;
    let elementBottom = 0;

    // 窗口位置计算
    const elementRect = element.getBoundingClientRect();
    elementTop = elementRect.top + window.scrollY;
    elementBottom = elementRect.bottom + window.scrollY;

    // 判断当前滚动位置是否在元素可视范围内
    if (scrollPosition >= elementTop && scrollPosition < elementBottom) {
      // 节流处理防止频繁更新
      if (activeCategory.value !== category.categoryName) {
        activeCategory.value = category.categoryName;
      }
      break;
    }
  }
};

// 获取分类数据
const fetchCategories = async () => {
  if (categories.length !== 0) {
    categories.splice(0, categories.length)
  }
  loading.value = true
  try {
    const res = await getArticleListFromCategoryService()
    for (const data of res.data.data) {
      categories.push({
        id: data.id,
        categoryName: data.categoryName,
        posts: data.simpleArticleList
      })
    }
  } catch {
    loading.value = false
  }
}

onMounted(async () => {
  await fetchCategories();

  // 根据容器类型添加滚动监听
  const scrollElement = window.document;
  scrollElement.addEventListener('scroll', handleScroll);

  // 初始化检测一次
  handleScroll();
})

onBeforeUnmount(() => {
  const scrollElement = window.document;
  scrollElement.removeEventListener('scroll', handleScroll);

})
</script>

<style lang="scss" scoped>
@use '/src/assets/styles/variables.scss' as va;

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

.empty-action {
  display: flex;
  justify-content: center;
  margin-bottom: 24px;
}

.categories-nav {
  position: sticky;
  top: 80px;
  z-index: 10;
  background: var(--grey-1);
  margin: -2rem;
  margin-bottom: 2rem;
  padding: 1rem 2rem;
  border-radius: va.$border-radius-lg;
  display: flex;
  gap: 1rem;
  justify-content: center;
  flex-wrap: wrap;

  &::-webkit-scrollbar {
    display: none;
  }

  .category-tab {
    padding: va.$spacing-xs va.$spacing-md;
    border-radius: va.$border-radius-lg;
    background: var(--hover-secondary);
    color: var(--color-black);
    transition: all 0.3s ease;
    white-space: nowrap;
    display: flex;
    align-items: center;
    gap: va.$spacing-xs;
    font-size: 0.9em;

    &:hover {
      transform: translateY(-2px);
    }

    &.active {
      background: va.$primary;
      color: var(--color-white);
    }
  }
}

.category-group {
  margin-bottom: 3rem;
  scroll-margin-top: 100px;

  &:last-child {
    margin-bottom: 0;
  }
}

.post-item {
  display: flex;
  align-items: center;
  gap: 1.5rem;
  padding: 1rem;
  margin: 1rem 0;
  transition: all 0.3s ease;
  color: var(--grey-8);

  &:hover {
    transform: translateX(10px);
    background: var(--hover-bg);
    color: var(--color-blue);
  }

  .post-date {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: va.$spacing-xs;
    border: 1px solid var(--grey-8);
    border-radius: va.$border-radius-sm;
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
  color: var(--text-secondary);
  height: 260px;
  width: 100%;
}

@media (max-width: 768px) {
  .categories-page {
    padding: 1rem;
  }

  .content-card {
    padding: 1rem;
    border-radius: 0;
  }

  .categories-nav {
    top: 64px;
    margin: -1rem;
    margin-bottom: 1rem;
    padding: 0.5rem 1rem;
  }
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
  /* 右对齐关键属性 */
  font-size: 0.8em;
  /* 缩小字体 */
  opacity: 0.8;
  /* 降低透明度 */
  font-weight: normal;
  /* 常规字重 */
  color: va.$text-primary;
}
</style>
