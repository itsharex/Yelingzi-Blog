<template>
  <div class="page">

    <div class="header"></div>

    <div class="container">
      <!-- 左侧历史记录 -->
      <div class="container-left" :class="{ active: isShowHistory }">
        <History :session-list="sessionList" :session-id="sessionId" v-on:hide-history="toggleHistory" />
      </div>

      <div class="history-header">
        <ImageWithFallback class="logo" :src="Logo" />
        <SvgIcon name="icon-sidebarcebianlan" size="32" class="header-icon pointer" @click="toggleHistory" />
      </div>

      <!-- 右侧：要么空态、要么聊天态 -->
      <div v-if="sessionId" class="container-center">
        <div class="chat-wrapper">
          <AIChat :history-list="historyList" :is-typing="isTyping" :typing-chat-id="typingChatId" />
        </div>
        <div class="input-wrapper">
          <ChatInput @send="handleSend" />
        </div>
        <div class="tip">内容由 AI 生成，请仔细甄别</div>
      </div>

      <!-- 空态：占满剩余宽度 -->
      <div v-else class="container-empty">
        <div class="title">Yelingzi Blog</div>
        <div class="input-wrapper-empty">
          <ChatInput @send="handleSend" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import History from './History.vue';
import AIChat from './AIChat.vue';
import ChatInput from './ChatInput.vue';
import { onMounted, onUnmounted, ref, watch, type Ref } from 'vue';
import type { ChatHistoryView, SessionHistory, SessionHistoryView } from '@/types/chatai';
import { addSessionIdService, getHistoryBySessionService, getSessionListService, sendAIChatServiceService, sendAIChatsServiceService } from '@/api/chatai';
import { useUserStore } from '@/stores';
import { useRoute, useRouter } from 'vue-router';
import ImageWithFallback from '@/components/Image/ImageWithFallback.vue';
import Logo from '@/assets/images/yeling.jpg'
import { addWsMessageHandler, removeWsMessageHandler } from '@/utils/websocket';

/* ---------------- 数据 ---------------- */
const userStore = useUserStore();
const route = useRoute();
const router = useRouter();
const sessionId = ref('');
const sessionList = ref<SessionHistoryView[]>([]);
const historyList = ref<ChatHistoryView[]>([]);
const abortController = ref<AbortController | null>(null);
const isTyping = ref(false);
const typingChatId = ref<string | number | null>(null);
const isShowHistory = ref(true);

/* ---------------- 方法 ---------------- */
//转换显示与隐藏历史记录
const toggleHistory = () => {
  isShowHistory.value = !isShowHistory.value;
};

/* ---------------- 公共方法 ---------------- */
const addMessage = (date: number, role: 'user' | 'assistant', text: string) => {
  return ({
    id: date,
    role: role,
    content: text
  });
};

// 加载会话列表
const loadSessionList = async () => {
  try {
    const { data } = await getSessionListService(userStore.deviceId);
    sessionList.value = data.data;
  } catch (error) {
    console.error('加载会话列表失败:', error);
    // 这里可以添加UI错误提示
  }
};

// 加载历史消息
const loadHistory = async (id: string) => {
  if (!id) {
    historyList.value = [];
    return;
  }

  try {
    const { data } = await getHistoryBySessionService({
      sessionId: id,
      device: userStore.deviceId
    });
    historyList.value = data.data;
  } catch (error) {
    console.error(`加载会话 ${id} 的历史消息失败:`, error);
    historyList.value = [];
    // 这里可以添加UI错误提示
  }
};


// 创建新会话
const createNewSession = async (text: string) => {
  try {
    const { data } = await addSessionIdService({
      device: userStore.deviceId,
      prompt: text
    });

    sessionId.value = data.data.sessionId;
    sessionList.value.push(data.data);
    return true;
  } catch (error) {
    console.error('创建新会话失败:', error);
    return false;
  }
};

// 处理用户发送消息
const handleSend = async (text: string, model: string) => {
  // 取消之前的请求（如果有）
  abortController.value?.abort();
  abortController.value = new AbortController();

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
    const isXiaoLing = model === '小玲';
    const [platform, modelName] = isXiaoLing ? [] : model.split('_');

    const requestParams = {
      sessionId: sessionId.value,
      prompt: text,
      device: userStore.deviceId,
      token: userStore.accessToken,
      signal: abortController.value?.signal
    };

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
    // router.push({ name: 'chatai', params: { sessionId: sessionId.value } });
  } catch (error) {
    const errorMessage = error instanceof Error
      ? error.message
      : '未知错误';
    const msg = historyList.value.find(m => m.id === typingChatId.value);
    if (msg) {
      msg.content += errorMessage
    }
  }
};

const onWsMessage = (data: any) => {

  const msg = historyList.value.find(m => m.id === typingChatId.value);
  console.log(data)
  if (data === 'DONE!!') {
    isTyping.value = false;
    typingChatId.value = null;
    return
  }
  if (typingChatId.value) {
    if (msg) {
      msg.content += data;
    }
  }

};

/* ---------------- 监听路由 ---------------- */
watch(
  () => route.params.sessionId as string | undefined,
  async (newId) => {
    sessionId.value = newId ?? '';
    await loadHistory(sessionId.value);
  },
  { immediate: true }
);

/* ---------------- 生命周期 ---------------- */
onMounted(() => {
  addWsMessageHandler('aichat', onWsMessage)
  loadSessionList()
});

// 组件卸载时取消所有请求
onUnmounted(() => {
  abortController.value?.abort();
  removeWsMessageHandler('aichat', onWsMessage)
});
</script>

<style lang="scss" scoped>
/* ===== 整体布局 ===== */
.page {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: var(--grey-1);
}

.header {
  height: 96px;
  flex-shrink: 0;
}

.container {
  display: flex;
  height: 100vh;
  overflow: hidden;
  position: relative;
}

/* 左侧固定 */
.container-left {
  padding-top: 30px;
  width: 0px;
  flex-shrink: 0;
  background-color: var(--grey-1);
  border-right: 1px solid #e4e7ed;
  overflow-y: auto;
  transition: all 0.3s ease;
  z-index: 10;

  &.active {
    width: 260px;
  }

}

.history-header {
  position: absolute;
  top: 10px;
  left: 10px;
  z-index: 9;
  line-height: 0;
  display: flex;
  align-items: center;
  border-radius: 8px;
  gap: 10px;
  // background: ;

  .logo {
    width: 32px;
    height: 32px;
  }

  .header-icon {
    color: #909399;

    &:hover {
      color: #409eff;
    }
  }
}

/* 聊天态：900 宽居中 */
.container-center {
  width: 900px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  background: #fff;
}

/* 聊天内容区：撑满剩余高度，可滚动 */
.chat-wrapper {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  box-sizing: border-box;
}

/* 输入区：固定在底部 */
.input-wrapper {
  flex-shrink: 0;
  padding: 8px 10px;
  border: 1px solid #e4e7ed;
  border-radius: 20px;
}

.tip {
  display: flex;
  justify-content: center;
  padding: 10px 0;
  color: var();
  font-size: 12px;
}

/* 空态：占满剩余空间，纵向 + 横向居中 */
.container-empty {
  flex: 1;
  /* 占剩余宽度 */
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 24px;
}

.title {
  font-size: 50px;
}

/* 底部输入框：同聊天态样式 */
.input-wrapper-empty {
  width: 720px;
  padding: 8px 10px;
  border: 1px solid #e4e7ed;
  border-radius: 20px;
}

@media screen and (max-width: 768px) {
  .container-center {
    width: 100%;
  }

  .input-wrapper-empty {
    width: 90%;
  }

  .container-left {
    width: 260px;
    left: -260px;
    position: absolute;
    height: 100%;

    &.active {
      width: 260px;
      transform: translateX(260px);
    }
  }
}
</style>
