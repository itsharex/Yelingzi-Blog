<template>
  <view class="footer-container-warp">
    <view class="content-warp">
      <view class="sheming display-warp" style="text-wrap: nowrap">本站内容仅用于学习交流，如有侵权！请联系站长删除。敬请谅解！</view>
      <view class="copy-right display-warp" style="margin-right: 14px">Copyright ©2024 - {{ new Date().getFullYear() }} By {{ t('nickname') }} 版权所有</view>

      <view class="beianhao-value hover-shadow" @tap="handleICPBeian">
        <view class="num" :style="{ 'background-color': state.domainBeianBg }">{{ t('icpBeian') }}</view>
      </view>
      <view class="web-beian-warp hover-shadow"@tap="handleWebBeian">
        <image style="width: 21px; height: 21px; vertical-align: middle" src="https://www.yeling.top/image/common/images/gongan.png" />
        <view :style="{ 'background-color': state.webBeianBg }">{{ t('beian') }}</view>
      </view>
	      <view v-if="ver != ''" class="display-warp">版本号：{{ ver }}</view>
    </view>
  </view>
</template>

<script lang="ts" setup>
import { reactive } from 'vue';
import { t } from '@/utils/i18n';
import { getRandomColor } from '@/utils/theme.ts';

const state = reactive({
  webBeianBg: getRandomColor(),
  domainBeianBg: getRandomColor()
});

const ver =__APP_FULL_VERSION__;

// ICP备案号
const handleICPBeian = () => {
  uni.setClipboardData({
    data: 'https://beian.miit.gov.cn/',
    success() {
      uni.showToast({ title: '链接已复制，请粘贴到浏览器打开', icon: 'none' })
    }
  })
}


// 网站备案号
const handleWebBeian = () => {
	uni.setClipboardData({
	  data: 'https://beian.mps.gov.cn/#/query/webSearch?code=13010802002515',
	  success() {
	    uni.showToast({ title: '链接已复制，请粘贴到浏览器打开', icon: 'none' })
	  }
	})
  
};
</script>

<style lang="scss" scoped>
.footer-container-warp {
  width: 100%;
  padding: 15px 0 ;
  box-shadow: 0 1px 20px 10px rgba(255, 255, 255, 0.394);
  background: linear-gradient(-45deg, #ee7752, #ce3e75, #23a6d5, #23d5ab);
  background-size: 400% 400%;
  animation: animation 10s ease infinite;

  .content-warp {
    height: 100%;
    margin: 0 auto;
    width: 90%;
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    align-items: center;
    animation-duration: 1s;
    animation-fill-mode: both;

    .display-warp {
      display: flex;
      justify-content: center;
      font-size: 14px;
      color: #fff;
      margin: 5px 0;
      text-align: center;
    }

    .beianhao-value {
      display: flex;
      align-items: center;
      border-radius: 5px;
      overflow: hidden;
      height: 22px;

      .num {
        height: 100%;
        padding: 4px 10px 4px 4px;
        display: inline-block;
      }
    }

    .web-beian-warp {
      display: flex;
      align-items: center;
      border-radius: 5px;
      overflow: hidden;
      height: 22px;

    }
  }

  .app-version {
    color: #fff;
  }
}

@keyframes animation {
  0% {
    background-position: 0% 50%;
  }

  50% {
    background-position: 100% 50%;
  }

  100% {
    background-position: 0% 50%;
  }
}
</style>