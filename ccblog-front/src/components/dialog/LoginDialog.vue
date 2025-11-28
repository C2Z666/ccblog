<template>
  <!-- 登录 Modal -->
  <el-dialog 
    v-model="loginModal" 
    title="" 
    width="420px" 
    :border="false"
    :close-on-click-modal="false"
    custom-class="login-dialog"
  >
    <div class="login-content">
      <div class="login-header">
        <h2 class="login-title">账号登录</h2>
        <p class="login-subtitle">登录后畅享更多权益</p>
      </div>
      
      <el-form
        ref="formRef"
        :model="dynamicValidateForm"
        label-position="left"
        label-width="0"
        class="login-form"
      >
        <!-- 用户名输入框 -->
        <el-form-item
          prop="username"
          :rules="[
            {
              required: true,
              message: '用户名不能为空',
              trigger: ['blur', 'change']
            }
          ]"
          class="login-form-item"
        >
          <el-input 
            v-model="dynamicValidateForm.username" 
            placeholder="请输入用户名"
            prefix-icon="el-icon-user"
            clearable
            class="login-input"
          />
        </el-form-item>
        
        <!-- 密码输入框 -->
        <el-form-item
          prop="password"
          :rules="[
            {
              required: true,
              message: '密码不能为空',
              trigger: ['blur', 'change']
            }
          ]"
          class="login-form-item"
        >
          <el-input 
            v-model="dynamicValidateForm.password" 
            type="password" 
            placeholder="请输入密码"
            prefix-icon="el-icon-lock"
            show-password
            class="login-input"
          />
        </el-form-item>
        
        <!-- 记住密码和忘记密码 -->
        <div class="login-options">
          <el-checkbox v-model="dynamicValidateForm.rememberMe" class="remember-me">记住我(敬请期待)</el-checkbox>
          <el-link type="primary" :underline="false" class="forgot-password">忘记密码?(敬请期待)</el-link>
        </div>
        
        <!-- 登录按钮 -->
        <el-form-item class="login-form-item login-btn-group">
          <el-button 
            type="primary" 
            @click="submitForm(formRef)"
            :loading="isLoading"
            class="login-button"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
      
      <!-- 其他登录方式 -->
      <div class="other-login-section">
        <div class="divider-text">
          <span>其他登录方式(敬请期待)</span>
        </div>
        <div class="oauth-box">
          <el-icon class="oauth-icon"><user /></el-icon>
          <el-icon class="oauth-icon"><chat-line-square /></el-icon>
          <el-icon class="oauth-icon"><share /></el-icon>
        </div>
      </div>
      
      <!-- 协议 -->
      <div class="agreement-box">
        <div class="agreement-content">
          <span class="agreement-text">登录即同意</span>
          <el-link type="primary" :underline="false" class="agreement-link">用户协议</el-link>
          <span class="agreement-text">和</span>
          <el-link type="primary" :underline="false" class="agreement-link">隐私政策</el-link>
        </div>
      </div>
    </div>
  </el-dialog>
</template>
<script setup lang="ts">

import { reactive, ref, watch, onMounted } from 'vue'
import type { FormInstance } from 'element-plus'
import { ElIcon } from 'element-plus'
import { User, ChatLineSquare, Share } from '@element-plus/icons-vue'
import { doGet, doPost } from '@/http/BackendRequests'
import type { CommonResponse } from '@/http/ResponseTypes/CommonResponseType'
import { BASE_URL, LOGIN_USER_NAME_URL } from '@/http/URL'
import { getCookie, messageTip, refreshPage, setAuthToken } from '@/util/utils'
import { MESSAGE_TYPE } from '@/constants/MessageTipEnumConstant'
import { COOKIE_DEVICE_ID } from '@/constants/CookieConstants'
import { useGlobalStore } from '@/stores/global'
import { useRouter } from 'vue-router'
import { startCacheRefresh } from '@/stores/useFollowIdCache'
const globalStore = useGlobalStore()

const props = defineProps<{
  clicked: boolean,
}>()

const emit = defineEmits<{
  (e: 'update:clicked', value: boolean): void
}>()

const loginModal = ref(false)
let init = false

// 监听父组件传入的clicked属性变化
watch(() => props.clicked, (newVal) => {
  loginModal.value = newVal
  // 如果是第一次打开，需要建立长连接
  if(!init && newVal){
    init = true
  }
})

// 监听内部loginModal状态变化，通知父组件
watch(loginModal, (newVal) => {
  emit('update:clicked', newVal)
})

// 组件挂载时检查是否有记住的用户名
onMounted(() => {
  const rememberedUsername = localStorage.getItem('rememberedUsername')
  if (rememberedUsername) {
    dynamicValidateForm.username = rememberedUsername
    dynamicValidateForm.rememberMe = true
  }
})

const formRef = ref<FormInstance>()

const dynamicValidateForm = reactive<{
  username: string,
  password: string,
  rememberMe: boolean
}>({
  username: '',
  password: '',
  rememberMe: false
})


const router = useRouter()
const isLoading = ref(false)

const submitForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate((valid) => {
    if (valid) {
      isLoading.value = true
      doPost<CommonResponse>(LOGIN_USER_NAME_URL, {
        username: dynamicValidateForm.username,
        password: dynamicValidateForm.password
      })
        .then((response) => {
          isLoading.value = false
          if(response.data.status.code === 0){
            messageTip("登录成功", MESSAGE_TYPE.SUCCESS)
            // 记住用户名密码
            if (dynamicValidateForm.rememberMe) {
              localStorage.setItem('rememberedUsername', dynamicValidateForm.username)
            } else {
              localStorage.removeItem('rememberedUsername')
            }
            setAuthToken(response.data.result.token)
            console.log(response.data)
            // 关闭登录对话框
            loginModal.value = false
            // 粉丝缓存轮询
            startCacheRefresh(Number(response.data.result.userId));
            // 登录成功后设置全局用户信息
            const completeGlobal = {
              ...response.data.global,
              isLogin: true,
              user: response.data.global?.user || {}
            }
            completeGlobal.user.userId = response.data.result.userId;
            completeGlobal.user.photo = response.data.result.photo;
            completeGlobal.user.userName = response.data.result.userName;
            globalStore.setGlobal(completeGlobal)
            router.push({ name: 'home' })
          } else {
            messageTip(response.data.status.msg || "登录失败，请重试", MESSAGE_TYPE.ERROR)
          }
        })
        .catch((error) => {
          isLoading.value = false
          console.error(error)
          messageTip("网络错误，请稍后重试", MESSAGE_TYPE.ERROR)
        })
      } else {
        messageTip("请按要求填写用户名密码", MESSAGE_TYPE.ERROR)
      }
  })
}

const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
}


</script>



<style scoped>
/* 登录对话框容器 */
.login-dialog {
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

/* 登录内容区域 */
.login-content {
  padding: 30px 0;
  width: 100%;
  display: flex;
  flex-direction: column;
}

/* 登录标题区域 */
.login-header {
  text-align: center;
  margin-bottom: 30px;
  animation: fadeIn 0.5s ease-in-out;
}

.login-title {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
}

.login-subtitle {
  font-size: 14px;
  color: #666;
  margin: 0;
}

/* 登录表单 */
.login-form {
  width: 100%;
  max-width: 360px;
  margin: 0 auto;
}

.login-form-item {
  margin-bottom: 24px;
  transition: all 0.3s ease;
}

/* 输入框样式 */
.login-input {
  height: 44px;
  border-radius: 8px;
  border-color: #e4e7ed;
  transition: all 0.3s ease;
  font-size: 14px;
}

.login-input:focus {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.login-input ::v-deep(.el-input__inner) {
  height: 44px;
  border-radius: 8px;
  font-size: 14px;
}

/* 登录选项 */
.login-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  position: relative;
  z-index: 1;
}

:deep(.el-checkbox) {
  font-size: 14px;
  color: #606266;
}

:deep(.el-checkbox:hover .el-checkbox__inner) {
  border-color: #409eff;
}

:deep(.el-checkbox__input.is-checked .el-checkbox__inner) {
  background-color: #409eff;
  border-color: #409eff;
}

.forgot-password {
  font-size: 14px;
  color: #409eff;
  font-weight: 500;
  position: relative;
  overflow: hidden;
}

.forgot-password::after {
  content: '';
  position: absolute;
  width: 0;
  height: 2px;
  bottom: 0;
  left: 0;
  background-color: #409eff;
  transition: width 0.3s ease;
}

.forgot-password:hover::after {
  width: 100%;
}

.forgot-password:hover {
  color: #1890ff;
}

/* 登录按钮组 */
.login-btn-group {
  margin-bottom: 32px;
  position: relative;
  z-index: 1;
}

.login-button {
  width: 100%;
  height: 50px;
  border-radius: 12px;
  font-size: 17px;
  font-weight: 600;
  border: none;
  transition: all 0.4s ease;
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #409eff, #1890ff);
  color: white;
  box-shadow: 0 4px 20px rgba(64, 158, 255, 0.3);
}

/* 按钮渐变动画 */
.login-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.6s ease;
}

.login-button:hover::before {
  left: 100%;
}

.login-button:hover {
  background: linear-gradient(135deg, #1890ff, #337ecc);
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(64, 158, 255, 0.4);
}

.login-button:active {
  transform: translateY(-1px);
  box-shadow: 0 5px 15px rgba(64, 158, 255, 0.3);
}

/* 加载状态样式 */
:deep(.el-button--primary.is-loading) {
  background: linear-gradient(135deg, #a0cfff, #66b1ff);
  cursor: not-allowed;
  box-shadow: 0 4px 20px rgba(102, 177, 255, 0.3);
}

:deep(.el-button--primary.is-loading:hover) {
  background: linear-gradient(135deg, #a0cfff, #66b1ff);
  transform: none;
  box-shadow: 0 4px 20px rgba(102, 177, 255, 0.3);
}

/* 其他登录方式 */
.other-login-section {
  width: 100%;
  max-width: 380px;
  margin: 0 auto 25px;
  position: relative;
  z-index: 1;
}

.divider-text {
  position: relative;
  text-align: center;
  margin-bottom: 28px;
}

.divider-text::before,
.divider-text::after {
  content: '';
  position: absolute;
  top: 50%;
  width: 38%;
  height: 1px;
  background: linear-gradient(to right, transparent, #e4e7ed, transparent);
}

.divider-text::before {
  left: 0;
}

.divider-text::after {
  right: 0;
}

.divider-text span {
  padding: 0 15px;
  font-size: 14px;
  color: #909399;
  background-color: #fff;
  position: relative;
  z-index: 1;
}

.oauth-box {
  display: flex;
  justify-content: center;
  gap: 40px;
}

.oauth-icon {
  font-size: 36px;
  color: #606266;
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 12px;
  border-radius: 50%;
  background: linear-gradient(135deg, #f5f7fa, #e4e7ed);
  box-shadow: 5px 5px 10px #d1d9e6, -5px -5px 10px #ffffff;
}

.oauth-icon:hover {
  transform: scale(1.1);
  box-shadow: 8px 8px 16px #d1d9e6, -8px -8px 16px #ffffff;
  color: #409eff;
}

/* 悬停缩放效果 */
.hover-scale {
  transition: transform 0.3s ease;
}

.hover-scale:hover {
  transform: scale(1.1);
}

/* 协议区域 */
.login-agreement {
  margin-top: 16px;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  width: 100%;
  margin-left: auto;
  margin-right: auto;
}

.agreement-content {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 6px;
  flex-wrap: wrap;
}

.agreement-text {
  font-size: 14px;
  color: #606266;
  line-height: 1.5;
}

.agreement-link {
  font-size: 14px;
  color: #409eff !important;
  font-weight: 500;
  position: relative;
  padding: 0 2px;
  transition: all 0.3s ease;
}

.agreement-link::after {
  content: '';
  position: absolute;
  width: 0;
  height: 1px;
  bottom: -1px;
  left: 0;
  background-color: #409eff;
  transition: width 0.3s ease;
}

.agreement-link:hover {
  color: #66b1ff !important;
  transform: translateY(-1px);
}

.agreement-link:hover::after {
  width: 100%;
}

/* 动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 浮动动画 */
@keyframes float {
  0%, 100% {
    transform: translate(0, 0);
  }
  50% {
    transform: translate(10px, 10px);
  }
}

/* 脉冲动画 */
@keyframes pulse {
  0%, 100% {
    box-shadow: 0 8px 32px rgba(64, 158, 255, 0.3);
  }
  50% {
    box-shadow: 0 8px 32px rgba(64, 158, 255, 0.5);
  }
}

.pulse-animation {
  animation: pulse 3s ease-in-out infinite;
}

/* 表单元素进入动画 */
.login-form-item {
  animation: slideIn 0.5s ease-out;
  opacity: 0;
  animation-fill-mode: forwards;
}

.login-form-item:nth-child(1) { animation-delay: 0.1s; }
.login-form-item:nth-child(2) { animation-delay: 0.2s; }
.login-form-item:nth-child(3) { animation-delay: 0.3s; }
.login-form-item:nth-child(4) { animation-delay: 0.4s; }

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 响应式设计 */
@media (max-width: 480px) {
  .login-dialog {
    width: 95% !important;
    margin: 0 auto;
    border-radius: 16px;
  }
  
  .login-content {
    padding: 30px 20px;
  }
  
  .app-logo {
    width: 70px;
    height: 70px;
  }
  
  .login-title {
    font-size: 24px;
  }
  
  .login-form,
  .other-login-section {
    max-width: 100%;
  }
  
  .oauth-box {
    gap: 30px;
  }
  
  .oauth-icon {
    font-size: 30px;
    padding: 10px;
  }
  
  .login-input {
    height: 46px;
  }
  
  .login-input .el-input__inner {
    height: 46px;
  }
  
  .login-button {
    height: 46px;
  }
  
  .agreement-content {
    flex-direction: column;
    gap: 2px;
  }
}

/* 全局样式覆盖 */
:deep(.el-dialog__headerbtn) {
  top: 20px;
  right: 20px;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background-color: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

:deep(.el-dialog__headerbtn:hover) {
  background-color: #e4e7ed;
  transform: scale(1.1);
}

:deep(.el-dialog__headerbtn .el-dialog__close) {
  font-size: 18px;
  color: #909399;
  transition: color 0.3s;
  line-height: 1;
}

:deep(.el-dialog__headerbtn .el-dialog__close:hover) {
  color: #606266;
}

:deep(.el-checkbox__label) {
  font-size: 14px;
}

:deep(.el-input__prefix-inner) {
  padding-right: 8px;
}

/* 平滑过渡 */
* {
  transition-property: background-color, border-color, color, fill, stroke, opacity, box-shadow, transform;
}

/* 禁用文本选择 */
.app-logo,
.oauth-icon,
.login-button {
  user-select: none;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
}
</style>
