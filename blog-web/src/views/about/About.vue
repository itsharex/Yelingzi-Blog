<!-- about -->
<template>

  <el-image class="img animated swashIn" :src="image" lazy fit="cover">
    <template #error>
      <div class="error-fallback">
        <el-image :src="defaultFallback" alt="404" />
      </div>
    </template>
  </el-image>
  <div class="tiele-container">
    <div class="tiele">
      <!-- 标题 -->
      <p class="arttiele">{{ t('about') }}</p>
    </div>
  </div>
  <div class="page-container bg">
    <!-- 头像 -->
    <div class="avatar">
      <el-avatar class="about-avatar" :size="160" :src="ava" />
    </div>
    <div class="about-content">
      <p>大家好！我是叶玲子</p>
      <p>欢迎光临次元裂缝中的小栈</p>
      <p>这里堆满像素泡面与魔法阵奶茶</p>
      <p>我会用左手的函数编织异世界攻略</p>
      <p>用右手的画笔记录动漫星轨的秘语</p>
      <p>当数据洪流淹没黄昏的服务器时</p>
      <p>请收下这串治愈代码：</p>
      <p>while(true){ console.log("要开心哦~") }</p>
      <p>在次元裂缝中漂泊的每个灵魂</p>
      <p>都有二进制写不完的传说</p>
      <p>魔法少女的裙摆藏着死循环</p>
      <p>机甲骑士的剑锋闪烁递归星辰</p>
      <p>---------------------------------------------------------</p>
      <p>忘词了</p>
    </div>
    <snowHr />
    <FriendInfoSection :title="t('aboutMe')" :items="aboutme" />
    <FriendInfoSection :title="t('aboutBlog')" :items="aboutBlog" />
  </div>


</template>

<script setup lang="ts">
import defaultFallback from '@/assets/images/404.gif'
import snowHr from '@/components/Hr/DividerLine.vue';
import FriendInfoSection from '../friend/FriendInfoSection.vue';
import image from '@/assets/images/about-me.jpg'
import ava from '@/assets/images/avatar2.jpg'
import { ref } from 'vue';
import { useI18nStore } from '@/stores';

const aboutBlog = ref([
  '本网站采用 Vue3 + TypeScript 构建前端架构，搭配 Spring Boot 开发后端服务，致力于长期维护与持续迭代更新，保障功能稳定性与体验优化。',
  '已开发并集成即时聊天系统与 AI 智能体功能，支持通过对话交互调用网页端部分服务，实现更便捷的功能操作与智能交互体验。',
  '本网站均为自主学习、设计并开发的成果，覆盖从前期架构设计、数据库模型搭建，到核心业务逻辑实现的完整开发流程，全程自主把控技术细节与功能落地。'
])
const aboutme = ref([
  '你好呀～我是叶玲子，一个喜欢在虚拟与现实之间反复横跳的人。',
  '我喜欢：',
  '🎮 打游戏：从 RPG 到 FPS，只要好玩就停不下来',
  '📺 看番追剧：热血、治愈、中二都可以，泪点和笑点并存',
  '🌊 网上冲浪：追新梗、逛论坛、翻技术文章，像一条停不下来的鱼',
  '',
  '如果你也喜欢二次元、游戏、技术，',
  '那就来和我一起在网络的星海里冒险吧～'
])
const i18nStore = useI18nStore()
const t = (key: string) => {
  return i18nStore.currentConfig[key] || key
}
</script>

<style lang="scss" scoped>
@use '/src/assets/styles/mixin.scss' as m;

.tiele-container {
  @include m.flex;
  flex-direction: column;
  position: relative;
  width: 100%;
  height: 42vh;
  color: var(--color-pink);
}

.tiele {
  @include m.flex;
  flex-direction: column;
  position: fixed;
  z-index: -1;

  .arttiele {
    margin-top: 10vh; // 向下移动 10 视口高度
    font-family: "Fredericka the Great", Mulish, -apple-system, "PingFang SC", "Microsoft YaHei",
      sans-serif;
    font-size: 3.5em;
    line-height: 1.2;
    animation: titleScale 1s;
  }

  .title {
    letter-spacing: 0.1em;
  }
}

.img {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw; // 改用视窗单位
  height: 42vh;
  z-index: -9;
}

.error-fallback {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%; // 继承父容器高度
  display: flex; // 使用 flex 布局
  background: #f5f5f5;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    display: block; // 消除图片默认间隙
    filter: grayscale(80%) blur(1px);
    opacity: 0.9;
  }
}

.avatar {
  margin-top: 3rem;
  text-align: center;
}

.about-avatar {
  transition: all 0.5s;
}

.about-avatar:hover {
  transform: rotate(360deg);
}

.about-content {
  word-break: break-word;
  line-height: 2.4;
  font-size: 1.5rem;
  text-align: center;
}

@media (max-width: 767px) {
  .about-content {
    font-size: 1.2rem;
  }
}

.animated {
  animation-duration: 1s;
  animation-fill-mode: both;
}


@keyframes swashIn {
  0% {
    opacity: 0;
    transform-origin: 50% 50%;
    transform: scale(0, 0);
  }

  90% {
    opacity: 1;
    transform-origin: 50% 50%;
    transform: scale(0.9, 0.9);
  }

  100% {
    opacity: 1;
    transform-origin: 50% 50%;
    transform: scale(1, 1);
  }
}

.swashIn {
  animation-name: swashIn;
}
</style>
