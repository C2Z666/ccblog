<template>
  <!-- 注册 Modal -->
  <el-dialog 
    v-model="registerModal" 
    title="" 
    width="420px" 
    :border="false"
    :close-on-click-modal="false"
    custom-class="register-dialog"
  >
    <div class="register-content">
      <div class="register-header">
        <h2 class="register-title">账号注册</h2>
        <p class="register-subtitle">注册后畅享更多权益</p>
      </div>
      
      <el-form
        ref="formRef"
        :model="form"
        label-position="left"
        label-width="0"
        class="register-form"
      >
        <!-- 用户名输入框 -->
        <el-form-item
          prop="userName"
          :rules="[
            { 
              required: true, 
              message: '用户名不能为空', 
              trigger: ['blur', 'change'] 
            }
          ]"
          class="register-form-item"
        >
          <el-input 
            v-model="form.userName" 
            placeholder="请输入用户名"
            prefix-icon="el-icon-user"
            clearable
            class="register-input"
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
          class="register-form-item"
        >
          <el-input 
            v-model="form.password" 
            type="password" 
            placeholder="请输入密码"
            prefix-icon="el-icon-lock"
            show-password
            class="register-input"
          />
        </el-form-item>
        
        <!-- 确认密码输入框 -->
        <el-form-item
          prop="confirm"
          :rules="[
            { required: true, message: '请再次输入密码', trigger: 'blur' },
            { validator: validatePass, trigger: 'blur' }
          ]"
          class="register-form-item"
        >
          <el-input
            v-model="form.confirm"
            type="password"
            placeholder="请再次输入密码"
            prefix-icon="el-icon-lock"
            show-password
            class="register-input"
          />
        </el-form-item>
        
        <!-- 按钮组 -->
        <div class="register-btn-group">
          <el-button 
            type="primary" 
            @click="submitForm(formRef)"
            :loading="isLoading"
            class="register-button"
          >
            注册
          </el-button>
          <el-button 
            @click="resetForm(formRef)"
            class="reset-button"
          >
            清空
          </el-button>
        </div>
      </el-form>
      
      <!-- 协议区域 -->
      <div class="agreement-box">
        <div class="agreement-content">
          <span class="agreement-text">注册即同意</span>
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
import { doPost } from '@/http/BackendRequests'
import type { CommonResponse } from '@/http/ResponseTypes/CommonResponseType'
import { getCookie, messageTip, refreshPage, setAuthToken } from '@/util/utils'
import { REGISTER_USER_NAME_URL } from '@/http/URL'
import { MESSAGE_TYPE } from '@/constants/MessageTipEnumConstant'
import { useGlobalStore } from '@/stores/global' // 全局变量需要的
import { useFollowIdCache, startCacheRefresh } from '@/stores/useFollowIdCache'
import { useRouter } from 'vue-router'
const globalStore = useGlobalStore()

/* props & emit */
const props = defineProps<{ clicked: boolean }>()
const emit = defineEmits<{
  close: []
  toLogin: []   // 注册成功后通知父组件打开登录弹窗
}>()

/* 弹窗显隐 */
const registerModal = ref(false)
const isLoading = ref(false)
let init = false

watch(() => props.clicked, () => {
  registerModal.value = true
  // 如果是第一次打开
  if(!init){
    init = true
  }
})

/* 表单 */
const formRef = ref<FormInstance>()
const form = reactive({
  userName: '',
  password: '',
  confirm: ''
})

/* 确认密码校验 */
const validatePass = (rule: any, value: string, callback: any) => {
  if (value !== form.password) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

/* 提交注册 */
const router = useRouter()
const submitForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate((valid) => {
    if (valid) {
      isLoading.value = true
      doPost<CommonResponse>(REGISTER_USER_NAME_URL, {
        userName: form.userName,
        password: form.password
      })
        .then((response) => {
          isLoading.value = false
          if(response.data.status.code === 0){
            messageTip("注册成功", MESSAGE_TYPE.SUCCESS)
            setAuthToken(response.data.result.token)
            // 关闭注册对话框
            registerModal.value = false
            // 粉丝缓存轮询
            startCacheRefresh(Number(response.data.result.userId));
            // 设置全局用户信息
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
            messageTip(response.data.status.msg || "注册失败，请重试", MESSAGE_TYPE.ERROR)
          }
        })
        .catch((error) => {
          isLoading.value = false
          console.error(error)
          messageTip("网络错误，请稍后重试", MESSAGE_TYPE.ERROR)
        })
    } else {
      messageTip("请按要求填写表单", MESSAGE_TYPE.ERROR)
    }
  })
}

/* 清空 */
const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
}
</script>

<style scoped>
/* 注册对话框容器 */
.register-dialog {
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

/* 注册内容区域 */
.register-content {
  padding: 30px 0;
  width: 100%;
  display: flex;
  flex-direction: column;
}

/* 注册标题区域 */
.register-header {
  text-align: center;
  margin-bottom: 30px;
  animation: fadeIn 0.5s ease-in-out;
}

.register-title {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
}

.register-subtitle {
  font-size: 14px;
  color: #666;
  margin: 0;
}

/* 注册表单 */
.register-form {
  width: 100%;
  max-width: 360px;
  margin: 0 auto;
}

.register-form-item {
  margin-bottom: 24px;
  transition: all 0.3s ease;
}

/* 输入框样式 */
.register-input {
  height: 44px;
  border-radius: 8px;
  border-color: #e4e7ed;
  transition: all 0.3s ease;
  font-size: 14px;
}

.register-input:focus {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.register-input ::v-deep(.el-input__inner) {
  height: 44px;
  border-radius: 8px;
  font-size: 14px;
}

/* 注册按钮组 */
.register-btn-group {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
  position: relative;
  z-index: 1;
}

.register-button {
  flex: 1;
  height: 50px;
  border-radius: 12px;
  font-size: 17px;
  font-weight: 600;
  border: none;
  transition: all 0.4s ease;
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #67c23a, #409eff);
  color: white;
  box-shadow: 0 4px 20px rgba(103, 194, 58, 0.3);
}

/* 按钮渐变动画 */
.register-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.6s ease;
}

.register-button:hover::before {
  left: 100%;
}

.register-button:hover {
  background: linear-gradient(135deg, #409eff, #67c23a);
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(103, 194, 58, 0.4);
}

.register-button:active {
  transform: translateY(-1px);
  box-shadow: 0 5px 15px rgba(103, 194, 58, 0.3);
}

/* 重置按钮 */
.reset-button {
  flex: 1;
  height: 50px;
  border-radius: 12px;
  font-size: 17px;
  font-weight: 500;
  border: 1px solid #dcdfe6;
  transition: all 0.3s ease;
  background: #fff;
  color: #606266;
}

.reset-button:hover {
  border-color: #409eff;
  color: #409eff;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);
}

.reset-button:active {
  transform: translateY(0);
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

/* 协议区域 */
.agreement-box {
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

/* 表单元素进入动画 */
.register-form-item {
  animation: slideIn 0.5s ease-out;
  opacity: 0;
  animation-fill-mode: forwards;
}

.register-form-item:nth-child(1) { animation-delay: 0.1s; }
.register-form-item:nth-child(2) { animation-delay: 0.2s; }
.register-form-item:nth-child(3) { animation-delay: 0.3s; }

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
  .register-dialog {
    width: 95% !important;
    margin: 0 auto;
    border-radius: 16px;
  }
  
  .register-content {
    padding: 30px 20px;
  }
  
  .register-title {
    font-size: 24px;
  }
  
  .register-form {
    max-width: 100%;
  }
  
  .register-input {
    height: 46px;
  }
  
  .register-input .el-input__inner {
    height: 46px;
  }
  
  .register-button,
  .reset-button {
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

:deep(.el-input__prefix-inner) {
  padding-right: 8px;
}

/* 平滑过渡 */
* {
  transition-property: background-color, border-color, color, fill, stroke, opacity, box-shadow, transform;
}

/* 禁用文本选择 */
.register-button,
.reset-button {
  user-select: none;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
}
</style>