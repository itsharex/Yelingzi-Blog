<template>
  <view class="container">
	  
	      <!-- 页面遮罩（工具栏展开时才显示） -->
	<view
	        v-if="showToolBoard"
	        class="page-mask"
	        @tap.stop="closeToolBoard" 
	        @click.stop="closeToolBoard"  
	      />
    <!-- 聊天页头部 -->
    <view class="chat-header">
      <!-- 返回 -->
      <view class="header-left">
        <SvgIcon name="icon-back" class="icon-back" />
      </view>

      <!-- 昵称与状态 -->
      <view class="header-center">
        <text class="chat-nickname">{{ chating.nickname }}</text>
      </view>

      <!-- 更多 -->
      <view class="header-right" @tap="showMore">
        <SvgIcon name="icon-list" class="icon-more" />
      </view>
    </view>

    <!-- 聊天记录区域（占位） -->

		<recordVue class="chat-area" ref="chatRef" :has-more="hasMore" :chating="chating" :showToolBoard="showToolBoard"/>


    <!-- 底部输入 -->
    <GroupChatInput v-if="chating.chatType === 'group'" @scrollToBottom="scrollToBottom" 
	@open-tool-board="openToolBoard"  @close-tool-board="closeToolBoard" 
	class="chat-input" :class="{ active: showToolBoard }" catchtap ref="chatInputRef" />
    <chat-input v-else @scrollToBottom="scrollToBottom" @open-tool-board="openToolBoard" 
	@close-tool-board="closeToolBoard" 
	class="chat-input" :class="{ active: showToolBoard }" catchtap ref="chatInputRef"/>
	
  </view>
</template>

<script lang="ts" setup>
/* ----------------- 1. 依赖引入 ----------------- */
import { onLoad, onUnload } from '@dcloudio/uni-app'
import { nextTick, reactive, ref, toRefs, watch } from 'vue'
import { addWsMessageHandler, removeWsMessageHandler } from '@/utils/websocket'
import { useUserStore, useChatStore } from '@/stores'
import {
  getChatListService,
  getNewChatCountByGroupService,
  getNewChatCountBySingleService
} from '@/api/chat'
import type { Chat, ChatMessage, WsMsg } from '@/types/chatType'
import recordVue from './record.vue'
import chatInput from './ChatInput.vue'
import GroupChatInput from './GroupChatInput.vue'

/* ----------------- 2. 状态定义（只读化） ----------------- */
const userStore = useUserStore()
const chatStore = useChatStore()
const chatRef = ref()
const showToolBoard = ref(false)
const chatInputRef = ref()
const scrollTop = ref(0)         
const scrollHeight = ref(0)

const state = reactive({
  currentChatId: '',               // 当前会话 id
  chating: {} as Chat,             // 当前会话快照（仅当前页可写）
  unReadMessage: 0,                // 本页未读计数
  hasMore: true                    // 是否还能向上翻页
})
const { chating, unReadMessage, hasMore } = toRefs(state)

/* ----------------- 3. 生命周期钩子函数 ----------------- */
/* 3.1 路由进入：解析参数 → 初始化会话 → 注册 ws → 拉最新消息 */
onLoad(async (option: AnyObject = {}) => {
  const { chatType } = option
  if (!chatType) return failAndBack('没有这个会话喵！')

  const target = chatStore.chatList.find(c => c.route === chatType)
  if (!target) return failAndBack('加载失败了喵！')

  await initSession(target.id)
  addWsMessageHandler('chat', onWsMessage)
})

/* 3.2 路由离开：卸载 ws → 把当前会话写回 store */
onUnload(() => {
  removeWsMessageHandler('chat', onWsMessage)
  saveSession()
})

/* ----------------- 4. 业务函数（按执行顺序摆放） ----------------- */
/** 初始化会话：把 store 里的会话完整拷贝到当前页面，再拉最新消息 */
async function initSession(id: string) {
  state.currentChatId = id
  const target = chatStore.chatList.find(c => c.id === id)!
  state.chating = cloneChat(target)          // 深拷贝，避免直接改 store
  await loadLatestMessages()
}

/** WebSocket 消息统一入口 */
function onWsMessage({ receiver, message }: WsMsg) {
  if (receiver !== state.currentChatId) return updateOtherSession(receiver, message)
  appendMessage(message)
}

/** 把最新消息追加到当前列表，并滚动到底部 */
function appendMessage(msg: ChatMessage) {
  const list = state.chating.chatMessageList
  if (list.some(m => m.id === msg.id)) return 
  list.push(msg)
  state.chating.lastMessage = msg
  nextTick(() => chatRef.value?.scrollToBottom())
}

/** 更新「非当前」会话的未读数与 lastMessage（只写 store） */
function updateOtherSession(receiver: string, msg: ChatMessage) {
  const target = chatStore.chatList.find(c => c.id === receiver)
  if (!target) return
  target.lastMessage = msg
  target.unReadChat++
}

/** 加载最新消息（向下） */
async function loadLatestMessages() {
  const { chatMessageList, lastMessage, unReadChat } = state.chating
  const direction: 'before' | 'after' = unReadChat > 1000 ? 'before' : 'after'
  const limit = direction === 'before' ? 50 : 20
  const cursor = direction === 'before'
    ? (lastMessage.id || 0) + 1
    : chatMessageList.at(-1)?.id || 0

  const { data } = await getChatListService({ cursor, direction, limit, type: state.chating.chatType }, userStore.deviceId)

  if (direction === 'before') {
    state.chating.chatMessageList = [...data.list, ...chatMessageList]
  } else {
    state.chating.chatMessageList = mergeMessages(chatMessageList, data.list)
    state.unReadMessage = unReadChat
    state.chating.unReadChat = 0
  }
  state.hasMore = data.hasMore
}

/** 向上翻页：加载更多历史消息 */
async function onLoadMore(done: () => void) {
  if (!state.hasMore) return done()
  chatRef.value?.lockScroll()
  try {
    const firstId = state.chating.chatMessageList[0]?.id || 0
    const { data } = await getChatListService({
      cursor: firstId,
      direction: 'before',
      limit: 20,
      type: state.chating.chatType
    }, userStore.deviceId)
    state.chating.chatMessageList.unshift(...data.list)
    state.hasMore = data.hasMore
    nextTick(() => (chatRef.value?.restoreScroll(), done()))
  } catch {
    state.hasMore = false
    done()
  }
}

/** 把当前页会话写回 store（唯一写入口） */
function saveSession() {
  const idx = chatStore.chatList.findIndex(c => c.id === state.currentChatId)
  if (idx > -1) chatStore.chatList[idx] = cloneChat(state.chating)
}

/** 更多按钮 */
function showMore() {
  uni.showToast({ title: '更多功能开发中…', icon: 'none' })
}

/** 打开工具面板 */
const openToolBoard = () => {
	showToolBoard.value = true
}

/** 关闭工具面板 */
const closeToolBoard = () => {
	showToolBoard.value = false
	setTimeout(() => {
		chatInputRef.value?.closeAllTools()
	}, 200);
	
}

/** 真正滚到底的函数 */
function scrollToBottom () {
  chatRef.value?.scrollToBottom()
}

watch(
  () => showToolBoard.value,
  (val) => {
    if (val) {
      setTimeout(() => {
        scrollToBottom()
      }, 200)
    }
  }
)

/* ----------------- 5. 工具函数 ----------------- */
/** 轻量级深拷贝 Chat 对象 */
function cloneChat(c: Chat): Chat {
  return JSON.parse(JSON.stringify(c))
}
/** 合并消息，去重 + 按 id 升序 */
function mergeMessages(oldList: ChatMessage[], newList: ChatMessage[]): ChatMessage[] {
  const map = new Map<number, ChatMessage>()
  oldList.forEach(m => map.set(m.id, m))
  newList.forEach(m => map.set(m.id, m))
  return Array.from(map.values()).sort((a, b) => a.id - b.id)
}
/** 异常提示并返回上一页 */
function failAndBack(msg: string) {
  uni.showToast({ title: msg, icon: 'none' })
  setTimeout(() => uni.navigateBack(), 1500)
}

</script>

<style lang="scss">
/* 变量 */
$header-height: 88rpx;
$header-bg: #ffffff;
$border-color: #eeeeee;

.container{
	overflow-y: hidden;
	height: 100vh;
	position: relative;
	display: flex;
	flex-direction: column;
}

.page-mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 650rpx;
  z-index: 9; 
}

.chat-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: $header-height;
  background: $header-bg;
  border-bottom: 1px solid $border-color;
  display: flex;
  align-items: center;
  padding: 0 32rpx;
  z-index: 999;
}

.header-left,
.header-right {
  flex-shrink: 0;
  width: 48rpx;
  height: 48rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.header-center {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.chat-nickname {
  font-size: 34rpx;
  color: #333;
  font-weight: 500;
}

.online-status {
  margin-top: 4rpx;
  font-size: 24rpx;
  color: #1aad19;
}

.chat-area {
    flex: 1;
    overflow-y: auto;
    background-color: #f5f5f5;
    transition: height 0.25s;
  background-color: #f5f5f5;
}

// .chat-area.tool-board-open {
//   height: calc(100vh - #{$header-height} - 200rpx - 450rpx);
//   transform: translateY(-450rpx);
// }
.chat-input {
  height: 200rpx;
  transition: height 0.25s;
}

.chat-input.active {
  height: 650rpx; /* 200 + 450 */
}

</style>