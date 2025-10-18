<template>
	<view class="form-main">
		<view class="middle-img" >
			<ImageWithFallback src="/image/common/images/friendLetterMiddle.jpg" @tap.stop="void 0" :lazy="false" :preview="false" mode="widthFix" />
		</view>
		
		<view class="form-content">
			<!-- 标题 -->
			<view class="title">有朋自远方来</view>

			<!-- 表单 -->
			<uni-forms ref="formRef" :modelValue="friend" :rules="rules" label-width="160rpx" class="form-friend">
				<uni-forms-item label="名称" name="title">
					<uni-easyinput v-model="friend.title" placeholder="请输入网站名称" :inputBorder="false" />
				</uni-forms-item>

				<uni-forms-item label="简介" name="introduction">
					<uni-easyinput v-model="friend.introduction" placeholder="一句话介绍" :inputBorder="false" />
				</uni-forms-item>

				<uni-forms-item label="封面" name="cover">
					<uni-easyinput v-model="friend.cover" placeholder="https:// 开头" :inputBorder="false" />
				</uni-forms-item>

				<uni-forms-item label="网址" name="url">
					<uni-easyinput v-model="friend.url" placeholder="https:// 开头" :inputBorder="false" />
				</uni-forms-item>

				<!-- 提交按钮 -->
				<button class="submit-btn" @tap="submitFriend">提交</button>
			</uni-forms>

			<!-- 插图 -->
			<ImageWithFallback src="/image/common/images/friendLetterBiLi.png" mode="widthFix" class="friend-img" />

			<view class="tips">欢迎交换友链</view>

		</view>
	</view>
</template>
k
<script lang="ts" setup>
	import { ref } from 'vue'
	import ImageWithFallback from '../Image/ImageWithFallback.vue'
	import { addFriendService, addUserFriendService } from '@/api/friend'
	import { useUserStore } from '../../stores'
	const userStore = useUserStore()

	/* 表单数据 */
	const friend = ref({
		title: '',
		introduction: '',
		cover: '',
		url: ''
	})

	/* 校验规则 */
	const rules = {
		title: {
			rules: [{ required: true, errorMessage: '名称是什么呢' }]
		},
		introduction: {
			rules: [{ required: true, errorMessage: '介绍一下网站吧' }]
		},
		cover: {
			rules: [
				{ required: true, errorMessage: '要有个形象才行吖' },
				{
					validateFunction: (rule : any, val : string) =>
						/^https:\/\//i.test(val) ? true : '封面链接必须以 https:// 开头'
				}
			]
		},
		url: {
			rules: [
				{ required: true, errorMessage: '没有网址可访问不到捏' },
				{
					validateFunction: (rule : any, val : string) =>
						/^https:\/\//i.test(val) ? true : '网址链接必须以 https:// 开头'
				}
			]
		}
	}

	/* 提交 */
	const formRef = ref<any>()
	async function submitFriend() {
		try {
			await formRef.value.validate()
		} catch {
			uni.showToast({ title: '请完善表单', icon: 'none' })
			return
		}


		try {
			// 未登录走匿名申请，已登录走用户申请
			await (userStore.getIsLogin()
				? addUserFriendService({ ...friend.value })
				: addFriendService({ ...friend.value }))

			uni.showToast({ title: '提交成功，待管理员审核！', icon: 'success' })
			resetFriend()
			emit('closeShowEnvelope')
		} catch {
			uni.showToast({ title: '提交失败，稍后再试~', icon: 'none' })
		}

	}

	/* 重置 */
	function resetFriend() {
		friend.value = { title: '', introduction: '', cover: '', url: '' }
	}

	/* 关闭弹窗事件 */
	const emit = defineEmits(['closeShowEnvelope'])
</script>

<style lang="scss">

	.form-main {

	}
	
	.form-content{
		padding: 20rpx 60rpx 60rpx 60rpx;
		background: #fafafa;
		border-radius: 30rpx;
	}

	/* 标题 */
	.title {
		text-align: center;
		font-size: 48rpx;
		font-weight: bold;
		margin: 40rpx;
		color: #333;
	}

	.middle-img {
		width: 100%;
		border-radius: 30rpx 30rpx 0 0;
		overflow: hidden;
	}

	/* 表单卡片 */
	.form-friend {
		width: 80vw;
		max-width: 600rpx;
		margin: 0 auto 40rpx;
		border-radius: 16rpx;
		padding: 40rpx 30rpx;
		box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
	}

	/* 提交按钮 */
	.submit-btn {
		width: 240rpx;
		height: 80rpx;
		line-height: 80rpx;
		text-align: center;
		color: #fff;
		font-size: 32rpx;
		border-radius: 40rpx;
		margin: 40rpx auto 0;
		background: linear-gradient(90deg, #9cd0ed 0%, #ed6ea0 100%);
		box-shadow: 0 4rpx 16rpx rgba(237, 110, 160, 0.3);

		&:active {
			transform: scale(0.96);
			opacity: 0.9;
		}
	}

	/* 插图 */
	.friend-img {
		display: block;
		width: 100%;
		margin: 40rpx auto 20rpx;
	}

	/* 底部提示 */
	.tips {
		text-align: center;
		font-size: 24rpx;
		color: #999;
	}
</style>