<template>

  <div class="article-bg">

    <div class="article-header">
      <ArticleHeader :article="article" />
    </div>

    <bgImage :src="article.articleCover" :transition-duration="1000" />

    <snowHr margin="10px"></snowHr>

    <div class="article-page">
      <!-- 中间主内容区（修改为竖向排列） -->
      <main class="article-main-container" :style="{ marginRight: showSidebar ? '320px' : '60px' }">
        <div>
          <H1 class="mian-title">{{ article.title }}</H1>
          <div class="mian-theme">
            <ArticlePreviewTheme />
          </div>
        </div>
        <div class="article-main">
          <!-- 文章内容 -->
          <MdPreview :id="'article'" v-model="article.content" ref="preview" @onGetCatalog="getCatalog"
            :previewTheme="previewTheme" :theme="isDark ? 'dark' : 'light'" />
        </div>
        <!-- 文章底部分割线 -->
        <el-divider border-style="dotted" />
        <!-- 文章页脚 -->
        <ArticleFooter :article="article" :show-share="showShareMenu" @like="handleParentLike" @share="toggleShareMenu"
          @copy="copyLink" />
        <!-- 评论 -->
        <Comment :id="props.id" :comment-type="1" :author-id="article.userId" />
      </main>

      <!-- 右侧边栏 -->
      <div class="fixed-right-aside" :class="{ 'fixed-right-aside-active': showSidebar }">
        <div class="affix" :class="{ 'affix-avtive': showSidebar }">
          <div v-if="showSidebar" style="margin-left: 10px;">
            <el-card class="toc-container">
              <div class="toc-header">
                <div class="title-wrapper">
                  <SvgIcon name="icon-list" size="20" color="#49b1f5" class="icon" />
                  <span>目录</span>
                </div>
                <div class="progress-wrapper">
                  <SvgIcon name="icon-read" size="18" style="vertical-align: -0.3em;" class="icon" />
                  <span class="progress-text">{{ readProgress }}</span>
                </div>
              </div>
              <div class="toc-content">
                <MdCatalog v-if="scrollElement" :editorId="'article'" :scrollElement="scrollElement"
                  :scrollElementOffsetTop="100" :offsetTop="120" @onActive="handleActiveHeading" />
              </div>
            </el-card>

            <ArticleAction :position="actionBarLeft" :article="article" @like="handleParentLike"
              @favorite="toggleFavorite" @toggle-sidebar="toggleSidebar" @scroll-comments="scrollToComments">
            </ArticleAction>

            <!-- 推荐文章 -->
            <ArticleRecommend />
          </div>

          <div v-else>
            <!-- 竖向排列行为侧边栏 -->
            <ArticleActionCol :article="article" @like="handleParentLike" @favorite="toggleFavorite"
              @toggle-sidebar="toggleSidebar"></ArticleActionCol>
          </div>
        </div>
      </div>

    </div>

  </div>
</template>

<script lang="ts" setup>
import 'md-editor-v3/lib/preview.css';
import { ref, onMounted, onUnmounted, watch, inject, type Ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import type { Article } from '@/types/article'
import { ElMessage } from 'element-plus'
import { numberToBoolean } from '@/utils/common'
import { MdCatalog, MdPreview, type HeadList } from 'md-editor-v3'
import snowHr from '@/components/Hr/DividerLine.vue'
import ArticleAction from './ArticleAction.vue'
import ArticleFooter from './ArticleFooter.vue'
import ArticleRecommend from './ArticleRecommend.vue'
import Comment from '@/components/Comment/Comment.vue'
import bgImage from '@/components/Image/BgImage.vue';
import { addArticleLikeService, delArticleLikeService, getArticleByIdService, getArticleLikeService } from '@/api/article'
import { useUserStore, useBlogStore } from '@/stores';
import ArticleActionCol from './ArticleActionCol.vue';
import ArticleHeader from './ArticleHeader.vue';
import ArticlePreviewTheme from './ArticlePreviewTheme.vue';

const blogStore = useBlogStore();
const isDark = computed(() => blogStore.blogInfo.isDark);
const userStore = useUserStore()
const route = useRoute()
const router = useRouter()
const props = defineProps(['id'])
// 响应式数据
const article = ref<Article>({
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
const readProgress = ref(0)

const actionBarLeft = ref(0)
const showShareMenu = ref(false)
const loading = ref(false)
const showSidebar = ref(window.innerWidth > 768)
const previewTheme = computed(() => userStore.previewTheme)

const layoutRef = inject<Ref<HTMLElement | null>>('scrollContainer', ref<HTMLElement | null>(null));

const scrollElement = computed(() => {
  return layoutRef.value || undefined;
});

const totalHeadings = ref(0);
// 获取文章详情
const getArticle = async () => {
  try {
    const res = await getArticleByIdService(props.id);

    if (res.data.code === 0) {
      // 如果返回的状态码为0，跳转到404页面
      router.push('/404');
      return;
    }

    article.value = res.data.data;

    if (userStore.getIsLogin()) {
      getArticleLike();
    }
  } catch (error) {
    // 如果请求失败，跳转到404页面
    console.error('请求文章失败:', error);
    router.push('/404');
  }
};

const getArticleLike = async () => {
  const res = await getArticleLikeService(article.value.id)
  article.value.isLike = numberToBoolean(res.data.data)
}

const getCatalog = (HeadList: HeadList[]) => {
  totalHeadings.value = HeadList.length
};

// 高亮标题变化事件
const handleActiveHeading = (heading: any) => {

  if (totalHeadings.value <= 0) {
    return;
  }
  readProgress.value = Math.round((heading.index / totalHeadings.value) * 100);
};
const handleParentLike = async () => {

  if (isRequesting.value) return;

  try {
    isRequesting.value = true;

    if (!userStore.getIsLogin()) {
      ElMessage.warning("请先登录");
      return;
    }

    if (article.value.isLike) {
      await delArticleLikeService(article.value.id);
      article.value.likeCount -= 1;
    } else {
      await addArticleLikeService(article.value.id);
      article.value.likeCount += 1;
    }

    article.value.isLike = !article.value.isLike;
  } finally {
    setTimeout(() => {
      isRequesting.value = false;
    }, 600);
  }
};
// 生命周期
onMounted(() => {
  getArticle()
})

onUnmounted(() => {

})

// 监听路由变化
watch(
  () => props.id,
  (newId) => {
    if (newId) getArticle()
  }
)

// 侧边栏切换
const toggleSidebar = () => {
  console.log('llll')
  showSidebar.value = !showSidebar.value
}

const toggleFavorite = () => {
  ElMessage.warning('暂未开放')
  return
}


const scrollToComments = () => {
  const commentsSection = document.querySelector('.comment-section')
  if (commentsSection) {
    commentsSection.scrollIntoView({ behavior: 'smooth' })
  }
}

const like = () => {
  console.log("Parent toggleLike called");

  toggleLike()
}

// 点赞功能
const isRequesting = ref(false)
// 点赞功能
const toggleLike = async () => {
  console.log("Parent toggleLike called");
  if (isRequesting.value) return

  try {
    isRequesting.value = true

    if (!userStore.getIsLogin()) {
      ElMessage.warning("请先登录")
      return
    }

    // 执行点赞/取消操作
    if (article.value.isLike) {
      await await delArticleLikeService(article.value.id)
      article.value.likeCount -= 1
    } else {
      await addArticleLikeService(article.value.id)
      article.value.likeCount += 1
    }

    // 状态切换
    article.value.isLike = !article.value.isLike
  } finally {
    setTimeout(() => {
      isRequesting.value = false
    }, 600)
  }
}

/**
 * 分享
 */
const toggleShareMenu = () => {
  showShareMenu.value = !showShareMenu.value
}
/**
 * 关闭分享
 */
const closeShareMenu = () => {
  showShareMenu.value = false
}
const currentUrl = () => {
  return window.location.href
}
/**
 * 复制链接
 */
const copyLink = async () => {
  try {
    await navigator.clipboard.writeText(currentUrl())
    ElMessage.success('链接已复制到剪贴板')
  } catch (err) {
    ElMessage.error('复制失败，请手动复制')
  }
  closeShareMenu()
}
watch(() => showSidebar.value, (newValue) => {
  console.log(newValue)
})

</script>

<style lang="scss" scoped>
.article-bg {
  background-color: var(--article-bg);
  z-index: -1;
}

.article-header {
  height: 25vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  padding-top: 4rem;
  min-height: 300px;

}

/* 容器定位修正 */
.article-page {
  position: relative;
  max-width: 1300px;
  margin: 0 auto;
}


/* 中间主内容区容器 */
.article-main-container {
  display: flex;
  flex-direction: column;
  border: 1px solid var(--grey-9-a1);
  border-radius: 12px;
  transition: margin-right 0.5s ease;
  background-color: var(--md-bg);
}

.mian-title {
  text-align: center;
}

.mian-theme {
  display: flex;
  justify-content: flex-end;
}

/* 文章内容样式 */
.article-main {
  position: relative;
  flex: 1;
  box-sizing: border-box;
  // overflow: visible;
  padding: 10px 25px;
}

/* 目录容器样式 */
.toc-container {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  min-height: 160px;
}

.toc-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  border-bottom: 1px solid var(--border-color);
  padding-bottom: 8px;
}

.title-wrapper {
  display: flex;
  align-items: center;
  margin: 0;
  font-weight: bold;
  font-size: 18px;
  color: var(--color-blue);
}

.title-wrapper .icon {
  margin-right: 8px;
  color: var(--color-blue);
}

.progress-wrapper {
  display: flex;
  align-items: center;
  font-size: 0.9em;
  border-radius: 12px;
  background: rgba(73, 177, 245, 0.1);
  padding: 4px 8px;
  color: var(--text-secondary);
  transition: all 0.3s ease;
  color: var(--color-aqua);

  .icon {
    margin-right: 8px;
    color: var(--color-blue);
  }
}

.progress-text {
  font-variant-numeric: tabular-nums;
  min-width: 3em;
  text-align: right;
}

.progress-text::after {
  content: '%';
  margin-left: 2px;
  opacity: 0.7;
}


/* 推荐文章样式 */
.article-recommend {
  margin-top: 24px;
  background-color: var(--bg-secondary);
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}


.fixed-right-aside {
  position: absolute;
  top: 0;
  right: 0;
  z-index: 100;
  height: 100%;
  transition: width 0.5s ease;
  width: 60px;
}

.fixed-right-aside-active {
  width: 320px;
}

.affix {
  position: sticky;
  top: 360px;
}

.affix-avtive {
  top: 74px;
}

.simple-right-sidear {
  width: 60px;
  margin-left: 10px;
  border-radius: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.action-button {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  margin-bottom: 20px;

  &:hover {
    color: var(--color-pink);
  }
}


:deep(.md-editor-preview .md-editor-code .md-editor-code-head) {
  z-index: 1;
  top: 74px;
  cursor: url('/src/assets/cursors/up.cur'), pointer;
  // padding-bottom: 1.2em;
}

:deep(.md-editor-catalog-link span) {
  cursor: url('/src/assets/cursors/up.cur'), pointer;
}



@media (max-width: 767px) {

  .article-main-container {
    width: 100%;
  }

  .fixed-right-aside {
    width: 60px;
    right: 20px;
  }

  .fixed-right-aside-active {
    width: 360px;
    right: 10px;
  }

}
</style>
