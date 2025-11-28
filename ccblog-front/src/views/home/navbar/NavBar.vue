<template>
  <div class="category-nav-wrapper">
    <!-- 类别导航栏 - 固定高度，可滚动类别 -->
    <div class="category-container">
      <!-- 固定的导航头部 -->
      <div class="category-header">
        <!-- 可滚动的类别内容区域 -->
        <div class="category-scroll-wrapper">
          <div class="category-list">
            <div 
              v-for="(subCategory, index) in categories" 
              :key="subCategory.categoryId"
              class="category-item-wrapper"
              :data-category-index="index"
            >
              <router-link 
                :to="{ path: '/', query: { category: subCategory.category } }" 
                class="category-item"
                :class="{'category-item-active': subCategory.category === activeCategory}"
                @mouseenter="onCategoryHover(index)"
                @mouseleave="onCategoryLeave(index)"
              >
                <span class="category-text">{{subCategory.category}}</span>
                <span class="category-highlight"></span>
              </router-link>
            </div>
          </div>
        </div>
        <!-- 固定的搜索按钮 -->
        <button 
          @click="ifSearchActive = true"
          class="category-search-button"
          aria-label="搜索"
          :class="{ 'search-button-active': ifSearchActive }"
        >
          <el-icon class="search-icon">
            <Search />
          </el-icon>
        </button>
      </div>
    </div>
    
    <!-- 美化后的搜索框 - 修改为覆盖模式 -->
    <transition name="search-fade">
      <div v-if="ifSearchActive" class="search-container search-overlay">
        <div class="search-wrapper">
          <div class="search-input-container">
            <div class="search-icon-left">
              <el-icon size="20">
                <Search />
              </el-icon>
            </div>
            <el-input
              v-model="searchInput"
              placeholder="搜索文章、教程、标签..."
              @keyup.enter="handleSearch"
              clearable
              class="search-input"
              :autofocus="ifSearchActive"
            />
            <el-button 
              @click="handleSearch"
              class="search-button"
              :loading="loading"
              icon="el-icon-search"
            >
              <template #default>
                <div class="search-button-content">
                  <div class="search-button-icon">
                    <el-icon size="18">
                      <Search />
                    </el-icon>
                  </div>
                  <span>搜索</span>
                </div>
              </template>
            </el-button>
          </div>
          
          <el-button 
            class="search-close-button"
            circle
            @click="ifSearchActive = false"
            :class="{ 'search-close-button-active': ifSearchActive }"
          >
            <el-icon size="20"><Close /></el-icon>
          </el-button>
        </div>
        
        <!-- 搜索建议或历史记录区域 - 预留接口 -->
        <div v-if="searchSuggestions.length > 0" class="search-suggestions">
          <div v-for="(suggestion, index) in searchSuggestions" :key="index" 
               class="search-suggestion-item" 
               @click="selectSuggestion(suggestion)">
            <div class="suggestion-icon-wrapper">
              <el-icon size="16">
                <Search />
              </el-icon>
            </div>
            <span>{{ suggestion }}</span>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup lang="ts">

import { onMounted, onUnmounted, ref, watch, nextTick, reactive } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Close, Search } from '@element-plus/icons-vue'
import { doGet } from '@/http/BackendRequests'
import type { CommonResponse } from '@/http/ResponseTypes/CommonResponseType'
import type { ArticleCategoryType } from '@/http/ResponseTypes/CategoryType/ArticleCategoryType'

const route = useRoute()
const router = useRouter()

const ifSearchActive = ref(false)
const searchSuggestions = ref<string[]>([])
const props = defineProps<{
  categories: ArticleCategoryType[]
}>()

const activeCategory = ref('全部')
const hoveredCategories = reactive<{[key: number]: boolean}>({})

onMounted(() => {
  activeCategory.value = route.query.category as string || '全部'
})

// 监听路由参数变化
watch(() => route.query.category, (newCategory) => {
  // console.log('NavBar分类参数变化:', newCategory)
  activeCategory.value = newCategory as string || '全部'
})

// 监听路由参数变化
watch(() => route.query.category, (newCategory) => {
  activeCategory.value = newCategory as string || '全部'
})

// ==================== 搜索输入框 ====================
const loading = ref(false)
const searchInput = ref('')
const keyWord = ref('')
interface ListItem {
  column?: string,
  columnId?: number,
  id: number,
  readType?: string,
  sort?: string,
  title: string,
}
const options = ref<ListItem[]>([])

// 处理搜索
const handleSearch = () => {
  if (searchInput.value.trim()) {
    // 使用router.push代替window.location.href，保留登录状态
          router.push({
            path: '/query-article',
            query: { query: encodeURIComponent(searchInput.value.trim()) }
          })
          
          // 修改后
          router.push({
            path: '/query-article',
            query: { query: searchInput.value.trim() }
          })
    
    // 搜索后重置状态
    ifSearchActive.value = false
  }
}

// 选择搜索建议
const selectSuggestion = (suggestion: string) => {
  searchInput.value = suggestion
  handleSearch()
}

// 当搜索框激活时，自动聚焦
watch(ifSearchActive, async (newVal) => {
  if (newVal) {
    await nextTick()
    const inputElement = document.querySelector('.search-input input') as HTMLInputElement
    if (inputElement) {
      inputElement.focus()
    }
  }
})

// 类别悬停处理函数 - 用于高级动画效果
const onCategoryHover = (index: number) => {
  hoveredCategories[index] = true;
  
  // 为当前悬停的类别添加缩放动画
  const wrapper = document.querySelector(`.category-item-wrapper[data-category-index="${index}"]`);
  if (wrapper) {
    (wrapper as HTMLElement).style.transform = 'scale(1.02)';
    (wrapper as HTMLElement).style.transition = 'transform 0.3s ease';
  }
};

// 类别离开悬停处理函数
const onCategoryLeave = (index: number) => {
  hoveredCategories[index] = false;
  
  // 重置类别缩放
  const wrapper = document.querySelector(`.category-item-wrapper[data-category-index="${index}"]`);
  if (wrapper) {
    (wrapper as HTMLElement).style.transform = 'scale(1)';
  }
}

// 鼠标滚轮横向滚动支持
const handleWheelScroll = (event: WheelEvent) => {
  const scrollWrapper = document.querySelector('.category-scroll-wrapper') as HTMLElement;
  if (scrollWrapper) {
    // 阻止默认的纵向滚动
    event.preventDefault();
    // 实现横向滚动
    scrollWrapper.scrollLeft += event.deltaY;
  }
}

// 添加鼠标滚轮事件监听
onMounted(() => {
  activeCategory.value = route.query.category as string || '全部';
  
  // 为滚动区域添加鼠标滚轮事件监听
  const scrollWrapper = document.querySelector('.category-scroll-wrapper') as HTMLElement;
  if (scrollWrapper) {
    scrollWrapper.addEventListener('wheel', handleWheelScroll, { passive: false });
    
    // 添加触摸设备支持
    let startX: number, scrollLeft: number;
    const handleTouchStart = (e: TouchEvent) => {
      scrollWrapper.style.scrollBehavior = 'auto'; // 禁用平滑滚动以提高触摸性能
      startX = e.touches[0].pageX - scrollWrapper.offsetLeft;
      scrollLeft = scrollWrapper.scrollLeft;
    };
    
    const handleTouchMove = (e: TouchEvent) => {
      if (!startX) return;
      e.preventDefault();
      const x = e.touches[0].pageX - scrollWrapper.offsetLeft;
      const walk = (x - startX) * 2; // 滚动速度
      scrollWrapper.scrollLeft = scrollLeft - walk;
    };
    
    const handleTouchEnd = () => {
      startX = 0;
      scrollWrapper.style.scrollBehavior = 'smooth'; // 恢复平滑滚动
    };
    
    scrollWrapper.addEventListener('touchstart', handleTouchStart);
    scrollWrapper.addEventListener('touchmove', handleTouchMove, { passive: false });
    scrollWrapper.addEventListener('touchend', handleTouchEnd);
    
    // 清理事件监听器
    onUnmounted(() => {
      scrollWrapper.removeEventListener('wheel', handleWheelScroll);
      scrollWrapper.removeEventListener('touchstart', handleTouchStart);
      scrollWrapper.removeEventListener('touchmove', handleTouchMove);
      scrollWrapper.removeEventListener('touchend', handleTouchEnd);
    });
  }
})
</script>

<style scoped>
:root {
  --primary-color: #409eff;
  --primary-hover: #66b1ff;
  --text-regular: #606266;
  --text-light: #909399;
  --bg-white: #ffffff;
  --border-radius: 25px;
  --shadow-light: 0 2px 12px 0 rgba(0, 0, 0, 0.08);
  --shadow-medium: 0 4px 16px rgba(0, 0, 0, 0.12);
  --transition-fast: all 0.2s ease;
  --transition-normal: all 0.3s ease;
  --transition-slow: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 高级动画的类别导航样式 */
.category-nav-wrapper {
  width: 100%;
  margin-bottom: 16px;
  position: relative;
}

.category-container {
  background-color: var(--bg-white);
  border-radius: 12px;
  box-shadow: var(--shadow-light);
  border: 1px solid rgba(224, 224, 224, 0.6);
  overflow: hidden;
}

.category-header {
  display: flex;
  align-items: center;
  min-height: 64px;
  padding: 8px 16px;
  position: relative;
}

/* 可滚动的类别包装器 */
.category-scroll-wrapper {
  flex: 1;
  overflow-x: auto;
  overflow-y: hidden;
  scrollbar-width: thin;
  scrollbar-color: rgba(153, 153, 153, 0.3) transparent;
  -ms-overflow-style: -ms-autohiding-scrollbar;
  padding: 4px 0;
  min-width: 0;
  scroll-behavior: smooth;
  cursor: grab; /* 显示可抓取的光标 */
}

.category-scroll-wrapper:active {
  cursor: grabbing; /* 显示正在抓取的光标 */
}

/* 自定义滚动条 */
.category-scroll-wrapper::-webkit-scrollbar {
  height: 6px;
}

.category-scroll-wrapper::-webkit-scrollbar-track {
  background: transparent;
  border-radius: 3px;
}

.category-scroll-wrapper::-webkit-scrollbar-thumb {
  background: rgba(153, 153, 153, 0.3);
  border-radius: 3px;
  transition: background 0.3s ease;
}

.category-scroll-wrapper::-webkit-scrollbar-thumb:hover {
  background: rgba(153, 153, 153, 0.5);
}

/* 类别列表 */
.category-list {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 4px 0;
  margin: 0;
  white-space: nowrap;
  min-width: max-content;
  transition: transform 0.3s ease;
}

/* 类别项包装器 */
.category-item-wrapper {
  position: relative;
  flex-shrink: 0;
}

/* 类别项 - 固定大小，高级动画效果 */
.category-item {
  position: relative;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 110px;
  height: 44px;
  padding: 0 16px;
  color: var(--text-regular);
  text-decoration: none;
  border-radius: 22px;
  font-size: 14px;
  font-weight: 500;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  background-color: rgba(245, 247, 250, 0.8);
  border: 1px solid transparent;
  transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
  will-change: transform, background-color, box-shadow, color;
}

/* 类别高亮元素 - 用于高级动画效果 */
.category-highlight {
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.6s ease;
  pointer-events: none;
}

/* 悬停效果 */
.category-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  background-color: rgba(240, 242, 245, 1);
}

.category-item:hover .category-highlight {
  left: 100%;
}

/* 激活状态 - 保持每个类别的特色色彩 */
.category-item-active {
  color: white;
  font-weight: 600;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  border-color: transparent;
  transform: translateY(-2px);
}

.category-item-active:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.2);
}

/* 为每个类别设置激活状态的特色颜色 - 循环颜色方案 */
.category-item-wrapper {
  --category-colors: 
    #2563eb, #10b981, #f59e0b, #8b5cf6, #ec4899, 
    #06b6d4, #6366f1, #ef4444, #14b8a6, #f97316,
    #84cc16, #d946ef;
  
  /* 使用CSS变量和calc函数实现循环颜色 */
  --color-index: calc((var(--i) - 1) % 12);
}

/* 设置每个元素的索引变量 */
.category-item-wrapper:nth-child(1) { --i: 1; }
.category-item-wrapper:nth-child(2) { --i: 2; }
.category-item-wrapper:nth-child(3) { --i: 3; }
.category-item-wrapper:nth-child(4) { --i: 4; }
.category-item-wrapper:nth-child(5) { --i: 5; }
.category-item-wrapper:nth-child(6) { --i: 6; }
.category-item-wrapper:nth-child(7) { --i: 7; }
.category-item-wrapper:nth-child(8) { --i: 8; }
.category-item-wrapper:nth-child(9) { --i: 9; }
.category-item-wrapper:nth-child(10) { --i: 10; }
.category-item-wrapper:nth-child(11) { --i: 11; }
.category-item-wrapper:nth-child(12) { --i: 12; }

/* 对于12个以上的类别，使用循环索引 */
.category-item-wrapper:nth-child(n+13) {
  --i: calc((n) % 12 + 1);
}

/* 为激活状态设置背景色 */
.category-item-wrapper .category-item-active {
  /* 使用HSL颜色空间实现循环颜色，与现有颜色方案保持一致 */
  background-color: hsl(calc(30 * (var(--i, 1) - 1)), 70%, 50%);
}

/* 为不同类别设置不同的颜色方案 */
.category-item-wrapper:nth-child(1) .category-item:not(.category-item-active) {
  color: #2563eb;
  background-color: rgba(37, 99, 235, 0.08);
  border-color: rgba(37, 99, 235, 0.2);
}

.category-item-wrapper:nth-child(1) .category-item:not(.category-item-active):hover {
  background-color: rgba(37, 99, 235, 0.15);
  box-shadow: 0 4px 12px rgba(37, 99, 235, 0.15);
}

.category-item-wrapper:nth-child(2) .category-item:not(.category-item-active) {
  color: #10b981;
  background-color: rgba(16, 185, 129, 0.08);
  border-color: rgba(16, 185, 129, 0.2);
}

.category-item-wrapper:nth-child(2) .category-item:not(.category-item-active):hover {
  background-color: rgba(16, 185, 129, 0.15);
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.15);
}

.category-item-wrapper:nth-child(3) .category-item:not(.category-item-active) {
  color: #f59e0b;
  background-color: rgba(245, 158, 11, 0.08);
  border-color: rgba(245, 158, 11, 0.2);
}

.category-item-wrapper:nth-child(3) .category-item:not(.category-item-active):hover {
  background-color: rgba(245, 158, 11, 0.15);
  box-shadow: 0 4px 12px rgba(245, 158, 11, 0.15);
}

.category-item-wrapper:nth-child(4) .category-item:not(.category-item-active) {
  color: #8b5cf6;
  background-color: rgba(139, 92, 246, 0.08);
  border-color: rgba(139, 92, 246, 0.2);
}

.category-item-wrapper:nth-child(4) .category-item:not(.category-item-active):hover {
  background-color: rgba(139, 92, 246, 0.15);
  box-shadow: 0 4px 12px rgba(139, 92, 246, 0.15);
}

.category-item-wrapper:nth-child(5) .category-item:not(.category-item-active) {
  color: #ec4899;
  background-color: rgba(236, 72, 153, 0.08);
  border-color: rgba(236, 72, 153, 0.2);
}

.category-item-wrapper:nth-child(5) .category-item:not(.category-item-active):hover {
  background-color: rgba(236, 72, 153, 0.15);
  box-shadow: 0 4px 12px rgba(236, 72, 153, 0.15);
}

.category-item-wrapper:nth-child(6) .category-item:not(.category-item-active) {
  color: #06b6d4;
  background-color: rgba(6, 182, 212, 0.08);
  border-color: rgba(6, 182, 212, 0.2);
}

.category-item-wrapper:nth-child(6) .category-item:not(.category-item-active):hover {
  background-color: rgba(6, 182, 212, 0.15);
  box-shadow: 0 4px 12px rgba(6, 182, 212, 0.15);
}

.category-item-wrapper:nth-child(7) .category-item:not(.category-item-active) {
  color: #6366f1;
  background-color: rgba(99, 102, 241, 0.08);
  border-color: rgba(99, 102, 241, 0.2);
}

.category-item-wrapper:nth-child(7) .category-item:not(.category-item-active):hover {
  background-color: rgba(99, 102, 241, 0.15);
  box-shadow: 0 4px 12px rgba(99, 102, 241, 0.15);
}

.category-item-wrapper:nth-child(8) .category-item:not(.category-item-active) {
  color: #ef4444;
  background-color: rgba(239, 68, 68, 0.08);
  border-color: rgba(239, 68, 68, 0.2);
}

.category-item-wrapper:nth-child(8) .category-item:not(.category-item-active):hover {
  background-color: rgba(239, 68, 68, 0.15);
  box-shadow: 0 4px 12px rgba(239, 68, 68, 0.15);
}

.category-item-wrapper:nth-child(9) .category-item:not(.category-item-active) {
  color: #14b8a6;
  background-color: rgba(20, 184, 166, 0.08);
  border-color: rgba(20, 184, 166, 0.2);
}

.category-item-wrapper:nth-child(9) .category-item:not(.category-item-active):hover {
  background-color: rgba(20, 184, 166, 0.15);
  box-shadow: 0 4px 12px rgba(20, 184, 166, 0.15);
}

.category-item-wrapper:nth-child(10) .category-item:not(.category-item-active) {
  color: #f97316;
  background-color: rgba(249, 115, 22, 0.08);
  border-color: rgba(249, 115, 22, 0.2);
}

.category-item-wrapper:nth-child(10) .category-item:not(.category-item-active):hover {
  background-color: rgba(249, 115, 22, 0.15);
  box-shadow: 0 4px 12px rgba(249, 115, 22, 0.15);
}

/* 增加更多颜色方案 - 非激活状态 */
.category-item-wrapper:nth-child(11) .category-item:not(.category-item-active) {
  color: #84cc16;
  background-color: rgba(132, 204, 22, 0.08);
  border-color: rgba(132, 204, 22, 0.2);
}

.category-item-wrapper:nth-child(11) .category-item:not(.category-item-active):hover {
  background-color: rgba(132, 204, 22, 0.15);
  box-shadow: 0 4px 12px rgba(132, 204, 22, 0.15);
}

.category-item-wrapper:nth-child(12) .category-item:not(.category-item-active) {
  color: #d946ef;
  background-color: rgba(217, 70, 239, 0.08);
  border-color: rgba(217, 70, 239, 0.2);
}

.category-item-wrapper:nth-child(12) .category-item:not(.category-item-active):hover {
  background-color: rgba(217, 70, 239, 0.15);
  box-shadow: 0 4px 12px rgba(217, 70, 239, 0.15);
}

.category-item-wrapper:nth-child(13) .category-item:not(.category-item-active) {
  color: #0ea5e9;
  background-color: rgba(14, 165, 233, 0.08);
  border-color: rgba(14, 165, 233, 0.2);
}

.category-item-wrapper:nth-child(13) .category-item:not(.category-item-active):hover {
  background-color: rgba(14, 165, 233, 0.15);
  box-shadow: 0 4px 12px rgba(14, 165, 233, 0.15);
}

.category-item-wrapper:nth-child(14) .category-item:not(.category-item-active) {
  color: #f43f5e;
  background-color: rgba(244, 63, 94, 0.08);
  border-color: rgba(244, 63, 94, 0.2);
}

.category-item-wrapper:nth-child(14) .category-item:not(.category-item-active):hover {
  background-color: rgba(244, 63, 94, 0.15);
  box-shadow: 0 4px 12px rgba(244, 63, 94, 0.15);
}

.category-item-wrapper:nth-child(15) .category-item:not(.category-item-active) {
  color: #14b8a6;
  background-color: rgba(20, 184, 166, 0.08);
  border-color: rgba(20, 184, 166, 0.2);
}

.category-item-wrapper:nth-child(15) .category-item:not(.category-item-active):hover {
  background-color: rgba(20, 184, 166, 0.15);
  box-shadow: 0 4px 12px rgba(20, 184, 166, 0.15);
}

.category-item-wrapper:nth-child(16) .category-item:not(.category-item-active) {
  color: #8b5cf6;
  background-color: rgba(139, 92, 246, 0.08);
  border-color: rgba(139, 92, 246, 0.2);
}

.category-item-wrapper:nth-child(16) .category-item:not(.category-item-active):hover {
  background-color: rgba(139, 92, 246, 0.15);
  box-shadow: 0 4px 12px rgba(139, 92, 246, 0.15);
}

.category-item-wrapper:nth-child(17) .category-item:not(.category-item-active) {
  color: #f97316;
  background-color: rgba(249, 115, 22, 0.08);
  border-color: rgba(249, 115, 22, 0.2);
}

.category-item-wrapper:nth-child(17) .category-item:not(.category-item-active):hover {
  background-color: rgba(249, 115, 22, 0.15);
  box-shadow: 0 4px 12px rgba(249, 115, 22, 0.15);
}

.category-item-wrapper:nth-child(18) .category-item:not(.category-item-active) {
  color: #22c55e;
  background-color: rgba(34, 197, 94, 0.08);
  border-color: rgba(34, 197, 94, 0.2);
}

.category-item-wrapper:nth-child(18) .category-item:not(.category-item-active):hover {
  background-color: rgba(34, 197, 94, 0.15);
  box-shadow: 0 4px 12px rgba(34, 197, 94, 0.15);
}

.category-item-wrapper:nth-child(19) .category-item:not(.category-item-active) {
  color: #06b6d4;
  background-color: rgba(6, 182, 212, 0.08);
  border-color: rgba(6, 182, 212, 0.2);
}

.category-item-wrapper:nth-child(19) .category-item:not(.category-item-active):hover {
  background-color: rgba(6, 182, 212, 0.15);
  box-shadow: 0 4px 12px rgba(6, 182, 212, 0.15);
}

.category-item-wrapper:nth-child(20) .category-item:not(.category-item-active) {
  color: #ec4899;
  background-color: rgba(236, 72, 153, 0.08);
  border-color: rgba(236, 72, 153, 0.2);
}

.category-item-wrapper:nth-child(20) .category-item:not(.category-item-active):hover {
  background-color: rgba(236, 72, 153, 0.15);
  box-shadow: 0 4px 12px rgba(236, 72, 153, 0.15);
}


/* 搜索按钮样式 - 优化设计 */
.category-search-button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 48px;
  border-radius: 50%;
  border: 2px solid var(--primary-color);
  background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
  color: var(--primary-color);
  cursor: pointer;
  transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  margin-left: 12px;
  flex-shrink: 0;
  box-shadow: 
    0 2px 8px rgba(0, 0, 0, 0.06),
    inset 0 2px 4px rgba(255, 255, 255, 0.7);
  z-index: 10;
  overflow: hidden;
}

/* 搜索按钮悬停效果 */
.category-search-button:hover {
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-hover) 100%);
  color: white;
  transform: scale(1.1);
  box-shadow: 
    0 4px 20px rgba(64, 158, 255, 0.4),
    0 0 0 4px rgba(64, 158, 255, 0.15);
  border-color: transparent;
}

/* 搜索按钮激活状态 */
.search-button-active {
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-hover) 100%);
  color: white;
  box-shadow: 
    0 6px 24px rgba(64, 158, 255, 0.45),
    0 0 0 6px rgba(64, 158, 255, 0.2);
  border-color: transparent;
  transform: scale(1.05);
}

/* 搜索按钮波纹效果 */
.category-search-button::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 0;
  height: 0;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.3);
  transform: translate(-50%, -50%);
  transition: width 0.8s, height 0.8s;
}

.category-search-button:hover::before {
  width: 250px;
  height: 250px;
}

/* 搜索按钮光效 */
.category-search-button::after {
  content: '';
  position: absolute;
  top: -2px;
  left: -2px;
  right: -2px;
  bottom: -2px;
  border-radius: 50%;
  background: linear-gradient(45deg, var(--primary-color), transparent, transparent);
  z-index: -1;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.category-search-button:hover::after {
  opacity: 1;
}

/* 搜索图标样式 */
.search-icon {
  width: 20px;
  height: 20px;
  transition: all 0.3s ease;
  position: relative;
  z-index: 1;
}

/* 搜索按钮激活时的图标动画 */
.search-button-active .search-icon {
  animation: pulse 1.5s infinite;
}

/* 脉冲动画 */
@keyframes pulse {
  0%, 100% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.1);
    opacity: 0.8;
  }
}

/* 搜索容器 */
.search-container {
  width: 100%;
  padding: 20px 0;
  background-color: #ffffff;
  border-radius: 12px;
  margin-top: 8px;
  box-shadow: var(--shadow-medium);
  position: relative;
  border: 1px solid #e4e7ed;
}

/* 添加微妙的阴影效果替代渐变背景 */
.search-container {
  box-shadow: 
    0 2px 12px rgba(0, 0, 0, 0.08),
    0 0 0 1px rgba(0, 0, 0, 0.02);
  background-color: #ffffff;
}

/* 搜索框覆盖模式 */
.search-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
  margin-top: 0;
  border-radius: 12px;
  box-shadow: 
    0 4px 20px rgba(0, 0, 0, 0.15),
    0 0 0 1px rgba(0, 0, 0, 0.05);
  background: linear-gradient(180deg, #ffffff 0%, #f8f9fa 100%);
  animation: slideDown 0.3s ease-out;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px) scale(0.98);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
    box-shadow: 
      0 4px 20px rgba(0, 0, 0, 0.15),
      0 0 0 1px rgba(0, 0, 0, 0.05);
  }
}

/* 搜索包装器 */
.search-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px;
  position: relative;
  z-index: 1;
}

/* 搜索输入容器 */
.search-input-container {
  display: flex;
  align-items: center;
  background-color: white;
  border-radius: var(--border-radius);
  padding: 0 16px;
  flex: 1;
  box-shadow: var(--shadow-light);
  transition: var(--transition-normal);
}

.search-input-container:focus-within {
  box-shadow: var(--shadow-medium);
  transform: translateY(-2px);
}

/* 左侧搜索图标 */
.search-icon-left {
  color: var(--primary-color);
  margin-right: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, rgba(64, 158, 255, 0.1) 0%, rgba(64, 158, 255, 0.05) 100%);
  padding: 6px;
  border-radius: 50%;
  transition: all 0.3s ease;
}

.search-input-container:focus-within .search-icon-left {
  background: linear-gradient(135deg, rgba(64, 158, 255, 0.2) 0%, rgba(64, 158, 255, 0.1) 100%);
  transform: scale(1.1);
}

.search-icon-left .el-icon {
  color: var(--primary-color);
}

/* 搜索输入框 */
.search-input {
  flex: 1;
  border: none;
  outline: none;
  padding: 12px 0;
}

.search-input .el-input__wrapper {
  border: none;
  box-shadow: none !important;
  padding: 0;
}

.search-input .el-input__inner {
  font-size: 16px;
  padding: 0;
  height: auto;
  line-height: normal;
}

/* 搜索按钮 - 精致美化版 */
.search-button {
  margin-left: 16px;
  border-radius: 25px;
  padding: 0 24px;
  font-size: 15px;
  font-weight: 600;
  border: none;
  /* 渐变背景 */
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  color: white;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  height: 44px;
  position: relative;
  overflow: hidden;
  box-shadow: 
    0 2px 8px rgba(64, 158, 255, 0.25),
    inset 0 1px 0 rgba(255, 255, 255, 0.2);
}

/* 按钮内容容器 */
.search-button-content {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  position: relative;
  z-index: 1;
}

/* 搜索图标容器 */
.search-button-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.2);
  width: 24px;
  height: 24px;
  border-radius: 50%;
  transition: all 0.3s ease;
}

/* 按钮悬浮效果 */
.search-button:hover {
  background: linear-gradient(135deg, #66b1ff 0%, #409eff 100%);
  transform: translateY(-2px);
  box-shadow: 
    0 6px 20px rgba(64, 158, 255, 0.35),
    inset 0 1px 0 rgba(255, 255, 255, 0.3);
}

.search-button:hover .search-button-icon {
  transform: scale(1.1) rotate(90deg);
  background: rgba(255, 255, 255, 0.3);
}

/* 按钮点击效果 */
.search-button:active {
  transform: translateY(0);
  box-shadow: 
    0 2px 8px rgba(64, 158, 255, 0.25),
    inset 0 2px 4px rgba(0, 0, 0, 0.15);
}

/* 按钮脉冲动画 */
.search-button::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 0;
  height: 0;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.3);
  transform: translate(-50%, -50%);
  transition: width 0.6s, height 0.6s;
}

.search-button:hover::after {
  width: 200px;
  height: 200px;
}

/* 按钮加载状态 */
.search-button.is-loading {
  opacity: 0.9;
}

.search-button.is-loading .search-button-icon {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* 关闭按钮 */
.search-close-button {
  margin-left: 12px;
  width: 40px;
  height: 40px;
  background-color: #f5f7fa;
  border: 1px solid #dcdfe6;
  color: var(--text-regular);
  transition: var(--transition-fast);
  position: relative;
  overflow: hidden;
}

.search-close-button::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 0;
  height: 0;
  border-radius: 50%;
  background-color: rgba(64, 158, 255, 0.1);
  transform: translate(-50%, -50%);
  transition: width 0.6s, height 0.6s;
}

.search-close-button:hover::before {
  width: 100px;
  height: 100px;
}

.search-close-button:hover {
  background-color: #ecf5ff;
  color: var(--primary-color);
  border-color: #c6e2ff;
  transform: scale(1.1) rotate(90deg);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);
}

/* 搜索建议列表 */
.search-suggestions {
  max-width: 800px;
  margin: 12px auto 0;
  padding: 0 20px;
  background-color: white;
  border-radius: 12px;
  box-shadow: var(--shadow-light);
  overflow: hidden;
  position: relative;
  z-index: 1;
  border: 1px solid #e4e7ed;
}

.search-suggestion-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  cursor: pointer;
  transition: var(--transition-fast);
  border-bottom: 1px solid #f0f0f0;
}

.search-suggestion-item:last-child {
  border-bottom: none;
}

.search-suggestion-item:hover {
  background-color: #f8f9fa;
  transform: translateX(4px);
}

.search-suggestion-item .el-icon {
  margin-right: 12px;
  color: var(--text-regular);
}

/* 动画效果 - 覆盖模式优化 */
.search-fade-enter-active,
.search-fade-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.search-fade-enter-from {
  opacity: 0;
  transform: translateY(-10px) scale(0.98);
}

.search-fade-leave-to {
  opacity: 0;
  transform: translateY(-10px) scale(0.98);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .search-toggle-btn {
    margin-left: 8px;
    width: 36px;
    height: 36px;
  }
  
  .search-icon {
    width: 18px;
    height: 18px;
  }
  
  .search-container {
    padding: 16px 0;
    margin-top: 4px;
  }
  
  .search-wrapper {
    padding: 0 16px;
    flex-direction: column;
    gap: 12px;
  }
  
  .search-input-container {
    width: 100%;
    flex-wrap: wrap;
  }
  
  .search-button {
    margin-left: 0;
    width: 100%;
    margin-top: 12px;
  }
  
  .search-close-button {
    margin-left: 0;
    position: absolute;
    top: 16px;
    right: 16px;
  }
  
  .search-suggestions {
    margin-top: 8px;
    padding: 0 16px;
  }
  
  .search-suggestion-item {
    padding: 10px 12px;
  }
}

@media (max-width: 480px) {
  .search-input .el-input__inner {
    font-size: 14px;
  }
  
  .search-button {
    padding: 8px 20px;
    font-size: 14px;
    height: 40px;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .category-nav-wrapper {
    margin-bottom: 12px;
  }
  
  .category-header {
    min-height: 56px;
    padding: 6px 12px;
  }
  
  .category-item {
    width: 90px;
    height: 38px;
    padding: 0 12px;
    font-size: 13px;
    border-radius: 19px;
  }
  
  .category-list {
    gap: 8px;
  }
  
  .category-search-button {
    width: 38px;
    height: 38px;
    margin-left: 8px;
  }
  
  .search-icon {
    width: 18px;
    height: 18px;
  }
}

@media (max-width: 480px) {
  .category-item {
    width: 80px;
    height: 34px;
    font-size: 12px;
    border-radius: 17px;
  }
  
  .category-search-button {
    width: 34px;
    height: 34px;
  }
  
  .search-icon {
    width: 16px;
    height: 16px;
  }
}

/* 移除默认样式 */
.category-search-btn {
  display: none;
}

.center-content {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
