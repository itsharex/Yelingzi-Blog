<template>

  <el-card class="simple-right-sidear">

    <div class="action-button pointer" @click="handleToggleSidebar">
      <SvgIcon name="icon-list" size="18" />
    </div>

    <el-badge :value="article.likeCount || 0">
      <div class="action-button pointer" @click="handleLike">
        <SvgIcon name="icon-dianzan_kuai" size="18" class="icon-avtive" :class="{ active: article.isLike }" />
      </div>
    </el-badge>

    <el-badge :value="article.starCount || 0">
      <div class="action-button pointer" @click="handleFavorite">
        <SvgIcon name="icon-star" size="18" class="icon-avtive" :class="{ active: article.isStar }" />
      </div>
    </el-badge>

    <div class="action-button pointer" @click="handleToggleSidebar">
      <SvgIcon name="icon-huanyuan" size="18" />
    </div>

    <el-badge :value="article.commentCount || 0">
      <div class="action-button pointer">
        <SvgIcon name="icon-xiaoxi" size="18" />
      </div>
    </el-badge>

  </el-card>

</template>


<script lang="ts" setup>
import type { Article } from '@/types/article'
import type { PropType } from 'vue';


const props = defineProps({
  article: {
    type: Object as PropType<Article>,
    required: true
  }
})
const emits = defineEmits(['like', 'favorite', 'toggle-sidebar']);

const handleLike = () => {
  emits('like');
};

const handleFavorite = () => {
  emits('favorite');
};

const handleToggleSidebar = () => {
  emits('toggle-sidebar');
};

</script>

<style lang="scss" scoped>
.simple-right-sidear {
  width: 60px;
  margin-left: 10px;
  border-radius: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.action-button {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  margin-bottom: 20px;

  &:hover {
    color: var(--color-pink);
  }
}

.active {
  color: var(--color-red);
  animation: like-animation 0.8s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
  display: inline-block;
}


.icon-avtive {
  transition: transform 0.3s ease-out;
}

@keyframes like-animation {
  0% {
    transform: scale(1);
  }

  50% {
    transform: scale(1.4);
    /* 放大到140% */
  }

  100% {
    transform: scale(1.1);
    /* 最终保持110%大小 */
  }
}
</style>
