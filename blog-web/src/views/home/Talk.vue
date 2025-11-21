<template>
  <div class="talk-swiper">
    <SvgIcon name="icon-xiaoxi" size="16" class="talk-icon"></SvgIcon>
    <span class="talk-text">{{ t('talk') }}</span>
    <el-carousel class="swiper-container" arrow="never" direction="vertical">
      <el-carousel-item v-if="talkList.length > 0" v-for="(talk, index) in talkList" :key="index"
        @click="toTalk(talk.id)">
        <div class="slide-content pointer">{{ talk.content }}</div>
      </el-carousel-item>
      <el-carousel-item v-else>
        <div class="slide-content">
          加载中<span></span><span></span><span></span>
        </div>
      </el-carousel-item>
    </el-carousel>
    <SvgIcon name="icon-arrow-right" class="arrow" />
    <SvgIcon name="icon-arrow-right" class="arrow" />
    <SvgIcon name="icon-arrow-right" class="arrow" />
  </div>
</template>

<script setup lang="ts">
import { getTopTalkListService } from '@/api/talk';
import type { SimpleTalk } from '@/types/talk';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { t } from '@/utils/i18n'

const router = useRouter()
const talkList = ref<SimpleTalk[]>([]);

const getTopTalkList = async () => {
  const res = await getTopTalkListService()
  talkList.value = res.data.data
}

const toTalk = (id: number) => {
  if (!id) return;
  router.push({ name: 'talk', params: { id: id } })
}

onMounted(() => {

  getTopTalkList()
});
</script>

<style lang="scss" scoped>
@use '/src/assets/styles/mixin.scss' as m;

.talk-swiper {
  @include m.flex;
  margin: 0 0.5rem;
  padding: 0.6rem 1rem;
  font-size: 1rem;
  border-radius: 0.5rem;
  box-shadow: 0 0.625rem 1.875rem -0.9375rem var(--box-bg-shadow);
  transition: all 0.2s ease-in-out 0s;
  gap: 10px;

  &:hover {
    box-shadow: 0 0 2rem var(--box-bg-shadow);
  }
}

.talk-text {
  white-space: nowrap;
}

.swiper-container {
  width: 100%;
  height: 1.6rem;
  border-radius: 0.75rem;
  line-height: 1.6rem;

  &:hover {
    color: var(--comment-btn);
  }
}

.slide-content {
  width: 100%;
  height: 100%;
  text-align: center;
}

.talk-icon {
  transform: translateY(1px);
}

:deep(.el-carousel__indicator--vertical) {
  visibility: hidden;
}

.arrow {
  animation: 1.5s passing infinite;
}

@keyframes passing {
  0% {
    transform: translateX(-50%);
    opacity: 0;
  }

  50% {
    transform: translateX(0);
    opacity: 1;
  }

  100% {
    transform: translateX(50%);
    opacity: 0;
  }
}

.slide-content span {
  display: inline-block;
  width: 6px;
  height: 6px;
  background-color: #888;
  border-radius: 50%;
  animation: typingDots 1.4s infinite ease-in-out both;
}

.slide-content span:nth-child(1) {
  animation-delay: -0.32s;
}

.slide-content span:nth-child(2) {
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
</style>
