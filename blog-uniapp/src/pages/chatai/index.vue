<template>
	<view class="page">
		<!-- 1. 左侧抽屉 -->
		<view>
			<uni-drawer ref="drawer" mode="left" :width="260">
				<view class="close">
					<History :session-list="sessionList" :session-id="sessionId" @hide-history="closeHistory" @updateSessionId="updateSessionId"/>
				</view>
			</uni-drawer>
		</view>

		<!-- 2. 顶部自定义导航 -->
		<view class="header">
			<SvgIcon name="icon-sidebarcebianlan" size="42" @tap="openHistory" />
			<view class="nav-center">
				<picker mode="selector" :range="options" range-key="label" :value="modelIndex" @change="switchModel">
					<view class="model-picker">
						{{ options[modelIndex].label }}
						<text class="iconfont icon-arrow-down" />
					</view>
				</picker>
				<text class="tip">内容由 AI 生成，请仔细甄别</text>
			</view>
		</view>

		<!-- 3. 聊天主体 -->
		<view class="container">
			<!-- 有会话才展示 -->
			<view v-if="historyList.length > 0" class="center-wrap">
				<scroll-view class="chat-wrapper" scroll-y :scroll-top="scrollTop" :scroll-with-animation="true">
					<AIChat  :history-list="historyList" :is-typing="isTyping" :typing-chat-id="typingChatId" />
				</scroll-view>

			</view>

			<!-- 空态 -->
			<view v-else class="empty">
				<text class="title">开始一段新对话</text>
			</view>

			<view class="input-wrapper">
				<ChatInput @send="handleSend" />
			</view>
		</view>
	</view>
</template>

<script setup lang="ts">
	import History from './History.vue';
	import AIChat from './AIChat.vue';
	import ChatInput from './ChatInput.vue';
	import { onMounted, onUnmounted, ref, watch, type Ref, computed } from 'vue';
	import type { ChatHistoryView, SessionHistory, SessionHistoryView } from '@/types/chatai';
	import { addSessionIdService, getHistoryBySessionService, getSessionListService, sendAIChatServiceService, sendAIChatsServiceService } from '@/api/chatai';
	import { useUserStore } from '@/stores';;
	import { debounce, forEach } from 'lodash-es';
	import ImageWithFallback from '@/components/Image/ImageWithFallback.vue';
	import { addWsMessageHandler, removeWsMessageHandler } from '@/utils/websocket';
	import { onLoad, onShow, onUnload } from '@dcloudio/uni-app';
	import { marked } from 'marked'

	/* ---------------- 数据 ---------------- */
	const userStore = useUserStore();
	const sessionId = ref('');
	const sessionList = ref<SessionHistoryView[]>([]);
	const historyList = ref<ChatHistoryView[]>([]);
	const isTyping = ref(false);
	const typingChatId = ref<string | number | null>(null);
	const scrollTop = ref(0)
	// 抽屉实例
	const drawer = ref()
	// 模型切换
	const options = [
		{ label: '小玲', value: '小玲' },
		{ label: 'qwen-plus', value: 'dashscope_qwen-plus-2025-04-28' },
		{ label: 'deepseek-r1', value: 'dashscope_deepseek-r1-0528' }
	]
	const modelIndex = ref(0)
	const currentModel = computed(() => options[modelIndex.value]?.value)
	function switchModel(e : any) {
		modelIndex.value = e.detail.value
	}


	/* ---------------- 公共方法 ---------------- */
	const addMessage = (date : number, role : 'user' | 'assistant', text : string) => {
		return ({
			id: date,
			role: role,
			content: text
		});
	};

	/* ---------------- 方法 ---------------- */
	//转换显示与隐藏历史记录
	const openHistory = () => {
		drawer.value.open()
	};
	const closeHistory = () => {
		drawer.value.close()
	};

	// 加载会话列表
	const loadSessionList = async () => {
		try {
			const { data } = await getSessionListService(userStore.deviceId);
			console.log(data)
			sessionList.value = data;
		} catch (error) {
			console.error('加载会话列表失败:', error);
		}
	};

	// 加载历史消息
	const loadHistory = async (id : string) => {
		if (!id) {
			historyList.value = [];
			return;
		}

		try {
			const { data } = await getHistoryBySessionService({
				sessionId: id,
				device: userStore.deviceId
			});
			for(let i of data){
				// let content = await marked(i.content)
				historyList.value.push({
					role: i.role, content: i.content,
					id: 0
				})
			}

		} catch (error) {
			console.error(`加载会话 ${id} 的历史消息失败:`, error);
			historyList.value = [];
		}
	};
	//更新会话
	const updateSessionId = (id : string) =>{
		sessionId.value = id
		historyList.value = []
		loadHistory(id)
		closeHistory()
	}


	// 创建新会话
	const createNewSession = async (text : string) => {
		try {
			const { data } = await addSessionIdService({
				device: userStore.deviceId,
				prompt: text
			});

			sessionId.value = data.sessionId;
			sessionList.value.push(data);
			console.log('创建新会话成功')
			return true;
		} catch (error) {
			console.error('创建新会话失败:', error);
			return false;
		}
	};

	// 处理用户发送消息
	const handleSend = async (text : string) => {

		// 1. 无 sessionId 先创建会话
		if (!sessionId.value && !(await createNewSession(text))) {
			return;
		}

		const messageId = Date.now();
		// 添加用户消息
		historyList.value.push(addMessage(messageId, 'user', text))
		isTyping.value = true;

		const assistantMessageId = messageId + 1;
		historyList.value.push(addMessage(assistantMessageId, 'assistant', ''));
		typingChatId.value = assistantMessageId;

		try {
			const isXiaoLing = currentModel.value === '小玲';
			const [platform = '', modelName = ''] = isXiaoLing ? [] : currentModel.value?.split('_') || [];

			const requestParams = {
				sessionId: sessionId.value,
				prompt: text,
				device: userStore.deviceId,
			};

			// 触发 AI 开始生成（HTTP 请求）
			if (isXiaoLing) {
				await sendAIChatServiceService(requestParams);
			} else {
				await sendAIChatsServiceService(
					{
						options: {
							platform,
							model: modelName,
							temperature: 0.7
						},
						sessionId: sessionId.value,
						prompt: text
					},
					requestParams.device);
			}

			// ✅ 不再处理 ReadableStream，等待 WebSocket 推送
		} catch (error) {
			const errorMessage = error instanceof Error ? error.message : '未知错误';
		} 
	};


	const onWsMessage = (data : any) => {

		const msg = historyList.value.find(m => m.id === typingChatId.value);

		if (data==='DONE!!') {	
			isTyping.value = false;
			typingChatId.value = null;
			return
		}
		if (typingChatId.value) {
			if (msg) {
				msg.content += data;
			}
		}
		console.log(msg)
		console.log(typingChatId.value)
		console.log(historyList.value)
	
	};

	/* ---------------- 初始化 ---------------- */
	const init = async () => {
		await loadSessionList()
		const lastSession = sessionList.value?.at(-1);
		if (lastSession) {
			sessionId.value = lastSession.sessionId;  // TS 推断此处必为 string
			await loadHistory(sessionId.value);
		}
	}

	/* ---------------- 生命周期 ---------------- */
	onLoad(() => {
		addWsMessageHandler('aichat', onWsMessage)
		init()
	})

	onUnload(() => {
		removeWsMessageHandler('aichat', onWsMessage)
	})
</script>

<style lang="scss" scoped>
	/* 主题色变量，可在 uni.scss 中统一覆盖 */
	$page-bg: #f5f6fa;
	$header-h: 96rpx;
	$drawer-w: 260rpx;

	.page {
		height: 100vh;
		background: $page-bg;
		display: flex;
		flex-direction: column;
		position: relative;
	}

	.my-drawer {
		height: 100vh;
		z-index: 100;
		background: #fafafa;
		width: 260rpx;
	}

	/* 顶部导航 */
	.header {
		padding: 0 76rpx 0 32rpx;
		display: flex;
		align-items: center;
		background: #fff;
		box-shadow: 0 2rpx 8rpx 0 rgba(0, 0, 0, 0.04);
		position: relative;
		z-index: 9;
		height: $header-h;

		.iconfont {
			font-size: 40rpx;
			color: #606266;
			margin-right: 24rpx;
		}

		.nav-center {
			flex: 1;
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;

			.model-picker {
				display: flex;
				align-items: center;
				font-size: 28rpx;
				color: #303133;

				.icon-arrow-down {
					margin-left: 8rpx;
					font-size: 24rpx;
					color: #909399;
				}
			}

			.tip {
				font-size: 22rpx;
				color: #909399;
				margin-top: 4rpx;
			}
		}
	}

	/* 主体 */
	.container {
		flex: 1;
		display: flex;
		flex-direction: column;
	}

	.center-wrap {
		flex: 1;
		/* 新增：吃光.container里剩余高度 */
		display: flex;
		flex-direction: column;
		background: #fff;
		border-radius: 24rpx 24rpx 0 0;
		overflow: hidden;
	}


	/* 聊天内容区 */
	.chat-wrapper {
		flex: 1;
		/* 新增：吃光.center-wrap里剩余高度 */
		padding: 24rpx;
		box-sizing: border-box;
	}

	/* 输入区 */
	.input-wrapper {
		flex-shrink: 0;
		padding: 16rpx 24rpx;
		border-top: 1rpx solid #d1d1d1;
		max-height: 240rpx;
	}

	/* 空态 */
	.empty {
		flex: 1;
		display: flex;
		align-items: center;
		justify-content: center;

		.title {
			font-size: 40rpx;
			color: #c0c4cc;
		}
	}
</style>