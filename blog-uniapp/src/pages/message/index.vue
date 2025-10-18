<!-- pages/message/message.vue -->
<template>

	<view class="message">
		<!-- 弹幕区域 40% -->
		<view class="message-header">
			<ImageWithFallback class="background" src="/image/common/images/board.png" />
			<view class="danmaku-box">
				<danmaku :list="danmuList" />
			</view>
			<view class="write-box">
				<text class="write-title">{{ t('messageBorad') }}</text>
				<view class="write-row">
					<input v-model="content" class="write-input" :placeholder="t('messageInput')" @confirm="send" />
					<button class="send-btn" size="mini" @click="send">
						{{ t('send') }}
					</button>
				</view>
			</view>
		</view>



		<!-- 留言列表 60% -->
		<scroll-view class="msg-container" scroll-y>
			<view v-for="m in msgList" :key="m.id" class="paper-card">
				<view class="paper-header">
					<YlAvatar class="paper-avatar" :src="m.userAvatar" :size="60" />
					<text class="paper-name">{{ m.nickname }}</text>
				</view>
				<text class="paper-content">{{ m.content }}</text>
				<text class="paper-time">{{ m.createTime }}</text>
			</view>

			<!-- 写留言 -->
		</scroll-view>
	</view>

</template>

<script setup lang="ts">
	import { onMounted, ref } from 'vue'
	import { getMessageListService, addMessageService } from '@/api/message'
	import danmaku from '@/components/danmaku/danmaku.vue'
	import YlAvatar from '@/components/Image/YlAvatar.vue'
	import ImageWithFallback from '@/components/Image/ImageWithFallback.vue';
	import { t } from '@/utils/i18n'
	import { onPageScroll } from '@dcloudio/uni-app';
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

	interface Msg {
		id : number
		nickname : string
		userAvatar : string
		content : string
		createTime : string
	}

	const content = ref('')
	const msgList = ref<Msg[]>([])
	const danmuList = ref<(Msg & { anim ?: any })[]>([])

	/* 获取留言 */
	async function getMsg() {
		const res = await getMessageListService()
		msgList.value = res.data || []
		// 弹幕只取最新的 20 条
		danmuList.value = msgList.value.slice(-20).map((m : Msg) => ({ ...m }))
	}


	/* 发送留言 */
	async function send() {
		if (!content.value.trim()) {
			uni.showToast({ title: t('contentEmpty'), icon: 'none' })
			return
		}
		const user = uni.getStorageSync('user')
		const newMsg : Msg = {
			id: Date.now(),
			nickname: user.nickname || '游客',
			userAvatar: user.userAvatar,
			content: content.value,
			createTime: formatTime(new Date())
		}
		msgList.value.push(newMsg)
		danmuList.value.push(newMsg)
		content.value = ''
		uni.showToast({ title: t('sendOk'), icon: 'success' })
		await addMessageService({ content: newMsg.content })
	}

	function formatTime(d : Date) {
		return `${d.getFullYear()}-${d.getMonth() + 1}-${d.getDate()} ${d.getHours()}:${d.getMinutes().toString().padStart(2, '0')}`
	}

	onMounted(() => {
		getMsg()
	})
</script>

<style lang="scss" scoped>
	.message {
		width: 100vw;
		height: 100vh;
		background-size: 100% 120%;
		display: flex;
		flex-direction: column;
	}

	.message-header {
		width: 100%;
		height: 40%;
		position: relative;
	}

	.danmaku-box {
		height: 70%;
		overflow: hidden;
	}

	.background {
		position: absolute;
		left: 0;
		top: 0;
		width: 100%;
		height: 100%;
		z-index: -1;
	}

	.msg-container {
		height: 60%;
		padding: 20rpx;
		box-sizing: border-box;
	}

	.paper-card {
		background: url('@/static/images/paper.png') no-repeat;
		background-size: 100% 100%;
		width: 690rpx;
		padding: 30rpx;
		margin: 0 auto 30rpx;
		border-radius: 16rpx;
		box-sizing: border-box;
	}

	.paper-header {
		display: flex;
		align-items: center;
		margin-bottom: 20rpx;
	}

	.paper-avatar {
		margin-right: 16rpx;
	}

	.paper-name {
		font-weight: bold;
		font-size: 28rpx;
	}

	.paper-content {
		display: block;
		font-size: 28rpx;
		line-height: 1.6;
		margin-bottom: 10rpx;
	}

	.paper-time {
		display: block;
		text-align: right;
		font-size: 22rpx;
		color: #999;
	}

.write-box {
  display: flex;          
  flex-direction: column;
  align-items: center;        
  text-align: center; 
}
.write-title {
  font-size: 40rpx;
  color: #ff8c00;
  margin-bottom: 20rpx;
}
.write-row {
  display: flex;
  align-items: center;
  justify-content: center;
}
.write-input {
  width: 400rpx;
  height: 70rpx;
  background: #fff;
  border: 1px solid #ddd;
  border-radius: 35rpx;
  padding: 0 30rpx;
  font-size: 28rpx;
}
.send-btn {
  margin-left: 20rpx;
  height: 70rpx;
  line-height: 70rpx;
  padding: 0 40rpx;
  border-radius: 35rpx;
  background: #ff8c00;
  color: #fff;
  font-size: 28rpx;
}
</style>