<template>
  <div class="comment-item-wrap">
    <CursorCommentItem
      :item="item"
    >
      <template #children>
        
        <!-- 子评论区块 -->
        <div v-if="showSub" class="sub-wrap">
          <RecursiveCommentItem
            v-for="c in children"
            :key="c.commentId"
            :item="c"
            :comment-parents="props.commentParents"
            :target-index="props.targetIndex !== undefined ? props.targetIndex + 1 : undefined"
            :if-ok="props.ifOk"
            :authorId="props.authorId"
          />
          <div class="expand-container" v-if="hasMore">
            <div class="expand" @click="loadMore()">展开更多</div>
          </div>
        </div>
        <!-- 展开/收起一键切换 -->
        
        <div
          v-if="item.replyCnt > 0"
          class="toggle"
          @click="toggle"
        >
          {{ showSub ? '收起回复' : '展开回复' }}
        </div>
      </template>
    </CursorCommentItem>
  </div>
</template>

<script setup lang="ts">
import { provide,ref, computed, onMounted, watch, inject } from 'vue'
import { doGet } from '@/http/BackendRequests'
import type { CommonResponse } from '@/http/ResponseTypes/CommonResponseType'
import type { CommentItemDTO, CommentCursorVO } from '@/http/ResponseTypes/CommentType/CursorCommentType'
import CursorCommentItem from './CursorCommentItem.vue'
import { useRoute, useRouter } from 'vue-router'
import { COMMENT_CURSOR_URL } from '@/http/URL'
import { dayjs } from 'element-plus'
const route = useRoute()

const articleId = route.params.articleId
const limit = "5"

const props = defineProps<{
  // articleId: number
  authorId: number // 评论需要把文章作者id也传下去
  item: CommentItemDTO          // 当前这条评论
  commentParents?: number[]     // 评论链ID数组
  targetIndex?: number          // 目标评论在commentParents中的索引位置
  ifOk?: boolean               // 是否是已经完成了目标函数定位
}>()
// console.log("文章作者id:",props.authorId)

// 提供给子函数
provide('articleId',     articleId)      // 常量
provide('parentId',      props.item.commentId) // 当前节点就是"父级"
provide('topId',         props.item.topCommentId)
provide('authorId',      props.authorId) // 提供文章作者ID给子组件
// 添加评论的子级回调
function resetAndReload(newComment: CommentItemDTO) {
  children.value.unshift(newComment)          // 置顶
  // 重置游标为“新最后一条”
  const last = children.value[children.value.length - 1]
  cursor.value = last.commentTime
  cursorId.value = Number(last.commentId)
  // 如果当前总数 < limit，说明后端不会再有下一页(决定是否显示展开更多)
  hasMore.value = children.value.length >= Number(limit)
  // 因为自动显示新的数据一定是展开了,这时候直接显示收起
  props.item.replyCnt = 0   // ← 置为 true，收起按钮立即出现
  // 保持展开（不强制收起）
  if (!showSub.value) showSub.value = true
}
provide('resetAndReload', resetAndReload)

const emitOk = inject<() => void>('emitOk')!
provide('emitOk', emitOk)

// 删除评论的子级回调
function removeAndReload(deletedItem: CommentItemDTO) {
 // 先看是不是我直接子级
  const idx = children.value.findIndex(c => c.commentId === deletedItem.commentId)
  if (idx !== -1) {
    // 是我直接子级，本地处理掉，结束
    children.value.splice(idx, 1)
    hasMore.value = children.value.length >= Number(limit)
    if (children.value.length === 0) {
      showSub.value = false
      props.item.replyCnt = 0
    }
    return // 终止冒泡,删除完成
  }

  // 不是该层子级，往上抛一层
  emit('deleteComment', deletedItem)
}
provide('removeAndReload', removeAndReload)

const emit = defineEmits<{
//   like: [item: CommentItemDTO]
//   dislike: [item: CommentItemDTO]
//   reply: [item: CommentItemDTO]
  // loadMore: [parent: CommentItemDTO]   // 向父级要更多同级
  deleteComment: [target: CommentItemDTO]   // 新增
}>()


/* 子评论状态（仅当前节点） */
const children = ref<CommentItemDTO[]>([])
const hasMore = ref(true)
const cursor = ref<string>()
const cursorId = ref<number>()

/* 首次展开才拉数据 */
const loaded = ref(false)
// 确保数据已经加载
async function ensureLoad() {
  if (loaded.value) return
  loaded.value = true
  await fetchChildren()
}

/* 外部点击“展开更多”时触发 */
function loadMore() {
  fetchChildren()
}

// 加载子评论的函数
async function fetchChildren() {
  // 对于评论链中的评论，忽略hasMore的限制
  // 总是尝试加载下一页数据，直到找到目标评论
  const params: Record<string, any> = {
    articleId: articleId,
    parentId: Number(props.item.commentId),
    limit: 5 // 设置合适的每页加载数量
  }

  if (cursor.value) {
    params.cursor = dayjs(cursor.value).format('YYYY-MM-DDTHH:mm:ss') // 去时区
    params.cursorId = cursorId.value
  }
  
  try {
    const res = await doGet<CommonResponse<CommentCursorVO>>(COMMENT_CURSOR_URL, params)
    const vo = res.data.result!
    children.value.push(...vo.comments)
    
    // 仍然更新hasMore，但在processCommentChain中不依赖它
    hasMore.value = vo.hasMore
    if (vo.comments.length) {
      const last = vo.comments[vo.comments.length - 1]
      cursor.value = last.commentTime
      cursorId.value = Number(last.commentId)
    }
  } catch (error) {
    console.error('获取评论数据失败:', error)
    hasMore.value = false // 出错时停止加载
  }
}

/* 默认把“收起/展开”逻辑放到当前节点 */
const showSub = ref(false)
function toggle() {
  showSub.value = !showSub.value
  // 当展开时，无论如何都尝试加载数据
  if (showSub.value) ensureLoad()
}

// 递归查找评论链的函数，targetIndex表示当前要查找的目标评论在commentParents中的索引位置
async function findCommentInChain(targetIndex = -1) {
  // console.log("targetIndex:",targetIndex)
  if(!props.commentParents||targetIndex>0&&props.commentParents[targetIndex-1]!==props.item.commentId) return false // 直接返回
  try {
    // 当前需要查找的目标评论ID
    const targetCommentId = props.commentParents[targetIndex];
    
    await ensureLoad()

    let found = children.value.some(child => {
      console.log('child.commentId:', child.commentId, 'targetCommentId:', targetCommentId)
      return Number(child.commentId) === Number(targetCommentId)
    })
    let cnt = 0
    showSub.value = true 
    while (!found && hasMore.value && cnt < 20) {
      cnt++
      // 再拉下一页
      await fetchChildren() // 展开评论：继续拉取下一页子评论
      found = children.value.some(child => {
      console.log('child.commentId:', child.commentId, 'targetCommentId:', targetCommentId)
      return Number(child.commentId) === Number(targetCommentId)
      })
      if (found) {
        break
      }
    }

    // 如果这是最后一个目标评论，滚动到该位置
    if (targetCommentId === props.commentParents[props.commentParents.length - 1]) {
      // 直接通过事件通知顶级评论更新状态(已找到)
      emitOk()

      // 短暂延迟确保DOM已经渲染完成
      setTimeout(() => {
        const element = document.getElementById(`comment-${targetCommentId}`);
        if (element) {
            element.scrollIntoView({ behavior: 'smooth', block: 'center' });
            // 高亮提示更明显：黄色背景 + 红色边框
            element.classList.add('bg-yellow-200', 'border-2', 'border-blue-300');
            setTimeout(() => {
              element.classList.remove('bg-yellow-200', 'border-2', 'border-blue-300');
            }, 2000);
          }
        }, 300);
        return true;
      } 
  } catch (error) {
    console.error('查找评论链时出错:', error);
    return false;
  }
}


// 组件挂载时处理评论链展开
onMounted(async () => {
  // 根据传入的targetIndex开始处理评论链展开逻辑
  // console.log("props.targetIndex:",props.targetIndex)
  // 正常评论不用管/顶级评论不用管(length为1)/target超出不用管(已经找到了)
  if(props.targetIndex === undefined||props.commentParents&&props.commentParents.length==1||
      props.commentParents && props.targetIndex>=props.commentParents.length) return
  if(props.ifOk) return
  // console.log("props.ifOk:",props.ifOk)
  await findCommentInChain(props.targetIndex);
});

/* 提供给 CursorCommentItem 的 slot 作用域 */
provide('toggle', toggle)
</script>

<style scoped>
/* 子评论包装器样式 */
.sub-wrap {
  margin-left: 50px;
  position: relative;
  padding-left: 20px;
}

/* 层级连接线样式 */
.sub-wrap::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 2px;
  background: linear-gradient(to bottom, #e5e7eb 60%, transparent);
}

/* 展开更多按钮容器 */
.expand-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  margin: 12px 0;
  padding: 0;
}

/* 展开更多按钮样式 */
.expand {
  text-align: center;
  color: var(--reply-color, #0969da);
  cursor: pointer;
  font-size: 13px;
  padding: 8px 20px;
  border-radius: 6px;
  transition: all 0.2s ease;
  background: transparent;
  border: 1px solid var(--border-color, #e5e7eb);
  display: inline-block;
  width: auto;
  min-width: 100px;
}

.expand:hover {
  background-color: var(--hover-bg, #f9fafb);
  border-color: var(--reply-color, #0969da);
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

/* 展开/收起切换按钮样式 */
.toggle {
  cursor: pointer;
  color: var(--time-color, #6b7280);
  font-size: 12px;
  padding: 4px 8px;
  border-radius: 4px;
  transition: all 0.2s ease;
  margin-left: auto;
  display: inline-flex;
  align-items: center;
  gap: 4px;
  user-select: none;           /* 可选：防止文字被选中 */
}

.toggle:hover {
  background-color: var(--hover-bg, #f9fafb);
  color: var(--reply-color, #0969da);
}

.toggle-icon {
  transition: transform 0.2s ease;
}

.toggle:hover .toggle-icon {
  transform: rotate(45deg);
}

/* 子评论容器样式 */
.children-container {
  position: relative;
}

/* 动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(5px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.sub-wrap > .comment-item-wrap {
  animation: fadeIn 0.3s ease-out;
}

/* 高亮目标评论的样式优化 */
:deep(.bg-yellow-200) {
  animation: highlightPulse 2s ease-in-out;
}

@keyframes highlightPulse {
  0%, 100% {
    background-color: rgba(250, 204, 21, 0.2);
    border-color: #facc15;
  }
  50% {
    background-color: rgba(250, 204, 21, 0.3);
    border-color: #fcd34d;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .sub-wrap {
    margin-left: 30px;
    padding-left: 12px;
  }
  
  .toggle {
    font-size: 12px;
    padding: 4px 8px;
  }
  
  .expand {
    font-size: 12px;
    padding: 6px 12px;
  }
}
</style>