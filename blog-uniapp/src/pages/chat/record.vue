<template>
	<view class="chat-wrapper">
		<!-- 滚动区域 -->
		<scroll-view class="scroll-area" scroll-y :scroll-into-view="scrollInto" scroll-with-animatio>
			<!-- 触发器（空节点即可，scrolltolower 代替 IntersectionObserver） -->
			<view id="trigger" class="load-trigger" />

			<!-- 无消息 -->
			<view v-if="computedList.length === 0" class="no-message">
				向 TA 发送你的第一条消息吧
			</view>

			<!-- 加载提示 -->
			<view v-else>
				<view v-if="loading" class="loading-tip">加载中...</view>
				<view v-if="!hasMore" class="no-more-tip">没有更多消息了</view>
			</view>

			<!-- 消息列表 -->
			<view class="chat-container" v-for="(chat, idx) in computedList" :key="chat.id">

				<!-- 时间 -->
				<view v-if="chat.showTime" class="chat-time">
					{{ chat.formattedTime }}
				</view>

				<!-- 自己发的 -->
				<view v-if="chat.userId === userInfo.id || chat.nickname === userInfo.nickname" class="chat-item self"
					:id="'msg-' + chat.id">
					<view class="content">

						<text v-if="chating.chatType === 'group'" class="text-nickname">
							{{ chat.nickname }}
						</text>
						<!-- 文字 -->
						<view v-if="chat.messageType === 'text'" class="text-box">
							<text>
								{{ chat.message }}
							</text>
						</view>

						<!-- 图片 -->
						<view v-else-if="chat.messageType === 'image'" class="img-box">
							<ImageWithFallback class="image-box" :src="chat.message" :list="imageMessageContents" 
							 :isbindload="true" :maxwidth="255" :maxheight="200" @onload="scrollToBottom()"></ImageWithFallback>
						</view>

						<!-- 表情 -->
						<view v-else-if="chat.messageType === 'emoji'" class="emoji-box">
							<EmojiImage :text="chat.message" />
						</view>

					</view>
					<YlAvatar class="avatar" :src="userInfo.userAvatar" mode="aspectFill" />
				</view>

				<!-- 对方发的 -->
				<view v-else class="chat-item other" :id="'msg-' + chat.id">
					<YlAvatar class="avatar" :src="chat.userAvatar" mode="aspectFill" />
					<view class="content">
						<text v-if="chating.chatType === 'group'" class="text-nickname">
							{{ chat.nickname }}
						</text>
						<!-- 文字 -->
						<view v-if="chat.messageType === 'text'" class="text-box">
							<text>
								{{ chat.message }}
							</text>
						</view>

						<!-- 图片 -->
						<view v-else-if="chat.messageType === 'image'" class="img-box">
							<ImageWithFallback :src="chat.message" :list="imageMessageContents"
							 :isbindload="true" :maxwidth="255" :maxheight="200"/>
						</view>

						<!-- 表情 -->
						<view v-else-if="chat.messageType === 'emoji'" class="emoji-box">
							<EmojiImage :text="chat.message" />
						</view>
					</view>
				</view>

			</view>
		</scroll-view>
		<view id="bottom-anchor" style="height:30rpx"></view>
	</view>
</template>

<script lang="ts" setup>
	import { computed, ref, watch, nextTick, toRef, getCurrentInstance } from 'vue'
	import { useUserStore } from '@/stores'
	import { formatChatDisplayTime } from '@/utils/common'
	import { addWsMessageHandler, } from '@/utils/websocket'
	import ImageWithFallback from '@/components/Image/ImageWithFallback.vue'
	import EmojiImage from '@/components/Image/emojiImage.vue'
	import YlAvatar from '@/components/Image/YlAvatar.vue'
	import { ChatMessage } from '@/types/chatType'
	import type { Dayjs } from 'dayjs'

	/* props */
	const props = defineProps({
		chating: {
			type: Object,
			required: true
		},
		hasMore: {
			type: Boolean,
			default: true
		},
		showToolBoard: {
			type: Boolean,
			default: false
		}
	})

	/** 监听聊天列表长度 */
	const msgList = toRef(() => props.chating?.chatMessageList ?? [])

	/* emit */
	const emit = defineEmits(['load-more'])

	/* store */
	const userStore = useUserStore()
	const userInfo = userStore.userInfo

	/* 滚动相关 */
	const scrollInto = ref('')      // 滚动到某个节点
	let autoScrollEnabled = true   // 是否自动滚到底
	let lastScrollHeight = 0       // 插入数据前高度
	const ins = getCurrentInstance()

	/* 加载态 */
	const loading = ref(false)

	/* 计算属性：给每条消息加时间 */
	const computedList = computed(() => {
		const list = props.chating.chatMessageList || []
		return list.map((cur : any, idx : number) => {
			const formattedTime = formatChatDisplayTime(cur.createTime)
			if (idx === 0) return { ...cur, showTime: true, formattedTime }
			const prev = list[idx - 1]
			const diff = new Date(cur.createTime).getTime() - new Date(prev.createTime).getTime()
			const showTime = diff >= 3 * 60 * 1000
			return { ...cur, showTime, formattedTime }
		})
	})

	const imageMessageContents = computed(() => {
		// 1. 获取聊天消息列表（兜底空数组避免报错）
		const messageList = props.chating.chatMessageList || [];

		// 2. 筛选图片类型消息 + 提取content组成数组
		return messageList
			.filter((message : ChatMessage) => message.messageType === 'image')
			.map((imageMessage : ChatMessage) => imageMessage.message);
	});

	/* 加载更多 */
	function loadMore() {
		if (!props.hasMore || loading.value) return
		loading.value = true
		autoScrollEnabled = false
		lockScroll()
		emit('load-more', () => {
			loading.value = false
			restoreScroll()
			autoScrollEnabled = true
		})
	}

	/* 滚动到底部 */
	function scrollToBottom() {
		const last = computedList.value.at(-1)
		scrollInto.value = ''
		nextTick(() => {
			scrollInto.value = `msg-${last.id}`
			console.log(scrollInto.value)
		})

	}

	/* 锁定 / 恢复滚动位置 */
	function lockScroll() {
		// 小程序 scroll-view 没有 scrollHeight，只能自己算
		// 这里简单用「消息条数」代替，真实场景可用节点查询
		lastScrollHeight = computedList.value.length
	}
	function restoreScroll() {
		nextTick(() => {
			const newLen = computedList.value.length
			const diff = newLen - lastScrollHeight
			if (diff > 0 && computedList.value.length) {
				// 把滚动条定位到「倒数第 diff 条」即可近似恢复
				const target = computedList.value[newLen - diff - 1]
				if (target) scrollInto.value = `msg-${target.id}`
			}
		})
	}

	/* 监听新消息 */
	watch(
		() => msgList.value.length,
		() => {
			nextTick(() => {
				if (autoScrollEnabled) scrollToBottom()
			})
		}
	)

	/* 暴露给父组件 */
	defineExpose({ scrollToBottom, lockScroll, restoreScroll })
</script>

<style lang="scss">
	.chat-wrapper {
		display: flex;
		flex-direction: column;
		height: 100%;
		transition: height 0.25s;
	}

	.scroll-area {
		width: 100%;
		height: 100%;
		box-sizing: border-box;
		padding: 0 32rpx;
		transition: height 0.25s;
	}

	.load-trigger {
		height: 1px;
	}

	.no-message {
		text-align: center;
		font-size: 28rpx;
		color: #999;
		margin-top: 40rpx;
	}

	.loading-tip,
	.no-more-tip {
		text-align: center;
		font-size: 24rpx;
		color: #999;
		padding: 20rpx 0;
	}

	.chat-time {
		text-align: center;
		font-size: 24rpx;
		color: #999;
		margin: 16rpx 0;
	}

	.chat-item {
		display: flex;
		margin: 24rpx 0;
		align-items: flex-start;

		&.self {
			flex-direction: row;
			justify-content: flex-end;

			.content {
				align-items: flex-end;
			}

			.text-box {
				background: #95ec69;
				color: #000;
				max-width: 510rpx;
				border: 16rpx;
			}
		}

		&.other {
			.text-box {
				max-width: 510rpx;
				color: #333;
				background: #fff;
				border: 16rpx;
			}
		}

		.avatar {
			width: 80rpx;
			height: 80rpx;
			border-radius: 50%;
			flex-shrink: 0;
			margin: 0 16rpx;
		}

		.content {
			display: flex;
			flex-direction: column;
			margin-bottom: 30rpx;
		}

		.text-nickname {
			margin-bottom: 16rpx;
		}

		.text-box {
			padding: 16rpx 20rpx;
			border-radius: 16rpx;
			font-size: 30rpx;
			word-break: break-all;

		}

		.img-box {
			max-width: 510rpx;
			max-height: 400rpx;
			border-radius: 16rpx;
			overflow: hidden;
		}

		.image-box {
			width: 100%;
			height: 100%;
		}

		.emoji-box {
			width: 180rpx;
			height: 180rpx;
			border-radius: 12rpx;
			overflow: hidden;
		}
	}
</style>