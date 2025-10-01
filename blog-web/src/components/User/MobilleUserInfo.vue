<template>
  <div class="mobille-user-info">
    <div class="close">
      <SvgIcon name="icon-close1" class="pointer" @click="$emit('closeUserinfo')" v-pio="{ text: `关闭用户信息列表` }" />
    </div>
    <div class="user-info">
      <div class="info-item" v-if="userInfo.userAvatar">
        <YlAvatar :size="80" :src="userInfo.userAvatar" />
      </div>
      <div class="info-item" v-if="userInfo.nickname">
        昵称：{{ userInfo.nickname }}
      </div>
      <div class="info-item" v-if="userInfo.email">
        邮箱：{{ userInfo.email }}
      </div>
    </div>
    <div class="menu">
      <div class="menu-item pointer" @click="toggleUserinfo">
        <SvgIcon name="icon-geren" />
        {{ t('personalCenter') }}
      </div>
      <div class="menu-item pointer" @click="handleLogout">
        <SvgIcon name="icon-chexiao" class="svg-icon" />
        <span>{{ t('logOut') }}</span>
      </div>
    </div>
  </div>

</template>

<script lang="ts" setup>
import { computed } from 'vue';
import { t } from '@/utils/i18n';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores';
import { useChatStore } from '@/stores/modules/chat';
import { ElMessage } from 'element-plus';

const router = useRouter()
const userStore = useUserStore()
const chatStore = useChatStore()

const props = defineProps<{
  userInfo: {
    userAvatar: string
    nickname: string
    email: string
  }
}>()

const emit = defineEmits(['closeUserinfo', 'toUserInfo'])

const toggleUserinfo = () => {
  emit('toUserInfo')
}

const handleLogout = () => {
  userStore.removeToken()
  chatStore.removeChat()
  ElMessage.success('已退出登录')
}

</script>

<style lang="scss" scoped>
.mobille-user-info {
  position: relative;
}

.close {
  position: absolute;
  top: 12px;
  right: 12px;
  z-index: 10;
  padding: 6px;
  line-height: 0;
}

.close .svg-icon {
  color: var(--color-pink);
}

.user-info {
  padding: 20px;
  text-align: center;

  .info-item {
    margin-bottom: 15px;
    font-size: 16px;
    color: var(--grey-7);
  }
}

.menu {
  border-top: 1px solid var(--grey-9-a1);

  .menu-item {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 14px 20px;
    font-size: 1rem;
    color: var(--text-primary-color, #333);
    border-bottom: 1px solid var(--grey-9-a1, #eee);

    .svg-icon {
      margin-right: 10px;
      font-size: 1.2rem;
      color: var(--grey-6);
    }
  }
}
</style>
