<template>
  <HeaderBar></HeaderBar>
  <div class="empty-article-wrapper">
    <div class="empty-article-container">
      <div class="empty-content">
        <div class="empty-icon-wrapper">
          <div class="empty-icon animate-float">
            <el-icon size="80">
              <DocumentChecked />
            </el-icon>
          </div>
        </div>
        <h2 class="empty-title reveal-text">文章不存在或已被删除</h2>
        <p class="empty-description reveal-text">很抱歉，您访问的文章可能已被删除或不存在</p>
        <router-link to="/" class="back-home-btn">
          <el-button type="primary" size="large" class="animate-button">
            <el-icon>
              <HomeFilled />
            </el-icon>
            返回主页
          </el-button>
        </router-link>
        <div class="additional-links reveal-text">
          <router-link to="/about" class="link-item">
            <el-icon size="18"><User /></el-icon>

            <span>看看作者</span>
          </router-link>
          <router-link to="/plan" class="link-item">
            <el-icon size="18"><Document /></el-icon>
            <span>开发日程</span>
          </router-link>
        </div>
      </div>
    </div>
  </div>
  <Footer class="custom-footer"></Footer>
  <LoginDialog :clicked="loginDialogClicked"></LoginDialog>
  <RegisterDialog :clicked="registerDialogClicked"></RegisterDialog>
</template>

<script setup lang="ts">
import { DocumentChecked, HomeFilled, Document } from '@element-plus/icons-vue'
import HeaderBar from '../layout/HeaderBar.vue';
import Footer from '../layout/Footer.vue';
import LoginDialog from '../dialog/LoginDialog.vue';
import RegisterDialog from '../dialog/RegisterDialog.vue';
import { provide, ref, onMounted } from 'vue';
import { User } from '@element-plus/icons-vue'

// 登录框
const changeClicked = () => {
  loginDialogClicked.value = !loginDialogClicked.value
  console.log("clicked: ", loginDialogClicked.value)
}

provide('loginDialogClicked', changeClicked)
const loginDialogClicked = ref(false)

// 注册框
const changeRegisterClicked = () => {
  registerDialogClicked.value = !registerDialogClicked.value
  console.log("clicked: ", registerDialogClicked.value)
}

provide('registerDialogClicked', changeRegisterClicked)
const registerDialogClicked = ref(false)

// 组件挂载后添加动画类
onMounted(() => {
  setTimeout(() => {
    const elements = document.querySelectorAll('.reveal-text');
    elements.forEach((el, index) => {
      setTimeout(() => {
        (el as HTMLElement).classList.add('revealed');
      }, index * 150);
    });
  }, 100);
});
</script>

<style scoped>
/* 全局容器样式 */
.empty-article-wrapper {
  min-height: calc(100vh - 200px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  background: radial-gradient(circle at 50% 50%, #ffffff 0%, #f0f4f8 100%);
  position: relative;
  overflow: hidden;
}

/* 背景装饰元素 */
.empty-article-wrapper::before,
.empty-article-wrapper::after {
  content: '';
  position: absolute;
  border-radius: 50%;
  filter: blur(100px);
  opacity: 0.4;
  z-index: 0;
}

.empty-article-wrapper::before {
  top: -20%;
  left: -10%;
  width: 40%;
  height: 80%;
  background: linear-gradient(135deg, #c4e0f9 0%, #e0f2fe 100%);
  animation: float-bg 20s ease-in-out infinite alternate;
}

.empty-article-wrapper::after {
  bottom: -20%;
  right: -10%;
  width: 40%;
  height: 80%;
  background: linear-gradient(135deg, #fce7f3 0%, #fbcfe8 100%);
  animation: float-bg 15s ease-in-out infinite alternate-reverse;
}

/* 主容器样式 */
.empty-article-container {
  position: relative;
  z-index: 1;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 内容卡片样式 */
.empty-content {
  text-align: center;
  background: rgba(255, 255, 255, 0.95);
  padding: 60px 40px;
  border-radius: 24px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.08);
  max-width: 500px;
  width: 100%;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.5);
}

.empty-content:hover {
  transform: translateY(-8px);
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.12);
}

/* 图标容器样式 */
.empty-icon-wrapper {
  margin-bottom: 32px;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 图标样式 */
.empty-icon {
  color: #409eff;
  background: linear-gradient(135deg, #e6f7ff 0%, #bae7ff 100%);
  width: 140px;
  height: 140px;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 50%;
  box-shadow: 0 8px 25px rgba(64, 158, 255, 0.2);
  position: relative;
}

.empty-icon::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 120%;
  height: 120%;
  background: linear-gradient(135deg, rgba(64, 158, 255, 0.1) 0%, rgba(64, 158, 255, 0) 70%);
  border-radius: 50%;
  transform: translate(-50%, -50%);
  z-index: -1;
  opacity: 0.7;
}

/* 标题样式 */
.empty-title {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
  margin-bottom: 16px;
  line-height: 1.3;
  letter-spacing: -0.02em;
}

/* 描述文本样式 */
.empty-description {
  font-size: 16px;
  color: #606266;
  margin-bottom: 32px;
  line-height: 1.7;
}

/* 返回主页按钮样式 */
.back-home-btn {
  display: inline-block;
  margin-bottom: 32px;
}

/* 附加链接样式 */
.additional-links {
  display: flex;
  justify-content: center;
  gap: 32px;
  flex-wrap: wrap;
}

.link-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #606266;
  text-decoration: none;
  padding: 12px 16px;
  border-radius: 12px;
  transition: all 0.3s ease;
  min-width: 80px;
}

.link-item:hover {
  background-color: #f5f7fa;
  color: #409eff;
  transform: translateY(-2px);
}

.link-item .el-icon {
  margin-bottom: 8px;
  font-size: 24px;
}

.link-item span {
  font-size: 14px;
  font-weight: 500;
}

/* 文字渐入动画 */
.reveal-text {
  opacity: 0;
  transform: translateY(20px);
  transition: opacity 0.6s ease, transform 0.6s ease;
}

.reveal-text.revealed {
  opacity: 1;
  transform: translateY(0);
}

/* 按钮动画效果 */
.animate-button {
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.animate-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: all 0.5s ease;
}

.animate-button:hover::before {
  left: 100%;
}

.animate-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(64, 158, 255, 0.4);
}

/* 图标浮动动画 */
@keyframes float {
  0% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-10px);
  }
  100% {
    transform: translateY(0px);
  }
}

/* 背景浮动动画 */
@keyframes float-bg {
  0% {
    transform: translate(0%, 0%) scale(1);
  }
  50% {
    transform: translate(5%, 5%) scale(1.05);
  }
  100% {
    transform: translate(-5%, -5%) scale(0.95);
  }
}

.animate-float {
  animation: float 4s ease-in-out infinite;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .empty-article-wrapper {
    padding: 20px;
    min-height: calc(100vh - 180px);
  }
  
  .empty-content {
    padding: 48px 24px;
    margin: 0 10px;
    border-radius: 16px;
  }
  
  .empty-title {
    font-size: 24px;
  }
  
  .empty-description {
    font-size: 14px;
    margin-bottom: 24px;
  }
  
  .empty-icon {
    width: 120px;
    height: 120px;
  }
  
  .empty-icon-wrapper {
    margin-bottom: 24px;
  }
  
  .additional-links {
    gap: 20px;
  }
  
  .back-home-btn {
    margin-bottom: 24px;
  }
}

@media (max-width: 480px) {
  .empty-content {
    padding: 40px 20px;
  }
  
  .empty-title {
    font-size: 22px;
  }
  
  .additional-links {
    flex-direction: column;
    align-items: center;
    gap: 16px;
  }
  
  .link-item {
    width: 100%;
    max-width: 200px;
  }
}
</style>