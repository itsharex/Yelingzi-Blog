<template>
  <div class="reply-box">
    <!-- 输入框 -->
    <div class="reply-box-text">
      <textarea v-if="textareaStyles" ref="textareaRef" v-model="content" class="reply-box-textarea pen"
        :style="textareaStyles" :placeholder="placeholderText" @input="inputActive" @keydown="handleKeydown" />
    </div>

    <!-- 底部操作栏：选择器 + 发送按钮 -->
    <div class="reply-actions">
      <el-select v-model="model" placeholder="模型选择" class="model-select">
        <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>

      <button class="send-btn" @click="send" :disabled="content == ''"
        :class="content != '' ? 'pointer' : 'user-select-none'">
        <SvgIcon name="icon-fasong" size="20" />
      </button>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { nextTick, ref, type CSSProperties } from 'vue';

const content = ref('')
const placeholderText = ref('尽管问~')
const model = ref('小玲')

const options = [
  {
    value: '小玲',
    label: '小玲',
  },
  {
    value: 'dashscope_qwen-plus-2025-04-28',
    label: 'qwen-plus',
  },
  {
    value: 'dashscope_deepseek-r1-0528',
    label: 'deepseek-r1',
  },
]

const textareaStyles = ref<CSSProperties>({ height: '52px' });
const textareaRef = ref<HTMLTextAreaElement | null>(null);


/* ---------- 高度自适应 ---------- */
const inputActive = () => adjustTextareaHeight()

const adjustTextareaHeight = () => {
  if (!textareaRef.value) return
  const el = textareaRef.value
  const style = window.getComputedStyle(el)
  const pad = parseFloat(style.paddingTop) + parseFloat(style.paddingBottom)

  el.style.height = 'auto'
  if (!el.value.trim()) {
    el.style.height = '52px'
    return
  }
  const newHeight = Math.max(52, Math.min(el.scrollHeight - pad, 200))
  el.style.height = `${newHeight}px`
}

/* ---------- 键盘 ---------- */
const handleKeydown = (e: KeyboardEvent) => {
  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault()   // 阻止默认换行
    send()
  }
  // 如果 shift+enter，浏览器默认换行，不做任何处理
}

/* ---------- 发送 ---------- */
const emit = defineEmits<{
  send: [text: string, model: string]
}>()

const send = () => {
  const text = content.value.trim()
  if (!text) return

  emit('send', text, model.value)
  content.value = ''
  nextTick(() => adjustTextareaHeight())
}

</script>

<style lang="scss" scoped>
.reply-box {
  display: flex;
  flex-direction: column;
  background-color: var(--grey-1);
}

/* ---------- 输入框 ---------- */
.reply-box-text {
  width: 100%;
}

.reply-box-textarea {
  width: 100%;
  min-height: 52px;
  max-height: 200px;
  font-size: 14px;
  line-height: 1.5;
  color: var(--grey-9, #303133);
  resize: none;
  outline: none;
  border: none;
  background: transparent;
  overflow-y: auto !important;
  scrollbar-width: auto;
  -ms-overflow-style: auto;
  padding: 0;

  &::placeholder {
    color: var(--grey-4, #c0c4cc);
  }
}



/* ---------- 底部栏 ---------- */
.reply-actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

/* 选择器 */
.model-select {
  width: 160px;

  :deep(.el-input__wrapper) {
    box-shadow: none !important;
    border: 1px solid #e4e7ed;
    border-radius: 6px;
  }
}

/* 发送按钮 */
.send-btn {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  border: 1px solid #409eff;
  background: #409eff;
  color: #fff;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  transition: all .2s;

  /* 可点击状态 */
  &:hover:not(:disabled) {
    background: #66b1ff;
    border-color: #66b1ff;
  }

  &:active:not(:disabled) {
    transform: scale(.95);
  }

  /* 不可点击状态 */
  &:disabled {
    opacity: .45;
    background: #a0cfff;
    border-color: #a0cfff;
  }
}
</style>
