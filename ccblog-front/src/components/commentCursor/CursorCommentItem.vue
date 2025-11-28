<template>
  <div class="comment-card" :class="{ folded: item.dislike }" :id="`comment-${item.commentId}`">
    <img class="avatar" :src="item.userPhoto || defaultAvatar" />

    <div class="content-wrap">
      <!-- 头部：昵称 + 时间 -->
      <div class="header">
        <span class="nick">{{ item.userName }}</span>
        <span class="time">{{ friendlyTime(item.commentTime) }}</span>
      </div>

      <!-- 正文 -->
      <div v-show="!item.dislike" class="body">{{ item.commentContent }}</div>

      <!-- 彩色操作条 -->
      <!-- 删除 -->
        <el-button
          text
          size="small"
          @click="handleDelete(item)"
          v-if="global.isLogin && global.user.userId === item.userId"
        >
          <el-icon color="#999"><Delete /></el-icon>
          <span class="count">删除</span>
        </el-button>
      
      <div class="action-bar">
        <!-- 点赞 -->
        <el-button
          text
          size="small"
          :class="{ active: item.like }"
          :loading="(item as any)._btnLoading"
          @click="handleLike(item)"
        >
          <!-- <el-icon color="#ff4d4f"><CaretTop /></el-icon> -->
           <img class="read-comment-praise"
                 src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAACXBIWXMAABYlAAAWJQFJUiTwAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAJ9SURBVHgB7VZNbtNQEP7GP7AkN8DcoJyA5gRNTwCR2kqsUm9YEKEaoZRl0hUSBLWcAHOCpjdIT1AfIewgTjzM+AccxwHXLRYS/STnvbyxZ743b34e8L+DcEMMhu+fERk7DLTAuAwRjjz3eVD1+xsROD75eARmr7AczDlsVyVhoCbeDt85mXHmqLtkbst0Ko9jk31aVU9tAgvYW7FxYNJ3D85eufuTOZtKYiZu3X4z/LBdRU9tAia4E0+YL7I1z+3OmPEpkWOrip7aBED0RAcb7K8KeBb/alD+LQIa+TI48kxfuAfTVSlVMlybgAafpN1RYgujopwIOzpGEhuogGuloTc8bd2j5TmS8/VfHu7t5uUaeCaRygORPaqi00JFJAVn2UuNa667xXfEeJZ+rePR+Kool7gIQg67+RpBJTvo5QMoZHPXxFLW8Tld2lhoUqMOfg/JlKjdT2Pnpwf0bDlx3worG1EnPlENF0m5OayO5+7NyjRLHXgMfCsNQhumrBviRfRAhnqyu0KAYXmpcX/BfKKekL+dvBIheOUddkuNK7QO6A43ycXDvolYr5OtGTnlD3VU41rVZLdfccvIipMYDdYINAHZZJyicgR+4wQ0hbVH6Px7RBeNE9BM0lGbVxorTRNImhcxf8mvNxcDafOywBM0TUCqqEa/I09QbF6NECDQdjKuN6hGCJSlX2MENqXfGgFKS6hlmA9wi7hvLJ/qWEy/DL96AfMlEXWkU/mD0XiCkjud7kRk56iOltwRYz3ShM7KXlhpx4PheBR3qxzS6zbSi0YtyOZe9919D38ioNC2vIDl6NyQC2bWtzWVomvf93gWwg7KXH+HfwY/AGsn+Lf3Dim6AAAAAElFTkSuQmCC"/>
            
          <span class="count">{{ item.likeCnt || '点赞' }}</span>
        </el-button>

        <!-- 点踩（折叠） -->
        <el-button
          text
          size="small"
          :class="{ active: item.dislike }"
          @click="handleDislike(item)"
        >
          <el-icon color="#9333ea"><Remove /></el-icon>
          <span class="count">{{ item.dislike ? '已折叠' : '点踩' }}</span>
        </el-button>

        <!-- 回复 -->
        <el-button
          text
          size="small"
          @click="replyStatusChange"
        >
          <el-icon color="#0969da"><ChatDotRound /></el-icon>
          <span class="count">
            {{ !replyEnabled && !item.dislike ? (item.userName ? `回复(${item.userName})` : '回复') : '取消回复' }}
          </span>
        </el-button>
      </div>

      <!-- 回复输入框 -->
      <div v-if="replyEnabled" class="reply-box">
        <el-input
          v-model="textarea"
          :rows="2"
          resize="none"
          type="textarea"
          :placeholder="'回复 @' + item.userName"
        />
        <div class="reply-action">
          <el-button
            size="small"
            type="primary"
            :disabled="!textarea.trim() || isCommenting"
            @click="handleReply"
          >
            评论<el-icon class="el-icon--right"><ChatSquare /></el-icon>
          </el-button>
        </div>
      </div>

      <!-- 子评论插槽 -->
      <slot v-if="!item.dislike" name="children" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ChatDotRound, ChatSquare, Delete} from '@element-plus/icons-vue'  // ① 局部彩色图标

import type { CommentItemDTO } from '@/http/ResponseTypes/CommentType/CursorCommentType';

import dayjs from 'dayjs'
import type { CommonResponse } from "@/http/ResponseTypes/CommonResponseType";
import relativeTime from 'dayjs/plugin/relativeTime'
import { COMMENT_LIKE_URL, COMMENT_SUBMIT_URL,COMMENT_DISLIKE_URL,COMMENT_DELETE_URL } from '@/http/URL'
import { doGet,doPost } from '@/http/BackendRequests'
import { useGlobalStore } from '@/stores/global'
import { OperateTypeEnum } from '@/constants/OperateTypeConstants'
import { messageTip } from '@/util/utils'
const globalStore = useGlobalStore()
const global = globalStore.global
const showLoginDialog = inject<() => void>('loginDialogClicked')
import { inject, ref } from 'vue'
import { useRoute } from 'vue-router'
dayjs.extend(relativeTime)
import { Remove } from '@element-plus/icons-vue' // 折叠要用
import { CommentOperationType } from '@/constants/CommentOperationType';
import { ElMessageBox } from 'element-plus';

// 拿到上层传下来的参数
const props = defineProps<{
  item: CommentItemDTO
}>()
const authorId  = inject<number>('authorId')!
const parentId  = inject<number>('parentId')!
const topId     = inject<number>('topId')!
const resetAndReload = inject<(c: CommentItemDTO) => void>('resetAndReload')!
const articleId     = inject<number>('articleId')!

// 下面定义点击事件
const friendlyTime = (iso: string) => dayjs(iso).fromNow()

/* ===== 点赞 ===== */
/* ===== 删除 ===== */
async function handleDelete(item: CommentItemDTO) {
  if (!global.isLogin) {
    showLoginDialog?.()
    return
  }
  await ElMessageBox.confirm('删除后不可恢复，确定继续？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })

  const btnKey = `delete_${item.commentId}`
  if ((item as any)._btnLoading) return
  ;(item as any)._btnLoading = true

  try {
    await doGet<CommonResponse>(COMMENT_DELETE_URL, {
      commentId: item.commentId,
      type: CommentOperationType.DELETE
    })
    messageTip('删除成功', 'success')
    /* 通知父级刷新（把当前评论从列表移除） */
    // removeAndReload(item) // 改为不移除方案
    item.commentContent = '该评论已被删除' // 修改为占位方案
  } catch {
    messageTip('删除失败', 'error')
  } finally {
    ;(item as any)._btnLoading = false
  }
}

/* ===== 点赞 ===== */
async function handleLike(item: CommentItemDTO) {
  if (!global.isLogin) {
    showLoginDialog?.()
    return
  }
  const btnKey = `like_${item.commentId}`          // 简单 loading 键
  if ((item as any)._btnLoading) return            // 防抖
  ;(item as any)._btnLoading = true

  const isCancel = item.like
  try {
    // console.log("点击了点赞,comment_id=",item.commentId)
    await doGet<CommonResponse>(COMMENT_LIKE_URL, {
      commentId: item.commentId,
      type: isCancel ? CommentOperationType.CANCEL_LIKE : CommentOperationType.LIKE
    })

    // 本地即时刷新
    item.like = !isCancel
    item.likeCnt += isCancel ? -1 : 1
  } finally {
    ;(item as any)._btnLoading = false
  }
}

/* ===== 点踩 ===== */
/* 折叠状态 */

/* 点踩 = 折叠 */
async function handleDislike(item:CommentItemDTO) {
   if (!global.isLogin) {
    showLoginDialog?.()
    return
  }
  const btnKey = `dislike_${item.commentId}`          // 简单 loading 键
  if ((item as any)._btnLoading) return            // 防抖
  ;(item as any)._btnLoading = true

  const isCancel = item.dislike
  try {
    // console.log("点击了点踩,comment_id=")
    // console.log("isCancle=",isCancel)
    await doGet<CommonResponse>(COMMENT_DISLIKE_URL, {
      commentId: item.commentId,
      type: isCancel ? CommentOperationType.CANCEL_DISLIKE : CommentOperationType.DISLIKE
    })

    // 本地即时刷新
    item.dislike = !isCancel // 切换折叠/展开
    item.dislikeCnt += isCancel ? -1 : 1
  } finally {
    ;(item as any)._btnLoading = false
  }
}

/* ===== 回复 ===== */
// 输入文本框
const textarea = ref('')
const replyEnabled = ref(false)
const replyStatusChange = () => {
  if (!global.isLogin) {
    showLoginDialog?.()
    return
  }
  replyEnabled.value = !replyEnabled.value
}
const isCommenting  = ref(false)        // 提交中状态
async function handleReply() {
  if (!global.isLogin) {
    showLoginDialog?.()
    return
  }

  isCommenting.value = true
  try {
    const res = await doPost<CommonResponse>(COMMENT_SUBMIT_URL, {
      articleId:     Number(articleId),   // 顶层提供
      commentContent: textarea.value,
      parentCommentId: Number(parentId),
      topCommentId:    Number(topId),
      authorId:        Number(authorId),
    })
    messageTip('回复成功', 'success')
    textarea.value=''
     replyEnabled.value = false   // 发送后收起输入框
    // replyEnabled.value = false
    /* 通知父级（RecursiveCommentItem）刷新子评论 */
    // 3. 把游标重置 → 触发“刷新第一页”逻辑
    resetAndReload(res.data.result!)        // 现在会先清空再拉最新 5 条（含刚才那条）
  } catch {
    messageTip('回复失败', 'error')
  } finally {
    isCommenting.value = false
  }
}




/* 兜底头像 */
const defaultAvatar = 'https://cdn.tobebetterjavaer.com/paicoding/avatar/0092.png'

</script>

<style scoped>
/* 彩色变量：一键换肤 */
:root {
  --primary-color: #4f46e5;
  --secondary-color: #06b6d4;
  --danger-color: #ef4444;
  --warning-color: #f59e0b;
  --info-color: #8b5cf6;
  --success-color: #10b981;
  --card-bg: #ffffff;
  --fold-bg: #f9fafb;
  --radius: 16px;
  --gap: 16px;
  --avatar-size: 32px;
  --time-color: #6b7280;
  --border-color: #e5e7eb;
  --hover-bg: #f9fafb;
  --transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  --shadow: 0 1px 3px rgba(0,0,0,0.05);
  --shadow-hover: 0 6px 20px rgba(0,0,0,0.08);
  --nick-gradient: linear-gradient(90deg, var(--primary-color), var(--info-color));
  --border-gradient: linear-gradient(90deg, var(--primary-color), var(--info-color));
  --reply-color: #0969da;
  --like-color: #ef4444;
  --dislike-color: #9333ea;
}

.comment-card {
  display: flex;
  gap: 12px;
  padding: 16px;
  background: var(--card-bg);
  border-radius: var(--radius);
  margin-bottom: var(--gap);
  box-shadow: var(--shadow);
  transition: var(--transition);
  position: relative;
  overflow: hidden;
  border-left: 3px solid var(--secondary-color);
  transform-origin: top;
  animation: fadeInUp 0.5s ease-out;
  
  &::before {
    content: '';
    position: absolute;
    left: -3px;
    top: 0;
    bottom: 0;
    width: 3px;
    background: var(--border-gradient);
    opacity: 0;
    transition: opacity 0.3s ease;
  }

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 10px 25px -5px rgba(79, 70, 229, 0.15), 0 8px 10px -6px rgba(79, 70, 229, 0.1);
    background-color: var(--hover-bg);
  }
  
  &:hover::before {
    opacity: 1;
  }
}

/* 左侧彩色指示器 */
.comment-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 0;
  background: linear-gradient(to bottom, var(--reply-color), #3b82f6);
  transition: height 0.3s ease;
  border-radius: 0 2px 2px 0;
}

.comment-card:hover {
  box-shadow: var(--shadow-hover);
  transform: translateY(-1px);
}

.comment-card:hover::before {
  height: 100%;
}

.comment-card.folded {
  background: var(--fold-bg);
  opacity: 0.8;
}

.avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  flex-shrink: 0;
  object-fit: cover;
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  position: relative;
  overflow: hidden;
  z-index: 1;
}

.comment-card:hover .avatar {
  transform: scale(1.1) translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  filter: brightness(1.05);
}

.content-wrap {
  flex: 1;
  min-width: 0;
}

.header {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 8px;
  margin-bottom: 6px;
  flex-wrap: wrap;
  line-height: 1.4;
}

.nick {
  font-weight: 600;
  font-size: 14px;
  transition: all 0.3s ease;
  color: #333;
  display: inline-block;
  position: relative;
  
  &::after {
    content: '';
    position: absolute;
    bottom: -2px;
    left: 0;
    width: 0;
    height: 2px;
    background: var(--primary-color);
    transition: width 0.3s ease;
  }
  
  .comment-card:hover & {
    color: var(--primary-color);
    transform: translateY(-1px);
  }
  
  .comment-card:hover &::after {
    width: 100%;
    background: var(--primary-color);
  }
}

.comment-card:hover .nick {
  color: var(--reply-color);
}

.time {
  font-size: 12px;
  color: #999;
  transition: color 0.2s ease;
  margin-left: auto;
}

.body {
  color: #333;
  line-height: 1.6;
  font-size: 14px;
  margin-bottom: 10px;
  word-break: break-word;
  transition: color 0.3s ease;
  padding-left: 0;
}

.comment-card:hover .body {
  color: #1f2937;
}

/* 操作按钮组样式优化 */
.action-bar {
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 13px;
  color: #6b7280;
  user-select: none;
  padding-top: 8px;
  position: relative;
  transition: all 0.3s ease;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: -16px;
    right: -16px;
    height: 1px;
    background: linear-gradient(90deg, transparent, var(--border-color), transparent);
    opacity: 0;
    transition: opacity 0.3s ease;
  }
  
  .comment-card:hover &::before {
    opacity: 1;
  }
}

/* 点赞按钮样式 */
.action-bar button {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 6px 12px;
  border-radius: 20px;
  transition: all 0.3s ease;
  color: inherit;
  font-size: inherit;
  background: transparent;
  border: 1px solid transparent;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  z-index: 1;
  
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(79, 70, 229, 0.05), transparent);
    transition: left 0.5s ease;
    z-index: -1;
  }
  
  &:hover {
    background: rgba(79, 70, 229, 0.08);
    color: var(--primary-color);
    transform: translateY(-1px);
    border-color: rgba(79, 70, 229, 0.2);
    box-shadow: 0 2px 8px rgba(79, 70, 229, 0.1);
  }
  
  &:hover::before {
    left: 100%;
  }
  
  &:active {
    transform: translateY(0);
  }

  /* 点赞按钮样式 */
  &.like-button {
  height: 28px;
  padding: 0 12px;
  font-size: 13px;
  color: #6b7280;
  transition: all 0.2s ease;
  border-radius: 14px;
  border: 1px solid transparent;
  position: relative;
  overflow: hidden;
}
}

.action-bar .like-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 77, 79, 0.1), transparent);
  transition: left 0.5s ease;
}

.action-bar .like-button:hover::before {
  left: 100%;
}

.action-bar .like-button:hover {
  color: var(--like-color);
  background-color: rgba(255, 77, 79, 0.08);
  border-color: rgba(255, 77, 79, 0.2);
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(255, 77, 79, 0.1);
}

.action-bar .like-button.active {
  color: var(--like-color);
  background-color: rgba(255, 77, 79, 0.1);
  border-color: rgba(255, 77, 79, 0.3);
  font-weight: 500;
}

/* 点踩按钮样式 */
.action-bar .dislike-button {
  height: 28px;
  padding: 0 12px;
  font-size: 13px;
  color: #6b7280;
  transition: all 0.2s ease;
  border-radius: 14px;
  border: 1px solid transparent;
  position: relative;
  overflow: hidden;
}

.action-bar .dislike-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(147, 51, 234, 0.1), transparent);
  transition: left 0.5s ease;
}

.action-bar .dislike-button:hover::before {
  left: 100%;
}

.action-bar .dislike-button:hover {
  color: var(--dislike-color);
  background-color: rgba(147, 51, 234, 0.08);
  border-color: rgba(147, 51, 234, 0.2);
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(147, 51, 234, 0.1);
}

.action-bar .dislike-button.active {
  color: var(--dislike-color);
  background-color: rgba(147, 51, 234, 0.1);
  border-color: rgba(147, 51, 234, 0.3);
  font-weight: 500;
}

/* 回复按钮样式 */
.action-bar .reply-button {
  height: 28px;
  padding: 0 12px;
  font-size: 13px;
  color: #6b7280;
  transition: all 0.2s ease;
  border-radius: 14px;
  border: 1px solid transparent;
}

.action-bar .reply-button:hover {
  color: var(--reply-color);
  background-color: rgba(9, 105, 218, 0.08);
  border-color: rgba(9, 105, 218, 0.2);
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(9, 105, 218, 0.1);
}

.count {
  margin-left: 4px;
  font-weight: 500;
  transition: color 0.2s ease;
}

.action-bar .like-button.active .count {
  color: var(--like-color);
}

.action-bar .dislike-button.active .count {
  color: var(--dislike-color);
}

/* 回复框样式优化 */
.reply-box {
  margin-top: 12px;
  padding: 12px;
  background-color: var(--hover-bg);
  border-radius: var(--radius);
  border: 1px solid var(--border-color);
  animation: slideIn 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 3px;
    background: var(--border-gradient);
  }
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(-5px);
    box-shadow: 0 0 0 rgba(0,0,0,0);
  }
  to {
    opacity: 1;
    transform: translateY(0);
    box-shadow: 0 1px 3px rgba(0,0,0,0.05);
  }
}

.reply-box .el-input {
  margin-bottom: 8px;
}

.reply-box .el-input__inner {
  border-radius: 12px;
  border-color: var(--border-color);
  transition: all 0.3s ease;
  resize: none;
  min-height: 80px;
  background: white;
}

.reply-box .el-input__inner:focus {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(79, 70, 229, 0.15);
  border-width: 1px;
  transform: translateY(-1px);
}

.reply-action {
  display: flex;
  justify-content: flex-end;
  margin-top: 8px;
  gap: 8px;
  position: relative;
}

.reply-action .el-button {
  transition: all 0.2s ease;
  border-radius: 8px;
  padding: 6px 16px;
}

.reply-action .el-button:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.reply-action .el-button--primary {
  background: #4f46e5;
  border: none;
  color: white;
  font-weight: 500;
  text-shadow: 0 1px 2px rgba(0,0,0,0.1);
  box-shadow: 0 2px 6px rgba(79, 70, 229, 0.3);
}

.reply-action .el-button--primary:hover:not(:disabled) {
  background: #4338ca;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(79, 70, 229, 0.4);
}

/* 动画效果 */
@keyframes pulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.15); }
  100% { transform: scale(1); }
}

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

.action-bar .like-button.active, .action-bar .dislike-button.active {
  animation: pulse 0.6s cubic-bezier(0.4, 0, 0.2, 1);
  color: var(--success-color);
  background: rgba(16, 185, 129, 0.1);
  border-color: rgba(16, 185, 129, 0.3);
}

.action-bar .dislike-button.active {
  color: var(--danger-color);
  background: rgba(239, 68, 68, 0.1);
  border-color: rgba(239, 68, 68, 0.3);
}

/* 响应式设计 */
@media (max-width: 768px) {
  :root {
    --avatar-size: 12px;
    --radius: 10px;
    --gap: 10px;
  }
  
  .comment-card {
    padding: 14px;
  }
  
  .action-bar {
    gap: 12px;
  }
  
  .action-bar .like-button,
  .action-bar .dislike-button,
  .action-bar .reply-button {
    height: 26px;
    padding: 0 10px;
    font-size: 12px;
  }
}
</style>