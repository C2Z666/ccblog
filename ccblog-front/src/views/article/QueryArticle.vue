<template>
  <div class="query-article-container">
    <!-- 顶部导航栏 -->
    <HeaderBar />
  
    <!-- 搜索栏 - 美化版本 -->
    <div class="search-section enhanced-search-section">
      <div class="search-box modern-search-box">
        <el-input
          v-model="searchQuery"
          placeholder="输入搜索内容..."
          :prefix-icon="Search"
          :suffix-icon="loading ? 'el-icon-loading' : ''"
          @keyup.enter="handleSearch"
          clearable
          class="modern-search-input"
        />
        <button 
          @click="handleSearch" 
          :class="['modern-search-button', { 'searching': loading }]"
          :disabled="loading"
        >
          <span class="search-button-content">
            <el-icon class="search-button-icon" :class="{ 'rotating': loading }">
              <Search />
            </el-icon>
            <span>搜索</span>
          </span>
          <span class="search-pulse"></span>
        </button>
      </div>
      
      <!-- 搜索历史组件 -->
      <div class="search-history-container" v-if="searchHistory.length > 0">
        <div class="search-history-header">
          <span class="history-title">
            <el-icon class="history-icon">
              <el-icon-menu />
            </el-icon>
            搜索历史
          </span>
          <button @click="clearSearchHistory" class="clear-history-btn">
            <el-icon>
              <el-icon-delete />
            </el-icon>
            清空
          </button>
        </div>
        <div class="search-history-list">
          <div 
            v-for="(item, index) in searchHistory" 
            :key="index"
            class="history-item"
            @click="handleHistoryItemClick(item.query)"
            :title="item.query"
          >
            <span class="history-text">{{ truncateText(item.query, 4) }}</span>
            <el-icon class="history-arrow">
              <el-icon-arrow-right />
            </el-icon>
          </div>
        </div>
      </div>
    </div>

    <!-- 搜索结果区域 -->
    <div class="content-container">
      <el-skeleton :loading="loading" animated :throttle="200" class="skeleton-container">
        <template #template>
          <div class="main-content">
            <div class="article-area">
              <div class="section-header mb-4">
                <h2 class="section-title">
                  <i class="el-icon-document"></i> 搜索结果
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
            <div class="article-area">
              <div class="section-header mb-6">
                <h2 class="section-title">
                  <i class="el-icon-document-checked"></i> 搜索结果
                </h2>
                <div class="section-divider"></div>
              </div>

              <!-- 搜索结果列表 -->
              <div v-if="articles.records && articles.records.length > 0" class="article-list-wrapper">
                <div class="articles-grid">
                  <div v-for="article in articles.records" :key="article.articleId" class="article-item">
                    <router-link :to="`/article/detail/${article.articleId}`" class="article-link">
                      <h3 class="article-title" v-html="highlightText(article.title)"></h3>
                      <div class="article-summary" v-html="highlightText(article.summary || '')"></div>
                      <div class="article-meta">
                        <span class="author-info">
                          <img :src="article.authorAvatar || '/favicon.ico'" :alt="article.authorName" class="author-avatar" />
                          {{ article.authorName }}
                        </span>
                        <span class="article-date">{{ formatDate(article.createTime) }}</span>
                        <span class="article-stats">
                          <i class="el-icon-view"></i> {{ article.count.readCnt || 0 }}
                        </span>
                        <span class="article-stats">
                          <i class="el-icon-message"></i> {{ article.count.commentCnt || 0 }}
                        </span>
                      </div>
                    </router-link>
                  </div>
                </div>
              </div>

              <!-- 空状态 -->
              <div v-else-if="!loading" class="empty-state">
                <div class="empty-content">
                  <div class="empty-icon">
                    <i class="el-icon-search"></i>
                  </div>
                  <h3>未找到相关内容</h3>
                  <p class="empty-desc">尝试使用其他关键词进行搜索</p>
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
    <Footer />
  </div>
    <LoginDialog :clicked="loginDialogClicked"></LoginDialog>
  <RegisterDialog :clicked="registerDialogClicked"></RegisterDialog>
</template>

<script setup lang="ts">
// 导入依赖
import { ref, reactive, onMounted, provide } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { doGet } from '@/http/BackendRequests'
import type { CommonResponse } from '@/http/ResponseTypes/CommonResponseType'
import type { BasicPageType } from '@/http/ResponseTypes/PageType/BasicPageType'
import type { ArticleType } from '@/http/ResponseTypes/ArticleType/ArticleType'
import { useGlobalStore } from '@/stores/global'
import HeaderBar from '@/components/layout/HeaderBar.vue'
import Footer from '@/components/layout/Footer.vue'
import { Search, ArrowLeft } from '@element-plus/icons-vue'
import LoginDialog from '@/components/dialog/LoginDialog.vue'
import RegisterDialog from '@/components/dialog/RegisterDialog.vue'
import { SEARCH_ARTICLE_LIST_URL } from '@/http/URL'


const route = useRoute()
const router = useRouter()
const globalStore = useGlobalStore()

// 定义搜索历史项类型
interface SearchHistoryItem {
  query: string
  timestamp: number
}

// 响应式数据
const searchQuery = ref('')
const loading = ref(false)
const currentPage = ref(1)
const totalPage = ref(0)
const pageSize = ref(10)
const articles = reactive<BasicPageType<ArticleType>>({
    records: [],
    total: 0,
    size: 10,
    current: 1,
    pages: 0,
    orders: [],
    optimizeCountSql: false,
    searchCount: false,
    maxLimit: 0,
    countId: ''
})
// 搜索历史数据
const searchHistory = ref<SearchHistoryItem[]>([])

// 从路由参数获取初始搜索词并加载搜索历史
onMounted(() => {
  // 加载本地存储的搜索历史
  loadSearchHistory()
  
  // 处理路由参数中的搜索词
  const query = route.query.query as string
  if (query) {
    searchQuery.value = query
    // 添加到搜索历史，记录第一次点击进来的搜索
    addSearchHistory(query)
    fetchArticles()
  }
})

// 加载搜索历史
const loadSearchHistory = () => {
  const savedHistory = localStorage.getItem('searchHistory')
  if (savedHistory) {
    try {
      searchHistory.value = JSON.parse(savedHistory)
    } catch (error) {
      console.error('Failed to parse search history:', error)
      searchHistory.value = []
    }
  }
}

// 保存搜索历史
const saveSearchHistory = () => {
  localStorage.setItem('searchHistory', JSON.stringify(searchHistory.value))
}

// 添加搜索历史项
const addSearchHistory = (query: string) => {
  if (!query.trim()) return
  
  // 移除重复项（如果存在）
  searchHistory.value = searchHistory.value.filter(item => item.query !== query)
  
  // 添加到历史记录开头
  searchHistory.value.unshift({
    query,
    timestamp: Date.now()
  })
  
  // 限制历史记录数量（最多保存20条）
  if (searchHistory.value.length > 20) {
    searchHistory.value = searchHistory.value.slice(0, 20)
  }
  
  // 保存到本地存储
  saveSearchHistory()
}

// 清空搜索历史
const clearSearchHistory = () => {
  searchHistory.value = []
  localStorage.removeItem('searchHistory')
}

// 截断文本（超过指定长度显示省略号）
const truncateText = (text: string, maxLength: number): string => {
  if (text.length <= maxLength) {
    return text
  }
  return text.substring(0, maxLength) + '...'
}

// 处理点击历史记录项
const handleHistoryItemClick = (query: string) => {
  searchQuery.value = query
  handleSearch()
}

// 搜索文章
const fetchArticles = () => {
  if (!searchQuery.value.trim()) {
    return
  }
  
  loading.value = true
  doGet<CommonResponse>(SEARCH_ARTICLE_LIST_URL, {
    query: searchQuery.value,
    currentPage: currentPage.value,
    pageSize: pageSize.value
  })
    .then((response) => {
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
    })
    .catch((error) => {
      console.error('Failed to fetch query articles:', error)
    })
    .finally(() => {
      loading.value = false
    })
}

// 处理搜索
const handleSearch = () => {
  if (!searchQuery.value.trim()) return
  
  currentPage.value = 1
  
  // 添加到搜索历史
  addSearchHistory(searchQuery.value)
  
  // 直接调用fetchArticles函数发送搜索请求
  fetchArticles()
  
  // 同时更新路由参数，保持URL同步
  router.push({
    path: '/query-article',
    query: { query: searchQuery.value }
  })
}

// 分页操作
const onPageSizeChange = (newPageSize: number) => {
  pageSize.value = newPageSize
  fetchArticles()
}

const onCurrentPageChange = (newCurrentPage: number) => {
  currentPage.value = newCurrentPage
  fetchArticles()
}

// 高亮文本中的搜索关键词
const highlightText = (text: string): string => {
  if (!searchQuery.value || !text) {
    return text || ''
  }
  
  const keywords = searchQuery.value.split(/\s+/).filter(k => k.trim())
  if (keywords.length === 0) {
    return text
  }
  
  const regex = new RegExp(`(${keywords.map(keyword => 
    keyword.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')
  ).join('|')})`, 'gi')
  
  return text.replace(regex, '<span class="highlight">$1</span>')
}

// 格式化日期
const formatDate = (dateString?: string): string => {
  if (!dateString) {
    return ''
  }
  
  const date = new Date(dateString)
  const now = new Date()
  const diff = now.getTime() - date.getTime()
  
  // 小于1分钟
  if (diff < 60000) {
    return '刚刚'
  }
  
  // 小于1小时
  if (diff < 3600000) {
    return Math.floor(diff / 60000) + '分钟前'
  }
  
  // 小于24小时
  if (diff < 86400000) {
    return Math.floor(diff / 3600000) + '小时前'
  }
  
  // 小于30天
  if (diff < 2592000000) {
    return Math.floor(diff / 86400000) + '天前'
  }
  
  // 其他情况显示完整日期
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  
  // 如果是今年，不显示年份
  if (year === now.getFullYear()) {
    return `${month}-${day}`
  }
  
  return `${year}-${month}-${day}`
}

// 返回上一页
const handleBack = () => {
  router.back()
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
/* 全局容器样式 */
.query-article-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding-bottom: 60px;
}

/* 返回按钮美化 */
.back-button-wrapper {
  position: relative;
  z-index: 100;
  margin: 20px auto;
  width: fit-content;
}

.modern-back-button {
  position: relative;
  background: rgba(255, 255, 255, 0.8);
  border: none;
  border-radius: 30px;
  padding: 12px 24px;
  cursor: pointer;
  overflow: hidden;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.modern-back-button:hover {
  transform: translateY(-3px) translateX(-2px);
  box-shadow: 0 6px 25px rgba(0, 0, 0, 0.15);
  background: rgba(255, 255, 255, 0.95);
}

.modern-back-button:active {
  transform: translateY(0);
}

.back-button-content {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #333;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.modern-back-button:hover .back-button-content {
  transform: translateX(-3px);
}

.back-icon {
  font-size: 16px;
  color: #409eff;
  transition: transform 0.3s ease;
}

.modern-back-button:hover .back-icon {
  animation: backIconBounce 0.6s ease;
}

@keyframes backIconBounce {
  0%, 100% { transform: translateX(0); }
  50% { transform: translateX(-5px); }
}

.back-button-ripple {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.4);
  transform: scale(0);
  animation: ripple-animation 0.6s linear;
}

@keyframes ripple-animation {
  to { transform: scale(4); opacity: 0; }
}

/* 搜索区域美化 */
.enhanced-search-section {
  position: relative;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40px 20px;
  margin-top: -20px;
  margin-bottom: 30px;
  border-radius: 0 0 30px 30px;
  box-shadow: 0 10px 30px rgba(102, 126, 234, 0.3);
  overflow: hidden;
}

.enhanced-search-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: 
    radial-gradient(circle at 25% 30%, rgba(255, 255, 255, 0.1) 0%, transparent 50%),
    radial-gradient(circle at 75% 60%, rgba(255, 255, 255, 0.08) 0%, transparent 50%);
  pointer-events: none;
}

.modern-search-box {
  display: flex;
  max-width: 800px;
  margin: 0 auto 20px;
  gap: 16px;
  position: relative;
}

.modern-search-input {
  flex: 1;
  height: 50px;
  border: none;
  border-radius: 25px;
  padding: 0 60px 0 25px;
  font-size: 16px;
  background: rgba(255, 255, 255, 0.95);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.modern-search-input:focus-within {
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.15), 0 0 0 3px rgba(64, 158, 255, 0.2);
  transform: translateY(-2px);
}

.modern-search-button {
  position: relative;
  min-width: 100px;
  height: 50px;
  border: none;
  border-radius: 25px;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.9) 0%, rgba(255, 255, 255, 0.7) 100%);
  color: #409eff;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  overflow: hidden;
  box-shadow: 
    0 4px 15px rgba(0, 0, 0, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.5);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  align-items: center;
  justify-content: center;
}

.modern-search-button:hover {
  transform: translateY(-3px);
  box-shadow: 
    0 6px 20px rgba(0, 0, 0, 0.15),
    inset 0 1px 0 rgba(255, 255, 255, 0.5);
  background: linear-gradient(135deg, rgba(255, 255, 255, 1) 0%, rgba(255, 255, 255, 0.9) 100%);
}

.modern-search-button:active {
  transform: translateY(0);
  box-shadow: 
    0 2px 10px rgba(0, 0, 0, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.5);
}

.search-button-content {
  display: flex;
  align-items: center;
  gap: 8px;
  z-index: 2;
  position: relative;
}

.search-button-icon {
  font-size: 18px;
  transition: transform 0.3s ease;
}

.modern-search-button:hover .search-button-icon {
  transform: scale(1.1) rotate(15deg);
}

.search-pulse {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 0;
  height: 0;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.5);
  transform: translate(-50%, -50%);
  transition: width 0.6s ease, height 0.6s ease;
}

.modern-search-button:hover .search-pulse {
  width: 200px;
  height: 200px;
}

.searching .search-button-icon.rotating {
  animation: rotate 1s linear infinite;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* 搜索历史组件样式 */
.search-history-container {
  max-width: 800px;
  margin: 20px auto 0;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 15px;
  padding: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  animation: slideInFromBottom 0.5s ease-out;
}

.search-history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 2px solid rgba(64, 158, 255, 0.1);
}

.history-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.history-icon {
  color: #409eff;
  font-size: 20px;
}

.clear-history-btn {
  display: flex;
  align-items: center;
  gap: 5px;
  background: none;
  border: none;
  color: #999;
  font-size: 14px;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.clear-history-btn:hover {
  background: rgba(233, 30, 99, 0.1);
  color: #e91e63;
  transform: translateX(2px);
}

.search-history-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.history-item {
  display: flex;
  align-items: center;
  gap: 6px;
  background: linear-gradient(135deg, rgba(64, 158, 255, 0.1) 0%, rgba(102, 177, 255, 0.1) 100%);
  padding: 8px 16px;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid rgba(64, 158, 255, 0.2);
  position: relative;
  overflow: hidden;
}

.history-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.4), transparent);
  transition: left 0.5s ease;
}

.history-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(64, 158, 255, 0.3);
  border-color: rgba(64, 158, 255, 0.4);
  background: linear-gradient(135deg, rgba(64, 158, 255, 0.2) 0%, rgba(102, 177, 255, 0.2) 100%);
}

.history-item:hover::before {
  left: 100%;
}

.history-text {
  font-size: 14px;
  color: #555;
  font-weight: 500;
  white-space: nowrap;
}

.history-arrow {
  font-size: 12px;
  color: #409eff;
  transition: transform 0.3s ease;
}

.history-item:hover .history-arrow {
  transform: translateX(3px) rotate(10deg);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .search-history-container {
    margin: 15px;
    padding: 15px;
  }
  
  .history-title {
    font-size: 14px;
  }
  
  .clear-history-btn {
    font-size: 12px;
  }
  
  .history-item {
    padding: 6px 12px;
  }
  
  .history-text {
    font-size: 13px;
  }
}

@keyframes slideInFromBottom {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
    transform: scale(1);
  }
  50% {
    opacity: 0.7;
    transform: scale(1.1);
  }
}

/* 内容容器修复 - 解决穿透问题 */
.content-container {
  position: relative;
  max-width: 1200px;
  margin: -20px auto 0;
  background: white;
  border-radius: 30px 30px 0 0;
  padding: 40px 30px;
  box-shadow: 0 -5px 30px rgba(0, 0, 0, 0.1);
  z-index: 2;
  animation: slideInUp 0.6s ease-out;
}

@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 文章区域样式增强 */
.section-header {
  position: relative;
  margin-bottom: 40px;
  padding-bottom: 20px;
}

.section-title {
  font-size: 28px;
  font-weight: 700;
  color: #333;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 12px;
}

.section-title i {
  color: #409eff;
  font-size: 32px;
  animation: float 3s ease-in-out infinite;
}

.section-divider {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 80px;
  height: 4px;
  background: linear-gradient(90deg, #409eff, #66b1ff);
  border-radius: 2px;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-5px);
  }
}

/* 文章卡片样式增强 */
.articles-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 30px;
}

.article-item {
  background: #fff;
  border-radius: 16px;
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(229, 233, 240, 0.8);
}

.article-item:hover {
  transform: translateY(-8px);
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
  border-color: rgba(64, 158, 255, 0.2);
}

.article-link {
  display: block;
  padding: 24px;
  color: inherit;
  text-decoration: none;
  transition: color 0.3s ease;
}

.article-link:hover {
  color: #409eff;
}

.article-title {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin: 0 0 16px;
  line-height: 1.4;
  transition: color 0.3s ease;
}

.article-link:hover .article-title {
  color: #409eff;
}

.article-summary {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin: 0 0 20px;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-meta {
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 13px;
  color: #999;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
}

.author-avatar {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid rgba(64, 158, 255, 0.2);
}

.article-stats {
  display: flex;
  align-items: center;
  gap: 4px;
}

.article-stats i {
  font-size: 14px;
}

/* 高亮样式增强 */
:deep(.highlight) {
  background: linear-gradient(120deg, #a8edea 0%, #fed6e3 100%);
  padding: 0 4px;
  border-radius: 4px;
  font-weight: 600;
  color: #333;
  animation: highlightPulse 1s ease;
}

@keyframes highlightPulse {
  0% {
    background: linear-gradient(120deg, #a8edea 0%, #fed6e3 100%);
    transform: scale(1);
  }
  50% {
    background: linear-gradient(120deg, #8ee6e0 0%, #fec8d8 100%);
    transform: scale(1.05);
  }
  100% {
    background: linear-gradient(120deg, #a8edea 0%, #fed6e3 100%);
    transform: scale(1);
  }
}

/* 空状态样式增强 */
.empty-state {
  text-align: center;
  padding: 80px 20px;
  animation: fadeIn 0.8s ease;
}

.empty-content {
  display: inline-block;
  padding: 40px;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.empty-icon {
  width: 80px;
  height: 80px;
  margin: 0 auto 20px;
  background: rgba(64, 158, 255, 0.1);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.empty-icon i {
  font-size: 40px;
  color: #409eff;
  animation: bounce 2s ease-in-out infinite;
}

.empty-content h3 {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin: 0 0 10px;
}

.empty-desc {
  font-size: 14px;
  color: #666;
  margin: 0;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

/* 分页样式增强 */
.pagination-container {
  margin-top: 50px;
  display: flex;
  justify-content: center;
}

:deep(.pagination) {
  & .el-pagination__page {
    transition: all 0.3s ease;
    border-radius: 6px;
    margin: 0 4px;
  }
  
  & .el-pagination__page:hover {
    background: rgba(64, 158, 255, 0.1);
    color: #409eff;
    transform: translateY(-2px);
  }
  
  & .el-pagination__page.is-current {
    background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
    color: white;
    box-shadow: 0 4px 15px rgba(64, 158, 255, 0.3);
    transform: translateY(-2px);
  }
  
  & .el-pagination__prev, & .el-pagination__next {
    transition: all 0.3s ease;
    border-radius: 6px;
  }
  
  & .el-pagination__prev:hover, & .el-pagination__next:hover {
    background: rgba(64, 158, 255, 0.1);
    color: #409eff;
    transform: translateY(-2px);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .back-button-wrapper {
    margin: 15px 20px;
  }
  
  .modern-back-button {
    padding: 10px 20px;
  }
  
  .enhanced-search-section {
    padding: 30px 15px;
    border-radius: 0 0 20px 20px;
  }
  
  .modern-search-box {
    flex-direction: column;
    gap: 12px;
  }
  
  .modern-search-input {
    height: 45px;
    font-size: 14px;
  }
  
  .modern-search-button {
    height: 45px;
    min-width: 100%;
  }
  
  .content-container {
    margin: -15px auto 0;
    padding: 25px 20px;
    border-radius: 20px 20px 0 0;
  }
  
  .section-title {
    font-size: 24px;
  }
  
  .section-title i {
    font-size: 28px;
  }
  
  .articles-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  
  .article-item {
    border-radius: 12px;
  }
  
  .article-link {
    padding: 20px;
  }
  
  .article-title {
    font-size: 18px;
  }
  
  .article-meta {
    flex-wrap: wrap;
    gap: 12px;
  }
}

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
  --highlight-color: #fff2e8;
  --highlight-border: #ffbb96;
}

/* 容器样式 */
.query-article-container {
  min-height: calc(100vh - var(--header-height, 60px) - var(--footer-height, 60px));
  background-color: var(--bg-secondary);
  padding-bottom: 0;
}

/* 返回按钮样式 */
  .back-button-wrapper {
    position: static;
    margin-bottom: 24px;
    z-index: 1000;
    display: flex;
    justify-content: flex-start;
    padding: 0 20px;
  }

  .enhanced-back-button {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    padding: 12px 20px;
    background-color: white;
    border: 2px solid var(--primary-color);
    border-radius: 8px;
    color: var(--primary-color);
    font-size: 14px;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.3s ease;
    box-shadow: 0 4px 12px rgba(64, 158, 255, 0.2);
  }

  .enhanced-back-button:hover {
    background-color: var(--primary-color);
    color: white;
    transform: translateY(-2px);
    box-shadow: 0 6px 20px rgba(64, 158, 255, 0.3);
  }

  .enhanced-back-button:active {
    transform: translateY(0);
    box-shadow: 0 2px 8px rgba(64, 158, 255, 0.25);
  }

  .back-icon {
    width: 20px;
    height: 20px;
    transition: all 0.3s ease;
  }

  .back-text {
    transition: all 0.3s ease;
  }

  /* 响应式设计 */
  @media (max-width: 768px) {
    .back-button-wrapper {
      padding: 0 16px;
      margin-bottom: 20px;
    }

    .enhanced-back-button {
      padding: 10px 16px;
      font-size: 13px;
      gap: 6px;
    }

    .back-icon {
      width: 18px;
      height: 18px;
    }
  }

  @media (max-width: 480px) {
    .back-text {
      display: none;
    }

    .enhanced-back-button {
      width: 44px;
      height: 44px;
      border-radius: 50%;
      padding: 0;
      gap: 0;
      justify-content: center;
      align-items: center;
    }

    .back-icon {
      width: 20px;
      height: 20px;
    }
  }

/* 搜索区域 */
.search-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40px 20px;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.search-box {
  max-width: 600px;
  margin: 0 auto;
  display: flex;
  gap: 12px;
  position: relative;
  z-index: 1;
}

.search-box .el-input {
  flex: 1;
  height: 48px;
}

.search-box .el-input__wrapper {
  height: 48px;
  border-radius: 24px;
  background-color: white;
}

.search-box .el-input__inner {
  height: 48px;
  line-height: 48px;
  font-size: 16px;
  border-radius: 24px;
}

.search-box .el-button {
  height: 48px;
  padding: 0 32px;
  border-radius: 24px;
  font-size: 16px;
  font-weight: 600;
}

.search-info {
  margin-top: 16px;
  color: white;
  font-size: 14px;
  opacity: 0.9;
}

/* 内容容器 */
.content-container {
  max-width: 1200px;
  margin: -20px auto 0;
  padding: 0 20px;
  position: relative;
  z-index: 2;
}

/* 主内容区域 */
.main-content {
  display: grid;
  grid-template-columns: 1fr;
  gap: 30px;
}

/* 文章区域 */
.article-area {
  background-color: var(--bg-primary);
  border-radius: var(--border-radius);
  padding: 36px;
  box-shadow: var(--shadow-light);
  transition: var(--transition-base);
  position: relative;
  overflow: hidden;
  border: 1px solid rgba(229, 233, 240, 0.8);
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
}

/* 骨架屏样式 */
.skeleton-container {
  min-height: 600px;
}

.article-skeleton {
  padding: 20px 0;
  border-bottom: 1px solid #ebeef5;
}

.article-skeleton:last-child {
  border-bottom: none;
}

.skeleton-divider {
  height: 1px;
  background-color: #ebeef5;
  margin-top: 20px;
}

/* 文章列表样式 */
.article-list-wrapper {
  animation: fadeIn 0.7s ease-in;
}

.articles-grid {
  display: grid;
  gap: 24px;
}

.article-item {
  padding: 20px;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  transition: var(--transition-base);
  background-color: white;
}

.article-item:hover {
  border-color: var(--primary-color);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);
  transform: translateY(-2px);
}

.article-link {
  color: inherit;
  text-decoration: none;
  display: block;
}

.article-title {
  font-size: 1.4rem;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 12px 0;
  line-height: 1.5;
  transition: color 0.3s ease;
}

.article-title:hover {
  color: var(--primary-color);
}

.article-summary {
  font-size: 1rem;
  color: var(--text-regular);
  line-height: 1.7;
  margin-bottom: 16px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.article-meta {
  display: flex;
  align-items: center;
  gap: 20px;
  font-size: 0.875rem;
  color: var(--text-secondary);
}

.author-info {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
}

.author-avatar {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  object-fit: cover;
}

.article-stats {
  display: flex;
  align-items: center;
  gap: 4px;
}

.article-stats i {
  font-size: 14px;
}

/* 高亮样式 */
.highlight {
  background-color: var(--highlight-color);
  color: #e6a23c;
  padding: 2px 4px;
  border-radius: 3px;
  font-weight: 500;
  border: 1px solid var(--highlight-border);
}

/* 空状态样式 */
.empty-state {
  text-align: center;
  padding: 80px 20px;
}

.empty-content {
  display: inline-block;
}

.empty-icon {
  font-size: 64px;
  color: #c0c4cc;
  margin-bottom: 24px;
}

.empty-content h3 {
  font-size: 1.5rem;
  color: var(--text-primary);
  margin: 0 0 12px 0;
}

.empty-desc {
  font-size: 1rem;
  color: var(--text-secondary);
  margin: 0;
}

/* 分页样式 */
.pagination-container {
  margin-top: 40px;
  display: flex;
  justify-content: center;
}

.pagination {
  display: flex;
  justify-content: center;
}

/* 动画 */
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

/* 响应式设计 */
@media (max-width: 768px) {
  .search-box {
    flex-direction: column;
  }
  
  .search-box .el-input,
  .search-box .el-button {
    width: 100%;
  }
  
  .content-container {
    padding: 0 16px;
  }
  
  .article-area {
    padding: 24px 16px;
  }
  
  .section-title {
    font-size: 1.5rem;
  }
  
  .article-title {
    font-size: 1.25rem;
  }
  
  .article-meta {
    flex-wrap: wrap;
    gap: 12px;
  }
}
</style>