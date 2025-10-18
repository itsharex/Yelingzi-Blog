<template>
	<FixedLayoutVue :scroll-top="scrollTop" :scroll-height="scrollHeight">
		<view class="home-warp">
		<view class="about">
		  <!-- 顶部大图 -->
		  <ImageWithFallback
		    class="img swashIn"
		    src="/image/common/images/about-me.jpg"
			  mode="aspectFill"
		  />
		
		  <!-- 标题区 -->
		  <view class="title-wrap">
		    <text class="title">{{ t('about') }}</text>
		  </view>
		
		  <!-- 内容区 -->
		  <view class="page-box">
		    <!-- 头像 -->
		    <view class="avatar-box">
		      <YlAvatarVue class="avatar" src="/image/common/images/avatar2.jpg" size="220" mode="aspectFill" />
		    </view>
		
		    <!-- 文案 -->
		    <view class="content">
		      <text>大家好！我是叶玲子</text>
		      <text>欢迎光临次元裂缝中的小栈</text>
		      <text>这里堆满像素泡面与魔法阵奶茶</text>
		      <text>我会用左手的函数编织异世界攻略</text>
		      <text>用右手的画笔记录动漫星轨的秘语</text>
		      <text>当数据洪流淹没黄昏的服务器时</text>
		      <text>请收下这串治愈代码：</text>
		      <text>while(true){ console.log("要开心哦~") }</text>
		      <text>在次元裂缝中漂泊的每个灵魂</text>
		      <text>都有二进制写不完的传说</text>
		      <text>魔法少女的裙摆藏着死循环</text>
		      <text>机甲骑士的剑锋闪烁递归星辰</text>
		      <text>------------------------------</text>
		      <text>忘词了</text>
		    </view>
		
		    <!-- 分割线 -->
		    <DividerLine />
		
		    <!-- 信息块 -->
		    <TextList :title="t('aboutMe')" :items="aboutMe" />
		    <TextList :title="t('aboutBlog')" :items="aboutBlog" />
		  </view>
		</view>	
		</view>
	</FixedLayoutVue>
		

</template>

<script setup>
import { ref } from 'vue'
import TextList from '@/components/TextList/TextList.vue'
import DividerLine from '@/components/Hr/DividerLine.vue'
import YlAvatarVue from '@/components/Image/YlAvatar.vue'
import ImageWithFallback from '@/components/Image/ImageWithFallback.vue';
import { t } from '@/utils/i18n';
import FixedLayoutVue from '@/components/Layout/FixedLayout.vue';
import { onPageScroll } from '@dcloudio/uni-app'
	const scrollTop = ref(0)
	const windowHeight = ref(uni.getSystemInfoSync().windowHeight)
	const scrollHeight = ref(windowHeight.value + 1)
	
	
	onPageScroll((e) => {
		scrollTop.value = e.scrollTop
		
		uni.createSelectorQuery()
			.select('.home-warp')       
			.boundingClientRect(rect => {
				scrollHeight.value =  rect?.height - windowHeight.value
			})
			.exec();
	})

/* 图片加载失败处理 */
const imgErr = ref(false)
function onImgError() {
  imgErr.value = true
}

/* 数据 */
const aboutMe = ref([
	'你好呀～我是叶玲子，一个喜欢在虚拟与现实之间反复横跳的人。',
	'我喜欢：',
	'🎮 打游戏：从 RPG 到 FPS，只要好玩就停不下来',
	'📺 看番追剧：热血、治愈、中二都可以，泪点和笑点并存',
	'🌊 网上冲浪：追新梗、逛论坛、翻技术文章，像一条停不下来的鱼',
	'',
	'如果你也喜欢二次元、游戏、技术，',
	'那就来和我一起在网络的星海里冒险吧～'
])
const aboutBlog = ref([
  '本网站采用 Vue3 + TypeScript 构建前端架构，搭配 Spring Boot 开发后端服务，致力于长期维护与持续迭代更新，保障功能稳定性与体验优化。',
  '已开发并集成即时聊天系统与 AI 智能体功能，支持通过对话交互调用网页端部分服务，实现更便捷的功能操作与智能交互体验。',
  '本网站均为自主学习、设计并开发的成果，覆盖从前期架构设计、数据库模型搭建，到核心业务逻辑实现的完整开发流程，全程自主把控技术细节与功能落地。'
])
</script>

<style scoped>
/* 顶部大图 */
.img{
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 42vh;
  z-index: -9;
}

/* 标题 */
.title-wrap{
  display: flex;
  justify-content: center;
  align-items: center;
  height: 42vh;
}
.title{
  font-size: 56rpx;
  color: var(--ui-PINK, #ff5777);
  animation: titleScale 1s;
}

/* 内容区 */
.page-box{
  padding: 30rpx;
  background: #fff;
  min-height: 100vh;
  box-sizing: border-box;
}
.avatar-box{
  display: flex;
  text-align: center;
  justify-content: center;
  margin: 40rpx 0;
}
.avatar{
  transition: transform .5s;
}
.avatar:hover{
  transform: rotate(360deg);
}

.content{
  line-height: 2.4;
  font-size: 34rpx;
  text-align: center;
}
.content text{
  display: block;
}

/* 简单动画 */
@keyframes titleScale{
  0% { transform: scale(.8); opacity: .6; }
  100%{ transform: scale(1); opacity: 1; }
}
@keyframes swashIn{
  0% { transform: scale(0); opacity: 0; }
  90%{ transform: scale(.9); opacity: 1; }
  100%{ transform: scale(1); opacity: 1; }
}
.swashIn{
  animation: swashIn 1s both;
}
</style>