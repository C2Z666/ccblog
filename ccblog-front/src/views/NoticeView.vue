<template>
  <!-- 导航栏 -->
  <HeaderBar></HeaderBar>

  <!-- 正文内容 -->
  <div class="notice-wrap">

    <!-- 导航选项栏 -->
    <div class="notice-nav">
      <div class="notice-nav-inner">
        <div class="clear-notice-btn">
          <el-button type="primary" size="small" @click="clearUnreadNotice" :loading="clearLoading" :disabled="!hasAnyNotices || loading">
            一键清除通知
          </el-button>
        </div>
        <el-badge :value="unreadCount['system']" :offset="[-20, 12]" :show-zero="false">
          <el-link @click="changeNoticeType(NoticeTypeEnum.SYSTEM_TYPE)">
            <template #default>
              <span :class="{'color-text-link-orange': currentTab == NoticeTypeEnum.SYSTEM_TYPE}" class="resized-text-size font-bold mr-4 p-2"> 系统通知 </span>
            </template>
          </el-link>
        </el-badge>
        <el-badge :value="unreadCount[NoticeTypeEnum.COMMENT_TYPE]" :offset="[-20, 12]" :show-zero="false">
          <el-link @click="changeNoticeType(NoticeTypeEnum.COMMENT_TYPE)">
            <template #default>
              <span :class="{'color-text-link-orange': currentTab == NoticeTypeEnum.COMMENT_TYPE}" class="resized-text-size font-bold mr-4 p-2"> 评论 </span>
            </template>
          </el-link>
        </el-badge>
        <el-badge :value="unreadCount[NoticeTypeEnum.REPLY_TYPE]" :offset="[-20, 12]" :show-zero="false">
          <el-link @click="changeNoticeType(NoticeTypeEnum.REPLY_TYPE)">
            <template #default>
              <span :class="{'color-text-link-orange': currentTab == NoticeTypeEnum.REPLY_TYPE}" class="resized-text-size font-bold mr-4 p-2"> 回复 </span>
            </template>
          </el-link>
        </el-badge>
        <el-badge :value="unreadCount['like']" :offset="[-20, 12]" :show-zero="false">
          <el-link @click="changeNoticeType(NoticeTypeEnum.LIKE_TYPE)">
            <template #default>
              <span :class="{'color-text-link-orange': currentTab == NoticeTypeEnum.LIKE_TYPE}" class="resized-text-size font-bold mr-4 p-2"> 点赞 </span>
            </template>
          </el-link>
        </el-badge>
        <el-badge :value="unreadCount['collect']" :offset="[-20, 12]" :show-zero="false">
          <el-link @click="changeNoticeType(NoticeTypeEnum.COLLECT_TYPE)">
            <template #default>
              <span :class="{'color-text-link-orange': currentTab == NoticeTypeEnum.COLLECT_TYPE}" class="resized-text-size font-bold mr-4 p-2"> 收藏 </span>
            </template>
          </el-link>
        </el-badge>
        <el-badge :value="unreadCount['follow']" :offset="[-20, 12]" :show-zero="false">
          <el-link @click="changeNoticeType(NoticeTypeEnum.FOLLOW_TYPE)">
            <template #default>
              <span :class="{'color-text-link-orange': currentTab == NoticeTypeEnum.FOLLOW_TYPE}" class="resized-text-size font-bold mr-4 p-2"> 关注消息 </span>
            </template>
          </el-link>
        </el-badge>


      </div>
    </div>
<!-- 内容区域容器 -->
    <div class="notice-content-container">
      <!-- 内容显示区域 -->
      <div class="notice-content">
        <!-- 评论页 -->
        <div v-if="currentTab === NoticeTypeEnum.COMMENT_TYPE" id="itemList">
          <el-skeleton :loading="loading" :throttle="200">
            <template #template>
              <div v-for="(item, id) in 5" :key="id" class="center-content notification h-36 w-full">
                <el-skeleton-item class="m-2"></el-skeleton-item>
                <el-skeleton-item class="m-2"></el-skeleton-item>
                <el-skeleton-item class="m-2"></el-skeleton-item>
                <el-skeleton-item class="m-2"></el-skeleton-item>
              </div>
            </template>
            <template #default>
              <div v-if="!loading && (!noticeData.comment.list || noticeData.comment.list.records.length == 0)" class="notification">暂无评论消息</div>
              <NoticeComment v-else :list="noticeData.comment.list"></NoticeComment>
            </template>
          </el-skeleton>
        </div>

        <!-- 回复页 -->
        <div v-if="currentTab === NoticeTypeEnum.REPLY_TYPE" id="itemList">
          <el-skeleton :loading="loading">
            <template #template>
              <div v-for="(item, id) in 5" :key="id" class="center-content notification h-36 w-full">
                <el-skeleton-item class="m-2"></el-skeleton-item>
                <el-skeleton-item class="m-2"></el-skeleton-item>
                <el-skeleton-item class="m-2"></el-skeleton-item>
                <el-skeleton-item class="m-2"></el-skeleton-item>
              </div>
            </template>
            <template #default>
              <div v-if="!loading && (!noticeData.reply.list || noticeData.reply.list.records.length == 0)" class="notification">暂无回复消息</div>
              <NoticeReply v-else :list="noticeData.reply.list"></NoticeReply>
            </template>
          </el-skeleton>
        </div>

        <!-- 点赞通知 -->
        <div v-if="currentTab === NoticeTypeEnum.LIKE_TYPE" id="itemList">
          <el-skeleton :loading="loading">
            <template #template>
              <div v-for="(item, id) in 5" :key="id" class="center-content notification h-36 w-full">
                <el-skeleton-item class="m-2"></el-skeleton-item>
                <el-skeleton-item class="m-2"></el-skeleton-item>
                <el-skeleton-item class="m-2"></el-skeleton-item>
                <el-skeleton-item class="m-2"></el-skeleton-item>
              </div>
            </template>
            <template #default>
              <div v-if="!loading && (!noticeData.like.list || noticeData.like.list.records.length == 0)" class="notification">暂无点赞消息</div>
              <NoticePraise v-else :list="noticeData.like.list"></NoticePraise>
            </template>
          </el-skeleton>
        </div>

        <!-- 收藏通知 -->
        <div v-if="currentTab === NoticeTypeEnum.COLLECT_TYPE" id="itemList">
          <el-skeleton :loading="loading">
            <template #template>
              <div v-for="(item, id) in 5" :key="id" class="center-content notification h-36 w-full">
                <el-skeleton-item class="m-2"></el-skeleton-item>
                <el-skeleton-item class="m-2"></el-skeleton-item>
                <el-skeleton-item class="m-2"></el-skeleton-item>
                <el-skeleton-item class="m-2"></el-skeleton-item>
              </div>
            </template>
            <template #default>
              <div v-if="!loading && (!noticeData.collect.list || noticeData.collect.list.records.length == 0)" class="notification">暂无收藏消息</div>
              <NoticeCollect v-else :list="noticeData.collect.list"></NoticeCollect>
            </template>
          </el-skeleton>
        </div>

        <!-- 关注消息 -->
        <div v-if="currentTab === NoticeTypeEnum.FOLLOW_TYPE" id="itemList">
          <el-skeleton :loading="loading">
            <template #template>
              <div v-for="(item, id) in 5" :key="id" class="center-content notification h-36 w-full">
                <el-skeleton-item class="m-2"></el-skeleton-item>
                <el-skeleton-item class="m-2"></el-skeleton-item>
                <el-skeleton-item class="m-2"></el-skeleton-item>
                <el-skeleton-item class="m-2"></el-skeleton-item>
              </div>
            </template>
            <template #default>
              <div v-if="!loading && (!noticeData.follow.list || noticeData.follow.list.records.length == 0)" class="notification">暂无关注消息</div>
              <NoticeFollow v-else :list="noticeData.follow.list"></NoticeFollow>
            </template>
          </el-skeleton>
        </div>

        <!-- 系统通知 -->
        <div v-if="currentTab === NoticeTypeEnum.SYSTEM_TYPE" id="itemList">
          <el-skeleton :loading="loading">
            <template #template>
              <div v-for="(item, id) in 5" :key="id" class="center-content notification h-36 w-full">
                <el-skeleton-item class="m-2"></el-skeleton-item>
                <el-skeleton-item class="m-2"></el-skeleton-item>
                <el-skeleton-item class="m-2"></el-skeleton-item>
                <el-skeleton-item class="m-2"></el-skeleton-item>
              </div>
            </template>
            <template #default>
              <div v-if="!loading && (!noticeData.system.list || noticeData.system.list.records.length == 0)" class="notification">暂无通知</div>
              <NoticeSystem v-else :list="noticeData.system.list"></NoticeSystem>
            </template>
          </el-skeleton>
        </div>
      </div>

      <!-- 统一分页组件 -->
      <div class="pagination-container">
        <el-pagination 
          :page-sizes="[10, 20]" 
          hide-on-single-page 
          v-model:current-page="currentPage[currentTab as string]"
          v-model:page-size="pageSize[currentTab as string]" 
          layout="sizes, prev, pager, next, jumper" 
          :page-count="totalPage[currentTab as string] || 1"
          :default-current-page="1"
          @update:size="onPageSizeChange" 
          @update:current-page="onCurrentPageChange">
        </el-pagination>
      </div>
    </div>

    <!-- 底部信息 -->
    <Footer></Footer>
    <LoginDialog :clicked="loginDialogClicked"></LoginDialog>
  </div>
</template>

<script setup lang="ts">
import Footer from '@/components/layout/Footer.vue'
import HeaderBar from '@/components/layout/HeaderBar.vue'
import { useGlobalStore } from '@/stores/global'
import { computed, onMounted, provide, reactive, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { doGet } from '@/http/BackendRequests'
import type { CommonResponse } from '@/http/ResponseTypes/CommonResponseType'
import { defaultNoticeMsgResponse, type NoticeMsgResponseType } from '@/http/ResponseTypes/NoticeMsgResponseType'

import { UNREAD_NOTICE_URL, UNREAD_NOTICE_CLEAR_URL } from '@/http/URL'
import { doPost } from '@/http/BackendRequests'
import LoginDialog from '@/components/dialog/LoginDialog.vue'
import NoticeComment from '@/views/notice/NoticeComment.vue'
import { NoticeTypeEnum } from '@/constants/NoticeTypeConstants'
import NoticeReply from '@/views/notice/NoticeReply.vue'
import NoticeSystem from '@/views/notice/NoticeSystem.vue'

import NoticeFollow from '@/views/notice/NoticeFollow.vue'
import NoticeCollect from '@/views/notice/NoticeCollect.vue'
import NoticePraise from '@/views/notice/NoticePraise.vue'
const globalStore = useGlobalStore()
const global = globalStore.global
const route = useRoute()
const router = useRouter()

// 骨架屏
const loading = ref(true)
// 清除按钮加载状态
const clearLoading = ref(false)


// 分页信息
const currentPage = ref<{[key: string]: number}>({
  comment: 1,
  reply: 1,
  like: 1,
  collect: 1,
  follow: 1,
  system: 1
})

const pageSize = ref<{[key: string]: number}>({
  comment: 10,
  reply: 10,
  like: 10,
  collect: 10,
  follow: 10,
  system: 10
})

const totalPage = ref<{[key: string]: number}>({
  comment: 0,
  reply: 0,
  like: 0,
  collect: 0,
  follow: 0,
  system: 0
})

const noticeData = ref<{[key: string]: NoticeMsgResponseType}>({
  comment: {...defaultNoticeMsgResponse},
  reply: {...defaultNoticeMsgResponse},
  like: {...defaultNoticeMsgResponse},
  collect: {...defaultNoticeMsgResponse},
  follow: {...defaultNoticeMsgResponse},
  system: {...defaultNoticeMsgResponse}
})

const unreadCount = ref<{[key: string]: number}>({
  comment: 0,
  reply: 0,
  like: 0,
  collect: 0,
  follow: 0,
  system: 0
})


onMounted(() => {
  getNotices()
})

watch(() => route.params.noticeType, (newVal, oldVal) => {
  getNotices()
})

const getNotices = () => {
  doGet<CommonResponse<NoticeMsgResponseType>>(UNREAD_NOTICE_URL + '/' + route.params.noticeType, {
    currentPage: currentPage.value[`${route.params.noticeType}`],
    pageSize: pageSize.value[`${route.params.noticeType}`]
  })
    .then((res) => {
      console.log(res)
      globalStore.setGlobal(res.data.global)
      // 先保存原始数据
      const originalResult = res.data.result
      
      // 克隆一份数据并修改消息项状态为已读（这样可以立即显示未读状态）
      const clonedResult = JSON.parse(JSON.stringify(originalResult))
      
      // 直接赋值克隆后的数据
      Object.assign(noticeData.value[`${route.params.noticeType}`], clonedResult)
      Object.assign(unreadCount.value, clonedResult.unreadCountMap)
      
      // 页面加载后模拟将消息标记为已读（通过修改每个消息项的state）
      setTimeout(() => {
        // 将当前页面的消息标记为已读
        if (noticeData.value[`${route.params.noticeType}`].list?.records) {
          noticeData.value[`${route.params.noticeType}`].list.records.forEach((record: any) => {
            if (record.state === 0) { // 如果是未读消息
              record.state = 1 // 标记为已读
            }
          })
        }
        
        // 清除未读计数
        unreadCount.value[`${route.params.noticeType}`] = 0
      }, 5000) // 增加延迟到5秒，让用户能更清晰地看到高亮效果
      
      // 确保pages存在，如果不存在则根据总数和每页大小计算
      if (res.data.result.list && res.data.result.list.pages) {
        totalPage.value[`${route.params.noticeType}`] = Number(res.data.result.list.pages)
      } else if (res.data.result.list && res.data.result.list.total) {
        // 如果没有pages字段，根据总数和每页大小计算总页数
        totalPage.value[`${route.params.noticeType}`] = Math.ceil(Number(res.data.result.list.total) / pageSize.value[`${route.params.noticeType}`])
      } else {
        totalPage.value[`${route.params.noticeType}`] = 0
      }
      // console.log('当前通知类型:', route.params.noticeType)
      // console.log('总页数:', totalPage.value[`${route.params.noticeType}`])
    })
    .finally(() => {
      loading.value = false
    })
}


// 获取当前的通知类型（好像用处不大，后面可以考虑删掉）
const currentTab = computed(() => {
  return route.params.noticeType
})

// 计算是否有任何通知存在
const hasAnyNotices = computed(() => {
  // 全面检查所有六个栏目是否存在任何通知记录或未读通知
  // 包括评论、回复、点赞、收藏、关注、系统通知
  const allTypes = ['comment', 'reply', 'like', 'collect', 'follow', 'system']
  
  return allTypes.some(type => {
    // 检查是否有未读通知
    if (unreadCount.value[type] > 0) {
      return true
    }
    
    // 检查是否有任何通知记录（无论是否已读）
    if (noticeData.value[type] && 
        noticeData.value[type].list && 
        noticeData.value[type].list.records && 
        noticeData.value[type].list.records.length > 0) {
      return true
    }
    
    return false
  })
})

// 分页
const onCurrentPageChange = (newCurrentPage: number) => {
  currentPage.value[`${route.params.noticeType}`] = newCurrentPage
  getNotices()
}
const onPageSizeChange = (newPageSize: number) => {
  currentPage.value[`${route.params.noticeType}`] = 1 // 切换每页大小时重置到第一页
  pageSize.value[`${route.params.noticeType}`] = newPageSize
  getNotices()
}

// 点击各个按钮切换通知类型
const changeNoticeType = (targetNoticeType: string) => {
  // if (currentTab.value == targetNoticeType){
  //   window.location.reload()
  // }else{
  //   router.push('/notice/' + targetNoticeType)
  // }
  router.push('/notice/' + targetNoticeType)
}

// 一键清除通知
const clearUnreadNotice = () => {
  clearLoading.value = true
  doPost<CommonResponse>(UNREAD_NOTICE_CLEAR_URL, {})
    .then((res) => {
      // 清除成功后将所有栏目的未读数量置为0
      Object.keys(unreadCount.value).forEach(key => {
        unreadCount.value[key] = 0
      })
      // 重新获取当前页面的通知数据
      getNotices()
    })
    .catch((error) => {
      console.error('清除已读通知失败:', error)
    })
    .finally(() => {
      clearLoading.value = false
    })
}


// 登录框
const changeClicked = () => {
  loginDialogClicked.value = !loginDialogClicked.value
  // console.log("clicked: ", loginDialogClicked.value)
}

provide('loginDialogClicked', changeClicked)
const loginDialogClicked = ref(false)

</script>



<style scoped>

.resized-text-size{
  font-size: 1.125rem/* 18px */;
  line-height: 1.75rem/* 28px */;
}

@media (max-width: 768px) {
  .resized-text-size{
    font-size: 0.875rem/* 14px */;
    line-height: 1.25rem/* 20px */;
  }
}

.clear-notice-btn {
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
}

.notice-nav-inner {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  flex-wrap: wrap;
  padding-bottom: 16px;
}

/* 主容器样式 */
.notice-wrap {
  display: flex;
  flex-direction: column;
  min-height: calc(100vh - 200px); /* 减去头部和底部高度 */
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

/* 导航栏样式 */
.notice-nav {
  width: 100%;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #e0e0e0;
}

/* 内容区域容器样式 */
.notice-content-container {
  display: flex;
  flex-direction: column;
  flex: 1;
  min-height: 500px; /* 设置最小高度 */
  width: 100%;
  background-color: #ffffff;
  border-radius: 4px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

/* 内容区域样式 */
.notice-content {
  flex: 1;
  min-height: 400px;
  max-height: 600px; /* 固定内容区域最大高度 */
  overflow-y: auto; /* 内容超过高度时显示滚动条 */
  padding: 20px;
  background-color: #ffffff;
}

/* 分页容器样式 */
.pagination-container {
  width: 100%;
  padding: 16px 0;
  background-color: #ffffff;
  border-top: 1px solid #e0e0e0;
}

/* 分页组件样式 */
:deep(.el-pagination) {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  padding: 0;
  margin: 0;
}

/* 移除左右两侧空格 */
:deep(.el-pagination__sizes) {
  margin-right: 0 !important;
  padding-right: 0;
}

:deep(.el-pagination__jump) {
  margin-left: 0 !important;
  padding-left: 0;
}

:deep(.el-pagination__prev),
:deep(.el-pagination__next),
:deep(.el-pagination__pager) {
  margin: 0;
}

/* 确保整个分页组件紧凑显示 */
:deep(.el-pagination__total),
:deep(.el-pagination__sizes),
:deep(.el-pagination__prev),
:deep(.el-pagination__pager),
:deep(.el-pagination__next),
:deep(.el-pagination__jump) {
  margin-right: 10px !important;
}

:deep(.el-pagination__jump) {
  margin-right: 0 !important;
}

/* 当没有消息时的样式 */
.notification {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
  color: #909399;
  font-size: 14px;
}

/* 调整骨架屏样式 */
:deep(.el-skeleton) {
  width: 100%;
}

</style>
