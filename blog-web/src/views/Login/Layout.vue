<template>
  <!-- 登陆和注册 -->
  <div class="my-animation-hideToShow pc-container">
    <div class="in-up" id="loginAndRegist">
      <div class="form-container sign-up-container">
        <Regist v-on:to-login="switchToLogin" />
      </div>
      <div class="form-container sign-in-container">
        <Login v-on:to-regist="switchToRegister" />
      </div>
      <div class="overlay-container">
        <div class="overlay">
          <div class="overlay-panel myCenter overlay-left">
            <h1>已有帐号？</h1>
            <p>请登录</p>
            <button class="ghost" @click="switchToLogin()">登录</button>
          </div>
          <div class="overlay-panel myCenter overlay-right">
            <h1>没有帐号？</h1>
            <p>立即注册吧</p>
            <button class="ghost" @click="switchToRegister()">注册</button>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="m-container">
    <div class="m-reg" :class="{ 'm-reg-active': !isLoginPage }">
      <Regist v-on:to-login="switchToLogin" />
    </div>
    <div class="m-login">
      <Login v-on:to-regist="switchToRegister" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import Regist from './Regist.vue'
import Login from './Login.vue'

const isLoginPage = ref(true)


const switchToLogin = () => {
  document.querySelector('#loginAndRegist')?.classList.remove('right-panel-active')
  isLoginPage.value = true
}
const switchToRegister = () => {
  document.querySelector('#loginAndRegist')?.classList.add('right-panel-active')
  isLoginPage.value = false
}


</script>

<style lang="scss" scoped>
.myCenter {
  display: flex;
  justify-content: center;
  align-items: center;
}

.pc-container {
  height: 100vh;
  width: 100vw;
  display: flex;
  justify-content: center;
  align-items: center;
}

.in-up {
  opacity: 0.9;
  border-radius: 10px;
  box-shadow: 0 15px 30px var(--grey-9-a1), 0 10px 10px var(--grey-9-a1);
  position: relative;
  overflow: hidden;
  width: 750px;
  height: 450px;
  margin: 0 auto;
}


.form-container {
  position: absolute;
  height: 100%;
  width: 300px;
  margin: 0 auto;

  transition: all 0.5s ease-in-out;
  background: var(--grey-0);
  flex-direction: column;
}

.sign-in-container {
  left: 0;
  width: 50%;
}

.sign-up-container {
  left: 0;
  width: 50%;
  opacity: 0;
}

.input-flex {
  flex: 1;
  margin-right: 10px;
}


.in-up p {
  font-size: 14px;
  letter-spacing: 1px;
  margin: 20px 0 30px 0;
}

.in-up button {
  border-radius: 2rem;
  border: none;
  background: var(--lightRed);
  color: var(--grey-0);
  font-size: 16px;
  font-weight: bold;
  padding: 12px 45px;
  letter-spacing: 2px;
}

.in-up button:hover {
  animation: scale 0.8s ease-in-out;
}

.in-up button.ghost {
  background: transparent;
  border: 1px solid var(--grey-0);
}

.sign-up-container button {
  margin-top: 20px;
}

.overlay-container {
  position: absolute;
  left: 50%;
  width: 50%;
  height: 100%;
  overflow: hidden;
  transition: all 0.5s ease-in-out;
}

.overlay {
  background: var(--gradualRed);
  color: var(--grey-0);
  position: relative;
  left: -100%;
  height: 100%;
  width: 200%;
}

.overlay-panel {
  position: absolute;
  top: 0;
  flex-direction: column;
  height: 100%;
  width: 50%;
  transition: all 0.5s ease-in-out;
}

.overlay-right {
  right: 0;
  transform: translateY(0);
}

.overlay-left {
  transform: translateY(-20%);
}

.in-up.right-panel-active .sign-in-container {
  transform: translateY(100%);
}

.in-up.right-panel-active .overlay-container {
  transform: translateX(-100%);
}

.in-up.right-panel-active .sign-up-container {
  transform: translateX(100%);
  opacity: 1;
}

.in-up.right-panel-active .overlay {
  transform: translateX(50%);
}

.in-up.right-panel-active .overlay-left {
  transform: translateY(0);
}

.in-up.right-panel-active .overlay-right {
  transform: translateY(20%);
}

.my-animation-hideToShow {
  animation-name: hideToShow;
  visibility: visible;
  display: flex;
}

.m-container {
  width: 100%;
  position: relative;
  height: 100vh;
  overflow-x: hidden;
  display: none;
}


.m-login,
.m-reg {
  width: 100%;
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
}

.m-reg {
  position: absolute;
  transform: translateX(100%) translateY(-50%);
  transition: all 0.6s ease;

  background-color: #fff;
  z-index: 10;
}

.m-reg-active {
  transform: translateX(0) translateY(-50%);
}

/* 显示 */
@keyframes hideToShow {
  from {
    opacity: 0;
  }

  to {
    opacity: 1;
  }
}

:deep(.el-input__wrapper) {
  cursor: url('/src/assets/cursors/beam.cur'), not-allowed !important;
}

:deep(.el-input__inner) {
  cursor: url('/src/assets/cursors/beam.cur'), not-allowed !important;
}

:deep(.el-image__inner) {
  cursor: url('/src/assets/cursors/up.cur'), not-allowed !important;
}

:deep(.el-button) {
  cursor: url('/src/assets/cursors/up.cur'), not-allowed !important;
}

:deep(.el-button>span) {
  cursor: url('/src/assets/cursors/up.cur'), not-allowed !important;
}

@media (max-width: 767px) {
  .my-animation-hideToShow {
    display: none;
  }

  .m-container {
    display: block;
    overflow-y: hidden;
  }
}
</style>
