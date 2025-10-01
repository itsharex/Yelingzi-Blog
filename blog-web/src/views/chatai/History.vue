<template>
  <div class="history-panel">
    <!-- 顶部 Logo + 图标 -->
    <div class="history-header">
      <ImageWithFallback class="logo" :src="Logo" />
      <SvgIcon name="icon-sidebarcebianlan" size="20" class="header-icon pointer" @click="onHideHistory" />
    </div>

    <!-- 开启新对话 -->
    <button class="new-btn" @click="onPushChaiAI">
      <SvgIcon name="icon-jia" class="btn-icon pointer" />
      开启新对话
    </button>

    <!-- 历史会话标题 -->
    <div class="sub-title">
      <SvgIcon name="icon-jishi" class="title-icon" />
      历史会话
    </div>

    <!-- 历史列表 -->
    <div class="history-list">
      <div v-for="item in sessionList" :key="item.sessionId" class="history-item pointer"
        :class="{ active: item.sessionId === sessionId }" @click="onPushSession(item.sessionId)">
        <div>{{ item.description }}</div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import SvgIcon from '@/components/SvgIcon/SvgIcon.vue';
import ImageWithFallback from '@/components/Image/ImageWithFallback.vue';
import Logo from '@/assets/images/yeling.jpg'
import type { PropType } from 'vue';
import type { SessionHistoryView } from '@/types/chatai';
import { useRoute, useRouter } from 'vue-router';

const router = useRouter()
const emit = defineEmits(['hideHistory'])
const props = defineProps({
  sessionList: {
    type: Array as PropType<SessionHistoryView[]>,
    required: true
  },
  sessionId: {
    type: String,
    required: true
  }
})
const onPushSession = (sessionId: string) => {
  router.push({ name: 'chatai', params: { sessionId: sessionId } })
}
const onPushChaiAI = () => {
  router.push({ name: 'chatai-empty' })
}
const onHideHistory = () => {
  emit('hideHistory')
}
</script>


<style lang="scss" scoped>
.history-panel {
  width: 260px; // 与左侧导航同宽
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 16px;
  box-sizing: border-box;
  font-size: 14px;
  color: var(--grey-7);

}

/* ---------- 头部 ---------- */
.history-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;

  .logo {
    width: 36px;
    height: 36px;
    border-radius: 6px;
  }

  .header-icon {
    color: #909399;

    &:hover {
      color: #409eff;
    }
  }
}

/* ---------- 新建按钮 ---------- */
.new-btn {
  width: 100%;
  height: 40px;
  border: 1px solid #dcdfe6;
  border-radius: 6px;
  background-color: var(--grey-1);
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  font-size: 16px;
  transition: all 0.2s;

  &:hover {
    border-color: #409eff;
    color: #409eff;
  }

}

/* ---------- 小标题 ---------- */
.sub-title {
  margin: 20px 0 12px;
  display: flex;
  align-items: center;
  gap: 6px;
  font-weight: 500;

  .title-icon {
    color: #909399;
  }
}

/* ---------- 历史列表 ---------- */
.history-list {
  flex: 1;
  overflow-y: auto;

  .history-item {
    padding: 10px 12px;
    margin-bottom: 8px;
    border-radius: 6px;
    transition: background 0.2s;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;


    &:hover {
      background: #ecf5ff;
      color: #409eff;
    }

    &.active {
      background-color: #aee1ff;
      color: #fff;

      &:hover {
        background-color: #98caff;
      }
    }
  }
}
</style>
