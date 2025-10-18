<template>
  <view class="container">
    <!-- 头部 -->
    <view class="header">
      <text class="title">{{ t('search') }}</text>
	  <SvgIcon name="icon-close1" size="64" class="icon" @tap="onClose" />
    </view>

    <!-- 搜索框 -->
    <view class="search-wrapper">
      <view class="search-container">
        <view class="search-input">
          <input
            class="input text"
            :value="content"
            :placeholder="t('seachInput')"
            @input="onInput"
            @confirm="search"
          />
          
          <SvgIcon name="icon-Search" color="#F78EE4" size="60"  @tap="search" />
          
        </view>
      </view>

      <!-- 结果列表 -->
      <view class="search-results">
        <SearchArticle
          v-if="searchContent"
          :search="searchContent"
          @navigate="handleArticleNavigate"
        />
		<MyEmpty v-else></MyEmpty>
      </view>
    </view>
  </view>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { t } from '@/utils/i18n'
import SearchArticle from './SearchArticle.vue';
import MyEmpty from '@/components/Empty/Empty.vue'

/* ---------- 关闭 ---------- */
const emit = defineEmits(['close'])
const onClose = () => emit('close')

/* ---------- 搜索 ---------- */
const content = ref('')
const searchContent = ref('')

const onInput = (e: any) => {
  content.value = e.detail.value
}
const search = () => {
  searchContent.value = content.value
}

/* ---------- 跳转文章 ---------- */
const handleArticleNavigate = (id: number) => {
  uni.navigateTo({ url: `/pages/article/article?id=${id}` })
  setTimeout(() => onClose(), 300)
}
</script>

<style lang="scss" scoped>
.container {
  padding: 20px 0;
  width: 100%;
  background-color: var(--article-bg);
  min-height: 100vh;
  /* 改为min-height确保内容不足时也能撑开 */
}

.search-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  max-width: 1400px;
  margin: 0 auto;
  padding: 24px 0;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 60px;
  position: relative;
}

.title {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  font-size: 32px;
  font-weight: 500;
  margin: 0 auto;
}

.icon {
  margin-left: auto;
  color: #ff7575;
}

.search-container {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 36px;
}

.search-input {
  display: flex;
  align-items: center;
  background-color: #ff7575;
  border-radius: 10px;
  box-shadow: 0 6px 15px #ff7575, 0 0 0 10px #fff;
  width: 600px;
  padding: 7px 20px;
}

.search-input .input {
  width: 80%;
  flex: 1;
  height: 38px;
  font: 200 22px;
  outline: none;
  color: #fff;
  background-color: transparent;
}

.search-input .input::placeholder {
  color: #8f3636;
}

.send {
  height: 40px;
  background-color: transparent;
  border: none;
}


.search-results {
  width: 100%;
  max-width: 640px;
  margin: 0 auto;
  padding: 0;
}

@media (max-width: 767px) {
  .search-input {
    width: 100%;
  }

  .header {
    padding-right: 30px;
  }
}
</style>
