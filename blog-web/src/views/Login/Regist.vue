<template>
  <div class="container">
    <div class="header">
      <h1 class="form-title">注册</h1>
      <div class="to-login pointer" @click="emit('toLogin')">去登录</div>
    </div>
    <h1 class="form-title"></h1>
    <el-form :model="form" :rules="rules" ref="registForm" size="large" class="login-form">

      <el-form-item prop="email">
        <el-input class="form-input" v-model="form.email" placeholder="邮箱"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input class="form-input" v-model="form.password" type="password" show-password placeholder="密码"></el-input>
      </el-form-item>
      <el-form-item prop="repassword">
        <el-input class="form-input" v-model="form.repassword" type="password" show-password
          placeholder="确认密码"></el-input>
      </el-form-item>
      <el-form-item prop="verifyCode">
        <div class="myCenter form-input">
          <el-input v-model="form.verifyCode" placeholder="请输入验证码" class="input-flex"></el-input>
          <div @click="getMailVerifyCode()" :disabled="isCounting" class="button-vc input-flex myCenter pointer">
            {{ isCounting ? `${countdown}秒后重新发送` : '发送验证码' }}
          </div>
        </div>
      </el-form-item>
      <el-form-item>
        <el-button class="form-button-register" @click="handleRegister()">注册</el-button>
      </el-form-item>
    </el-form>
  </div>



</template>

<script lang="ts" setup>
import { getRegEmailVerifyCodeService, userRegisterService } from '@/api/login';
import { ElMessage, type FormInstance } from 'element-plus';
import { Md5 } from 'ts-md5';
import { reactive, ref } from 'vue';

const registForm = ref<FormInstance>()
const form = reactive({
  email: '',
  password: '',
  repassword: '',
  verifyCode: ''
})
const isCounting = ref(false)
const countdown = ref(0)

const emit = defineEmits(['toLogin'])

/* ---------- 校验规则 ---------- */
const rules = {
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { pattern: /^\S{6,32}$/, message: '6-32 位非空字符', trigger: 'blur' }
  ],
  repassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      validator: (_: any, v: string, cb: Function) =>
        v === form.password ? cb() : cb(new Error('两次密码不一致')),
      trigger: 'blur'
    }
  ],
  verifyCode: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}

/* ---------- 工具函数 ---------- */
const encrypt = (v: string) => Md5.hashStr(v).toString()
const startCountdown = () => {
  isCounting.value = true
  countdown.value = 120
  const t = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      clearInterval(t)
      isCounting.value = false
    }
  }, 1000)
}

/* 重置 & 动画 */
const resetForm = () => {
  Object.assign(form, { email: '', password: '', repassword: '', verifyCode: '' })
}

/* 发送邮箱验证码 */
const getMailVerifyCode = async () => {
  if (isCounting.value) return


  try {
    await registForm.value?.validateField('email')

  } catch {
    ElMessage.error('请完善表单后再提交')
    return
  }
  await getRegEmailVerifyCodeService({ email: form.email })


  ElMessage.success('验证码已发送')
  startCountdown()

}

/* 注册 */
const handleRegister = async () => {
  try {
    await registForm.value?.validate()
  } catch {
    ElMessage.error('请完善表单后再提交')
    return
  }


  const res = await userRegisterService({
    email: form.email,
    password: encrypt(form.password),
    verifyCode: form.verifyCode
  })
  ElMessage.success('注册成功')
  resetForm()
  emit('toLogin')

}



</script>


<style lang="scss" scoped>
.container {
  width: 300px;
  margin: 0 auto;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 3rem 0 1rem 0;
}

.myCenter {
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-form {
  width: 300px;
  margin: 0 auto;
}

.form-title {
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0rem 2rem 0 2rem;
}

.form-input {
  display: flex;
  justify-content: center;
  align-items: center;
}


.button-vc {
  width: 120px;
  height: 40px;
  padding: 0px;
  border-radius: 4px;
  background-color: var(--color-blue);
}

.input-flex {
  flex: 1;
  margin-right: 10px;
}

.form-button {
  margin: 0 auto;

  &:hover {
    color: var(--color-pink);
  }
}

.form-button-register {
  margin: 0 auto;
  height: 40px;
  width: 100px;
}

.to-login {
  display: none;
}

@media (max-width: 767px) {
  .to-login {
    display: block;
  }

}
</style>
