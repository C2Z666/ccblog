<template>
  <HeaderBar />

  <div class="home article-detail" v-if="ifUsualArticle">
    <div class="col-body pg-2-article" id="article-detail-body-div">
      <div class="com-3-layout" >
        <div class="layout-main">

          <!-- 正文 -->
          <ArticleDetail  
            :global="global" 
            :articleVo="articleVo" 
            :categoryId="articleVo.article.categoryId"
            @update-count="handleUpdateCount"
            @update-status="handleUpdateStatus"
            @update-article-info="handleUpdateArticleInfo"
          ></ArticleDetail>

          <!--  评论  -->
          <!-- <CommentList :comments="articleVo.comments" :hot-comment="articleVo.hotComment" :article="articleVo.article"></CommentList> -->
          <CursorCommentList 
            :article="articleVo.article" 
            :comment-parents="articleVo.commentParents" 
            :top-comment-info="articleVo.topCommentInfo || undefined"
          />

          <div
            class="correlation-article bg-color-white"
            id="relatedRecommend"
          >
            <!-- 关联推荐 -->
            <h4 class="correlation-article-title">相关推荐(敬请期待)</h4>
            <div class="bg-color-white">
              <div id="articleList"></div>
            </div>
          </div>
        </div>

        <div class="layout-side hidden-when-screen-small flex-col flex">

          <!-- 用户相关信息 -->
          <UserCard :global="global" :user="articleVo.author"></UserCard>


          <!-- 文章菜单  -->

          <div class="sticky top-5 overflow-auto" id="content-menu">
            <el-scrollbar>
              <em>文章目录</em>
              <el-divider></el-divider>
              <MdCatalog :editor-id="'id'" :scroll-element="scrollElement"></MdCatalog>
            </el-scrollbar>
          </div>

        </div>
      </div>
    </div>

    <!-- 底部信息 -->
    <Footer></Footer>
  </div>
  <LoginDialog :clicked="clicked"></LoginDialog>

</template>

<script setup lang="ts">
import Footer from '@/components/layout/Footer.vue'
import HeaderBar from '@/components/layout/HeaderBar.vue'
import { onMounted, provide, reactive, ref, watch, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  type CommonResponse,
} from '@/http/ResponseTypes/CommonResponseType'
import { doGet } from '@/http/BackendRequests'
import { ARTICLE_DETAIL_URL, ARTICLE_DETAIL_MORE_URL } from '@/http/URL'
import {
  type ArticleDetailResponse,
  defaultArticleDetailResponse
} from '@/http/ResponseTypes/ArticleDetailResponseType'
import ArticleDetail from '@/components/article/ArticleDetail.vue'
import { MdCatalog } from 'md-editor-v3'
import UserCard from '@/components/user/UserCard.vue'
import LoginDialog from '@/components/dialog/LoginDialog.vue'
import { useGlobalStore } from '@/stores/global'
import { setTitle } from '@/util/utils'
import CursorCommentList from '@/components/commentCursor/CursorCommentList.vue'

const route = useRoute()
// let global = reactive<GlobalResponse>({...defaultGlobalResponse})
let articleVo = reactive<ArticleDetailResponse>({...defaultArticleDetailResponse})
const globalStore = useGlobalStore()

const global = globalStore.global
const articleId = route.params.articleId

const scrollElement = document.documentElement;

const clicked = ref(false)


const changeClicked = () => {
  clicked.value = !clicked.value
  // console.log("clicked: ", clicked.value)
}

provide('loginDialogClicked', changeClicked)

//为了子组件评论时能够无缝更新，重新渲染，提供一个更新函数
const getArticleDetail = (response: ArticleDetailResponse) => {
  Object.assign(articleVo.comments, response.comments)
  // console.log(articleVo)
}

provide('updateArticleComment', getArticleDetail)

// ifUsualArticle 为true时，表示文章正常，为false时，表示文章是专栏文章，需要重定向，引入此变量从而避免在重定向过程中的闪烁情况
const ifUsualArticle = ref(false)
const router = useRouter()
// 从路由参数或URL哈希中获取commentId
const commentId = ref<string>('')

onMounted(async () => {
  // 检查URL中是否有comment-开头的哈希值
  const hash = window.location.hash
  if (hash && hash.startsWith('#comment-')) {
    commentId.value = hash.substring(9) // 提取#comment-后的ID部分
  }
  
  // 构建请求参数，仅包含commentId（默认为-1）作为查询参数
  const params = { 
    commentId: commentId.value || '-1'
  }
  
  // 使用路径参数articleId和查询参数commentId调用后端API
  doGet<CommonResponse>(ARTICLE_DETAIL_URL + `/${articleId}`, params)
    .then((response) => {

      // console.log(response)
      if (!response.data.redirect) {
        
        globalStore.setGlobal(response.data.global)
        // console.log("global: ", global)
        
        // 判断result是否为空
        if (!response.data.result || !response.data.result.article) {
          // 如果结果为空，导航到空文章页面
          router.replace("/empty-article")
          return
        }
        
        Object.assign(articleVo, response.data.result)
        setTitle(articleVo.article.title)
        ifUsualArticle.value = true

        articleVo.article.count = articleVo.article.count || {}
        // 预设所有计数为0，确保数据完整性
        articleVo.article.count.likeCnt = articleVo.article.count.likeCnt || 0;
        articleVo.article.count.commentCnt = articleVo.article.count.commentCnt || 0;
        articleVo.article.count.collectCnt = articleVo.article.count.collectCnt || 0;
        articleVo.article.count.readCnt = articleVo.article.count.readCnt || 0;
        
        // 数据加载完成后，滚动到评论
        setTimeout(() => {
          scrollToCommentInHash()
        }, 300)
      }else{
        router.replace("/column/" + response.data.result.columnId + '/' + response.data.result.sectionId)
      }
    })
})

// 处理URL哈希中的评论ID，滚动到对应位置
  function scrollToCommentInHash() {
    const hash = window.location.hash
    if (hash && hash.startsWith('#comment-')) {
      // 提取评论ID
      const commentId = hash.substring(9) // 9是"#comment-"的长度
      
      // 使用轮询的方式查找元素，确保在DOM更新后能找到
      let attempts = 0
      const maxAttempts = 10
      const checkInterval = 200
      
      const checkAndScroll = () => {
        attempts++
        const element = document.getElementById(`comment-${commentId}`)
        
        if (element) {
          // 确保元素在视口中
          element.scrollIntoView({ behavior: 'smooth', block: 'center' })
          // 添加高亮效果
          element.classList.add('bg-yellow-50') // 使用黄色背景高亮显示目标评论
          // 3秒后移除高亮效果
          setTimeout(() => {
            element.classList.remove('bg-yellow-50')
          }, 3000)
        } else if (attempts < maxAttempts) {
          // 如果没找到，继续尝试
          setTimeout(checkAndScroll, checkInterval)
        }
      }
      
      // 开始检查
      checkAndScroll()
    }
  }

// 监听路由变化，重新加载文章
watch(
  () => route.path,
  () => {
    // 路由变化后重新滚动到评论
    setTimeout(() => {
      scrollToCommentInHash()
    }, 300)
  }
)

// 监听文章数据加载完成，再次尝试滚动
watch(
  () => articleVo.commentParents,
  async () => {
    // 等待DOM更新完成
    await nextTick()
    scrollToCommentInHash()
  }
)

// 处理子组件传递的计数更新事件
const handleUpdateCount = (countType: 'like' | 'comment' | 'collect' | 'read', value: number) => {
  // console.log(`更新${countType}计数为:`, value);
  if (!articleVo.article.count) {
    articleVo.article.count = { likeCnt: 0, readCnt: 0, collectCnt: 0, commentCnt: 0 };
  }
  
  switch (countType) {
    case 'like':
      articleVo.article.count.likeCnt = value;
      break;
    case 'comment':
      articleVo.article.count.commentCnt = value;
      break;
    case 'collect':
      articleVo.article.count.collectCnt = value;
      break;
    case 'read':
      articleVo.article.count.readCnt = value;
      break;
  }
};

// 处理子组件传递的状态更新事件
const handleUpdateStatus = (statusType: 'liked' | 'commented' | 'collected', value: boolean) => {
  // console.log(`更新${statusType}状态为:`, value);
  articleVo.article[statusType] = value;
};

// 处理子组件传递的文章信息更新事件
const handleUpdateArticleInfo = (infoType: 'tags' | 'category' | 'authorName' | 'authorAvatar', value: any) => {
  // console.log(`更新文章${infoType}为:`, value);
  articleVo.article[infoType] = value;
};
</script>

<style scoped>

div.layout-main{
  padding: 0 60px 0;
}


@media (max-width: 768px) {
  div.layout-side {
    display: none;
  }
  div.layout-main{
    padding: 0 ;
  }
}

div#content-menu{
  height: calc(100vh - 70px);
}
</style>
