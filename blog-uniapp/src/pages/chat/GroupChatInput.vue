<template>
	<view class="chat-input-bar">

		<view class="chat-input-contain">

			<textarea v-model="content" class="input-textarea" :show-confirm-bar="false" maxlength="-1" fixed disabled
				@tap="select()" />
			<view>
				<button class="send-btn" :class="{ active: content.length > 0 }" @tap="send">发送</button>
			</view>

		</view>

		<view class="chat-tools">


			<!-- 4. 表情图标 -->
			<view class="input-icon" @tap="toggleEmoji">
				<SvgIcon name="icon-biaoqing" size="50" color="#666" />
			</view>

		</view>
		<!-- 工具面板（按需显示） -->
		<view class="tool-board">
			<view v-show="emojiVisible" class="emoji-panel">
				<image-list-mapper @select="handleEmojiSelect" />
			</view>
			<view v-show="textVisible" class="chip-wrapper">
				<view v-for="(input, idx) in inputList" :key="idx" class="chip" @tap="select(input)">
					{{ input }}
				</view>
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
	const inputList = ref(["大家好吖，喵！",
		"喵喵喵，喵！",
		"今天也是元气满满的一天呢~",
		"(≧∇≦)ﾉ",
		"awsl！这也太可爱了吧",
		"摸鱼ing，勿扰~",
		"qwq",
		"ovo"])

	const content = ref('')
	type ToolType = 'emoji' | 'text' | 'voice' | 'image'
	const lastTool = ref<ToolType | null>(null)
	const emojiVisible = ref(false)
	const textVisible = ref(false)

	/* ----------------- 关闭工具 ----------------- */
	function closeAllTools() {
		emojiVisible.value = false
		textVisible.value = false
		lastTool.value = null
	}
	/* ----------------- 选择文本 ----------------- */
	function select(input ?: string) {
		if (input) {
			content.value = input
		}

		if (lastTool.value === 'text') {
			emit('closeToolBoard')
		} else {
			// 切换工具 → 打开当前，关闭另一个
			closeAllTools()
			textVisible.value = true
			lastTool.value = 'text'
			emit('openToolBoard')
		}
	}

	/* ----------------- 文字发送 ----------------- */
	async function send() {
		if (!content.value.trim()) {
			uni.showToast({ title: '请输入内容', icon: 'none' })
			return
		}
		await sendMessageService({ type: 'group', message: content.value }, userStore.deviceId)
		content.value = ''
		onSendSuccess()
	}

	/* ----------------- 表情 ----------------- */
	function toggleEmoji() {
		if (emojiVisible.value) {
			emit('closeToolBoard')
		} else {
			closeAllTools()
			emojiVisible.value = true
			lastTool.value = 'emoji'
			emit('openToolBoard')
		}
	}

	function handleEmojiSelect(emoji : string) {
		emojiVisible.value = false
		sendEmojiMessageService({ type: 'group', message: emoji }, userStore.deviceId)
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
		line-height: 80rpx;
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

	.chip-wrapper {
		display: flex;
		flex-wrap: wrap;
		gap: 16rpx;
		padding: 8rpxpx 0;
	}

	.chip {
		padding: 4px 12px;
		border-radius: 16px;
		background: #f2f2f2;
		transition: all 0.2s;

		&.active {
			background: #ed6ea0;
			color: #fff;
		}

		&:hover:not(.active) {
			background: #ffe6fa;
		}
	}
</style>