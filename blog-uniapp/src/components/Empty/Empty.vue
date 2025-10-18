<template>
	<view class="custom-empty-warp">
		<view class="empty-warp">
			<!-- 加载中 -->
			<view v-if="loading" class="loading-warp">
				<image class="loading-img" src="https://www.yeling.top/image/common/images/loading.gif" mode="aspectFit" />
				<text class="empty-text">{{ loadingText }}</text>
			</view>

			<!-- 空数据 -->
			<view v-else class="img-warp">
				<image class="empty-img" src="https://www.yeling.top/image/common/images/empty.png" mode="aspectFit" />
				<text class="empty-text">{{ text }}</text>
			</view>
		</view>
	</view>
</template>

<script lang="ts" setup>
	const props = defineProps({
		text: {
			type: String,
			default: '暂无数据'
		},
		loading: {
			type: Boolean,
			default: false
		},
		loadingText: {
			type: String,
			default: '加载中...'
		}
	})
</script>

<style lang="scss">
	/* 小程序不支持 var()，直接给色值 */
	.custom-empty-warp {
		width: 100%;
		height: 100%;
		padding: 40rpx 0;

		.empty-warp {
			width: 100%;
			height: 100%;
			border-radius: 16rpx;
			padding: 40rpx 0;
			display: flex;
			align-items: center;
			justify-content: center;

			.img-warp,
			.loading-warp {
				flex: 1;
				display: flex;
				flex-direction: column;
				align-items: center;
				justify-content: center;
			}

			.empty-img {
				width: 360rpx;
				height: 360rpx;
			}

			.empty-text {
				margin-top: 24rpx;
				font-size: 28rpx;
				color: #999999;
			}
		}
	}
</style>