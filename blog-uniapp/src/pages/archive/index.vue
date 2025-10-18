<template>
	<FixedLayoutVue :scroll-top="scrollTop" :scroll-height="scrollHeight">
		<view class="home-warp">
			<PageHeader :title="t('archive')" bg-img="/image/common/images/bg-article.jpg" />
			<view class="archive-page">
			  <!-- 页头 -->
			
			
			  <!-- 时间轴 -->
			  <view class="timeline">
			    <view v-for="(item, index) in archives" :key="index" class="year-group">
			      <!-- 年标题 -->
			      <view class="year-header" @tap="toggleYear(item.year)">
			        <text class="year">{{ item.year }}</text>
			        <SvgIcon name="icon-xiala" size="24"
			          class="toggle-icon"
			          :class="{ 'is-open': !collapsedYears[item.year] }"
			        />
			      </view>
			
			      <!-- 展开内容 -->
			      <view
			        class="expand-wrapper"
			        :style="{
			          height: collapsedYears[item.year] ? '0' : expandHeight[item.year] + 'px'
			        }"
			      >
			        <view class="expand-content" :id="'expand-' + item.year">
			          <view
			            v-for="post in item.posts"
			            :key="post.id"
			            class="post-item"
			            @tap="goToPost(post.id)"
			          >
			            <view class="post-date">
			              <text class="month">{{ getMonth(post.createTime) }}</text>
			              <text class="day">{{ getDate(post.createTime) }}</text>
			            </view>
			            <view class="post-info">
			              <text class="post-title">{{ post.title }}</text>
			            </view>
			          </view>
			        </view>
			      </view>
			    </view>
			  </view>
			</view>
		</view>
	</FixedLayoutVue>

	    
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, nextTick, getCurrentInstance } from 'vue'
import PageHeader from '@/components/Layout/PageHeader.vue'

import { getDate, getMonth } from '@/utils/common'
import { getArticleListService } from '@/api/article'
import type { Archives } from '@/types/article'
import { t } from '@/utils/i18n'
import FixedLayoutVue from '@/components/Layout/FixedLayout.vue';
	import { onPageScroll } from '@dcloudio/uni-app'
	const scrollTop = ref(0)
	const windowHeight = ref(uni.getSystemInfoSync().windowHeight)
	const scrollHeight = ref(windowHeight.value + 1)


	onPageScroll((e) => {
		scrollTop.value = e.scrollTop

		uni.createSelectorQuery()
			.select('.home-warp')
			.boundingClientRect(rect => {
				scrollHeight.value = rect?.height - windowHeight.value
			})
			.exec();
	})

interface ArchivesItem {
  year: number
  posts: Archives[]
}

const archives = ref<ArchivesItem[]>([])
const collapsedYears = reactive<Record<number, boolean>>({})
const expandHeight = reactive<Record<number, number>>({}) // 保存每个年份展开高度

const goToPost = (id: number) => {
  uni.navigateTo({ url: `/pages/article/detail?id=${id}` })
}

const toggleYear = async (year: number) => {
  collapsedYears[year] = !collapsedYears[year]
  if (!collapsedYears[year]) {
    await nextTick()

    /* ---------- 关键修复 ---------- */
    const ins = getCurrentInstance()
    let q = uni.createSelectorQuery()
    // #ifndef H5
    if (ins != null) q = q.in(ins)
    // #endif
    /* -------------------------------- */

    q.select(`#expand-${year}`)
      .boundingClientRect((rect: any) => {
		  console.log(rect)
        if (rect) expandHeight[year] = rect.height
      })
      .exec()
  }
}

const getArchives = async () => {
  try {
    const res = await getArticleListService()
    const yearMap = new Map<number, Archives[]>()
    for (const article of res.data) {
      const year = new Date(article.createTime).getFullYear()
      if (!yearMap.has(year)) yearMap.set(year, [])
      yearMap.get(year)!.push(article)
    }

    yearMap.forEach((posts, year) => {
      archives.value.push({ year, posts })
      collapsedYears[year] = true // 默认收起
      expandHeight[year] = 0
    })
  } catch (e) {
    console.error('Failed to load archives:', e)
  }
}

onMounted(() => {
  getArchives()
})
</script>

<style scoped>
.archive-page {
  padding: 0 30rpx 40rpx;
  	background-color: #f5f7fa;
}

.timeline {
  position: relative;
  padding-left: 60rpx;

}

/* 竖线 */
.timeline::before {
  content: '';
  position: absolute;
  left: 0rpx;
  top: 0;
  bottom: 0;
  width: 2rpx;
  background: linear-gradient(to bottom, #409eff, #67c23a);
}

.year-group {
  margin-bottom: 60rpx;
}

.year-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 30rpx;
  padding-right: 20rpx;
  position: relative;
}

.year-header::before {
  content: '';
  position: absolute;
  left: -60rpx;
  top: 50%;
  transform: translateY(-50%);
  width: 30rpx;
  height: 2rpx;
  background: #409eff;
}

.year {
  font-size: 36rpx;
  font-weight: 700;
  color: var(--grey-8, #606266);
}

.toggle-icon {
  font-size: 24rpx;
  transition: transform 0.3s;
}

.toggle-icon.is-open {
  transform: rotate(-180deg);
}

.expand-wrapper {
  overflow: hidden;
  transition: height 0.35s ease;
}

.post-item {
  display: flex;
  align-items: center;
  gap: 30rpx;
  padding: 16rpx;
  border-radius: 8rpx;
  margin-bottom: 16rpx;
}

.post-item:active {
  background: #f5f7fa;
}

.post-date {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 8rpx;
  border: 1rpx solid #909399;
  border-radius: 6rpx;
  min-width: 80rpx;
}

.month {
  font-size: 24rpx;
  color: #909399;
}

.day {
  font-size: 32rpx;
  font-weight: 600;
  color: #303133;
}

.post-info {
  flex: 1;
  min-width: 0;
}

.post-title {
  font-size: 30rpx;
  line-height: 1.5;
  color: #303133;
  word-break: break-all;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}
</style>