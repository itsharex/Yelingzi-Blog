<template>
	<view class="brand-container" data-scroll="brand">
		<!-- 顶部品牌区 -->
		<view class="brand">
			<text class="artboard" :class="{ active: artboard }">{{ t('blogName') }}</text>

			<!-- 打字机 -->
			<Typeit v-if="yiYan.hitokotoList.length" :data="yiYan.hitokotoList" :speed="100" :delete-speed="50"
				:pause-time="3000" />
			<text v-else class="loading">正在加载每日一言…</text>
		</view>

		<!-- 波浪 -->
		<Wave />

		<!-- 向下箭头 -->
		<view class="arrow-wrapper" @tap="scrollDown">
			<text class="iconfont icon-xiala arrow-down" />
		</view>
	</view>
</template>

<script setup lang="ts">
	import { ref, onMounted, getCurrentInstance, nextTick } from 'vue'
	import Typeit from '@/components/Typeit/Typeit.vue'
	import Wave from '@/components/Waves/Wave.vue'
	import { useYiYanStore } from '@/stores/modules/yiyan'
	import { t } from '@/utils/i18n'
	const brandBox = ref(null)
	const yiYan = useYiYanStore()
	const artboard = ref(false)

	/* 生命周期 */
	onMounted(async () => {
		await yiYan.fetchHitokoto()

		const timer = setInterval(async () => {
			await yiYan.fetchHitokoto()
			if (yiYan.dailyCount >= 10 || yiYan.failedToday) clearInterval(timer)
		}, 15000)

		// 标题动画
		artboard.value = true
	})

	/* 向下滚动一页 */
	const { proxy } = getCurrentInstance()!;
	
function scrollDown() {
    console.log("🚀 Proxy:", proxy);
nextTick(() => {
        uni.createSelectorQuery()
          .in(proxy)
          .select('[data-scroll="brand"]') // 确保选择器正确
          .boundingClientRect((rect) => {
            console.log("✅ rect", rect);
          })
          .exec();
      });
}
</script>

<style lang="scss" scoped>
	/* 变量 */
	$headerHeight: 70vh;

	.brand-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		width: 100%;
		height: $headerHeight;
		min-height: 400rpx;
		position: relative;
		color: var(--header-text-color);
	}

	.brand {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}

	.artboard {
		font-family: 'CustomFont', "Fredericka the Great", Mulish, -apple-system, "PingFang SC", "Microsoft YaHei", sans-serif;
		font-size: 80rpx;
		line-height: 1.2;
		color: var(--color-pink);
		letter-spacing: -0.8em;
		opacity: 0;
	}

	.active {
		animation: unfoldSpacing 1.2s ease-out forwards;
	}

	.loading {
		margin-top: 20rpx;
		font-size: 28rpx;
		color: #666;
	}

	.arrow-down {
		font-size: 60rpx;
		color: var(--lightRed);
		animation: arrow-shake 1.5s ease-out infinite;
	}

	/* 动画 */
	@keyframes unfoldSpacing {
		0% {
			letter-spacing: -0.8em;
			opacity: 0;
		}

		100% {
			letter-spacing: normal;
			opacity: 1;
		}
	}

	@keyframes arrow-shake {
		0% {
			transform: translateY(0);
			opacity: 1;
		}

		30% {
			transform: translateY(25rpx);
			opacity: 0.5;
		}

		100% {
			transform: translateY(0);
			opacity: 1;
		}
	}

	.arrow-wrapper {
		display: inline-block;
		padding: 20rpx;
	}
</style>