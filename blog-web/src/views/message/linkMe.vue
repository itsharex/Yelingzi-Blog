<template>
  <section class="link-me">
    <!-- 五张背景占位 -->
    <div class="bg-wrap">
      <div v-for="i in 5" :key="i" class="bg-item" :class="i % 2 === 1 ? 'slide-right' : 'slide-left'" :style="{
        backgroundImage: `url(${getBgImage(i)})`,
        animationDelay: `${(i - 1) * 0.2}s`
      }"></div>
    </div>


    <!-- 输入区 -->
    <section class="input-area">
      <div class="header">联系博主</div>
      <textarea v-model="content" class="input-textarea pen" placeholder="点击这里编辑文本内容..." />

      <ElUpload class="upload" v-model:file-list="fileList" list-type="picture-card" :auto-upload="true"
        :before-upload="beforeUpload" :http-request="httpRequest" :on-remove="onRemove" :on-preview="handlePreview">
        <SvgIcon name="icon-close1" class="uploader-icon" />
      </ElUpload>

      <footer class="input-footer">
        <ElInput class="footer-email" v-model="email" autosize placeholder="请输入正确的邮箱..." :width="260" />
        <YlButton class="footer-button" width="100px" info="发送" default-color="#d8d8d8" focus-color="#9cd0ed"
          :focus="isValidEmail(email)" @click="send">
        </YlButton>
      </footer>
    </section>

    <!-- 预览弹窗 -->
    <ElDialog v-model="showPreview" :width="dialogWidth">
      <ElCarousel :initial-index="activeIndex" :autoplay="false" :fullscreen="true">
        <ElCarouselItem v-for="img in imageList" :key="img">
          <ImageWithFallback class="carousel-image" :src="img" />
        </ElCarouselItem>
      </ElCarousel>
    </ElDialog>
  </section>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import YlButton from '@/components/Button/YlButton.vue'
import ImageWithFallback from '@/components/Image/ImageWithFallback.vue'
import { useImageUpload } from '@/utils/imageUpload'
import { isValidEmail } from '@/utils/common'
import { ElMessage } from 'element-plus'
import { addLinkMeService } from '@/api/message'
import Bg1 from '@/assets/images/about-me.jpg'
import Bg2 from '@/assets/images/bg-article.jpg'
import Bg3 from '@/assets/images/talks-bg.jpg'
import Bg4 from '@/assets/images/album-bg.jpg'
import Bg5 from '@/assets/images/friendLetterMiddle.jpg'

const getBgImage = (index: number) => {
  const images = [Bg1, Bg2, Bg3, Bg4, Bg5]
  return images[index - 1]
}

const dialogWidth = computed(() => {
  const isMobile = window.innerWidth <= 768
  return isMobile ? '90%' : '50%'
})

/* 文本 */
const content = ref('')
const email = ref('')

/* 图片上传逻辑 */
const { fileList, imageList, beforeUpload, httpRequest, onRemove } = useImageUpload()

/* 预览 */
const showPreview = ref(false)
const activeIndex = ref(0)
function handlePreview() {
  if (!imageList.value.length) return
  activeIndex.value = 0
  showPreview.value = true
}

async function send() {
  if (!isValidEmail(email.value)) {
    ElMessage.error('请输入正确的邮箱地址！')
    return
  }

  if (content.value.trim() === '') {
    ElMessage.error('请输入留言内容！')
    return
  }

  await addLinkMeService({
    content: content.value,
    email: email.value,
    images: imageList.value
  })

  ElMessage.success('发送成功！博主会尽快联系您～')
  clearMessage()

}

const clearMessage = () => {
  content.value = ''
  email.value = ''
  fileList.value = []
}

</script>

<style scoped lang="scss">
.link-me {
  height: 100vh;
  width: 100vw;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: relative;
  overflow: hidden;
}

.bg-wrap {
  display: flex;
  flex-direction: column;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
}

.bg-item {
  flex: 0 0 20%;
  background-position: center;
  background-size: cover;
  background-repeat: no-repeat;
  opacity: 0.5;

  /* 初始位置 - 奇数在右，偶数在左 */
  transform: translateX(var(--initial-x, 100%));
}

.bg-item:nth-child(even) {
  filter: brightness(0.95);
  --initial-x: -100%;
}

.bg-item:nth-child(odd) {
  --initial-x: 100%;
}

/* 从右向左滑入的动画类 */
.slide-right {
  animation: slideInFromRight 1s ease-out forwards;
}

/* 从左向右滑入的动画类 */
.slide-left {
  animation: slideInFromLeft 1s ease-out forwards;
}

/* 从右向左滑入的动画 */
@keyframes slideInFromRight {
  from {
    transform: translateX(100%);
    opacity: 0;
  }

  to {
    transform: translateX(0);
    opacity: 1;
  }
}

/* 从左向右滑入的动画 */
@keyframes slideInFromLeft {
  from {
    transform: translateX(-100%);
    opacity: 0;
  }

  to {
    transform: translateX(0);
    opacity: 1;
  }
}

.bg-item {
  transition: filter 0.3s ease;
}

.input-area {
  padding: 16px;
  background: #ffecec;
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: 700px;
  height: 500px;
  border-radius: 12px;
  gap: 12px;
  box-shadow: 0 8px 16px var(--grey-9-a1);
}

.header {
  font-size: 32px;
  font-weight: bold;
  color: var(--color-pink);
  border-bottom: 2px solid rgb(255, 207, 207);
  margin-bottom: 12px;
  display: flex;
  justify-content: center;
}

.input-textarea {
  width: 100%;
  box-sizing: border-box;
  height: 200px;
  white-space: pre-wrap;
  word-wrap: break-word;
  word-break: break-all;

  letter-spacing: 1px;
  font-family: inherit;
  font-size: 12px;
  resize: none;
  outline: none;
}

.input-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 12px;
}

.footer-email {
  width: 240px;
}

.upload {
  overflow-x: scroll;
  flex-direction: row-reverse;
}

:deep(.el-upload-list--picture-card) {
  flex-wrap: nowrap;
  flex-direction: row-reverse;
}

:deep(.el-upload--picture-card) {
  cursor: url('@/assets/cursors/up.cur'), pointer;
}

:deep(.el-upload-list__item-actions) {
  cursor: url('@/assets/cursors/arrow.cur'), default;

  span {
    cursor: url('@/assets/cursors/up.cur'), pointer;
  }
}

:deep(.el-upload-list__item) {
  margin: 0 0 8px 8px;
}

:deep(.el-carousel__container) {
  height: 550px;
}

:deep(.el-input__wrapper) {
  cursor: url('@/assets/cursors/beam.cur'), text;

  input {
    cursor: url('@/assets/cursors/beam.cur'), text;
  }
}

.uploader-icon {
  width: 24px;
  height: 24px;
  transform: rotate(45deg);
}

.carousel-image {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

@media (max-width: 767px) {
  .bg-item {
    animation: none !important;
    opacity: 1 !important;
    transform: none !important;
  }

  .link-me {
    justify-content: flex-start;
  }

  .input-area {
    width: 90%;
    flex: 1;
    margin-top: 100px;
  }

  .input-textarea {
    height: 150px;
  }

  .footer-email {
    width: 100%;
    height: 40px;
  }

  .input-footer {
    flex-direction: column;
    gap: 12px;
    align-items: stretch;
  }

  .footer-button {
    width: 100% !important;
    height: 60px;
  }

  :deep(.el-carousel__container) {
    height: 300px;
  }

}
</style>
