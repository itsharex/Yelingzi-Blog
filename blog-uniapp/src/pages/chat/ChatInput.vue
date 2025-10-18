<template>
	<view class="chat-input-bar">

		<view class="chat-input-contain">

			<textarea v-model="content" class="input-textarea" :show-confirm-bar="false" maxlength="-1" fixed />
			<view>
				<button class="send-btn" :class="{ active: content.length > 0 }" @tap="send">发送</button>
			</view>

		</view>

		<view class="chat-tools">
			<!-- 1. 麦克风图标 -->
			<!--    <view class="input-icon" @tap="handleVoice">
		        <svg-icon name="icon-microphone" size="40" color="#666" />
		      </view> -->

			<!-- 2. 图片图标 -->
			<view class="input-icon" @tap="openFileSelect">
				<SvgIcon name="icon-zhaopian" size="50" color="#666" />
			</view>

			<!-- 3. 相机图标 -->
			<!--    <view class="input-icon" @tap="openCamera">
		        <svg-icon name="icon-camera" size="40" color="#666" />
		      </view> -->

			<!-- 4. 表情图标 -->
			<view class="input-icon" @tap="toggleEmoji">
				<SvgIcon name="icon-biaoqing" size="50" color="#666" />
			</view>

			<!-- 5. 加号图标 -->
			<!--    <view class="input-icon" @tap="openMore">
		        <svg-icon name="icon-plus" size="40" color="#666" />
		      </view> -->
		</view>
		<!-- 工具面板（按需显示） -->
		<view class="tool-board">
			<view v-show="emojiVisible" class="emoji-panel">
				<image-list-mapper @select="handleEmojiSelect" />
			</view>
		</view>
	</view>

</template>

<script lang="ts" setup>
	import { ref, defineExpose } from 'vue'
	import ImageListMapper from '@/components/Image/ImageListMapper.vue'
	import { sendSingleImageService, sendMessageService, sendEmojiMessageService } from '@/api/chat'
	import { useUserStore } from '@/stores'

	/* ----------------- 基础数据 ----------------- */
	const userStore = useUserStore()
	const emit = defineEmits<{ scrollToBottom : [], openToolBoard : [], closeToolBoard : [] }>()

	const content = ref('')
	const emojiVisible = ref(false)
	
	/* ----------------- 关闭工具 ----------------- */
	function closeAllTools() {
	  emojiVisible.value = false
	}

	/* ----------------- 选择图片并上传 ----------------- */
	function openFileSelect() {
		uni.chooseImage({
			count: 1,
			sizeType: ['compressed'],
			sourceType: ['album', 'camera'],
			success: async (res : any) => {
				const tempPath = res.tempFilePaths[0]
				const file = res.tempFiles[0]

				// 本地校验
				if (!beforeAvatarUpload(file)) return

				await uploadImg(tempPath)
			}
		})
	}

	/* 上传图片 */
	async function uploadImg(filePath : string) {
		try {
			// 先拿到上传凭证（视业务而定）
			const url = await sendSingleImageService(filePath, userStore.deviceId)
			console.log('图片上传成功', url)
			onSendSuccess()
		} catch (e : any) {
			uni.showToast({ title: e.message || '上传失败', icon: 'none' })
		}
	}

	/* 本地文件校验 */
	function beforeAvatarUpload(file : UniApp.ChooseFile) {
		// 1. 通过后缀名判断类型
		const ext = file.name?.split('.').pop()?.toLowerCase() || file.path.split('.').pop()?.toLowerCase()
		const allowedExts = ['jpg', 'jpeg', 'png']
		if (!ext || !allowedExts.includes(ext)) {
			uni.showToast({ title: '文件格式不符，请使用 jpg、png 格式文件', icon: 'none' })
			return false
		}

		// 2. 大小判断
		if (file.size > 5 * 1024 * 1024) {
			uni.showToast({ title: '文件大于 5MB!', icon: 'none' })
			return false
		}

		return true
	}

	/* ----------------- 文字发送 ----------------- */
	async function send() {
		if (!content.value.trim()) {
			uni.showToast({ title: '请输入内容', icon: 'none' })
			return
		}
		await sendMessageService({ type: 'single', message: content.value }, userStore.deviceId)
		content.value = ''
		onSendSuccess()
	}

	/* ----------------- 表情 ----------------- */
	function toggleEmoji() {
		if (emojiVisible.value) {
			emit('closeToolBoard')
			setTimeout(() => {
				emojiVisible.value = false;
			}, 200); 
		} else {
			emit('openToolBoard')
			emojiVisible.value = true;			
		}
	}
	
	async function handleEmojiSelect(emoji : string) {
		toggleEmoji()
		await sendEmojiMessageService({ type: 'single', message: emoji }, userStore.deviceId)
		onSendSuccess()
	}
	
	
	function onSendSuccess() {
		emit('scrollToBottom')
	}
	defineExpose({ closeAllTools })
</script>

<style lang="scss">
	.chat-input-bar {
		align-items: center;
		padding: 10rpx 20rpx;
		background-color: #eaeaea;
	}

	.chat-input-contain {
		display: flex;
		justify-content: space-between;
		align-items: center;
		gap: 30rpx;
		padding: 20rpx 0;
		width: 100%;
		box-sizing: border-box;
	}

	.input-textarea {
		height: 70rpx;
		border-radius: 16rpx;
		background-color: #fff;
		font-size: 28rpx;
		padding: 0 10rpx;
		box-sizing: border-box;
		flex: 1;
	}

	.send-btn {
		border-radius: 16rpx;
		width: 150rpx;
		height: 70rpx;
		background: #98d0ff;
		color: #e2e2e2;
		font-size: 28rpx;
		line-height: 80rpx;
		text-align: center;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.send-btn.active {
		background: #61cdff;
		color: #fff;
	}

	.chat-tools {
		display: flex;
		gap: 32rpx;
		padding-bottom: 24rpx;
	}

	// 左侧图标通用样式
	.input-icon {
		margin: 0 15rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}


	.emoji-panel {
		width: 100%;
		flex: 1;
		overflow-y: auto;
	}

	.tool-board {
		height: 450rpx;
		overflow-y: auto;
	}
</style>