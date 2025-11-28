import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import { UserHomeTabTypeEnum } from '@/constants/UserHomeTabTypeConstants'
import {useGlobalSize} from "element-plus";
import {getGlobalStore, useGlobalStore} from "@/stores/global";
import {doGet} from "@/http/BackendRequests";
import type {CommonResponse} from "@/http/ResponseTypes/CommonResponseType";
import {GLOBAL_INFO_URL} from "@/http/URL";
import {messageTip} from "@/util/utils";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/login',
      name: 'login',
      component: HomeView,
    },
    {
      path: '/index',
      name: 'index',
      component: HomeView,
    },
    {
      path: '/article/detail/:articleId',  // 表示路径参数
      name: "articleDetail",
      component: () => import('@/views/ArticleDetailView.vue')
    },
    {
      path: '/article/edit',
      name: "articleNew",
      component: () => import('@/views/ArticleEditView.vue')
    },
    {
      path: '/article/edit/:articleId',
      name: "articleEdit",
      component: () => import('@/views/ArticleEditView.vue')
    },
    {
      path: '/article/tag/:tagId',
      name: "tagArticles",
      component: () => import('@/views/TagArticlesView.vue')
    },
    {
      path: '/tag-articles',
      name: "tagArticlesByQuery",
      component: () => import('@/views/article/ArticleWithTag.vue')
    },
    // 作者个人信息页面
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/views/AboutView.vue')
    },
    // 后续更新计划
    {
      path: '/plan',
      name: 'plan',
      component: () => import('@/views/PlanView.vue')
    },
    // ai聊天页
    {
      path: '/chat/ai',
      name: 'chatAi',
      component: () => import('@/views/chat-home/ChatAiSession.vue'),
      meta: {
        loginRequired: true // 添加这个保证退出登录后跳转到主页(路由守卫会检测)
      }
    },
    // 用户聊天页面
    {      path: '/chat/user/:peerId',
      name: 'chatUser',
      component: () => import('@/views/chat-home/ChatUserView.vue')
    },
    // 用户聊天会话列表页面
    {      path: '/chat/home',
      name: 'chatSession',
      component: () => import('@/views/chat-home/ChatUserSession.vue'),
      meta: {
        loginRequired: true // 添加这个保证退出登录后跳转到主页(路由守卫会检测)
      }
    },
    {
      path: '/user/:userId',
      redirect(to) {
        return { name: 'userHome', params: { userId: to.params.userId, typeName: UserHomeTabTypeEnum.ArticlesTab } }
      },
    },
    {      
      path: '/user/:userId/:typeName',
      name: 'userHome',      
      component: () => import('@/views/UserHomeView.vue')    },
    {
      path: '/notice',
      redirect(to) {
        // 进入通知界面默认显示私信
        return { name: 'notice', params: { noticeType: 'system' } } 
      },
    },
    {
      path: '/notice/:noticeType',
      name: "notice",
      component: () => import('@/views/NoticeView.vue'),
    },
    {
      path: '/query-article',
      name: "queryArticle",
      component: () => import('@/views/article/QueryArticle.vue')
    },
    {
      path: '/empty-article',
      name: "emptyArticle",
      component: () => import('@/components/article/EmptyArticle.vue'),
      meta: {
        keepLoginState: true // 标记需要保持登录状态
      }
    }
  ]
})

router.beforeEach(async (to, from, next) => {
  console.log('to:',to)
  console.log('from:',from)
  if (to.meta?.loginRequired) {
    const globalStore = await getGlobalStore()
    await checkLoginStatus(globalStore)

    if (!globalStore.global.isLogin) {
      messageTip('请先登录', 'warning')
      return next('/')   // 直接结束
    }
  }
  next()                 // 已登录 / 不需要登录
})

async function checkLoginStatus(globalStore: any)  {
  await doGet<CommonResponse>(GLOBAL_INFO_URL, {})
      .then((res) => {
        globalStore.setGlobal(res.data.global)
      })
}

export default router
