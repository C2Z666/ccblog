<template>
  <HeaderBar />

  <!-- 正文内容 -->
  <div class="tag-article-container" @scroll="handleScroll">
    <!-- 标签标题区域 - 紧凑且下拉显示 -->
    <div class="tag-header-section collapsed" :class="{ expanded: isExpanded }">
      <div class="decorative-shapes">
        <div class="shape shape-1"></div>
        <div class="shape shape-2"></div>
        <div class="shape shape-3"></div>
      </div>
      
      <!-- 折叠状态显示的简洁内容 -->
      <div class="tag-header-content compact">
        <div class="compact-content">
          <div class="compact-icon-container">
            <i class="el-icon-tags compact-icon"></i>
          </div>
          <h1 class="tag-title compact-title">{{ currentTag }}</h1>
          <div v-if="articles.records && articles.records.length > 0" class="article-count compact-count">
            <span class="count-number">{{ articles.total || 0 }}</span>
            <span class="count-label">篇文章</span>
          </div>
        </div>
        
        <!-- 下拉提示 -->
        <!-- <div class="pull-hint">
          <i class="el-icon-d-arrow-down"></i>
          <span>下拉查看详情</span>
        </div> -->
      </div>
      
      <!-- 展开状态显示的完整内容 -->
      <div class="tag-header-content full">
        <div class="tag-icon-container">
          <i class="el-icon-tags"></i>
        </div>
        <h1 class="tag-title">{{ currentTag }}</h1>
        <p class="tag-description">发现与 <strong>{{ currentTag }}</strong> 相关的全部优质内容</p>
        <div v-if="articles.records && articles.records.length > 0" class="article-count">
          <span>{{ articles.total || 0 }} 篇文章</span>
        </div>
      </div>
    </div>

    <div class="content-container">
      <!-- 文章列表和侧边栏 -->
      <el-skeleton :loading="articlesLoading" animated :throttle="200" class="skeleton-container">
        <template #template>
          <div class="main-content">
            <div class="article-area">
              <div class="section-header mb-4">
                <h2 class="section-title">
                  <i class="el-icon-document"></i> 相关文章
                </h2>
                <div class="section-divider"></div>
              </div>
              <div v-for="(item, id) in 6" :key="id" class="article-skeleton">
                <el-skeleton-item variant="text" style="width: 80%; height: 24px; margin-bottom: 10px" />
                <el-skeleton-item variant="text" style="width: 100%; height: 60px; margin-bottom: 10px" />
                <el-skeleton-item variant="text" style="width: 60%; height: 18px" />
                <div class="skeleton-divider"></div>
              </div>
              <el-skeleton-item variant="text" style="width: 40%; height: 40px; margin: 20px auto" />
            </div>
          </div>
        </template>
        <template #default>
          <div class="main-content">
            <!-- 文章列表区域 -->
            <div class="article-area">
              <div class="section-header mb-6">
                <h2 class="section-title">
                  <i class="el-icon-document-checked"></i> 相关文章
                </h2>
                <div class="section-divider"></div>
              </div>
              
              <!-- 增强的文章列表 -->
              <div v-if="articles.records && articles.records.length > 0" id="articleList" class="article-list-wrapper">
                <div class="articles-grid">
                  <ArticleList :articles="articles.records" enhanced />
                </div>
              </div>
              
              <!-- 增强的空状态 -->
              <div v-else-if="!articlesLoading" class="empty-state">
                <div class="empty-content">
                  <div class="empty-icon">
                    <i class="el-icon-document-remove"></i>
                  </div>
                  <h3>暂无相关文章</h3>
                  <p class="empty-desc">我们还没有找到与该标签相关的内容</p>
                  <el-button type="primary" link @click="$router.push('/')">返回首页</el-button>
                </div>
              </div>

              <!-- 分页组件 -->
              <div v-if="articles.records && articles.records.length > 0" class="pagination-container">
                <el-pagination 
                  :page-sizes="[10, 20]" 
                  hide-on-single-page 
                  v-model:current-page="currentPage" 
                  v-model:page-size="pageSize" 
                  layout="sizes, prev, pager, next, jumper, total" 
                  :page-count="totalPage" 
                  :total="articles.total || 0"
                  :default-current-page="1"
                  @update:page-size="onPageSizeChange" 
                  @update:current-page="onCurrentPageChange"
                  class="pagination"
                />
              </div>
            </div>
          </div>
        </template>
      </el-skeleton>
    </div>

    <!-- 底部信息 -->
    <Footer class="custom-footer"></Footer>
  </div>
  <LoginDialog :clicked="loginDialogClicked"></LoginDialog>
  <RegisterDialog :clicked="registerDialogClicked"></RegisterDialog>
</template>

<script setup lang="ts">
import HeaderBar from '@/components/layout/HeaderBar.vue'
import { onMounted, provide, reactive, ref, watch } from 'vue'
import { doGet } from '@/http/BackendRequests'
import { TAG_ARTICLE_LIST_URL } from '@/http/URL'
import { type CommonResponse } from '@/http/ResponseTypes/CommonResponseType'
import ArticleList from '@/views/home/article/ArticleList.vue'
import Footer from '@/components/layout/Footer.vue'
import { useGlobalStore } from '@/stores/global'
import { type BasicPageType, defaultBasicPage } from '@/http/ResponseTypes/PageType/BasicPageType'
import type { ArticleType } from '@/http/ResponseTypes/ArticleType/ArticleType'
import { useRoute } from 'vue-router'
import LoginDialog from '@/components/dialog/LoginDialog.vue'
import RegisterDialog from '@/components/dialog/RegisterDialog.vue'

const globalStore = useGlobalStore()
const route = useRoute()

// 响应式数据
let articles = reactive<BasicPageType<ArticleType>>({...defaultBasicPage})
const currentPage = ref(1)
const totalPage = ref(0)
const pageSize = ref(10)
const articlesLoading = ref(true)
const currentTag = ref('')
const tagId = ref(1) // 初始化为1，后续从路由参数获取
const isExpanded = ref(false)
const startY = ref(0)
const pullDistance = ref(0)
const minPullDistance = 100 // 最小下拉距离，需要明显下拉才会触发展开

// 获取文章列表
const fetchArticles = () => {
  doGet<CommonResponse>(TAG_ARTICLE_LIST_URL, {
    tagId: tagId.value,
    currentPage: currentPage.value,
    pageSize: pageSize.value
  })
    .then((response) => {
      // console.log('Tag articles response:', response)
      if (response.data && response.data.result) {
        // 更新全局状态
        if (response.data.global) {
          globalStore.setGlobal(response.data.global)
        }
        // 更新文章数据
        Object.assign(articles, response.data.result)
        totalPage.value = Number(response.data.result.pages || 0)
        currentPage.value = Number(response.data.result.current || 1)
      }
      // 取消骨架屏显示
      articlesLoading.value = false
    })
    .catch((error) => {
      console.error('Failed to fetch tag articles:', error)
      articlesLoading.value = false
    })
}

// 监听路由参数变化
watch(() => route.query.tag, (newTag) => {
  if (newTag) {
    currentTag.value = newTag as string
    // 重置页码并重新获取文章
    currentPage.value = 1
    articlesLoading.value = true
    fetchArticles()
  }
}, { immediate: true })

// 分页操作
const onPageSizeChange = (newPageSize: number) => {
  pageSize.value = newPageSize
  fetchArticles()
}

const onCurrentPageChange = (newCurrentPage: number) => {
  currentPage.value = newCurrentPage
  fetchArticles()
}

// 页面加载时初始化
onMounted(() => {
  // 初始化时如果路由中有tag参数，则会通过watch触发fetchArticles
  // 这里可以添加其他初始化逻辑
  
  // 从路由参数获取tagId和tag名称
  const routeTagId = route.query.tagId
  const routeTag = route.query.tag
  
  if (routeTagId) {
    tagId.value = Number(routeTagId)
  }
  
  if (routeTag) {
    currentTag.value = String(routeTag)
  }
  
  // 获取文章列表
  fetchArticles()
  
  // 监听触摸事件来实现下拉展开
  const container = document.querySelector('.tag-header-section')
  if (container) {
    // container.addEventListener('touchstart', handleTouchStart)
    // container.addEventListener('touchmove', handleTouchMove)
    container.addEventListener('touchend', handleTouchEnd)
  }
})

// 清理事件监听器
const cleanup = () => {
  document.removeEventListener('touchstart', handleTouchStart)
  document.removeEventListener('touchmove', handleTouchMove)
  document.removeEventListener('touchend', handleTouchEnd)
}

// 滚动处理函数
const handleScroll = (event: Event) => {
  const container = event.target as HTMLElement
  if (container.scrollTop > 50 && isExpanded.value) {
    isExpanded.value = false
  }
}

// 触摸开始事件
const handleTouchStart = (event: TouchEvent) => {
  // 只有在页面顶部才能触发下拉展开
  if (window.scrollY === 0) {
    startY.value = event.touches[0].clientY
    pullDistance.value = 0
  }
}

// 触摸移动事件
const handleTouchMove = (event: TouchEvent) => {
  if (startY.value > 0 && window.scrollY === 0) {
    const currentY = event.touches[0].clientY
    const diff = currentY - startY.value
    
    // 只处理下拉操作
    if (diff > 0) {
      pullDistance.value = diff
      event.preventDefault() // 阻止页面滚动
    }
  }
}

// 触摸结束事件
const handleTouchEnd = () => {
  // 只有下拉距离超过阈值才会展开
  if (pullDistance.value >= minPullDistance) {
    isExpanded.value = true
  }
  
  // 重置状态
  startY.value = 0
  pullDistance.value = 0
}

// 登录框
const changeClicked = () => {
  loginDialogClicked.value = !loginDialogClicked.value
  // console.log("clicked: ", loginDialogClicked.value)
}

provide('loginDialogClicked', changeClicked)
const loginDialogClicked = ref(false)

// 注册框
const changeRegisterClicked = () => {
  registerDialogClicked.value = !registerDialogClicked.value
  // console.log("clicked: ", registerDialogClicked.value)
}

provide('registerDialogClicked', changeRegisterClicked)
const registerDialogClicked = ref(false)
</script>

<style scoped>
/* 变量定义 */
:root {
  --primary-color: #409eff;
  --secondary-color: #67c23a;
  --accent-color: #f7ba1e;
  --text-primary: #303133;
  --text-regular: #606266;
  --text-secondary: #909399;
  --bg-primary: #ffffff;
  --bg-secondary: #f5f7fa;
  --shadow-light: 0 2px 12px rgba(0, 0, 0, 0.06);
  --shadow-medium: 0 4px 16px rgba(0, 0, 0, 0.08);
  --shadow-hover: 0 8px 24px rgba(0, 0, 0, 0.12);
  --border-radius: 12px;
  --transition-base: all 0.3s ease;
}

/* 标签文章容器样式 */
.tag-article-container {
  min-height: calc(100vh - var(--header-height, 60px) - var(--footer-height, 60px));
  background-color: var(--bg-secondary);
}

/* 标签标题区域 - 紧凑且下拉显示 */
.tag-header-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  text-align: center;
  position: relative;
  overflow: hidden;
  color: white;
  transition: all 0.3s ease;
}

/* 折叠状态 - 高度较小 */
.tag-header-section.collapsed {
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 展开状态 */
.tag-header-section.expanded {
  height: 280px;
  padding: 40px 0;
}

/* 紧凑模式内容 */
.tag-header-content.compact {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  transition: opacity 0.3s ease, transform 0.3s ease;
}

.tag-header-section.expanded .tag-header-content.compact {
  opacity: 0;
  pointer-events: none;
  position: absolute;
  transform: translateY(-20px);
}

.compact-content {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 12px 24px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(12px);
  border-radius: 30px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.compact-content:hover {
  transform: translateY(-3px);
  background: rgba(255, 255, 255, 0.15);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

/* 图标容器 */
.compact-icon-container {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(8px);
  border: 2px solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.compact-icon-container::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(
    135deg,
    rgba(255, 255, 255, 0) 0%,
    rgba(255, 255, 255, 0.1) 50%,
    rgba(255, 255, 255, 0) 100%
  );
  transform: rotate(45deg);
  animation: shimmer 3s infinite;
}

@keyframes shimmer {
  0% {
    transform: translateX(-100%) rotate(45deg);
  }
  100% {
    transform: translateX(100%) rotate(45deg);
  }
}

.compact-icon {
  font-size: 24px;
  color: white;
  position: relative;
  z-index: 1;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

/* 标题样式 */
.tag-title.compact-title {
  font-size: 1.75rem;
  margin: 0;
  font-weight: 700;
  color: white;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  position: relative;
  overflow: hidden;
}

.tag-title.compact-title::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 3px;
  background: linear-gradient(90deg, rgba(255,255,255,0) 0%, rgba(255,255,255,0.6) 50%, rgba(255,255,255,0) 100%);
  transform: scaleX(0);
  transition: transform 0.3s ease;
}

.compact-content:hover .tag-title.compact-title::after {
  transform: scaleX(1);
}

/* 计数框样式 */
.compact-count {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.25);
  backdrop-filter: blur(10px);
  border-radius: 50px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s ease;
  font-weight: 600;
}

.compact-count:hover {
  background: rgba(255, 255, 255, 0.35);
  transform: scale(1.05);
}

.count-number {
  font-size: 1.1rem;
  color: white;
  font-weight: 700;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
}

.count-label {
  font-size: 0.9rem;
  color: rgba(255, 255, 255, 0.9);
  font-weight: 500;
}

/* 下拉提示 */
.pull-hint {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 8px;
  font-size: 0.9rem;
  opacity: 0.8;
  animation: bounce 2s infinite;
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(-5px);
  }
  60% {
    transform: translateY(-3px);
  }
}

/* 完整内容 - 默认隐藏 */
.tag-header-content.full {
  opacity: 0;
  pointer-events: none;
  transition: opacity 0.4s ease, transform 0.4s ease;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -40%);
  width: 100%;
}

/* 展开时显示完整内容 */
.tag-header-section.expanded .tag-header-content.full {
  opacity: 1;
  pointer-events: auto;
  position: static;
  transform: none;
  padding-top: 30px;
  animation: fadeInUp 0.6s ease-out;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 装饰性形状 */
.decorative-shapes {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: 0;
}

.shape {
  position: absolute;
  border-radius: 50%;
  opacity: 0.15;
  animation: float 15s infinite ease-in-out;
}

.shape-1 {
  width: 300px;
  height: 300px;
  background-color: rgba(255, 255, 255, 0.3);
  top: -150px;
  left: 10%;
  animation-delay: 0s;
}

.shape-2 {
  width: 200px;
  height: 200px;
  background-color: rgba(255, 255, 255, 0.2);
  bottom: -100px;
  right: 15%;
  animation-delay: 2s;
  animation-direction: reverse;
}

.shape-3 {
  width: 150px;
  height: 150px;
  background-color: rgba(255, 255, 255, 0.25);
  top: 30%;
  right: 25%;
  animation-delay: 5s;
}

/* 浮动动画 */
@keyframes float {
  0%, 100% {
    transform: translateY(0) translateX(0);
  }
  25% {
    transform: translateY(-20px) translateX(10px);
  }
  50% {
    transform: translateY(0) translateX(-10px);
  }
  75% {
    transform: translateY(20px) translateX(5px);
  }
}

.tag-header-content {
  position: relative;
  z-index: 1;
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px;
  animation: slideUp 0.8s ease-out;
}

/* 标签图标容器 */
.tag-icon-container {
  width: 80px;
  height: 80px;
  margin: 0 auto 24px;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.15);
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(10px);
  border: 2px solid rgba(255, 255, 255, 0.2);
  transition: var(--transition-base);
}

.tag-icon-container:hover {
  transform: scale(1.05);
  background-color: rgba(255, 255, 255, 0.2);
}

.tag-icon-container i {
  font-size: 36px;
  color: white;
}

.tag-title {
  font-size: 3rem;
  font-weight: 800;
  color: white;
  margin: 0 0 20px 0;
  letter-spacing: -0.5px;
  text-shadow: 
    0 2px 4px rgba(0, 0, 0, 0.2),
    0 0 20px rgba(255, 255, 255, 0.3);
  position: relative;
  display: inline-block;
}

/* 标题下的装饰线 */
.tag-title::after {
  content: '';
  position: absolute;
  bottom: -8px;
  left: 50%;
  transform: translateX(-50%);
  width: 100px;
  height: 4px;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.8), transparent);
  border-radius: 2px;
}

.tag-description {
  font-size: 1.35rem;
  color: rgba(255, 255, 255, 0.9);
  margin: 0 0 24px 0;
  line-height: 1.7;
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
  position: relative;
  padding: 0 10px;
}

.tag-description strong {
  color: white;
  font-weight: 700;
  position: relative;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
}

.tag-description strong::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, transparent, var(--accent-color), transparent);
  transform: scaleX(0);
  transition: transform 0.4s ease;
  border-radius: 1.5px;
}

.tag-description:hover strong::after {
  transform: scaleX(1);
}

/* 文章计数 */
.article-count {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  background: rgba(255, 255, 255, 0.15);
  padding: 12px 20px;
  border-radius: 50px;
  font-size: 1.1rem;
  font-weight: 600;
  backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
  position: relative;
  overflow: hidden;
  color: white;
}

.article-count::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg,
    rgba(255, 255, 255, 0) 0%,
    rgba(255, 255, 255, 0.2) 50%,
    rgba(255, 255, 255, 0) 100%
  );
  transition: left 0.6s ease;
}

.article-count:hover::before {
  left: 100%;
}

.article-count:hover {
  background: rgba(255, 255, 255, 0.25);
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.article-count span {
  position: relative;
  z-index: 1;
}

/* 内容容器 */
.content-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px 60px;
  position: relative;
  z-index: 2;
  margin-top: 0;
}

/* 主内容区域 */
.main-content {
  display: grid;
  grid-template-columns: 1fr;
  gap: 30px;
}

/* 文章列表区域 */
.article-area {
  background-color: var(--bg-primary);
  border-radius: var(--border-radius);
  padding: 36px;
  box-shadow: var(--shadow-light);
  transition: var(--transition-base);
  position: relative;
  overflow: hidden;
  border: 1px solid rgba(229, 233, 240, 0.8);
  border-top: none; /* 移除顶部边框 */
  margin-top: 16px; /* 添加顶部间距，确保与蓝色背景不重叠 */
}

.article-area::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background: linear-gradient(to bottom, var(--primary-color), var(--secondary-color));
}

.article-area:hover {
  box-shadow: var(--shadow-hover);
  transform: translateY(-2px);
}

.article-list-wrapper {
  animation: fadeIn 0.7s ease-in;
}

/* 文章网格布局 */
.articles-grid {
  display: grid;
  gap: 24px;
}

/* 分类标题样式 */
.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 32px;
  position: relative;
}

.section-title {
  font-size: 1.75rem;
  font-weight: 600;
  color: var(--text-primary);
  margin-right: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
  position: relative;
}

.section-title::before {
  content: '';
  position: absolute;
  bottom: -8px;
  left: 0;
  width: 60%;
  height: 3px;
  background: linear-gradient(to right, var(--primary-color), transparent);
  border-radius: 3px;
}

.section-title i {
  color: var(--primary-color);
  font-size: 1.25em;
}

.section-divider {
  flex: 1;
  height: 1px;
  background: linear-gradient(to right, var(--text-secondary), transparent);
  opacity: 0.3;
}

/* 分页容器 */
.pagination-container {
  margin-top: 40px;
  display: flex;
  justify-content: center;
}

.pagination {
  display: inline-flex;
  background: var(--bg-primary);
  border-radius: 8px;
  overflow: hidden;
  box-shadow: var(--shadow-light);
  padding: 8px;
}

:deep(.el-pagination) {
  --el-pagination-item-active-bg-color: var(--primary-color);
  --el-pagination-item-bg-color: transparent;
  --el-pagination-button-hover-bg-color: rgba(64, 158, 255, 0.1);
}

/* 增强的空状态 */
.empty-state {
  padding: 80px 0;
  text-align: center;
}

.empty-content {
  max-width: 400px;
  margin: 0 auto;
}

.empty-icon {
  width: 100px;
  height: 100px;
  margin: 0 auto 24px;
  border-radius: 50%;
  background-color: rgba(245, 247, 250, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px dashed var(--text-secondary);
  opacity: 0.6;
}

.empty-icon i {
  font-size: 48px;
  color: var(--text-secondary);
}

.empty-content h3 {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 12px 0;
}

.empty-desc {
  font-size: 1rem;
  color: var(--text-secondary);
  margin: 0 0 24px 0;
  line-height: 1.5;
}

/* 骨架屏样式 */
.skeleton-container {
  width: 100%;
}

.article-skeleton {
  margin-bottom: 24px;
  padding-bottom: 24px;
  border-bottom: 1px solid #f0f0f0;
}

.skeleton-divider {
  height: 1px;
  background-color: #f0f0f0;
  margin-top: 16px;
}

/* 动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  /* 折叠状态 - 更小高度 */
  .tag-header-section.collapsed {
    height: 80px;
  }
  
  /* 展开状态 */
  .tag-header-section.expanded {
    height: 240px;
    padding: 30px 0;
  }
  
  .compact-content {
    gap: 8px;
  }
  
  .compact-icon {
    font-size: 20px;
  }
  
  .tag-title.compact-title {
    font-size: 1.25rem;
  }
  
  .tag-icon-container {
    width: 60px;
    height: 60px;
    margin-bottom: 16px;
  }
  
  .tag-icon-container i {
    font-size: 28px;
  }
  
  .tag-title {
    font-size: 2rem;
  }
  
  .tag-description {
    font-size: 1.1rem;
  }
  
  .content-container {
    padding: 0 15px 40px;
    margin-top: -15px;
  }
  
  /* 展开状态时调整内容间距 */
  .tag-header-section.expanded + .content-container {
    margin-top: -30px;
  }
  
  .article-area {
    padding: 24px 20px;
  }
  
  .section-title {
    font-size: 1.5rem;
  }
  
  .section-header {
    margin-bottom: 24px;
  }
  
  .shape-1 {
    width: 200px;
    height: 200px;
    top: -100px;
  }
  
  .shape-2 {
    width: 150px;
    height: 150px;
    bottom: -75px;
  }
  
  .shape-3 {
    width: 100px;
    height: 100px;
  }
}

@media (max-width: 480px) {
  .tag-title {
    font-size: 2rem;
  }
  
  .article-area {
    padding: 20px 16px;
  }
  
  .pagination {
    padding: 4px;
  }
}
</style>