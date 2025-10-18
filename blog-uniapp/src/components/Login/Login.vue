<template>
	<view class="container">
		<!-- 顶部 -->
		<view class="header">
			<text class="form-title">登录</text>
			<text class="to-reg" @tap="toReg()">去注册</text>
		</view>


		<uni-forms ref="loginForm" :modelValue="form" :rules="rules" label-width="0" class="login-form">
			<!-- 邮箱 -->
			<uni-forms-item name="email" class="login-item">
				<uni-easyinput v-model="form.email" placeholder="邮箱" type="text" />
			</uni-forms-item>

			<!-- 密码 -->
			<uni-forms-item name="password" class="login-item">
				<uni-easyinput v-model="form.password" placeholder="密码" type="password" />
			</uni-forms-item>


			<!-- 验证码 -->
			<uni-forms-item name="verifyCode" class="login-item">
				<view class="code-row">
					<!-- 左侧输入框 -->
					<uni-easyinput v-model="form.verifyCode" placeholder="请输入验证码" class="flex-input" />
					<!-- 右侧验证码图 -->
					<view class="code-box" @click="getVerifyCode">
						<image :src="verifyCodeUrl" mode="aspectFit" class="code-img" />
					</view>
				</view>
			</uni-forms-item>
		</uni-forms>

		<button class="form-button-login" @tap="handleLogin()">登录</button>
		<view class="login-bottom">
			<view class="form-button" href="#" @tap="toCode()">
				验证码登录
			</view>
			<view class="forget" @tap="uni.switchTab({ url: '/pages/forget/index' })">忘记密码？</view>
		</view>


	</view>
</template>

<script setup lang="ts">
	import { onMounted, reactive, ref } from 'vue'
	import { Md5 } from 'ts-md5'
	import {
		getLoginEmailVerifyCodeService,
		getVerifyCodeService,
		userInfoService,
		userLoginService,
		userVerifyCodeLoginService
	} from '@/api/login'
	import { useUserStore } from '@/stores'

	/* ----------------- 基础变量 ----------------- */
	const userStore = useUserStore()
	const loginForm = ref<any>(null)

const emit = defineEmits(['change-mode'])

function toReg()  { emit('change-mode', 'regist') }
function toCode() { emit('change-mode', 'code') }

	const form = reactive({
		email: '',
		password: '',
		verifyCode: ''
	})

	/* ----------------- 图形验证码 ----------------- */
	const verifyCodeKey = ref('')
	const verifyCodeUrl = ref('')
	const getVerifyCode = async () => {
		const res = await getVerifyCodeService()
		verifyCodeKey.value = res.data.key
		verifyCodeUrl.value = res.data.image
	}

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
				{ pattern: '^\\S{6,32}$', errorMessage: '6-32 位非空字符' }
			]
		},
		verifyCode: {
			rules: [{ required: true, errorMessage: '请输入验证码' }]
		}
	}

	/* ----------------- 工具函数 ----------------- */
	const encrypt = (v : string) => Md5.hashStr(v).toString()

	/* ----------------- 登录 ----------------- */
	const handleLogin = async () => {
		try {
			await loginForm.value.validate()
		} catch {
			uni.showToast({ title: '请完善表单', icon: 'none' })
			return
		}

		try {

			const res = await userLoginService({
				email: form.email,
				password: encrypt(form.password),
				verifyCodeKey: verifyCodeKey.value,
				verifyCode: form.verifyCode
			})

			await loginSuccess(res)
			uni.showToast({ title: '登录成功', icon: 'success' })
			resetForm()
			redirectAfterLogin()
		} catch (e : any) {
			form.verifyCode = ''
			getVerifyCode()
		}
	}

	/* ----------------- 登录成功后续 ----------------- */
	const loginSuccess = async (token : { accessToken : string; refreshToken : string }) => {
		userStore.setTokens(token.accessToken, token.refreshToken)
		const userRes = await userInfoService()
		userStore.setUserState({ ...userRes })
	}

	/* ----------------- 跳转 ----------------- */
	const redirectAfterLogin = () => {
		const last = userStore.getLastShowWeb?.() // 根据你的 store 实现
		uni.navigateBack({ delta: 1 }) // 简单示例：返回上一页
		// 或者 uni.switchTab({ url: '/pages/index/index' })
	}

	/* ----------------- 重置 ----------------- */
	const resetForm = () => {
		form.email = ''
		form.password = ''
		form.verifyCode = ''
		getVerifyCode()
	}

	/* ----------------- 生命周期 ----------------- */
	onMounted(() => {
		if (userStore.getIsLogin()) {
			uni.switchTab({ url: '/pages/index/index' })
			return
		}
		getVerifyCode()
	})
</script>


<style lang="scss" scoped>
	$height: 80rpx;
	$radius: 8rpx;
	$border: 1rpx solid #dcdfe6;

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

	.to-reg {
		color: #007aff;
	}

	.login-form {
		width: 100%;
	}


	.login-item {
		height: $height;
	}

	.code-row {
		display: flex;
		align-items: center;
	}

	/* 左侧输入框自动填满 */
	.flex-input {
		flex: 1;
		margin-right: 16rpx;
	}

	/* 右侧图容器：固定宽 + 同高 + 同边框 + 同圆角 */
	.code-box {
		width: 220rpx;
		height: 68rpx;
		border: $border;
		border-radius: $radius;
		background: #fff;
		display: flex;
		align-items: center;
		justify-content: center;
		overflow: hidden;
	}

	.code-img {
		width: calc(100% - 4rpx);
		height: calc(100% - 4rpx);
	}

	.login-input {
		height: 100%;
		margin-right: 16rpx;
	}

	.code-btn {
		width: 220rpx;
		height: 37px;
		line-height: 68rpx;
		font-size: 28rpx;
		padding: 0;
	}

	.form-button-login {
		width: 100%;
		height: 88rpx;
		line-height: 88rpx;
		color: #fff;
		background-color: #007aff;
		border-radius: 8rpx;
		margin-top: 40rpx;
	}

	.login-bottom {
		display: flex;
		justify-content: space-between;
		padding-top: 20rpx;
		color: #007aff;
	}
</style>