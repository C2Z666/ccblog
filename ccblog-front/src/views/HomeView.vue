
<template>
  <HeaderBar />

  <!-- 正文内容 -->
  <div class="home">
    <!-- 顶部欢迎区域 -->
    <!-- 欢迎部分 - 仅在下拉时显示 -->
    <section 
      class="welcome-section" 
      :class="{ 'welcome-section-visible': isScrolledUp }"
    >
      <div class="welcome-container">
        <p class="welcome-subtitle welcome-subtitle-primary">Creativity And Connection</p>
        <p class="welcome-subtitle welcome-subtitle-secondary">CC Blog —— 让创意连接你我</p>
      </div>
    </section>

    <div id="categorySection" class="category-section">
      <NavBar v-if="!contentLoading" :categories="vo.categories"></NavBar>
    </div>

    <div class="content-container">
      <!-- 推荐文章 -->
      <el-skeleton :loading="contentLoading" animated :throttle="200" class="skeleton-container">
        <template #template>
          <div class="recommend-skeleton">
            <el-skeleton-item variant="text" style="width: 60%; height: 32px; margin-bottom: 20px" />
            <div class="skeleton-grid">
              <el-skeleton-item v-for="(item, id) in 4" :key="id" variant="image" class="skeleton-card" />
            </div>
          </div>
        </template>
        <template #default>
          <div v-if="!contentLoading && vo.topArticles.length > 0" class="recommend-section">
            <div class="section-header">
              <h2 class="section-title">
                <i class="el-icon-fire"></i> 推荐阅读
              </h2>
              <div class="section-divider"></div>
            </div>
             </div>
        </template>
      </el-skeleton>

      <!-- 文章列表和侧边栏 -->
      <el-skeleton :loading="articlesLoading" animated :throttle="200" class="skeleton-container">
        <template #template>
          <div class="main-content">
            <div class="article-area">
              <div v-for="(item, id) in 6" :key="id" class="article-skeleton">
                <el-skeleton-item variant="text" style="width: 80%; height: 24px; margin-bottom: 10px" />
                <el-skeleton-item variant="text" style="width: 100%; height: 60px; margin-bottom: 10px" />
                <el-skeleton-item variant="text" style="width: 60%; height: 18px" />
                <div class="skeleton-divider"></div>
              </div>
              <el-skeleton-item variant="text" style="width: 40%; height: 40px; margin: 20px auto" />
            </div>
            <div class="sidebar-area">
              <el-skeleton-item class="sidebar-card" variant="text" style="width: 100%; height: 250px" />
              <el-skeleton-item class="sidebar-card" variant="text" style="width: 100%; height: 300px" />
            </div>
          </div>
        </template>
        <template #default>
          <div class="main-content">
            <!-- 文章列表区域 -->
            <div class="article-area">
              <div class="section-header mb-4">
                <h2 class="section-title">
                  <i class="el-icon-document"></i> 文章推荐
                </h2>
                <div class="section-divider"></div>
              </div>
              <div id="articleList" class="cdc-article-panel__list article-list-wrapper">
                <ArticleList :articles="articles.records"></ArticleList>
              </div>

              <!-- 分页组件 -->
              <div class="pagination-container">
                <el-pagination 
                  :page-sizes="[10, 20]" 
                  hide-on-single-page 
                  v-model:current-page="currentPage" 
                  v-model:page-size="pageSize" 
                  layout="sizes, prev, pager, next" 
                  :page-count="totalPage" 
                  :default-current-page="1"
                  @update:page-size="onPageSizeChange" 
                  @update:current-page="onCurrentPageChange"
                  class="pagination"
                />
              </div>
            </div>

            <!-- 侧边栏区域 -->
            <div class="sidebar-area">
              <!-- 回到顶部按钮 -->
              <el-backtop :right="40" :bottom="40" :visibility-height="300" />
              
              <!-- 热门标签 -->
              <div class="sidebar-card">
                <div class="sidebar-card-header">
                  <h3 class="sidebar-card-title"><i class="el-icon-tags"></i> 热门标签</h3>
                </div>
                <div class="tag-cloud">
                  <el-skeleton v-if="loadingTags" animated :rows="6" :throttle="0" />
                  <template v-else>
                    <div
                      v-for="(tag, index) in displayTags"
                      :key="tag.tagId"
                      :class="[
                        'tag-item',
                        {
                          'tag-item-top-hot': (tag as any).isTopHot,
                          'tag-hot-ultra': (tag as any).isTopHot && tag.hotScore > 1000,
                          'tag-hot-high': (tag as any).isTopHot && tag.hotScore > 600 && tag.hotScore <= 1000,
                          'tag-hot-medium': (tag as any).isTopHot && tag.hotScore <= 600
                        }
                      ]"
                      @click="goToTag(tag)"
                    >
                      <div class="tag-inner"
                        :style="(tag as any).isTopHot ? { '--hot-glow-color': getHotGlowColor(tag.hotScore) } : {}"
                      >
                        <span class="tag-name">{{ tag.tag }}</span>
                        <div class="tag-meta"
                          :class="{ 'tag-meta-hot': (tag as any).isTopHot }"
                        >
                          <span class="hotness-icon"
                            v-if="(tag as any).isTopHot"
                          >
                            <i class="el-icon-fire"></i>
                          </span>
                          <span class="hotness-value">{{ formatHotScore(tag.hotScore) }}</span>
                        </div>
                      </div>
                    </div>
                  </template>
                </div>
              </div>

              <!-- 作者推荐 -->
              <div class="sidebar-card">
                <div class="sidebar-card-header">
                  <h3 class="sidebar-card-title"><i class="el-icon-user"></i> 推荐作者(每小时刷新)</h3>
                </div>
                <div class="author-list">
                  <div v-for="(author, index) in recommendedAuthors" :key="index" class="author-item">
                    <router-link :to="`/user/${author.userId}/articlesTab`" class="avatar-link">
                      <el-avatar :size="40" :src="author.avatar" class="author-avatar" />
                    </router-link>
                    <div class="author-info">
                      <div class="author-name">{{ author.userName }}</div>
                      <div class="author-stats">{{ author.fansCount }} 粉丝</div>
                    </div>
                    <el-button 
                      v-if="global.isLogin && global.user.userId !== String(author.userId)"
                      size="small" 
                      type="primary" 
                      plain 
                      class="follow-btn"
                      :disabled="followBtnDisabled[author.userId]"
                      @click="handleFollow(author.userId)"
                    >
                      {{ followStates[author.userId] ? '取消关注' : '关注' }}
                    </el-button>
                  </div>
                </div>
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
import { onMounted, provide, reactive, ref, watch, onUnmounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { doGet, doPost } from '@/http/BackendRequests'
import { CATEGORY_ARTICLE_LIST_URL, INDEX_URL, CATEGORY_LIST_URL, USER_HOT_USER_URL, USER_FOLLOW_URL, TAG_HOT_URL } from '@/http/URL'
import {
  type CommonResponse, defaultGlobalResponse, type GlobalResponse
} from '@/http/ResponseTypes/CommonResponseType'
import NavBar from '@/views/home/navbar/NavBar.vue'
import { defaultIndexVoResponse, type IndexVoResponse } from '@/http/ResponseTypes/IndexVoType'
import ArticleList from '@/views/home/article/ArticleList.vue'
import Footer from '@/components/layout/Footer.vue'
import { useGlobalStore } from '@/stores/global'
import { useFollowIdCache, updateFollowCache } from '@/stores/useFollowIdCache'
import { type BasicPageType, defaultBasicPage } from '@/http/ResponseTypes/PageType/BasicPageType'
import type { ArticleType } from '@/http/ResponseTypes/ArticleType/ArticleType'
import { useRoute, useRouter } from 'vue-router'
import LoginDialog from '@/components/dialog/LoginDialog.vue'
import RegisterDialog from '@/components/dialog/RegisterDialog.vue'
const globalStore = useGlobalStore()
const global = globalStore.global
const route = useRoute()
const router = useRouter()

// 控制欢迎区域显示/隐藏的响应式变量
const isScrolledUp = ref(true)
let lastScrollTop = 0
const SCROLL_THRESHOLD = 30000; // 增加展开阈值，使展开更难
const RETRACT_THRESHOLD = 30; // 收起阈值

// 监听滚动事件，控制欢迎区域显示/隐藏
const handleScroll = () => {
  const scrollTop = window.pageYOffset || document.documentElement.scrollTop
  // 调整逻辑：
  // 1. 只有当页面滚动到顶部附近时才显示
  // 2. 向上滚动超过较大阈值才显示，避免误触
  isScrolledUp.value = scrollTop < RETRACT_THRESHOLD || 
      (scrollTop < lastScrollTop && lastScrollTop - scrollTop > SCROLL_THRESHOLD)
  lastScrollTop = scrollTop <= 0 ? 0 : scrollTop
}

// 获取热门用户数据
const fetchHotUsers = () => {
  doGet<CommonResponse>(USER_HOT_USER_URL, {})
    .then((response) => {
      if (response.data && response.data.result) {
        // 从响应中获取热门用户数据并映射到所需格式
        recommendedAuthors.value = response.data.result.map((user: any) => ({
          userId: user.userId,
          userName: user.userName,
          fansCount: user.fansCount,
          avatar: user.avatar
        }))
        
        // 初始化按钮状态
        recommendedAuthors.value.forEach(author => {
          followBtnDisabled[author.userId] = false
        })
        
        // 关注状态将由watch监听器自动初始化（因为添加了immediate选项）
      }
    })
}

onMounted(() => {
  // 初始状态设置为可见（页面顶部）
  isScrolledUp.value = true
  window.addEventListener('scroll', handleScroll)
  // 先获取分类列表，然后再获取文章
  fetchCategories()
  // 获取热门用户数据
  fetchHotUsers()
  // 获取热门标签
  fetchHotTags()
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})

// let global = reactive<GlobalResponse>({...defaultGlobalResponse})
let vo = reactive<IndexVoResponse>({...defaultIndexVoResponse})
let articles = reactive<BasicPageType<ArticleType>>({...defaultBasicPage})
// 存储当前选中的分类ID，默认为0（全部）
const categoryId = ref(0)

// 定义标签数据结构
interface TagDTO {
  tagId: number
  tag: string
  status: number
  hotScore: number
}

// 热门标签数据
const hotTags = ref<TagDTO[]>([])
const displayTags = ref<TagDTO[]>([])
const loadingTags = ref(false)

// 推荐作者数据 - 从后端获取
const recommendedAuthors = ref<Array<{ userId: number; userName: string; fansCount: number; avatar: string }>>([])
// 关注状态管理
const followStates = reactive<{ [key: number]: boolean }>({})
const followBtnDisabled = reactive<{ [key: number]: boolean }>({})

// 添加watch监听器，实时更新关注状态，与NoticeFollowItem.vue保持一致
watch(
  [() => recommendedAuthors.value, useFollowIdCache(), () => global.isLogin],
  ([authors, followIdSet]) => {
    if (!global.isLogin) {
      // 如果未登录，清除所有关注状态
      authors.forEach(author => {
        followStates[author.userId] = false
      })
      return
    }
    // console.log("用户关注列表:", useFollowIdCache().value)
    // 根据缓存更新每个作者的关注状态
    authors.forEach(author => {
      // console.log("检查用户:", author.userId, followIdSet.has(Number(author.userId)))
      followStates[author.userId] = followIdSet.has(Number(author.userId))
    })
  },
  { immediate: true, deep: true }
)

// 处理关注/取消关注
const handleFollow = (userId: number) => {
  if (!global.isLogin) {
    // 如果未登录，可以打开登录对话框
    loginDialogClicked.value = true
    return
  }
  
  // 设置按钮禁用状态
  followBtnDisabled[userId] = true
  
  // 获取当前关注状态并确定目标状态
  const currentState = followStates[userId] || false
  const targetState = !currentState
  
  // 发送关注/取消关注请求
  // console.log("关注/取消关注请求:", userId, targetState)
  doPost<CommonResponse>(USER_FOLLOW_URL, {
    // userId: String(global.user.userId),
    followId: String(userId),
    followed: targetState
  })
    .then((res) => {
      // 确保响应成功且有数据后再更新状态
      if (res && res.data && res.data.result) {
        // console.log(`操作成功：${targetState ? '关注' : '取消关注'}用户ID: ${userId}`)
        
        // 根据关注状态更新缓存：关注时添加到缓存，取消关注时从缓存移除
        updateFollowCache(Number(userId), targetState)
        
        // 立即更新本地状态
        followStates[userId] = targetState
      

        // 更新粉丝数显示
        const author = recommendedAuthors.value.find(a => a.userId === userId)
        if (author) {
          if (targetState) {
            // 关注操作：增加粉丝数
            author.fansCount += 1
          } else {
            // 取消关注操作：减少粉丝数，但确保不会小于0
            author.fansCount = Math.max(0, author.fansCount - 1)
          }
        }
      }
    })
    .catch((err) => {
      console.error('关注操作失败:', err)
      // 错误时不改变状态
    })
    .finally(() => {
      // 无论成功失败都要恢复按钮状态
      followBtnDisabled[userId] = false
    })
}

// 获取分类列表
const fetchCategories = () => {
  doGet<CommonResponse>(CATEGORY_LIST_URL, {
    filtered: true  // 添加filtered参数，过滤掉数量为0的类别
  })
    .then((response) => {
      if (response.data && response.data.result) {
        // 创建分类数组，确保包含'全部'类别
        const categoriesList = [
          // { categoryId: 0, category: '全部', articleCount: 0 } // 添加'全部'类别
        ]
        // 合并后端返回的分类数据（已过滤数量为0的类别）
        categoriesList.push(...response.data.result)
        // 存储到vo.categories
        vo.categories = categoriesList
        // 根据路由参数确定当前选中的分类ID
        const categoryParam = route.query['category']
        if (categoryParam) {
          // 查找对应的分类ID
          const selectedCategory = vo.categories.find(cat => cat.category === categoryParam)
          if (selectedCategory) {
            categoryId.value = selectedCategory.categoryId
          }
        } else {
          // 默认选择全部（ID为0）
          categoryId.value = 0
        }
        // 获取文章列表
        fetchArticles()
      }
    })
}

// 获取热门标签数据
const fetchHotTags = () => {
  loadingTags.value = true
  doGet<CommonResponse>(TAG_HOT_URL, { limit: 15 })
    .then((response) => {
      if (response.data && response.data.result) {
        // 接收标签数据
        hotTags.value = response.data.result
        
        // 找出热度最高的3个标签
        const sortedTags = [...hotTags.value].sort((a, b) => b.hotScore - a.hotScore)
        const topTags = sortedTags.slice(0, 3)
        
        // 给热度最高的3个标签标记
        hotTags.value.forEach(tag => {
          if (topTags.some(topTag => topTag.tagId === tag.tagId)) {
            (tag as any).isTopHot = true
          }
        })
        
        // 乱序渲染所有标签
        displayTags.value = shuffleArray([...hotTags.value])
      }
    })
    .catch((error) => {
      console.error('获取热门标签失败:', error)
      ElMessage.error('获取热门标签失败')
    })
    .finally(() => {
      loadingTags.value = false
    })
}

// 数组乱序函数
const shuffleArray = <T>(array: T[]): T[] => {
  const shuffled = [...array]
  for (let i = shuffled.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1))
    ;[shuffled[i], shuffled[j]] = [shuffled[j], shuffled[i]]
  }
  return shuffled
}

// 格式化热度分数显示
const formatHotScore = (score: number): string => {
  if (score >= 1000) {
    return (score / 1000).toFixed(1) + 'k'
  }
  return score.toString()
}

// 根据热度值获取发光颜色
const getHotGlowColor = (score: number): string => {
  if (score > 1000) return '#ff5252'; // 超高热度 - 深红色
  if (score > 600) return '#ff9800';  // 高热度 - 橙色
  return '#ffc107';                      // 中等热度 - 黄色
}

// 跳转到标签页面
const goToTag = (tag: TagDTO) => {
  router.push({ path: '/tag-articles', query: { tag: tag.tag, tagId: tag.tagId.toString() } })
}

const fetchArticles = () => {
  console.log("global.isLogin:",global.isLogin)
  // 获取文章列表，使用categoryId参数
  doGet<CommonResponse>(CATEGORY_ARTICLE_LIST_URL, {
    categoryId: categoryId.value
  })
    .then((response) => {
      // console.log("收到回复")
      console.log(response)
      // console.log("准备设置isLogin",response.data.global)
      if(response.data){
        globalStore.setGlobal(response.data.global) // 在这里实际上把global整个替换掉了
        Object.assign(vo.topArticles, response.data.result.topArticles)
        // 分类信息已经通过fetchCategories获取，且现在响应中不再包含类别信息
        // @ts-ignore
        Object.assign(articles, response.data.result.articles)
        totalPage.value = Number(response.data.result.articles.pages)
        currentPage.value = Number(response.data.result.articles.current)
        console.log(articles)
        // 取消骨架屏显示
        articlesLoading.value = false
        contentLoading.value = false
      }
    })
}



// 监听路由参数变化
watch(() => route.query.category, (newCategory, oldCategory) => {
  if (newCategory !== oldCategory) {
    // console.log('分类参数变化:', newCategory)
    // 重置页面和加载状态
    currentPage.value = 1
    contentLoading.value = true
    articlesLoading.value = true
    
    // 根据新的分类名称查找对应的分类ID
    if (newCategory) {
      const selectedCategory = vo.categories.find(cat => cat.category === newCategory)
      if (selectedCategory) {
        categoryId.value = selectedCategory.categoryId
      } else {
        categoryId.value = 0 // 如果找不到匹配的分类，显示全部
      }
    } else {
      categoryId.value = 0 // 全部
    }
    
    // 重新获取文章
    fetchArticles()
  }
})

// 如下是分页操作
const currentPage = ref(1)
const totalPage = ref(0)
const pageSize = ref(10)

const onPageSizeChange = (newPageSize: number) => {
  // console.log("分页操作")
  doGet<CommonResponse>(CATEGORY_ARTICLE_LIST_URL, {
    categoryId: categoryId.value,
    currentPage: currentPage.value,
    pageSize: pageSize.value
  })
    .then((response) => {
      // console.log(response)
      if(response.data){
        globalStore.setGlobal(response.data.global)
        Object.assign(vo.topArticles, response.data.result.topArticles)
        // 分类信息已经通过fetchCategories获取，且现在响应中不再包含类别信息
        Object.assign(articles, response.data.result.articles)
        totalPage.value = Number(response.data.result.articles.pages)
        currentPage.value = Number(response.data.result.articles.current)
        console.log(articles)
      }
    })
}

const onCurrentPageChange = (newCurrentPage: number) => {
  // console.log("页面改变")
  doGet<CommonResponse>(CATEGORY_ARTICLE_LIST_URL, {
    categoryId: categoryId.value,
    currentPage: newCurrentPage,
    pageSize: pageSize.value
  })
    .then((response) => {
      // console.log(response)
      if(response.data){
        globalStore.setGlobal(response.data.global)
        Object.assign(vo.topArticles, response.data.result.topArticles)
        // 分类信息已经通过fetchCategories获取，且现在响应中不再包含类别信息
        Object.assign(articles, response.data.result.articles)
        totalPage.value = Number(response.data.result.articles.pages)
        currentPage.value = Number(response.data.result.articles.current)
        // console.log(articles)
      }
    })
}

// 骨架屏显示
const contentLoading = ref(true)
const articlesLoading = ref(true)

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
/* 全局样式 */
.home {
  min-height: calc(100vh - var(--header-height, 60px) - var(--footer-height, 60px));
  background-color: #f5f7fa;
}

/* 类别栏效果样式 - 使用固定定位确保绝对吸顶 */
.category-section {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background-color: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  padding: 10px 0;
  transition: all 0.3s ease;
  width: 100%;
  box-sizing: border-box;
  margin: 0;
}

/* 为内容容器添加足够的顶部内边距，避免被固定导航栏遮挡 */
.content-container {
  margin-top: 60px; /* 确保内容不被固定的导航栏遮挡 */
  position: relative;
  z-index: 1;
}

/* 欢迎区域 */
  .welcome-section {
    background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
    padding: 30px 0;
    text-align: center;
    position: relative;
    overflow: hidden;
    transition: all 0.5s ease;
    opacity: 0;
    transform: translateY(-20px);
    max-height: 0;
    pointer-events: none;
    visibility: hidden;
    box-sizing: border-box;
  }

  .welcome-section-visible {
    opacity: 1;
    transform: translateY(0);
    max-height: 200px;
    pointer-events: auto;
    visibility: visible;
  }

  .welcome-container {
    position: relative;
    z-index: 1;
    max-width: 800px;
    margin: 0 auto;
    padding: 10px;
  }

  .welcome-subtitle-primary {
    background: linear-gradient(135deg, #409eff, #67c23a);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
    font-size: 28px;
    font-weight: 700;
    margin: 0 0 12px 0;
    letter-spacing: 2px;
    text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
  }

  .welcome-subtitle-secondary {
    background: linear-gradient(135deg, #606266, #909399);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
    font-size: 18px;
    margin: 0;
    font-weight: 500;
    letter-spacing: 0.5px;
    transition: all 0.3s ease;
  }

  .welcome-section-visible .welcome-subtitle-primary {
    animation: textSlideIn 0.8s ease-out;
  }

  .welcome-section-visible .welcome-subtitle-secondary {
    animation: textSlideIn 0.8s ease-out 0.2s both;
  }

  @keyframes textSlideIn {
    from {
      opacity: 0;
      transform: translateY(10px);
      /* 移除letter-spacing变化，避免文字突然变大的视觉效果 */
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }

  /* 添加装饰元素 */
  .welcome-section::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    /* background-image: url('@/assets/static/grid-pattern.svg'); */
    background-size: cover;
    opacity: 0.1;
  }

  .welcome-section::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 50%;
    transform: translateX(-50%);
    width: 60%;
    height: 3px;
    background: linear-gradient(to right, transparent, #409eff, transparent);
    opacity: 0;
    transition: opacity 0.5s ease;
  }

  .welcome-section-visible::after {
    opacity: 1;
  }

/* 类别区域 */
.category-section {
  background-color: white;
  padding: 16px 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  position: sticky;
  top: var(--header-height, 60px);
  z-index: 100;
}

/* 内容容器 */
.content-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 30px 20px;
}

/* 推荐文章区域 */
.recommend-section {
  margin-bottom: 40px;
}

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.section-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #333;
  margin-right: 12px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.section-title i {
  color: #67c23a;
}

.section-divider {
  flex: 1;
  height: 2px;
  background: linear-gradient(to right, #67c23a, transparent);
}

/* 主内容区域 */
.main-content {
  display: grid;
  grid-template-columns: 1fr 320px;
  gap: 30px;
}

/* 文章列表区域 */
.article-area {
  background-color: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  transition: box-shadow 0.3s ease;
}

.article-area:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
}

.article-list-wrapper {
  animation: fadeIn 0.5s ease-in;
}

/* 分页容器 */
.pagination-container {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}

.pagination {
  display: inline-flex;
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

/* 侧边栏区域 */
.sidebar-area {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.sidebar-card {
  background-color: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.sidebar-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
}

.sidebar-card-header {
  margin-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
  padding-bottom: 12px;
}

.sidebar-card-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #333;
  display: flex;
  align-items: center;
  gap: 8px;
}

.sidebar-card-title i {
  color: #409eff;
}

/* 标签云容器 - 实现流式随机排列 */
.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  padding: 16px 4px;
  position: relative;
  justify-content: flex-start;
}

/* 减小标签项的随机偏移，使排列更紧凑 */
.tag-item:nth-child(odd) {
  margin-left: 15px;
}

.tag-item:nth-child(3n) {
  margin-left: 5px;
}

.tag-item:nth-child(5n) {
  margin-left: 3px;
}

.tag-item:nth-child(7n) {
  margin-left: 8px;
}

/* 标签基础样式 - 移除边框和框感 */
.tag-item {
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
  position: relative;
  min-width: fit-content;
  opacity: 0.95;
  will-change: transform, box-shadow;
  /* 使用随机偏移创建不规则排列感 */
  transform: translateX(var(--random-offset, 0px));
  /* 为每个标签设置不同的随机偏移 */
  --random-offset: calc((var(--item-index, 0) % 7) * 4px - 12px);
}

/* 标签内部容器 - 实现渐变和高级效果 */
.tag-inner {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  border-radius: 24px;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.85), rgba(245, 247, 250, 0.95));
  backdrop-filter: blur(10px);
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.04);
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
}

/* 超热门标签样式 - 最大但更紧凑 */
.tag-item-top-hot.tag-hot-ultra .tag-inner {
  padding: 10px 18px;
}

.tag-item-top-hot.tag-hot-ultra .tag-name {
  font-size: 1rem;
  font-weight: 600;
}

/* 高热门标签样式 - 中到大 */
.tag-item-top-hot.tag-hot-high .tag-inner {
  padding: 9px 16px;
}

.tag-item-top-hot.tag-hot-high .tag-name {
  font-size: 0.95rem;
  font-weight: 600;
}

/* 中等热门标签样式 - 中等 */
.tag-item-top-hot.tag-hot-medium .tag-inner {
  padding: 8px 15px;
}

.tag-item-top-hot.tag-hot-medium .tag-name {
  font-size: 0.9rem;
  font-weight: 500;
}

/* 普通标签根据热度调整大小 - 减小尺寸让每行能放下多个 */
.tag-item:not(.tag-item-top-hot) .tag-inner {
  padding: calc(5px + var(--hot-score, 0) * 0.002px) calc(10px + var(--hot-score, 0) * 0.003px);
}

/* 标签名称 - 减小普通标签字体大小 */
.tag-name {
  font-size: calc(0.75rem + var(--hot-score, 0) * 0.0005rem);
  font-weight: 500;
  color: #434343;
  letter-spacing: 0.1px;
  transition: color 0.3s ease;
}

/* 标签元数据区域 - 减小字体大小 */
.tag-meta {
  display: flex;
  align-items: center;
  gap: 3px;
  font-size: 0.7rem;
  color: #8c8c8c;
  transition: all 0.3s ease;
}

/* 热门标签元数据样式 */
.tag-meta-hot {
  gap: 3px;
}

/* 热度图标 - 调整大小 */
.hotness-icon {
  font-size: 9px;
  color: var(--hot-glow-color, #ff5252);
  animation: firePulse 1.8s infinite ease-in-out;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  filter: drop-shadow(0 0 2px rgba(255, 82, 82, 0.5));
}

/* 热度数值 - 减小字体大小 */
.hotness-value {
  font-weight: 600;
  font-size: 0.68rem;
  transition: all 0.3s ease;
}

/* 火焰脉冲动画 */
@keyframes firePulse {
  0%, 100% {
    transform: scale(1) rotate(1deg);
    opacity: 1;
    filter: drop-shadow(0 0 2px rgba(255, 82, 82, 0.5));
  }
  25% {
    transform: scale(1.15) rotate(-2deg);
    opacity: 0.9;
    filter: drop-shadow(0 0 4px rgba(255, 82, 82, 0.6));
  }
  50% {
    transform: scale(0.95) rotate(2deg);
    opacity: 0.85;
    filter: drop-shadow(0 0 3px rgba(255, 82, 82, 0.7));
  }
  75% {
    transform: scale(1.1) rotate(-1deg);
    opacity: 0.9;
    filter: drop-shadow(0 0 5px rgba(255, 82, 82, 0.8));
  }
}

/* 普通标签悬停效果 */
.tag-item:hover {
  transform: translateY(-3px);
  opacity: 1;
}

.tag-item:hover .tag-inner {
  background: linear-gradient(135deg, rgba(255, 255, 255, 1), rgba(240, 242, 245, 1));
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
}

.tag-item:hover .tag-name {
  color: #1890ff;
}

.tag-item:hover .hotness-value {
  color: #1890ff;
}

/* 高级发光效果 - 替代边框 */
.tag-item::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  border-radius: 24px;
  background: transparent;
  pointer-events: none;
  transition: all 0.3s ease;
  z-index: -1;
}

.tag-item:hover::after {
  transform: scale(1.05);
  background: linear-gradient(135deg, rgba(24, 144, 255, 0.1), rgba(24, 144, 255, 0));
}

/* 热门标签特殊样式 - 保持相对较大但更紧凑 */
.tag-item-top-hot .tag-inner {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.9), rgba(255, 249, 240, 0.95));
  box-shadow: 0 4px 12px rgba(255, 102, 0, 0.1);
  /* 确保热门标签在视觉上仍然突出 */
  transform: scale(1.05);
}

.tag-item-top-hot .tag-name {
  font-weight: 600;
  color: #333;
}

.tag-item-top-hot .hotness-value {
  color: var(--hot-glow-color, #ff5252);
  font-weight: 700;
  text-shadow: 0 0 2px rgba(255, 82, 82, 0.1);
}

/* 超热门标签效果 */
.tag-hot-ultra .tag-inner {
  box-shadow: 0 4px 16px rgba(255, 82, 82, 0.15);
}

.tag-hot-ultra::after {
  background: radial-gradient(circle, rgba(255, 82, 82, 0.15) 0%, transparent 70%);
  animation: hotPulseUltra 3s infinite ease-in-out;
}

/* 高热门标签效果 */
.tag-hot-high .tag-inner {
  box-shadow: 0 4px 14px rgba(255, 152, 0, 0.12);
}

.tag-hot-high::after {
  background: radial-gradient(circle, rgba(255, 152, 0, 0.12) 0%, transparent 70%);
  animation: hotPulseHigh 3.5s infinite ease-in-out;
}

/* 中等热门标签效果 */
.tag-hot-medium .tag-inner {
  box-shadow: 0 4px 12px rgba(255, 193, 7, 0.1);
}

.tag-hot-medium::after {
  background: radial-gradient(circle, rgba(255, 193, 7, 0.1) 0%, transparent 70%);
  animation: hotPulseMedium 4s infinite ease-in-out;
}

/* 热门发光动画 */
@keyframes hotPulseUltra {
  0%, 100% {
    transform: scale(1);
    opacity: 0.3;
  }
  50% {
    transform: scale(1.08);
    opacity: 0.15;
  }
}

@keyframes hotPulseHigh {
  0%, 100% {
    transform: scale(1);
    opacity: 0.25;
  }
  50% {
    transform: scale(1.06);
    opacity: 0.12;
  }
}

@keyframes hotPulseMedium {
  0%, 100% {
    transform: scale(1);
    opacity: 0.2;
  }
  50% {
    transform: scale(1.04);
    opacity: 0.1;
  }
}

/* 悬停时的高级效果 */
.tag-item-top-hot:hover {
  transform: translateY(-4px);
}

.tag-item-top-hot:hover .tag-inner {
  background: linear-gradient(135deg, rgba(255, 255, 255, 1), rgba(255, 245, 230, 1));
  box-shadow: 0 8px 20px rgba(255, 102, 0, 0.15);
}

.tag-item-top-hot:hover::after {
  transform: scale(1.1);
  opacity: 0.3;
}

/* 微交互效果 - 点击时 */
.tag-item:active .tag-inner {
  transform: scale(0.98);
  transition: transform 0.1s ease;
}

/* 响应式优化 */
@media (max-width: 768px) {
  .tag-cloud {
    gap: 8px;
    padding: 12px 2px;
  }
  
  .tag-inner {
    padding: 6px 14px;
    gap: 6px;
  }
  
  .tag-name {
    font-size: 0.88rem;
  }
  
  .tag-meta {
    font-size: 0.75rem;
  }
  
  .hotness-icon {
    font-size: 10px;
  }
}

/* 高级细节 - 添加光晕效果 */
.tag-cloud::before {
  content: '';
  position: absolute;
  top: -50px;
  right: -50px;
  width: 100px;
  height: 100px;
  background: radial-gradient(circle, rgba(255, 102, 0, 0.05) 0%, transparent 70%);
  border-radius: 50%;
  pointer-events: none;
}

/* 骨架屏容器样式调整 */
.tag-cloud .el-skeleton {
  width: 100%;
}

/* 作者列表 */
.author-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.author-item {
  display: flex;
  align-items: center;
  gap: 12px;
}

.author-avatar {
  transition: transform 0.3s ease;
}

.author-item:hover .author-avatar {
  transform: scale(1.1);
}

.author-info {
  flex: 1;
  min-width: 0;
}

.author-name {
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.author-stats {
  font-size: 0.85rem;
  color: #909399;
}

.follow-btn {
  font-size: 0.85rem;
  padding: 4px 12px;
}

/* 自定义页脚 */
.custom-footer {
  margin-top: 60px;
  background-color: #1e1e2f;
  color: rgba(255, 255, 255, 0.8);
}

/* 骨架屏样式 */
.skeleton-container {
  margin-bottom: 40px;
}

.recommend-skeleton {
  background-color: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.skeleton-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 20px;
}

.skeleton-card {
  height: 280px;
  border-radius: 8px;
}

.article-skeleton {
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;
}

.skeleton-divider {
  height: 1px;
  background-color: #f0f0f0;
  margin: 16px 0;
}

/* 动画 */
@keyframes fadeIn {
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
@media (max-width: 1024px) {
  .main-content {
    grid-template-columns: 1fr;
  }
  
  .welcome-title {
    font-size: 2rem;
  }
  
  .welcome-subtitle {
    font-size: 1rem;
  }
}

@media (max-width: 768px) {
  .welcome-section {
    padding: 40px 0;
  }
  
  .welcome-title {
    font-size: 1.8rem;
  }
  
  .content-container {
    padding: 20px 16px;
  }
  
  .article-area {
    padding: 20px 16px;
  }
  
  .sidebar-card {
    padding: 16px;
  }
  
  .section-title {
    font-size: 1.3rem;
  }
}

@media (max-width: 480px) {
  .welcome-title {
    font-size: 1.5rem;
  }
  
  .welcome-section {
    padding: 30px 0;
  }
  
  .skeleton-grid {
    grid-template-columns: 1fr;
  }
}
</style>

