<template>
	<view class="container">
		<!-- 移动端进度条 -->
		<view class="m-progress-warp">
			<view class="progress" :style="{ width: `${Math.round(currentScrollProgress)}%` }" />
		</view>

		<!-- 回到顶部按钮 -->
		<view v-if="currentScrollProgress > 30 && props.scrollTop > 800" class="m-progress-buuton-top" @tap="handleTop">
			<text class="iconfont icon-xiala m-svg-top"></text>
		</view>
	</view>
</template>

<script lang="ts" setup>
	import { ref, computed, onMounted, onUnmounted } from 'vue'
	import { onPageScroll } from '@dcloudio/uni-app'

	/* props */
	const props = defineProps({
		disabledSmooth: { type: Boolean, required: true },
		scrollTop: { type: Number, default: 0 },
		scrollHeight: { type: Number, default: 1 }
	})

	/* 响应式 */
	const windowWidth = ref(uni.getSystemInfoSync().windowWidth)

	/* 是否 PC 端（>768rpx 视为 PC） */
	const showPcProgress = computed(() => windowWidth.value > 768)

	/* 回到顶部 */
	const handleTop = () => {
		uni.pageScrollTo({
			scrollTop: 0,
			duration: props.disabledSmooth ? 0 : 300
		})
	}

	const currentScrollProgress = computed(() => {
	  const { scrollTop, scrollHeight } = props         
	  const p = (scrollTop / scrollHeight) * 100
	  return Math.min(100, Math.max(0, p))
	})

	/* 横屏/窗口变化时可重新取值（可选） */
	onMounted(() => {
		// #ifdef H5
		window.addEventListener('resize', () => {
			windowWidth.value = window.innerWidth
		})
		// #endif
	})
</script>

<style lang="scss" scoped>
	.container {
		width: 100%;
	}

	.m-progress-warp {
		position: fixed;
		top: 0;
		left: 0;
		width: 100%;
		height: 4rpx;
		z-index: 9999;

		.progress {
			height: 100%;
			background: linear-gradient(-90deg, #f03e07, #c15594, #23a6d5, #23d5ab);
		}
	}

	.m-progress-buuton-top {
		position: fixed;
		bottom: 200rpx;
		right: 32rpx;
		width: 80rpx;
		height: 60rpx;
		z-index: 9999;
		background-color: var(--color-pink-light);
		border-radius: 16rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.m-svg-top {
		transform: rotate(180deg);
		color: var(--color-pink);
	}
</style>