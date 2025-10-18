<template>
  <div class="chat-wrapper">
    <div class="chat-record" v-for="chat in historyList" :key="chat.id">

      <!-- 消息内容容器 -->
      <div class="bubble-area" :class="{ 'bubble-area--user': chat.role === 'user' }">
        <!-- AI 消息 -->
        <template v-if="chat.role !== 'user'">
          <div class="bubble bubble--ai">
            <div v-if="isTyping && chat.id === typingChatId" class="typing-indicator">
              思考中
              <span></span><span></span><span></span>
            </div>
            <div class="user-select">
              <!-- <mpHtml :id="`aichat-${chat.id}`" v-model="chat.content" /> -->
			  {{ chat.content }}
            </div>
          </div>
        </template>

        <!-- 用户消息 -->
        <template v-else>
          <div class="bubble bubble--user user-select">
            {{ chat.content }}
          </div>
        </template>
      </div>

      <!-- 工具图标 -->
      <div class="bubble-icon" :class="{ 'bubble-area--user': chat.role === 'user' }">
        <div class="copy">
          <SvgIcon @click="copy(chat.content)" name="icon-fuzhi" class="pointer" />
          <span class="icon-tip">复制</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { ChatHistoryView } from '@/types/chatai';
import { computed, type PropType } from 'vue';
import mpHtml from 'mp-html/dist/uni-app/components/mp-html/mp-html'

const props = defineProps({
  historyList: {
    type: Array as PropType<ChatHistoryView[]>,
    required: true
  },
  isTyping: {
    type: Boolean,
    default: false
  },
  typingChatId: {
    type: [String, Number, null],
    default: null
  }
});

const copy = async (text: string) => {
  await navigator.clipboard.writeText(text)
  uni.showToast({ title: '已复制到剪贴板', icon: 'success' })
};
</script>

<style scoped lang="scss">
/* ---------- 变量 ---------- */
$user-color: rgba(0, 0, 0, 0.1);
$radius: 12px;
$gap: 12px;


.chat-wrapper {
  padding: 16px;
  gap: $gap;
}

.chat-record {
  gap: $gap;

  &:hover .bubble-icon {
    visibility: visible;
  }
}

/* 用户消息靠右 */
.bubble-area--user {
  flex-direction: row-reverse;
}


.bubble--user {
  background: $user-color;
  width: fit-content;
  max-width: 70%;
  box-shadow: 0 2px 8px rgba(79, 140, 255, 0.08);
}

.bubble--ai {
  margin-top: 10px;
}


.bubble-area {
  display: flex;
  align-items: flex-start;
  flex: 1;

  .bubble {
    display: flex;
    flex-direction: column;
    padding: 10px 16px;
    border-radius: $radius;
    word-break: break-word;

    &--user {
      flex-direction: row-reverse;
      white-space: pre-wrap;
      border-top-right-radius: 4px;
      border-top-left-radius: $radius;
      border-bottom-left-radius: $radius;
      border-bottom-right-radius: $radius;
    }

    /* md-editor-v3 代码块横向滚动 */
    :deep(pre) {
      overflow-x: auto;
    }
  }
}


.bubble-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 6px 0 0 16px;
  visibility: hidden;
}

.copy {
  position: relative;
  display: inline-block;

  &:hover .icon-tip {
    visibility: visible;
  }
}

.icon-tip {
  visibility: hidden;
  top: 16px;
  right: 50%;
  transform: translateX(50%);
  color: var(--color-black);
  background-color: var(--color-pink-light);
  width: 50px;
  text-align: center;
  border-radius: 6px;
  position: absolute;
}

.chat-record--user .bubble-icon {
  justify-content: flex-end;
}

.chat-record:not(.chat-record--user) .bubble-icon {
  justify-content: flex-start;
}

.typing-indicator {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 4px;
}

.typing-indicator span {
  display: inline-block;
  width: 6px;
  height: 6px;
  background-color: #888;
  border-radius: 50%;
  animation: typingDots 1.4s infinite ease-in-out both;
}

.typing-indicator span:nth-child(1) {
  animation-delay: -0.32s;
}

.typing-indicator span:nth-child(2) {
  animation-delay: -0.16s;
}

@keyframes typingDots {

  0%,
  80%,
  100% {
    transform: scale(0);
  }

  40% {
    transform: scale(1);
  }
}

/* ---------- 移动端 ---------- */
@media (max-width: 600px) {
  .chat-wrapper {
    padding: 6px;
    gap: 8px;
  }

  .chat-record {
    gap: 6px;
    margin-bottom: 4px;
  }

  .bubble-area {
    max-width: 90%;
  }

  .bubble-area .bubble {
    font-size: 13px;
    padding: 8px 10px;
  }
}
</style>
