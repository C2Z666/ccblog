<template>
  <nav
    :data-islogin="global.isLogin? 'true' : 'false'"
    class="navbar fixed-top"
  >
    <div class="nav-container">
      <!-- Logo区域 -->
      <div class="logo-area" style="margin-left: -50px;">
        <a class="logo-link" href="/"
           @mouseenter="logoHover = true"
           @mouseleave="logoHover = false"
        >
          <div class="logo-wrapper"
               :class="{ 'logo-hover': logoHover }"
          >
            <img 
              class="logo-image" 
              src="/src/assets/static/img/ccblog_logo.png"
              alt="网站Logo"
              style="height: 48px; width: auto; max-width: 100%; object-fit: contain;"
            />
          </div>
        </a>
      </div>

      <!-- 桌面端导航菜单 -->
      <nav class="desktop-nav hidden-mobile">
        <ul class="nav-menu">
          <li 
            v-for="item in navItems" 
            :key="item.path"
            :class="['nav-item', { 'active': activeTab === item.path }]"
          >
            <router-link 
              :to="item.path" 
              class="nav-link"
              @click="activeTab = item.path"
            >
              <i :class="['nav-icon', item.icon]"></i>
              <span class="nav-text">{{ item.text }}</span>
            </router-link>
          </li>
        </ul>
      </nav>

      <!-- 移动端菜单按钮 -->
      <div class="mobile-menu-toggle visible-mobile">
        <el-dropdown 
          trigger="click" 
          @command="handleMobileMenuCommand"
          placement="bottom"
        >
          <el-button type="text" class="mobile-menu-btn">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="icon menu-icon"
              width="28"
              height="28"
              viewBox="0 0 24 24"
              stroke-width="2"
              stroke="currentColor"
              fill="none"
              stroke-linecap="round"
              stroke-linejoin="round"
            >
              <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
              <path d="M4 6l16 0"></path>
              <path d="M4 12l16 0"></path>
              <path d="M4 18l16 0"></path>
            </svg>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu class="mobile-dropdown-menu">
              <el-dropdown-item 
                v-for="item in navItems" 
                :key="item.path"
                :command="item.path"
                :class="{ 'active': activeTab === item.path }"
              >
                <i :class="['nav-icon', item.icon]"></i>
                <span class="nav-text">{{ item.text }}</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>

      <!-- 右侧功能区 -->
      <div class="nav-actions">
        <!-- 写文章按钮 -->
        <router-link 
          v-if="!route.path.includes('/article/edit') && route.path !== '/article/edit/' && global.isLogin"
          to="/article/edit"
          class="action-link create-article"
          title="写文章"
          @mouseenter="showTooltip('create-article')"
          @mouseleave="hideTooltip()"
          @mousemove="updateTooltipPosition($event, 'create-article')"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="icon"
            width="24"
            height="24"
            viewBox="0 0 24 24"
            stroke-width="2"
            stroke="currentColor"
            fill="none"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
            <path d="M17 3a2.85 2.83 0 1 1 4 4L7.5 20.5 2 22l1.5 -5.5L17 3z"></path>
          </svg>
        </router-link>

        <button
          v-else-if="route.path.includes('/article/edit') || route.path === '/article/edit/'"
          type="button"
          class="back-btn"
          @click="router.push('/')"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="icon btn-icon"
            width="18"
            height="18"
            viewBox="0 0 24 24"
            stroke-width="2"
            stroke="currentColor"
            fill="none"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
            <path d="M19 12h-15"></path>
            <path d="M12 19l-7 -7l7 -7"></path>
          </svg>
          返回主页
        </button>

        <!-- 未登录状态 -->
        <div v-if="!global.isLogin" class="auth-buttons">
          <el-button 
            class="auth-btn register-btn"
            @click="registerButton"
          >注册</el-button>
          <el-button 
            class="auth-btn login-btn"
            @click="loginButton"
          >登录</el-button>
        </div>

        <!-- 已登录状态 -->
        <div v-if="global.isLogin" class="user-actions">
          <!-- 功能图标组 -->
          <div class="action-icons">
            <!-- AI聊天按钮 -->
            <router-link 
              to="/chat/ai" 
              class="action-link ai-chat"
              title="AI聊天"
              @mouseenter="showTooltip('ai-chat')"
              @mouseleave="hideTooltip()"
              @mousemove="updateTooltipPosition($event, 'ai-chat')"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="icon"
                width="24"
                height="24"
                viewBox="0 0 24 24"
                stroke-width="2"
                stroke="currentColor"
                fill="none"
                stroke-linecap="round"
                stroke-linejoin="round"
              >
                <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
                <path d="M12 12m-9 0a9 9 0 1 0 18 0a9 9 0 1 0 -18 0"></path>
                <path d="M12 16v-6"></path>
                <path d="M9.5 11.5l5 -2.5"></path>
              </svg>
            </router-link>

            <!-- 私信按钮 -->
            <router-link 
              to="/chat/home" 
              class="action-link navbar-count-msg-box private-chat"
              title="私信"
              @mouseenter="showTooltip('private-chat')"
              @mouseleave="hideTooltip()"
            >
              <span
                v-if="global.userMsgNum != null && global.userMsgNum > 0"
                class="navbar-count-msg"
              >
                {{ global.userMsgNum }}
              </span>
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="icon"
                width="24"
                height="24"
                viewBox="0 0 24 24"
                stroke-width="2"
                stroke="currentColor"
                fill="none"
                stroke-linecap="round"
                stroke-linejoin="round"
              >
                <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
                <path d="M21 15a2 2 0 0 1 -2 2H7l-4 4V5a2 2 0 0 1 2 -2h14a2 2 0 0 1 2 2z"></path>
              </svg>
            </router-link>

            <!-- 通知按钮 -->
            <router-link 
              to="/notice/" 
              class="action-link navbar-count-msg-box notice"
              title="通知"
              @mouseenter="showTooltip('notice')"
              @mouseleave="hideTooltip()"
            >
              <span
                v-if="global.noticeMsgNum != null && global.noticeMsgNum > 0"
                class="navbar-count-msg"
              >
                {{ global.noticeMsgNum }}
              </span>
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="icon"
                width="24"
                height="24"
                viewBox="0 0 24 24"
                stroke-width="2"
                stroke="currentColor"
                fill="none"
                stroke-linecap="round"
                stroke-linejoin="round"
              >
                <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
                <path
                  d="M10 5a2 2 0 0 1 4 0a7 7 0 0 1 4 6v3a4 4 0 0 0 2 3h-16a4 4 0 0 0 2 -3v-3a7 7 0 0 1 4 -6"
                ></path>
                <path d="M9 17v1a3 3 0 0 0 6 0v-1"></path>
              </svg>
            </router-link>
          </div>

          <!-- 用户头像下拉菜单 -->
          <el-dropdown 
            trigger="click"
            placement="bottom"
            @command="handleUserMenuCommand"
          >
            <div class="user-profile"
                 @mouseenter="userMenuHover = true"
                 @mouseleave="userMenuHover = false"
            >
              <div class="avatar-wrapper"
                   :class="{ 'avatar-hover': userMenuHover }"
              >
                <img
                  class="user-avatar"
                  :src="global.user.photo ? global.user.photo : defaultAvatar"
                  alt="用户头像"
                  loading="lazy"
                />
                <div class="avatar-badge"></div>
              </div>
              <div class="user-info">
                <div class="user-name">{{ global.user.userName || '用户' }}</div>
                <div class="user-role">普通用户</div>
              </div>
              <span class="dropdown-arrow"
                    :class="{ 'arrow-rotate': userMenuHover }"
              >
                <i class="el-icon-arrow-down"></i>
              </span>
            </div>
            <template #dropdown>
              <el-dropdown-menu class="user-dropdown-menu">
                <el-dropdown-item command="personal-page"
                                  :disabled="!global.user.userId"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    class="icon menu-icon"
                    width="16"
                    height="16"
                    viewBox="0 0 24 24"
                    stroke-width="2"
                    stroke="currentColor"
                    fill="none"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  >
                    <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
                    <path d="M20 21v-2a4 4 0 0 0 -4 -4H8a4 4 0 0 0 -4 4v2"></path>
                    <circle cx="12" cy="7" r="4"></circle>
                  </svg>
                  个人主页
                </el-dropdown-item>
                <el-dropdown-item command="logout"
                                  divided
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    class="icon menu-icon"
                    width="16"
                    height="16"
                    viewBox="0 0 24 24"
                    stroke-width="2"
                    stroke="currentColor"
                    fill="none"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  >
                    <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
                    <path d="M9 21H5a2 2 0 0 1 -2 -2V5a2 2 0 0 1 2 -2h4"></path>
                    <path d="M16 17L21 12L16 7"></path>
                    <path d="M21 12H9"></path>
                  </svg>
                  登出
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
  </nav>
  
</template>

<script setup lang="ts">
import { inject, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
const router = useRouter()
const route = useRoute()
import { doGet, doPost} from '@/http/BackendRequests'
import {
  type CommonResponse,
} from '@/http/ResponseTypes/CommonResponseType'
import { useGlobalStore } from '@/stores/global'
const globalStore = useGlobalStore()

const global = globalStore.global

// 导航菜单项配置
const navItems = [
  { path: '/', text: '首页', icon: 'el-icon-s-home' },
  { path: '/about', text: '关于作者', icon: 'el-icon-user' },
  { path: '/plan', text: '开发日程', icon: 'el-icon-time' }
]

// 默认头像
const defaultAvatar = 'https://static.developers.pub/static/img/logo.b2ff606.jpeg'

// 交互状态
const activeTab = ref('/')
const logoHover = ref(false)
const userMenuHover = ref(false)
const userMenuVisible = ref(false)
const tooltipVisible = ref(false)
const tooltipContent = ref('')
const tooltipTarget = ref('')

// 监听路由变化，更新当前激活的标签
watch(
  () => router.currentRoute.value.path,
  (newPath) => {
    // 查找最匹配的导航项
    const matchedItem = navItems.find(item => 
      newPath === item.path || newPath.startsWith(item.path + '/')
    )
    activeTab.value = matchedItem ? matchedItem.path : '/'
  },
  { immediate: true }
)

// ======= 跳转到写文章 ==========
const writeArticle = () => {
  router.replace("/article/edit")
}

// 显示工具提示
const showTooltip = (target: string) => {
  tooltipTarget.value = target
  const titles = {
    'ai-chat': 'AI聊天',
    'private-chat': '私信',
    'notice': '通知'
  }
  tooltipContent.value = titles[target as keyof typeof titles] || ''
  tooltipVisible.value = true
}

// 更新工具提示位置
const updateTooltipPosition = (event: MouseEvent, target: string) => {
  if (!tooltipVisible.value) return
  
  const tooltip = document.querySelector('.tooltip') as HTMLElement
  if (tooltip) {
    const rect = (event.target as HTMLElement).getBoundingClientRect()
    tooltip.style.top = `${rect.top - 40}px`
    tooltip.style.left = `${rect.left + rect.width / 2 - tooltip.offsetWidth / 2}px`
  }
}

// 隐藏工具提示
const hideTooltip = () => {
  // 延迟隐藏，以便在快速移动时不会闪烁
  setTimeout(() => {
    tooltipVisible.value = false
  }, 200)
}

// 处理移动端菜单命令
const handleMobileMenuCommand = (command: string) => {
  router.push(command)
  activeTab.value = command
}

// 处理用户菜单命令
const handleUserMenuCommand = (command: string) => {
  userMenuVisible.value = false
  
  switch (command) {
    case 'personal-page':
      personalPage()
      break
    case 'tools-page':
      toolsPage()
      break
    case 'logout':
      logout()
      break
  }
}

import { messageTip, sleep } from '@/util/utils'
import { MESSAGE_TYPE } from '@/constants/MessageTipEnumConstant'
import { LOGOUT_URL } from '@/http/URL'
import { useFollowIdCache } from '@/stores/useFollowIdCache'

// 登录框的激活
const showLoginDialog = inject<() => void>('loginDialogClicked')
// 注册框的激活
const showRegisterLoginDialog = inject<() => void>('registerDialogClicked')

// 登录按钮处理
const loginButton = () => {
  if(showLoginDialog) {
    showLoginDialog()
  } else {
    console.error("未找到登录对话框注入方法")
  }
}

// 注册按钮处理
const registerButton = () => {
  if(showRegisterLoginDialog) {
    showRegisterLoginDialog()
  } else {
    console.error("未找到注册对话框注入方法")
  }
}

// 个人主页跳转
const personalPage = () => {
  if (!global.user.userId) {
    messageTip("请先登录", MESSAGE_TYPE.WARNING)
    return
  }
  
  if (route.fullPath.includes(`/user/${global.user.userId}`)) {
    messageTip("已经在个人主页了", MESSAGE_TYPE.INFO)
    return
  }
  
  router.push(`/user/${global.user.userId}`)
}

// 工具页跳转
const toolsPage = () => {
  if (route.fullPath.includes('/tools/')) {
    messageTip("已经在工具页了", MESSAGE_TYPE.INFO)
    return
  }
  router.push('/tools/')
}

// 退出登录
const logout = async () => {
  try {
    const response = await doPost<CommonResponse>(LOGOUT_URL, {})
    if(response.data.status.code === 0) {
      messageTip("退出登录成功", MESSAGE_TYPE.SUCCESS)
      await sleep(1)
      window.location.reload()
    }
  } catch (error) {
    console.error("退出登录失败:", error)
    messageTip("退出登录失败，请重试", MESSAGE_TYPE.ERROR)
  }
}

// 页面滚动处理 - 添加防抖和增强的视觉效果
let lastScrollTop = 0
let scrollTimeout: number | null = null
const SCROLL_THRESHOLD = 80 // 最小滚动阈值
const SCROLL_HIDE_THRESHOLD = 100 // 隐藏导航栏阈值

const handleScroll = () => {
  // 防抖处理，避免频繁触发
  if (scrollTimeout) {
    clearTimeout(scrollTimeout)
  }
  
  scrollTimeout = window.setTimeout(() => {
    const scrollTop = window.pageYOffset || document.documentElement.scrollTop
    const navbar = document.querySelector('.navbar')
    
    if (navbar) {
      // 滚动超过阈值时应用滚动样式
      if (scrollTop > SCROLL_THRESHOLD) {
        navbar.classList.add('navbar-scrolled')
      } else {
        navbar.classList.remove('navbar-scrolled')
      }
      
      // 隐藏/显示导航栏逻辑 - 仅在滚动距离足够时执行
      if (Math.abs(scrollTop - lastScrollTop) > 10) {
        if (scrollTop > lastScrollTop && scrollTop > SCROLL_HIDE_THRESHOLD) {
          // 向下滚动且超过阈值，隐藏导航栏
          navbar.classList.add('navbar-hidden')
        } else {
          // 向上滚动或在顶部，显示导航栏
          navbar.classList.remove('navbar-hidden')
        }
      }
    }
    
    lastScrollTop = scrollTop <= 0 ? 0 : scrollTop // 防止负滚动值
  }, 50)
}

// 生命周期钩子
onMounted(() => {
  // 初始化当前激活标签
  activeTab.value = router.currentRoute.value.path
  
  // 添加滚动事件监听
  window.addEventListener('scroll', handleScroll, { passive: true })
})

// 监听组件卸载
import { onUnmounted } from 'vue'
onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
/* 全局样式变量 */
:root {
  --navbar-height: 64px;
  --navbar-background: rgba(255, 255, 255, 0.95);
  --navbar-background-scrolled: rgba(255, 255, 255, 0.98);
  --navbar-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  --navbar-shadow-scrolled: 0 4px 16px rgba(0, 0, 0, 0.12);
  --primary-color: #409eff;
  --success-color: #67c23a;
  --warning-color: #e6a23c;
  --danger-color: #f56c6c;
  --text-primary: #303133;
  --text-regular: #606266;
  --text-secondary: #909399;
  --border-color: #e4e7ed;
  --hover-background: #f5f7fa;
  --transition-base: all 0.3s ease;
  --border-radius-base: 8px;
  --border-radius-small: 4px;
  --box-shadow-base: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

/* 基础导航栏样式 */
.navbar {
  height: var(--navbar-height);
  background-color: var(--navbar-background);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid var(--border-color);
  box-shadow: var(--navbar-shadow);
  transition: var(--transition-base);
  z-index: 999;
}

/* 滚动后的导航栏样式 */
.navbar-scrolled {
  background-color: var(--navbar-background-scrolled);
  box-shadow: var(--navbar-shadow-scrolled);
}

/* 导航栏隐藏动画 */
.navbar-hidden {
  transform: translateY(-100%);
}

/* 导航容器 */
.nav-container {
  display: flex;
  align-items: center;
  height: 100%;
  max-width: 2000px;
  margin: 0 auto;
  padding: 0 20px;
}

/* Logo区域 */
.logo-area {
  display: flex;
  align-items: center;
  position: relative;
  flex-shrink: 0;
  margin-left: -80px !important; /* 大幅增加负边距使logo更靠左，并使用important提高优先级 */
  transform: translateX(-10px); /* 额外使用transform辅助偏移 */
}

.logo-link {
  display: flex;
  align-items: center;
  text-decoration: none;
  position: relative;
  z-index: 10;
}

.logo-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  width: fit-content;
  height: fit-content;
  padding: 4px;
  border-radius: 12px;
  overflow: hidden;
  background: linear-gradient(135deg, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0.05) 100%);
  backdrop-filter: blur(10px);
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.logo-wrapper::before {
  content: '';
  position: absolute;
  top: -10px;
  left: -10px;
  right: -10px;
  bottom: -10px;
  background: linear-gradient(135deg, rgba(64, 158, 255, 0.1) 0%, rgba(102, 177, 255, 0.05) 100%);
  border-radius: 16px;
  z-index: -1;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.logo-hover {
  transform: translateY(-2px) scale(1.03);
  box-shadow: 0 6px 20px rgba(64, 158, 255, 0.15);
  border-color: rgba(64, 158, 255, 0.3);
}

.logo-hover::before {
  opacity: 1;
}

.logo-image {
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.05));
}

.logo-hover .logo-image {
  transform: scale(1.05);
  filter: drop-shadow(0 4px 8px rgba(64, 158, 255, 0.2));
}

/* 响应式调整 */
@media (max-width: 768px) {
  .logo-wrapper {
    padding: 2px;
    border-radius: 8px;
  }
  
  .logo-image {
    height: 40px !important;
  }
  
  .logo-hover {
    transform: translateY(-1px) scale(1.02);
  }
}

/* 桌面端导航菜单 */
.desktop-nav {
  display: flex;
  align-items: center;
  margin-left: auto;
}

.nav-menu {
  display: flex;
  list-style: none;
  margin: 0;
  padding: 0;
  gap: 8px;
}

.nav-item {
  position: relative;
  margin: 0;
}

.nav-link {
  display: flex;
  align-items: center;
  padding: 12px 24px;
  color: var(--text-regular);
  text-decoration: none;
  border-radius: 25px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  font-size: 15px;
  font-weight: 500;
  position: relative;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(4px);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

/* 导航链接悬停效果 */
.nav-link::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(64, 158, 255, 0.1), transparent);
  transition: all 0.6s ease;
}

.nav-link:hover {
  color: var(--primary-color);
  background-color: rgba(255, 255, 255, 0.8);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);
  border-color: rgba(64, 158, 255, 0.2);
}

.nav-link:hover::before {
  left: 100%;
}

/* 活动导航项 */
.nav-item.active .nav-link {
  color: var(--primary-color);
  background-color: rgba(64, 158, 255, 0.15);
  font-weight: 600;
  border-color: rgba(64, 158, 255, 0.3);
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.1);
}

.nav-item.active .nav-link::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 20%;
  right: 20%;
  height: 3px;
  background: linear-gradient(90deg, var(--primary-color), #66b1ff);
  border-radius: 3px;
  animation: pulse-glow 2s infinite;
}

/* 图标样式 */
.nav-icon {
  margin-right: 8px;
  font-size: 16px;
  transition: all 0.3s ease;
}

.nav-link:hover .nav-icon {
  transform: scale(1.1);
}

/* 脉冲光晕动画 */
@keyframes pulse-glow {
  0%, 100% {
    opacity: 0.7;
    transform: scaleX(1);
  }
  50% {
    opacity: 1;
    transform: scaleX(1.1);
  }
}

/* 移动端菜单 */
.mobile-menu-toggle {
  display: none;
}

.mobile-menu-btn {
  color: var(--text-regular);
  font-size: 24px;
  padding: 8px;
}

.mobile-dropdown-menu {
  min-width: 160px;
}

.mobile-dropdown-menu .el-dropdown-item {
  display: flex;
  align-items: center;
  padding: 12px 20px;
}

.mobile-dropdown-menu .el-dropdown-item.active {
  color: var(--primary-color);
  background-color: rgba(64, 158, 255, 0.1);
}

/* 右侧操作区 */
.nav-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

/* 写文章按钮 - 保持与其他图标一致的样式 */
          .create-article {
            background-color: transparent;
            color: var(--text-regular);
          }
          
          .create-article:hover {
            background-color: var(--hover-background);
            color: var(--primary-color);
            box-shadow: none;
          }
.create-article {
  background-color: var(--primary-color);
  color: white;
}

.create-article:hover {
  background-color: #66b1ff;
  color: white;
  box-shadow: 0 4px 8px rgba(64, 158, 255, 0.4);
}

/* 返回主页按钮 */

/* 返回主页按钮 */
.back-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  background-color: #f0f9ff;
  color: var(--primary-color);
  border: 1px solid #d9ecff;
  border-radius: var(--border-radius-base);
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: var(--transition-base);
}

.back-btn:hover {
  background-color: #e6f7ff;
  border-color: #91d5ff;
}

.btn-icon {
  font-size: 16px;
}

/* 认证按钮组 - 高级美化版 */
.auth-buttons {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-left: 24px;
  position: relative;
}

/* 使用更具体的选择器并提高优先级 */
.auth-btn,
.el-button.auth-btn {
  font-size: 15px !important;
  font-weight: 600 !important;
  border-radius: 30px !important;
  padding: 12px 28px !important;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1) !important;
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
  gap: 8px !important;
  position: relative !important;
  overflow: hidden !important;
  border: 2px solid transparent !important;
  text-transform: uppercase !important;
  letter-spacing: 0.5px !important;
  background: none !important;
  box-shadow: none !important;
}

/* 注册按钮 - 高级玻璃质感 */
.register-btn,
.el-button.register-btn,
.el-button--text.register-btn {
  color: var(--primary-color) !important;
  background: rgba(255, 255, 255, 0.1) !important;
  backdrop-filter: blur(8px) !important;
  border-image: linear-gradient(45deg, var(--primary-color), #66b1ff) 1 !important;
  box-shadow: 
    0 4px 15px rgba(64, 158, 255, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.3) !important;
}

/* 注册按钮悬浮效果 */
.register-btn:hover,
.el-button.register-btn:hover,
.el-button--text.register-btn:hover {
  color: var(--primary-color) !important;
  background: rgba(255, 255, 255, 0.2) !important;
  transform: translateY(-3px) !important;
  box-shadow: 
    0 8px 25px rgba(64, 158, 255, 0.25),
    inset 0 2px 0 rgba(255, 255, 255, 0.4) !important;
  border-image: linear-gradient(45deg, #66b1ff, var(--primary-color)) 1 !important;
}

/* 登录按钮 - 渐变发光效果 */
.login-btn,
.el-button.login-btn,
.el-button--primary.login-btn {
  background: linear-gradient(135deg, var(--primary-color), #66b1ff) !important;
  color: #000000 !important; /* 将文字颜色改为黑色以提高对比度 */
  border: 2px solid transparent !important;
  box-shadow: 
    0 4px 15px rgba(64, 158, 255, 0.3),
    0 0 0 1px rgba(64, 158, 255, 0.1) !important;
  position: relative !important;
}

/* 登录按钮图标 - 放在左侧，与注册按钮保持一致 */
.login-btn::before,
.el-button.login-btn::before {
  content: '🔑' !important;
  font-size: 18px !important;
  margin-right: 8px !important;
  animation: pulse-icon 2s infinite !important;
  position: relative !important;
  z-index: 2 !important;
}

/* 登录按钮发光边框效果 */
.login-btn::after,
.el-button.login-btn::after {
  content: '' !important;
  position: absolute !important;
  top: 0 !important;
  left: 0 !important;
  right: 0 !important;
  bottom: 0 !important;
  border-radius: 30px !important;
  padding: 2px !important;
  background: linear-gradient(135deg, var(--primary-color), #66b1ff, var(--primary-color)) !important;
  -webkit-mask: linear-gradient(#fff 0 0) content-box, linear-gradient(#fff 0 0) !important;
  mask: linear-gradient(#fff 0 0) content-box, linear-gradient(#fff 0 0) !important;
  -webkit-mask-composite: xor !important;
  mask-composite: exclude !important;
  pointer-events: none !important;
  animation: rotate-border 4s linear infinite !important;
  z-index: 1 !important;
}

/* 登录按钮悬浮效果 */
.login-btn:hover,
.el-button.login-btn:hover,
.el-button--primary.login-btn:hover {
  background: linear-gradient(135deg, #66b1ff, var(--primary-color)) !important;
  transform: translateY(-3px) !important;
  box-shadow: 
    0 12px 30px rgba(64, 158, 255, 0.4),
    0 0 0 2px rgba(64, 158, 255, 0.2) !important;
  color: #000000 !important; /* 确保悬浮时文字仍然是黑色 */
}

/* 按钮点击效果 */
.auth-btn:active {
  transform: translateY(-1px);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

/* 按钮图标样式 */
.register-btn::before,
.el-button.register-btn::before {
  content: '👤' !important;
  font-size: 18px !important;
  margin-right: 8px !important;
  animation: bounce-icon 2s infinite alternate !important;
  position: relative !important;
  z-index: 2 !important;
}

/* 图标动画 */
@keyframes bounce-icon {
  0% {
    transform: translateY(0);
  }
  100% {
    transform: translateY(-3px);
  }
}

@keyframes pulse-icon {
  0%, 100% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.1);
    opacity: 0.8;
  }
}

/* 边框旋转动画 */
@keyframes rotate-border {
  0% {
    background-position: 0% 50%;
  }
  100% {
    background-position: 200% 50%;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .auth-buttons {
    gap: 12px !important;
    margin-left: 16px !important;
  }
  
  .auth-btn,
  .el-button.auth-btn {
    padding: 10px 20px !important;
    font-size: 14px !important;
  }
  
  .register-btn::before,
  .login-btn::before,
  .el-button.register-btn::before,
  .el-button.login-btn::before {
    font-size: 16px !important;
  }
}

@media (max-width: 768px) {
  .logo-area {
    margin-left: -50px !important; /* 在移动端适当调整logo位置 */
  }
}

@media (max-width: 480px) {
  .auth-buttons {
    gap: 8px !important;
    margin-left: 8px !important;
  }
  
  .auth-btn,
  .el-button.auth-btn {
    padding: 8px 16px !important;
    font-size: 13px !important;
    border-radius: 25px !important;
  }
  
  .register-btn::before,
  .login-btn::before,
  .el-button.register-btn::before,
  .el-button.login-btn::before {
    font-size: 14px !important;
    margin-right: 4px !important;
    margin-left: 0 !important;
  }
  
  .logo-area {
    margin-left: -30px !important; /* 在超小屏幕上适当调整logo位置 */
  }
}

/* 用户操作区 */
.user-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

/* 功能图标组 */
.action-icons {
  display: flex;
  align-items: center;
  gap: 4px;
}

.action-link {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  color: var(--text-regular);
  border-radius: 50%;
  transition: var(--transition-base);
  position: relative;
  text-decoration: none;
}

.action-link:hover {
  color: var(--primary-color);
  background-color: var(--hover-background);
  transform: translateY(-1px);
}

.action-link .icon {
  font-size: 20px;
}

/* 消息角标容器 */
.navbar-count-msg-box {
  position: relative;
}

/* 消息角标 - 增强视觉效果 */
.navbar-count-msg {
  position: absolute;
  top: 4px;
  right: 2px;
  min-width: 20px;
  height: 20px;
  padding: 0 6px;
  background-color: #ff4d4f;
  color: white;
  font-size: 12px;
  font-weight: bold;
  line-height: 20px;
  text-align: center;
  border-radius: 10px;
  border: 2px solid white;
  transform: translate(50%, -50%);
  z-index: 1;
  box-shadow: 0 2px 4px rgba(255, 77, 79, 0.3);
  animation: pulse 2s infinite;
}

/* 脉冲动画效果 */
@keyframes pulse {
  0% {
    box-shadow: 0 2px 4px rgba(255, 77, 79, 0.3);
  }
  50% {
    box-shadow: 0 2px 8px rgba(255, 77, 79, 0.6);
  }
  100% {
    box-shadow: 0 2px 4px rgba(255, 77, 79, 0.3);
  }
}

/* 用户资料区域 */
.user-profile {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 4px 8px;
  border-radius: var(--border-radius-base);
  cursor: pointer;
  transition: var(--transition-base);
}

.user-profile:hover {
  background-color: var(--hover-background);
}

/* 头像包装器 */
.avatar-wrapper {
  position: relative;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  overflow: hidden;
  transition: var(--transition-base);
  border: 2px solid transparent;
}

.avatar-hover {
  transform: scale(1.05);
  border-color: var(--primary-color);
}

/* 用户头像 */
.user-avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 头像徽章 */
.avatar-badge {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 10px;
  height: 10px;
  background-color: var(--success-color);
  border: 2px solid white;
  border-radius: 50%;
}

/* 用户信息 */
.user-info {
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-width: 0;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
  color: var(--text-primary);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.user-role {
  font-size: 12px;
  color: var(--text-secondary);
  white-space: nowrap;
}

/* 下拉箭头 */
.dropdown-arrow {
  display: flex;
  align-items: center;
  color: var(--text-secondary);
  transition: var(--transition-base);
}

.arrow-rotate {
  transform: rotate(180deg);
  color: var(--primary-color);
}

/* 用户下拉菜单 */
.user-dropdown-menu {
  min-width: 160px;
  padding: 4px 0;
  border-radius: var(--border-radius-base);
  box-shadow: var(--box-shadow-base);
  animation: dropdownSlideIn 0.2s ease-out;
}

.user-dropdown-menu .el-dropdown-item {
  display: flex;
  align-items: center;
  padding: 10px 20px;
  font-size: 14px;
  transition: var(--transition-base);
}

.user-dropdown-menu .el-dropdown-item:hover {
  background-color: var(--hover-background);
  color: var(--primary-color);
}

.user-dropdown-menu .menu-icon {
  margin-right: 8px;
  font-size: 16px;
}

/* 动画效果 */
@keyframes dropdownSlideIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .nav-container {
    padding: 0 16px;
  }
  
  .user-info {
    display: none;
  }
  
  .nav-link {
    padding: 10px 16px;
  }
}

@media (max-width: 768px) {
  :root {
    --navbar-height: 56px;
  }
  
  .desktop-nav {
    display: none;
  }
  
  .mobile-menu-toggle {
    display: flex;
  }
  
  .create-btn, .back-btn {
    padding: 6px 12px;
    font-size: 13px;
  }
  
  .btn-icon {
    font-size: 14px;
  }
  
  .auth-buttons {
    gap: 6px;
  }
  
  .auth-btn {
    padding: 6px 12px;
    font-size: 13px;
  }
  
  .action-link {
    width: 36px;
    height: 36px;
  }
  
  .action-link .icon {
    font-size: 18px;
  }
  
  .navbar-count-msg {
    min-width: 16px;
    height: 16px;
    font-size: 11px;
    line-height: 16px;
  }
}

@media (max-width: 480px) {
  .nav-container {
    padding: 0 12px;
  }
  
  .logo-image {
    height: 36px;
  }
  
  .user-actions {
    gap: 8px;
  }
  
  .create-btn, .back-btn {
    padding: 4px 10px;
    font-size: 12px;
  }
  
  .create-btn span, .back-btn span {
    display: none;
  }
  
  .btn-icon {
    font-size: 16px;
  }
}

/* 可见性控制 */
.visible-mobile {
  display: none;
}

.hidden-mobile {
  display: flex;
}

@media (max-width: 768px) {
  .visible-mobile {
    display: flex;
  }
  
  .hidden-mobile {
    display: none;
  }
}
</style>
