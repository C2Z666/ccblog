<template>
  <div class="user-profile-container">
    <div class="profile-header" :style="{ backgroundImage: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' }">
      <div class="profile-card">
        <div class="avatar-section">
          <div class="avatar-container">
            <img :src="vo.photo || 'https://via.placeholder.com/120'" class="user-avatar" alt="用户头像" />
            <div v-if="global.isLogin && global.user.userId == vo.userId" class="avatar-edit-overlay" @click="uploadAvatar">
              <el-icon class="edit-icon"><edit /></el-icon>
              <span class="edit-text">更换头像</span>
            </div>
          </div>
          
          <div class="user-info">
            <div class="user-name-section">
              <h1 class="user-name">{{vo.userName}}</h1>
              <!-- 聊天按钮 - 根据登录状态显示不同内容 -->
              <div class="user-actions">
                <template v-if="global.isLogin && global.user.userId != vo.userId">
                  <el-button type="primary" @click="startChat" class="chat-button">
                    <el-icon><chat-dot-round /></el-icon>
                    发消息
                  </el-button>
                </template>
                <template v-else-if="!global.isLogin">
                  <el-button type="primary" @click="changeClicked" class="chat-button">
                    <el-icon><chat-dot-round /></el-icon>
                    登录后发消息
                  </el-button>
                </template>
              </div>
            </div>
            
            <!-- 用户标签 -->
            <div class="user-tags">
              <el-tag v-if="vo.company" type="info" class="custom-tag">
                <el-icon class="tag-icon"><Avatar /></el-icon>
                {{vo.company}}
              </el-tag>
              <el-tag v-if="vo.position" type="success" class="custom-tag">
                <el-icon class="tag-icon"><Position /></el-icon>
                {{vo.position}}
              </el-tag>
              <el-tag v-if="vo.region" type="warning" class="custom-tag">
                <el-icon class="tag-icon"><Location /></el-icon>
                {{vo.region}}
              </el-tag>
            </div>
            
            <!-- 用户简介 -->
            <div class="user-profile" v-if="global.isLogin && global.user.userId == vo.userId">
              <div class="profile-content" @click="openEditDialog">
                <p class="profile-text">{{vo.profile || '点击添加简介，让大家认识你吧'}}</p>
                <el-icon class="profile-edit-icon"><edit /></el-icon>
              </div>
              <div class="profile-progress">
                <span>个人资料完善度：</span>
                <el-progress :percentage="vo.infoPercent" :stroke-width="6" :show-text="false" class="progress-bar" />
                <span class="progress-text">{{vo.infoPercent}}%</span>
                <el-button type="text" @click="openEditDialog" class="complete-profile-btn">完善资料</el-button>
              </div>
            </div>
            
            <!-- 访客看到的简介 -->
            <div class="user-profile visitor" v-else-if="vo.profile">
              <p class="profile-text">{{vo.profile}}</p>
            </div>
          </div>
        </div>
        
        <!-- 统计数据 -->
        <div class="stats-container">
          <div class="stat-item">
            <div class="stat-value">{{vo.joinDayCount}}</div>
            <div class="stat-label">加入天数</div>
          </div>
          <div class="stat-divider"></div>
          <div class="stat-item">
            <div class="stat-value">{{vo.followCount}}</div>
            <div class="stat-label">关注数</div>
          </div>
          <div class="stat-divider"></div>
          <div class="stat-item">
            <div class="stat-value">{{vo.fansCount}}</div>
            <div class="stat-label">粉丝数</div>
          </div>
        </div>
      </div>
      <!-- <Footer /> -->
    </div>
    <!-- 这个就是点击换头像的注册点 -->
    <input type="file" ref="fileInput" @change="handleFileUpload" style="display: none;" accept="image/*">
    <LoginDialog v-model:clicked="loginDialogClicked"></LoginDialog>
  </div>


  <el-dialog
    :model-value="editInfoDialogVisible"
    @close="editInfoDialogVisible = false"
    width="600px"
    class="custom-dialog"
    destroy-on-close
  >
    <template #header>
      <div class="dialog-header-enhanced">
        <div class="title-container">
          <div class="title-blue-bg">
            <h2 class="dialog-title">编辑个人资料</h2>
          </div>
          <p class="dialog-subtitle">快来完善你的个人信息吧</p>
        </div>
      </div>
    </template>

    <template #default>
      <div class="edit-profile-container-enhanced">
        <div class="profile-form-content">
          <el-form
            class="form-container-enhanced"
            ref="userInfoFormRef"
            :model="userInfoForm"
            :size="userInfoFormSize"
            label-width="100px"
            status-icon
            :rules="userInfoFormRules"
          >
            <el-form-item 
              prop="userName" 
              label="用户名"
              class="form-item-enhanced"
            >
              <el-input
                v-model="userInfoForm.userName"
                maxlength="15"
                show-word-limit
                placeholder="请输入用户名（3-15个字符）"
                class="input-enhanced"
                :validate-event="false"
              >
                <template #prefix>
                  <el-icon class="input-icon"><User /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item 
              prop="company" 
              label="公司"
              class="form-item-enhanced"
            >
              <el-input
                v-model="userInfoForm.company"
                maxlength="15"
                show-word-limit
                placeholder="请输入公司名称"
                class="input-enhanced"
                :validate-event="false"
              >
                <template #prefix>
                  <el-icon class="input-icon"><OfficeBuilding /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item 
              prop="position" 
              label="职位"
              class="form-item-enhanced"
            >
              <el-input
                v-model="userInfoForm.position"
                maxlength="10"
                show-word-limit
                placeholder="请输入职位信息"
                class="input-enhanced"
                :validate-event="false"
              >
                <template #prefix>
                  <el-icon class="input-icon"><Position /></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item 
              prop="description" 
              label="简介"
              class="form-item-enhanced"
            >
              <el-input
                v-model="userInfoForm.description"
                maxlength="20"
                show-word-limit
                placeholder="请输入个人简介"
                class="input-enhanced"
                :validate-event="false"
              >
                <template #prefix>
                  <el-icon class="input-icon"><Document /></el-icon>
                </template>
              </el-input>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </template>
    <template #footer>
      <div class="dialog-footer-enhanced centered">
        <el-button 
          @click="editInfoDialogVisible = false" 
          class="cancel-btn-enhanced"
        >
          取消
        </el-button>
        <el-button 
          type="primary" 
          @click="saveUserInfo" 
          :disabled="isSaveDisabled" 
          class="save-btn-enhanced"
          :loading="isSaveDisabled"
        >
          {{ isSaveDisabled ? '保存中...' : '保存' }}
        </el-button>
      </div>
    </template>
  </el-dialog>

</template>

<script setup lang="ts">
import LoginDialog from '@/components/dialog/LoginDialog.vue'
import { useGlobalStore } from '@/stores/global'
import { Delete, Edit, Plus, ZoomIn, ChatDotRound, Avatar, Position, Location, UploadFilled, User, OfficeBuilding, Document } from '@element-plus/icons-vue'
import { provide, reactive, ref, watch } from 'vue'
import type {
  ComponentSize,
  FormInstance,
  FormRules,
  UploadUserFile
} from 'element-plus'
import { doFilePost, doPost } from '@/http/BackendRequests'
import type { CommonResponse } from '@/http/ResponseTypes/CommonResponseType'
import { FILE_UPLOAD_URL, USER_INFO_SAVE_URL } from '@/http/URL'
import { messageTip } from '@/util/utils'
import { useRoute, useRouter } from 'vue-router'
import type { UserBaseInfo } from '@/http/ResponseTypes/UserInfoType/UserStatisticInfoType'
const globalStore = useGlobalStore()
const global = globalStore.global
const router = useRouter()
const route = useRoute()

const props = defineProps<{
  // vo: UserHomeInfoResponseType
  vo: UserBaseInfo // 这个原来是UserHomeInfoResponseType,但是后端已经改成直接传数据过来了,所以不需要用这个里面的userHome来过渡
}>()

const userId = route.params.userId


// 修改用户信息
const editInfoDialogVisible = ref(false)

interface UserInfoForm {
  userName: string
  company: string
  position: string
  description: string
  photo: string
}
const userInfoFormSize = ref<ComponentSize>('default')
const userInfoFormRef = ref<FormInstance>()
const userInfoForm = reactive<UserInfoForm>({
  userName: '',
  company: '',
  position: '',
  description: '',
  photo: ''
})

const userInfoFormRules = reactive<FormRules<UserInfoForm>>({
  userName: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { min: 3, max: 15, message: '昵称长度位于3-15之间', trigger: 'blur' },
  ],
  company: [
    { required: true, message: '请输入公司', trigger: 'blur' },
    { min: 2, max: 15, message: '公司长度位于2-15之间', trigger: 'blur' },

  ],
  position: [
    { required: true, message: '请输入自身职位', trigger: 'blur' },
    { min: 3, max: 10, message: '职位长度位于3-10之间', trigger: 'blur' },

  ],
  description: [
    { required: true, message: '请输入自我简介', trigger: 'blur' },
    { min: 2, max: 20, message: '自我介绍长度位于4-20之间', trigger: 'blur' },

  ]
})
// 上传头像
// cover 用来保存返回的图片地址
const cover = ref('')
const fileList = ref<UploadUserFile[]>([])

// 从前端数据填充表单
const fillFormWithFrontendData = () => {
  if (props.vo) {
    fileList.value = [{ url: props.vo.photo, name: '头像' }]
    if (props.vo.photo != null) {
      cover.value = props.vo.photo
    }
    userInfoForm.userName = props.vo.userName || ''
    userInfoForm.company = props.vo.company || ''
    userInfoForm.position = props.vo.position || ''
    userInfoForm.description = props.vo.profile || ''
  }
}

// 打开编辑对话框
const openEditDialog = () => {
  fillFormWithFrontendData()
  editInfoDialogVisible.value = true
}

// 监听props变化，更新表单数据
watch(() => props.vo, () => {
  fillFormWithFrontendData()
}, {
  immediate: true
})

// 获取隐藏的input元素
const fileInput = ref<HTMLInputElement | null>(null);

// 更新头像函数
const updateAvatar = async (avatarUrl: string) => {
  isSaveDisabled.value = true
  try {
    const response = await doPost<CommonResponse>(USER_INFO_SAVE_URL, {
      userId: userId,
      photo: avatarUrl // 只更新头像字段
    })
    // console.log(response)
    if(response.data.result.status.code==0){
      messageTip('头像更新成功', 'success')
    }
    else{
      messageTip(response.data.result.status.msg, 'error')
    }
    // 实时更新页面上显示的头像
    if (props.vo) {
      // 由于props是只读的，使用一个临时的新对象来更新头像
      // 这样可以立即在页面上显示新头像，而不需要刷新页面
      Object.assign(props.vo, { photo: avatarUrl })
    }
  } catch (error) {
    console.error('更新头像失败:', error)
    messageTip('更新头像失败', 'error')
  } finally {
    isSaveDisabled.value = false
  }
}

const onUploadFile = (file: File) => {
  const formData = new FormData();
  // @ts-ignore
  formData.append('file', file);
  // console.log("file:",file)

  doFilePost<CommonResponse>(FILE_UPLOAD_URL, formData)
    .then((response) => {
      // console.log(response)
      const avatarUrl = response.data.result
      cover.value = avatarUrl
      if(response.data.result.status.code==0){
        messageTip('上传成功', 'success')
        // 上传成功后调用独立的更新头像函数
        updateAvatar(avatarUrl)
      }
      else{
        messageTip(response.data.result.status.msg, 'error')
      }
    }).catch((error) => {
    // console.error(error)
    messageTip('上传失败' , 'error')
  })
}

const uploadAvatar = () => {
  fileInput.value?.click()
}

const handleFileUpload = (event: Event) => {
  const target = event.target as HTMLInputElement;
  const file = target.files ? target.files[0] : null;
  if (file) {
    // console.log('File selected:', file.name);
    // console.log('File selected:', file);
    onUploadFile(file)
    // 处理文件上传
  }
};

// 开始聊天
const startChat = () => {
  // console.log('跳转到与用户', props.vo.userId, '的聊天界面');
  // 跳转到聊天会话页面，并传递用户ID作为参数
  router.push({ 
    path: '/chat/home', 
    query: { peerId: props.vo.userId } 
  });
};

// 保存用户信息
// 是否禁用保存按钮
const isSaveDisabled = ref(false)
const saveUserInfo = async () => {
  isSaveDisabled.value = true
  if(!userInfoFormRef.value){
    return
  }
  await userInfoFormRef.value.validate((valid, fields) => {
    if(valid){
      doPost<CommonResponse>(USER_INFO_SAVE_URL, {
        userId: userId,
        userName: userInfoForm.userName,
        company: userInfoForm.company,
        position: userInfoForm.position,
        profile: userInfoForm.description,
        // photo: cover.value
      }).then((response) => {
        // console.log(response)
        
        // 检查后端返回的状态码
        if (response && response.data.status && response.data.status.code === 0) {
          messageTip('保存成功', 'success')
          
          // 更新页面上显示的用户信息预览
          if (props.vo) {
            // 使用Object.assign创建新对象来更新响应式数据
            Object.assign(props.vo, {
              userName: userInfoForm.userName,
              company: userInfoForm.company,
              position: userInfoForm.position,
              profile: userInfoForm.description
            })
          }
          
          editInfoDialogVisible.value = false
        } else {
          // 后端返回错误信息，显示具体错误
          // console.log(response)
          const errorMsg = response?.data?.status?.msg || '保存失败，请稍后重试'
          messageTip(errorMsg, 'error')
        }
      }).catch((error) => {
        console.error(error)
        // 处理网络错误或其他异常
        messageTip('网络请求失败，请检查网络连接后重试', 'error')
      }).finally(() => {
        isSaveDisabled.value = false
      })
    }else{
      isSaveDisabled.value = false
    }
  })
}
// 登录框
const loginDialogClicked = ref(false)

const changeClicked = () => {
  loginDialogClicked.value = !loginDialogClicked.value
  // console.log("clicked: ", loginDialogClicked.value)
}


provide('loginDialogClicked', changeClicked)

</script>



<style scoped>
/* 主要容器样式 */
.user-profile-container {
  width: 100%;
  margin-bottom: 20px;
}

/* 头部渐变背景 */
.profile-header {
  padding: 30px 0;
  width: 100%;
  overflow: hidden;
}

/* 资料卡片样式 */
.profile-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  padding: 30px;
  max-width: 1200px;
  margin: 0 auto;
  position: relative;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.profile-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.15);
}

/* 头像部分样式 */
.avatar-section {
  display: flex;
  align-items: flex-start;
  gap: 30px;
  margin-bottom: 30px;
}

.avatar-container {
  position: relative;
  flex-shrink: 0;
}

.user-avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  border: 4px solid white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.user-avatar:hover {
  transform: scale(1.05);
}

.avatar-edit-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
  cursor: pointer;
  color: white;
}

.avatar-container:hover .avatar-edit-overlay {
  opacity: 1;
}

.edit-icon {
  font-size: 20px;
  margin-bottom: 5px;
}

.edit-text {
  font-size: 12px;
}

/* 用户信息部分 */
.user-info {
  flex: 1;
  min-width: 0;
}

.user-name-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  flex-wrap: wrap;
  gap: 15px;
}

.user-name {
  font-size: 28px;
  font-weight: 700;
  color: #333;
  margin: 0;
}

.chat-button {
  border-radius: 25px;
  padding: 8px 20px;
  font-size: 14px;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.3);
}

.chat-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.4);
}

/* 用户标签样式 */
.user-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 20px;
}

.custom-tag {
  font-size: 13px;
  padding: 6px 12px;
  border-radius: 16px;
  transition: all 0.3s ease;
}

.custom-tag:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.tag-icon {
  font-size: 12px;
  margin-right: 4px;
}

/* 用户简介样式 */
.user-profile {
  margin-bottom: 20px;
}

.profile-content {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 16px;
  background: #f5f7fa;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.profile-content:hover {
  background: #e6eaf1;
  transform: translateX(5px);
}

.profile-text {
  font-size: 14px;
  line-height: 1.6;
  color: #606266;
  margin: 0;
  flex: 1;
}

.profile-edit-icon {
  font-size: 16px;
  color: #909399;
  transition: color 0.3s ease;
}

.profile-content:hover .profile-edit-icon {
  color: #667eea;
}

/* 资料完善度进度条 */
.profile-progress {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 12px;
  font-size: 13px;
  color: #909399;
}

.progress-bar {
  flex: 1;
  height: 6px;
  border-radius: 3px;
}

.progress-text {
  font-weight: 600;
  color: #667eea;
  min-width: 35px;
}

.complete-profile-btn {
  color: #667eea;
  font-size: 13px;
  padding: 0;
  height: auto;
}

.complete-profile-btn:hover {
  color: #5a67d8;
  text-decoration: underline;
}

/* 访客看到的简介 */
.user-profile.visitor .profile-text {
  color: #4e5969;
  font-size: 15px;
}

/* 统计数据样式 */
.stats-container {
  display: flex;
  justify-content: space-around;
  padding-top: 25px;
  border-top: 1px solid #ebeef5;
}

.stat-item {
  text-align: center;
  flex: 1;
  transition: transform 0.3s ease;
}

.stat-item:hover {
  transform: translateY(-3px);
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #333;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.stat-divider {
  width: 1px;
  background: #ebeef5;
  margin: 0 20px;
}

/* 编辑弹窗样式优化 */
.custom-dialog {
  animation: dialogSlideIn 0.4s cubic-bezier(0.25, 0.8, 0.25, 1);
}

/* 对话框头部增强样式 */
.dialog-header-enhanced {
  position: relative;
  padding: 0;
  margin: 0;
  width: 100%;
  height: auto;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: visible;
}

/* 标题容器 */
.title-container {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  margin-top: 20px;
}

/* 标题蓝色背景框 */
.title-blue-bg {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  padding: 0;
  width: 100%;
  max-width: 350px;
  text-align: center;
  box-shadow: 0 4px 16px rgba(102, 126, 234, 0.3);
  margin-bottom: 15px;
}

/* 修复对话框错位问题 */
.custom-dialog .el-dialog__header {
  padding: 10px 20px 30px;
  margin: 0;
  height: auto;
  overflow: visible;
  background: transparent;
  text-align: center;
  position: relative;
}

/* 增强关闭按钮可见性 - 独立放置在右上角 */
.custom-dialog .el-dialog__headerbtn {
  z-index: 10;
  top: 10px;
  right: 10px;
  width: 36px;
  height: 36px;
  background-color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  border: 1px solid #e4e7ed;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: absolute;
}

.custom-dialog .el-dialog__headerbtn:hover {
  background-color: #ecf5ff;
  border-color: #d9ecff;
  transform: scale(1.1);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.custom-dialog .el-dialog__headerbtn .el-icon {
  color: #606266;
  font-size: 18px;
  transition: color 0.3s ease;
}

.custom-dialog .el-dialog__headerbtn:hover .el-icon {
  color: #667eea;
}

.dialog-title {
  position: relative;
  font-size: 24px;
  font-weight: 700;
  margin: 0;
  padding: 15px 20px;
  color: white;
  opacity: 0;
  transform: translateY(20px);
  animation: titleFadeIn 0.5s ease-out 0.2s forwards;
  text-align: center;
  width: 100%;
}

.dialog-subtitle {
  position: relative;
  font-size: 14px;
  font-weight: 400;
  margin: 0;
  color: #606266;
  opacity: 0;
  transform: translateY(20px);
  animation: subtitleFadeIn 0.5s ease-out 0.4s forwards;
  text-align: center;
  width: 100%;
}

/* 表单容器增强样式 */
.edit-profile-container-enhanced {
  padding: 20px 0;
}

.form-container-enhanced {
  width: 100%;
}

.form-item-enhanced {
  margin-bottom: 24px;
  position: relative;
  transition: all 0.3s ease;
}

.form-item-enhanced::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 4px;
  background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
  border-radius: 0 4px 4px 0;
  transform: translateX(-10px);
  opacity: 0;
  transition: all 0.3s ease;
}

.form-item-enhanced:focus-within::before {
  transform: translateX(0);
  opacity: 1;
}

.el-form-item__label {
  font-weight: 600;
  color: #4e5969;
  font-size: 14px;
  padding: 0;
  transition: all 0.3s ease;
}

.form-item-enhanced:focus-within .el-form-item__label {
  color: #667eea;
}

/* 输入框增强样式 */
.input-enhanced {
  border-radius: 10px;
  border: 2px solid #e4e7ed;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  background-color: #fafbfc;
  font-size: 15px;
}

.input-enhanced:hover {
  border-color: #c0c4cc;
  background-color: #ffffff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.input-enhanced:focus-within {
  border-color: #667eea;
  background-color: #ffffff;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.input-enhanced .el-input__wrapper {
  box-shadow: none;
  background: transparent;
}

.input-icon {
  color: #909399;
  font-size: 18px;
  transition: color 0.3s ease;
}

.input-enhanced:focus-within .input-icon {
  color: #667eea;
}

/* 字数统计样式 */
.el-input__count {
  font-size: 12px;
  color: #909399;
  background-color: transparent;
  border-top: none;
  padding: 2px 10px;
}

/* 对话框底部增强样式 */
.dialog-footer-enhanced {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 16px 24px 24px;
}

/* 按钮居中样式 */
.dialog-footer-enhanced.centered {
  justify-content: center;
}

.cancel-btn-enhanced {
  border-radius: 8px;
  padding: 10px 20px;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
  border: 1px solid #dcdfe6;
  background-color: #ffffff;
}

.cancel-btn-enhanced:hover {
  color: #606266;
  border-color: #c0c4cc;
  background-color: #f5f7fa;
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.save-btn-enhanced {
  border-radius: 8px;
  padding: 10px 24px;
  font-size: 14px;
  font-weight: 600;
  transition: all 0.3s ease;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  position: relative;
  overflow: hidden;
}

.save-btn-enhanced::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 0;
  height: 0;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  transform: translate(-50%, -50%);
  transition: width 0.6s, height 0.6s;
}

.save-btn-enhanced:hover::before {
  width: 300px;
  height: 300px;
}

.save-btn-enhanced:hover {
  transform: translateY(-1px);
  box-shadow: 0 6px 16px rgba(102, 126, 234, 0.4);
}

.save-btn-enhanced:active {
  transform: translateY(0);
}

/* 动画效果 */
@keyframes dialogSlideIn {
  from {
    opacity: 0;
    transform: translateY(-20px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

@keyframes titleFadeIn {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes subtitleFadeIn {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 表单元素出现动画 */
.form-item-enhanced {
  opacity: 0;
  transform: translateY(20px);
  animation: formItemFadeIn 0.5s ease-out forwards;
}

.form-item-enhanced:nth-child(1) { animation-delay: 0.5s; }
.form-item-enhanced:nth-child(2) { animation-delay: 0.6s; }
.form-item-enhanced:nth-child(3) { animation-delay: 0.7s; }
.form-item-enhanced:nth-child(4) { animation-delay: 0.8s; }

@keyframes formItemFadeIn {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 按钮出现动画 */
.dialog-footer-enhanced button {
  opacity: 0;
  transform: translateY(20px);
  animation: buttonFadeIn 0.5s ease-out forwards;
}

.dialog-footer-enhanced button:nth-child(1) { animation-delay: 0.9s; }
.dialog-footer-enhanced button:nth-child(2) { animation-delay: 1.0s; }

@keyframes buttonFadeIn {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .profile-card {
    padding: 20px;
    margin: 0 15px;
  }
  
  .avatar-section {
    flex-direction: column;
    align-items: center;
    text-align: center;
    gap: 20px;
  }
  
  .user-name-section {
    flex-direction: column;
    align-items: center;
    gap: 15px;
  }
  
  .user-name {
    font-size: 24px;
  }
  
  .profile-content {
    text-align: center;
    flex-direction: column;
    gap: 10px;
  }
  
  .profile-progress {
    flex-wrap: wrap;
    justify-content: center;
  }
  
  .stats-container {
    flex-direction: column;
    gap: 15px;
  }
  
  .stat-divider {
    width: 100%;
    height: 1px;
    margin: 15px 0;
  }
  
  .el-dialog {
    width: 90% !important;
    max-width: 400px;
  }
}

@media (max-width: 480px) {
  .profile-header {
    padding: 20px 0;
  }
  
  .user-avatar {
    width: 100px;
    height: 100px;
  }
  
  .user-name {
    font-size: 20px;
  }
  
  .user-tags {
    justify-content: center;
  }
}
</style>
