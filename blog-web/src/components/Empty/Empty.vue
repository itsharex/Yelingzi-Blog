<template>
  <div class="custom-empty-warp">
    <div class="empty-warp">
      <div class="loading-warp" v-if="loading">
        <img class="loading-img" :src="loadingImg" alt="" />
        <span class="empty-text">{{ loadingText }}</span>
      </div>
      <div class="img-warp" v-else>
        <img class="empty-img" :src="emptyImg" alt="" />
        <span v-if="true" class="empty-text">{{ text }}</span>
      </div>
      <slot></slot>
    </div>
  </div>
</template>

<script lang="ts" setup>
import loadingImg from '@/assets/images/loading.svg?url';
import emptyImg from '@/assets/images/empty.png';

const props = defineProps({
  text: {
    type: String,
    default: '暂无数据'
  },
  loading: {
    type: Boolean,
    default: false
  },
  loadingText: {
    type: String,
    default: '加载中...'
  }
});

</script>

<style lang="scss" scoped>
.custom-empty-warp {
  width: 100%;
  height: 100%;

  .empty-warp {
    background-color: var(--bg-image-warp-color);
    border-radius: var(--border-radius-2);
    height: 100%;
    width: 100%;

    .img-warp,
    .loading-warp {
      height: 100%;
      width: 100%;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;

      .loading-img {
        max-width: 80px;
        max-height: 80px;
        width: 100%;
        height: calc(100% - 20px);
      }

      .empty-img {
        max-width: 180px;
        max-height: 180px;
        width: 100%;
        height: calc(100% - 20px);
      }
    }

    .empty-text {
      height: 36px;
      margin-top: 20px;
      color: var(--empty-text-color);
    }
  }
}
</style>
