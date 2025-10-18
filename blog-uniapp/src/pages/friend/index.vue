<template>
	<FixedLayoutVue :scroll-top="scrollTop" :scroll-height="scrollHeight">
		<view class="home-warp">
		<PageHeader :title="t('friend')" bgImg="/image/common/images/friendLetterMiddle.jpg" />
		<view class="page-container">
			<view class="friend-page">
				<!-- 信封区域 -->
				<view class="form-wrap" :style="formWrapStyle" @tap="toggleEnvelope">
					<!-- 信封头部 -->
					<ImageWithFallback class="before-img" src="/image/common/images/friendLetterTop.png" mode="widthFix" :preview="false" />
		
					<!-- 信封中间 -->
					<view class="envelope">
						<FriendForm @closeShowEnvelope="showEnvelope = false" />
					</view>
		
					<!-- 信封底部 -->
					<ImageWithFallback class="after-img" src="/image/common/images/friendLetterBottom.png" mode="widthFix" :preview="false" />
				</view>
		
				<!-- 信息展示 -->
				<TextList :title="info" :items="siteInfo" :is-copy="true" />
				<TextList :title="prop" :items="applicationInfo" />
		
				<!-- 友链卡片 -->
				<FriendCard :friendList="friendList" :title="t('friendlyLink')" @click-resource-path="openLink" />
			</view>
		</view>			
		</view>
		

	</FixedLayoutVue>


</template>

<script setup lang="ts">
	/* -------------------- 1. 框架差异抹平 -------------------- */
	import { onMounted, onUnmounted } from 'vue'
	import { ref, reactive, computed, nextTick } from 'vue'
	import FriendCard from '@/components/Card/FriendCard.vue'
	import FriendForm from '@/components/Form/FriendForm.vue'
	import PageHeader from '@/components/Layout/PageHeader.vue'
	import ImageWithFallback from '@/components/Image/ImageWithFallback.vue'
	import TextList from '@/components/TextList/TextList.vue'
	import FixedLayoutVue from '@/components/Layout/FixedLayout.vue'
	import type { Friend } from '@/types/friend'
	import { getFriendService } from '@/api/friend'
	import { t } from '@/utils/i18n'
	import { onPageScroll } from '@dcloudio/uni-app'
	const scrollTop = ref(0)
	const windowHeight = ref(uni.getSystemInfoSync().windowHeight)
	const scrollHeight = ref(windowHeight.value + 1)
	
	
	onPageScroll((e) => {
		scrollTop.value = e.scrollTop
		
		uni.createSelectorQuery()
			.select('.home-warp')       
			.boundingClientRect(rect => {
				scrollHeight.value =  rect?.height - windowHeight.value
			})
			.exec();
	})

	/* -------------------- 2. 响应式数据 -------------------- */
	const showEnvelope = ref(false)
	const friendList = reactive<Friend[]>([])

	const prop = ref('🌸' + t('proposal'))
	const info = ref('🌸' + t('webInfo'))

	/* -------------------- 3. 样式计算 -------------------- */
	const formWrapStyle = computed(() => ({
		height: showEnvelope.value ? '1660rpx' : '560rpx',
		top: showEnvelope.value ? '-100rpx' : '0',
		transition: 'all 1s ease-in-out 0.3s'
	}))

	/* -------------------- 4. 常量数据 -------------------- */
	const siteInfo = [
		'网站名称: 叶玲子的小栈',
		'网址: https://www.yeling.top',
		'网站封面: https://www.yeling.top/image/logo.jpg',
		'描述: 记录学习、生活、兴趣的次元小栈'
	]

	const applicationInfo = [
		'点击上方信封✨✨✨',
		'不会添加带有广告营销和没有实质性内容的友链🚫🚫🚫',
		'申请之前请将本网站添加为您的友链哦🎟️🎟️🎟️'
	]

	/* -------------------- 5. 方法 -------------------- */
	function toggleEnvelope() {
		if (!showEnvelope.value) showEnvelope.value = true
	}

	function openLink(url : string) {
		// #ifdef H5
		window.open(url, '_blank')
		// #endif
		// #ifndef H5
		uni.setClipboardData({ data: url })
		// #endif
	}

	async function fetchFriends() {
		friendList.length = 0
		const res = await getFriendService()
		res.data.forEach((f : Friend) => friendList.push(f))
	}

	/* -------------------- 6. 生命周期 -------------------- */
	onMounted(() => {
		fetchFriends()
		prop.value = '🌸' + t('proposal')
		info.value = '🌸' + t('webInfo')

		// #ifdef H5
		// 仅 H5 需要处理划选
		const onMouseDown = (e : MouseEvent) => {
			const block = document.querySelector('.blockquote')
			if (block && !block.contains(e.target as Node)) {
				window.getSelection()?.removeAllRanges()
			}
		}
		document.addEventListener('mousedown', onMouseDown)
		onUnmounted(() => document.removeEventListener('mousedown', onMouseDown))
		// #endif
	})
</script>

<style lang="scss" scoped>
	.page-container {
		background-color: #fff;
	}

	.friend-page {
		width: 100%;
		position: relative;
	}

	.form-wrap {
		margin: 0 auto;
		overflow: hidden;
		width: 100%;
		position: relative;
		top: 0;
		z-index: 10;
	}

	.before-img {
		position: absolute;
		bottom: 186rpx;
		left: 0;
		width: 100%;
		height: auto;
		z-index: 2;
	}

	.after-img {
		position: absolute;
		bottom: -2rpx;
		left: 0;
		width: 100%;
		height: auto;
		z-index: 100;
	}

	.envelope {
		position: relative;
		margin: 0 auto;
		padding: 200rpx 20rpx 20rpx;
		height: 800rpx;
		z-index: 50;
	}

	.form-main {
		background: var(--grey-1, #fafafa);
		margin: 0 auto;
		border-radius: 10rpx;
		overflow: hidden;
		padding: 40rpx 0;
	}
</style>