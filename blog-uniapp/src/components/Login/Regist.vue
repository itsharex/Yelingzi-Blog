<template>
  <view class="container">
    <!-- 顶部 -->
    <view class="header">
      <text class="form-title">注册</text>
      <text class="to-login" @tap="toLogin">去登录</text>
    </view>

    <!-- 表单 -->
    <uni-forms
      ref="registForm"
      :modelValue="form"
      :rules="rules"
      label-width="0"
      class="login-form"
    >
      <!-- 邮箱 -->
      <uni-forms-item name="email">
        <uni-easyinput
          v-model="form.email"
          placeholder="邮箱"
          type="text"
        />
      </uni-forms-item>

      <!-- 密码 -->
      <uni-forms-item name="password">
        <uni-easyinput
          v-model="form.password"
          placeholder="密码"
          type="password"
        />
      </uni-forms-item>

      <!-- 确认密码 -->
      <uni-forms-item name="repassword">
        <uni-easyinput
          v-model="form.repassword"
          placeholder="确认密码"
          type="password"
        />
      </uni-forms-item>

      <!-- 验证码 -->
      <uni-forms-item name="verifyCode">
        <view class="code-box">
          <uni-easyinput
            v-model="form.verifyCode"
            placeholder="请输入验证码"
            class="code-input"
          />
          <button
            class="code-btn"
            :disabled="isCounting"
            @tap="getMailVerifyCode"
          >
            {{ isCounting ? `${countdown}s` : '发送验证码' }}
          </button>
        </view>
      </uni-forms-item>

      <!-- 注册按钮 -->
      <button class="form-button-register" @tap="handleRegister">
        注册
      </button>
    </uni-forms>
  </view>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { Md5 } from 'ts-md5'
import { getRegEmailVerifyCodeService, userRegisterService } from '@/api/login'

/* ----------------- emit ----------------- */
const emit = defineEmits(['change-mode'])

/* ----------------- 表单 ----------------- */
const registForm = ref<any>(null)
const form = reactive({
  email: '',
  password: '',
  repassword: '',
  verifyCode: ''
})

/* ----------------- 验证码倒计时 ----------------- */
const isCounting = ref(false)
const countdown = ref(0)

/* ----------------- 校验规则 ----------------- */
const rules = {
  email: {
    rules: [
      { required: true, errorMessage: '请输入邮箱' },
      { format: 'email', errorMessage: '邮箱格式不正确' }
    ]
  },
  password: {
    rules: [
      { required: true, errorMessage: '请输入密码' },
      { pattern: /^\\S{6,32}$/, errorMessage: '6-32 位非空字符' }
    ]
  },
  repassword: {
    rules: [
      { required: true, errorMessage: '请再次输入密码' },
      {
        validator: (_rule: any, value: string) =>
          value === form.password ? Promise.resolve() : Promise.reject('两次密码不一致')
      }
    ]
  },
  verifyCode: {
    rules: [{ required: true, errorMessage: '请输入验证码' }]
  }
}

/* ----------------- 工具函数 ----------------- */
const encrypt = (v: string) => Md5.hashStr(v).toString()

const startCountdown = () => {
  isCounting.value = true
  countdown.value = 120
  const timer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      clearInterval(timer)
      isCounting.value = false
    }
  }, 1000)
}

const resetForm = () => {
  Object.assign(form, {
    email: '',
    password: '',
    repassword: '',
    verifyCode: ''
  })
}

/* ----------------- 发送验证码 ----------------- */
const getMailVerifyCode = async () => {
  if (isCounting.value) return
  try {
    await registForm.value.validateField(['email'])
  } catch {
    uni.showToast({ title: '请完善邮箱', icon: 'none' })
    return
  }

  await getRegEmailVerifyCodeService({ email: form.email })
  uni.showToast({ title: '验证码已发送', icon: 'success' })
  startCountdown()
}

/* ----------------- 注册 ----------------- */
const handleRegister = async () => {
  try {
    await registForm.value.validate()
  } catch {
    uni.showToast({ title: '请完善表单', icon: 'none' })
    return
  }

  await userRegisterService({
    email: form.email,
    password: encrypt(form.password),
    verifyCode: form.verifyCode
  })

  uni.showToast({ title: '注册成功', icon: 'success' })
  resetForm()
  toLogin() 
}
function toLogin()  { emit('change-mode', 'login') }
</script>


<style lang="scss" scoped>
.container {
  padding: 60rpx 40rpx;
  width: 100%;
  box-sizing: border-box;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 40rpx;
}

.form-title {
  font-size: 48rpx;
  font-weight: bold;
}

.to-login {
  color: #007aff;
}

.login-form {
  width: 100%;
}

.code-box {
  display: flex;
  align-items: center;
}

.code-input {
  flex: 1;
  margin-right: 16rpx;
}

.code-btn {
  width: 220rpx;
  height: 80rpx;
  line-height: 80rpx;
  font-size: 28rpx;
  padding: 0;
}

.form-button-register {
  width: 100%;
  height: 88rpx;
  line-height: 88rpx;
  color: #fff;
  background-color: #007aff;
  border-radius: 8rpx;
  margin-top: 40rpx;
}


</style>
