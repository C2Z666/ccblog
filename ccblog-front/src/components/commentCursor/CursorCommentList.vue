<template>
  <div class="comment-list">
    <div>
      <svg xmlns="http://www.w3.org/2000/svg" style="display:none;" class="">
        <symbol id="icon-comment" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
          <path fill-rule="evenodd" clip-rule="evenodd"
                d="M4.62739 1.25C2.9347 1.25 1.5625 2.6222 1.5625 4.31489L1.56396 12.643C1.56403 14.3356 2.9362 15.7078 4.62885 15.7078H6.48326L6.93691 17.6869L6.93884 17.6948C7.16894 18.6441 8.28598 19.0599 9.08073 18.4921L12.7965 15.7078H15.5001C17.1928 15.7078 18.565 14.3355 18.565 12.6428L18.5635 4.31477C18.5635 2.62213 17.1913 1.25 15.4986 1.25H4.62739ZM5.98265 9.89255C6.68783 9.89255 7.2595 9.32089 7.2595 8.61571C7.2595 7.91053 6.68783 7.33887 5.98265 7.33887C5.27747 7.33887 4.70581 7.91053 4.70581 8.61571C4.70581 9.32089 5.27747 9.89255 5.98265 9.89255ZM9.95604 9.89255C10.6612 9.89255 11.2329 9.32089 11.2329 8.61571C11.2329 7.91053 10.6612 7.33887 9.95604 7.33887C9.25086 7.33887 8.6792 7.91053 8.6792 8.61571C8.6792 9.32089 9.25086 9.89255 9.95604 9.89255ZM15.2124 8.61571C15.2124 9.32089 14.6407 9.89255 13.9355 9.89255C13.2304 9.89255 12.6587 9.32089 12.6587 8.61571C12.6587 7.91053 13.2304 7.33887 13.9355 7.33887C14.6407 7.33887 15.2124 7.91053 15.2124 8.61571Z"
          ></path>
        </symbol>
        <symbol id="icon-zan" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
          <path fill-rule="evenodd" clip-rule="evenodd"
                d="M13.0651 3.25923C12.6654 2.21523 12.1276 1.60359 11.4633 1.40559C10.8071 1.21 10.2539 1.48626 9.97848 1.67918C9.43962 2.05668 9.17297 2.64897 9.0009 3.12662C8.93522 3.30893 8.87504 3.50032 8.82077 3.67291L8.82077 3.67292C8.80276 3.73019 8.78541 3.78539 8.76872 3.8375C8.6974 4.06017 8.63455 4.23905 8.56561 4.38315C8.07104 5.41687 7.64014 6.034 7.2617 6.43277C6.89154 6.8228 6.5498 7.0275 6.18413 7.21038C5.8887 7.35813 5.69369 7.66144 5.69365 8.00211L5.69237 17.3908C5.6923 17.8783 6.08754 18.2736 6.57511 18.2736H14.8382C15.2621 18.2736 15.5829 18.1393 15.8149 17.9421C15.9234 17.8497 15.9985 17.7554 16.0484 17.6856C16.0695 17.6561 16.088 17.6282 16.0983 17.6126L16.1017 17.6075L16.1033 17.6051L16.1194 17.5857L16.1428 17.5478C16.913 16.3019 17.4472 15.3088 17.8659 14.1183C18.3431 12.7613 18.5849 11.5853 18.6874 10.6685C18.7871 9.77617 18.7612 9.07318 18.6558 8.68779C18.5062 8.14118 18.138 7.82653 17.7668 7.66617C17.4231 7.51771 17.0763 7.49836 16.8785 7.49807L13.1134 7.44551C13.662 5.19751 13.31 3.89889 13.0651 3.25923ZM1.251 8.0848C1.22726 7.5815 1.62891 7.16046 2.13277 7.16046H3.4408C3.92832 7.16046 4.32354 7.55568 4.32354 8.04321V17.4303C4.32354 17.9178 3.92832 18.313 3.4408 18.313H2.57554C2.10419 18.313 1.71599 17.9427 1.69378 17.4718L1.251 8.0848Z"
          ></path>
        </symbol>
      </svg>
    </div>
    <div class="common-write-content">
        <el-input
          @click="() => {if (!global.isLogin) {if(showLoginDialog) showLoginDialog()}}"
          v-model="textarea"
          :rows="2"
          resize="none"
          type="textarea"
          maxlength="512"
          :placeholder="global.isLogin ? '讨论应以学习和精进为目的。请勿发布不友善或者负能量的内容，与人为善，比聪明更重要！' : '请先登录后再评论'"
        />
        <p class="flex justify-end m-2">
          <el-button @click="commentSubmit" :disabled="textarea.length === 0">
            评论<el-icon class="el-icon--right"><ChatSquare /></el-icon>
          </el-button>
        </p>
    </div>
    <div>共 {{ article.count.commentCnt }} 条评论</div>
    <!-- 无限层递归 -->
    <RecursiveCommentItem
    v-for="c in topList"
    :key="c.commentId"
    :item="c"
    :target-index="commentParents ? 1 : undefined "
    :comment-parents="commentParents && commentParents[0] === c.commentId ? commentParents : undefined"
    :if-ok="commentParents?isTargetCommentFound:true"
    :authorId="+props.article.author"
    @delete-comment="(d) => topList = topList.filter(c => c.commentId !== d.commentId)"
    />
    <!-- 顶级继续拉 -->
    <div v-if="topHasMore" class="expand" @click="loadTop">展开更多</div>
  </div>
</template>

<script setup lang="ts">
import type { CommentCursorVO, CommentItemDTO } from '@/http/ResponseTypes/CommentType/CursorCommentType'
import type { CommonResponse } from '@/http/ResponseTypes/CommonResponseType'
import { inject, provide, ref, watch } from 'vue'
import { doGet,doPost } from '@/http/BackendRequests'
import { COMMENT_CURSOR_URL } from '@/http/URL'
import { messageTip } from '@/util/utils'
import RecursiveCommentItem from './RecursiveCommentItem.vue'
import { OperateTypeEnum } from '@/constants/OperateTypeConstants'
import { COMMENT_LIKE_URL, COMMENT_SUBMIT_URL } from '@/http/URL'
import type { ArticleType } from '@/http/ResponseTypes/ArticleType/ArticleType'
import type { ArticleCommentType } from '@/http/ResponseTypes/CommentType/ArticleCommentType'
import { useRoute, useRouter } from 'vue-router'
import { dayjs } from 'element-plus'
import { useGlobalStore } from '@/stores/global'
const globalStore = useGlobalStore()
const global = globalStore.global
const showLoginDialog = inject<() => void>('loginDialogClicked')
const route = useRoute()

const articleId = route.params.articleId
const limit = "5"

const topList = ref<CommentItemDTO[]>([])

// 响应式变量，用于标记查找目标评论状态，一旦找到就设置为true并保持
const isTargetCommentFound = ref(false)
function emitOk() {
  // console.log("顶级评论标记:emitOk被调用")
  isTargetCommentFound.value = true
}
provide('emitOk', emitOk)

const topHasMore = ref(true)
const topCursor = ref<string>()
const topCursorId = ref<number>()

// 评论区的内容
const textarea = ref('')



const props = defineProps<{
//   comment: CommentCursorVO,
  article: ArticleType,
  commentParents?: number[],
  topCommentInfo?: CommentItemDTO | undefined
}>()

// console.log("获得article信息:",props.article)

/* 第一页（parentId = 0） */
init()
async function init() { 
  // 先加载普通的顶级评论
  await fetchTop() 
  
  // console.log("获得topCommentInfo信息:",props.topCommentInfo)
  // 如果存在置顶评论信息，将其添加到评论列表顶部，并移除列表中可能存在的同ID评论
  if (props.topCommentInfo) {
    // 移除列表中可能存在的同ID评论
    const existingIndex = props.topCommentInfo ? topList.value.findIndex(item => item.commentId === props.topCommentInfo!.commentId) : -1;
    if (existingIndex >= 0) {
      topList.value.splice(existingIndex, 1);
    }
    // 无论如何都将置顶评论添加到列表顶部
    topList.value.unshift(props.topCommentInfo);
  }
}

async function loadTop() { await fetchTop() }

async function fetchTop() {
  const params: Record<string, any> = {
    articleId: articleId,
    parentId: 0,
    limit: 5
  }
  if (topList.value.length) {
    const last = topList.value[topList.value.length - 1]
    params.cursor = dayjs(last.commentTime).format('YYYY-MM-DDTHH:mm:ss') // 去时区
    params.cursorId = Number(last.commentId)
  }

  const res = await doGet<CommonResponse<CommentCursorVO>>(COMMENT_CURSOR_URL, params)
  const vo = res.data.result!
  
  // 如果存在置顶评论信息，过滤掉新加载评论中重复的置顶评论
  let newComments = [...vo.comments]
  if (props.topCommentInfo) {
    newComments = newComments.filter(item => item.commentId !== props.topCommentInfo?.commentId)
  }
  
  topList.value.push(...newComments)
  topHasMore.value = vo.hasMore
  if (newComments.length) {
    const last = newComments[newComments.length - 1]
    topCursor.value = last.commentTime
    topCursorId.value = Number(last.commentId)
  }
}

// ==========  给文章评论  =======
const commentSubmit = () => {
  if (!global.isLogin) {
    if (showLoginDialog) {
      showLoginDialog()
    } else {
      console.error('showLoginDialog is not defined')
    }
    return
  }
  doPost<CommonResponse>(COMMENT_SUBMIT_URL, {
    articleId: props.article.articleId,
    authorId:props.article.author,
    commentContent: textarea.value,
  }).then((response) => {
    messageTip('评论成功', 'success')
    textarea.value = ''
    topList.value.unshift(response.data.result)
    // 把游标重置为“新最后一条”,否则会把刚才插入的查出来
    const last = topList.value[topList.value.length - 1]
    topCursor.value = last.commentTime
    topCursorId.value = Number(last.commentId)
  }).catch(() => {
    messageTip('评论失败', 'error')
  })
}

</script>

<style scoped>
/* 评论区容器样式 */
.comment-list {
  max-width: 100%;
  margin: 0 auto;
  padding: 16px 0;
}

/* 评论输入框区域样式 */
.common-write-content {
  background: #ffffff;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 24px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
  transition: box-shadow 0.2s ease;
}

.common-write-content:hover {
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}

.common-write-content .el-input__inner {
  border-radius: 8px;
  border-color: #e5e7eb;
  transition: all 0.2s ease;
  resize: none;
  min-height: 80px;
  font-size: 14px;
  line-height: 1.5;
}

.common-write-content .el-input__inner:focus {
  border-color: #0969da;
  box-shadow: 0 0 0 3px rgba(9, 105, 218, 0.1);
}

/* 评论按钮样式 */
.common-write-content .el-button {
  transition: all 0.2s ease;
  border-radius: 6px;
  font-weight: 500;
  padding: 8px 20px;
}

.common-write-content .el-button:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.common-write-content .el-button--primary {
  background: linear-gradient(135deg, #0969da, #3b82f6);
  border: none;
}

.common-write-content .el-button--primary:hover:not(:disabled) {
  background: linear-gradient(135deg, #0860c8, #2563eb);
}

/* 评论总数显示 */
.comment-list > div:nth-child(3) {
  font-size: 16px;
  font-weight: 600;
  color: #374151;
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 2px solid #f3f4f6;
  position: relative;
  padding-left: 28px;
}

.comment-list > div:nth-child(3)::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  width: 24px;
  height: 24px;
  background: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 24 24' stroke='%230969da'%3E%3Cpath stroke-linecap='round' stroke-linejoin='round' stroke-width='2' d='M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z' /%3E%3C/svg%3E") no-repeat center center;
}

/* 展开更多按钮样式 */
.expand {
  text-align: center;
  color: #0969da;
  cursor: pointer;
  margin: 16px 0;
  font-size: 14px;
  padding: 10px 20px;
  border-radius: 8px;
  transition: all 0.2s ease;
  background: transparent;
  border: 1px solid #e5e7eb;
  display: inline-block;
}

.expand:hover {
  background-color: #f9fafb;
  border-color: #0969da;
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

/* 评论列表容器 */
.comment-items-container {
  background: #ffffff;
  border-radius: 12px;
  padding: 8px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
  transition: box-shadow 0.2s ease;
}

.comment-items-container:hover {
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}

/* 递归评论项间距 */
.comment-list > .comment-item-wrap {
  margin-bottom: 16px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .comment-list {
    padding: 12px 0;
  }
  
  .common-write-content {
    padding: 12px;
    margin-bottom: 20px;
  }
  
  .common-write-content .el-input__inner {
    min-height: 70px;
    font-size: 13px;
  }
  
  .comment-list > div:nth-child(3) {
    font-size: 14px;
    margin-bottom: 12px;
  }
  
  .comment-list > div:nth-child(3)::before {
    width: 20px;
    height: 20px;
  }
  
  .expand {
    padding: 8px 16px;
    font-size: 13px;
  }
}

/* 动画效果 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.comment-item-wrap {
  animation: fadeInUp 0.4s ease-out;
}

/* 平滑滚动 */
* {
  scroll-behavior: smooth;
}
</style>