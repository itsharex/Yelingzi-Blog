<template>
	<view class="friend-card">
		<snowHr symbol="5" />
		<view class="category-title">🔥{{ props.title }}</view>

		<view class="card-container" v-if="props.friendList">
			<view v-for="(resourcePath, index) in props.friendList" :key="index" class="card-item shadow-box"
				@tap="clickResourcePath(resourcePath)">
				<view class="card-image">
					<ImageWithFallback :src="resourcePath.cover" />
				</view>

				<view class="card-body">
					<view class="card-title">
						<text v-if="resourcePath.recommendStatus">🔥</text>
						{{ resourcePath.title }}
					</view>
					<view class="card-desc">
						{{ resourcePath.introduction }}
					</view>
					<view class="card-time">
						<SvgIcon name="icon-rili1" class="icon-time" />
						<text>添加于 {{ getDateDiff(resourcePath.createTime) }}</text>
					</view>
				</view>
			</view>
		</view>
	</view>


</template>

<script setup lang="ts">
	import type { Friend } from '@/types/friend'
	import { getDateDiff } from '@/utils/common'
	import snowHr from '@/components/Hr/DividerLine.vue'
	import ImageWithFallback from '@/components/Image/ImageWithFallback.vue'

	/* -------------- 无需改动 -------------- */
	const props = defineProps<{
		friendList : Friend[]
		title : string
	}>()

	const emit = defineEmits<{
		(e : 'click-resource-path', url : string) : void
	}>()

	function clickResourcePath(resourcePath : Friend) {
		emit('click-resource-path', resourcePath.url)
	}
</script>

<style lang="scss" scoped>
	.friend-card{
		width: 100%;
		height: 100%;
	}

	.category-title{
		font-size: 42rpx;
		font-weight: 600;
		display: flex;
		justify-content: center;
		color: #e9546b;
		margin-bottom: 40rpx;
	}

	.card-container {
		display: flex;
		flex-wrap: wrap;
		justify-content: center;
	}

	.card-item {
		position: relative;
		border-radius: 10rpx;
		overflow: hidden;
		background-color: var(--grey-1-a3, #fafafa);
		margin: 10rpx;
		flex-shrink: 0;
		width: 80%;
		animation: zoomIn 0.8s ease-in-out;
	}

	.shadow-box {
		box-shadow: 0 1rpx 20rpx -6rpx rgba(0, 0, 0, 0.5);
		transition: all 0.3s ease;
	}

	.card-image {
		width: 100%;
		height: 360rpx;
		overflow: hidden;
		position: relative;
	}

	.card-body {
		padding: 20rpx;
		color: var(--grey-9, #333);
	}

	.card-title {
		font-size: 36rpx;
		font-weight: 600;
		margin-bottom: 5rpx;
		white-space: nowrap;
		text-overflow: ellipsis;
		overflow: hidden;
		transition: all 0.2s ease-in-out;
		color: #e9546b;
	
	}

	.card-desc {
		height: 40rpx;
		font-size: 28rpx;
		/* 14px → 28rpx */
		line-height: 1.4;
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 2;
		-webkit-box-orient: vertical;
		letter-spacing: 1rpx;
	}

	.card-time {
		display: flex;
		justify-content: flex-end;
		align-items: center;
		font-size: 24rpx;
		gap: 3rpx;

		.icon-time {
			width: 32rpx;
			height: 32rpx;
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