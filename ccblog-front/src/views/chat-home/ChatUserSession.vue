<template>
   <HeaderBar></HeaderBar>
    <!-- 正文内容 -->
    <div class="chat-main-container">
    <!-- <div class="notice-wrap"> -->
    <!-- 分情况显示：选择了用户时使用左右分栏布局，否则只显示私信列表 -->
    <div v-if="selectedUserId" class="chat-layout">
      <!-- 左侧聊天列表 -->
      <div class="chat-list-sidebar">
        <!-- 固定的私信列表标题行 -->
        <div class="chat-list-header" style="position: sticky; top: 0; z-index: 100; background-color: #fafafa;">
          <h3>私信列表</h3>
          <div class="header-actions">
            <button class="clear-unread-btn" @click="clearAllUnread" :disabled="!hasUnreadMessages">清空已读</button>
            <button class="close-chat-btn" @click="closeChat">退出聊天</button>
          </div>
        </div>
        <!-- 固定大小的会话容器，内部可滚动 -->
        <div class="chat-list-content" style="height: calc(100% - 60px); overflow-y: auto;">
          <div v-if="loading" class="loading">加载中...</div>
          <div v-else-if="error" class="error">加载失败，请重试</div>
          <div v-else-if="sessionList.length === 0" class="empty-message">暂无聊天会话</div>
          <ChatSessionItem 
            v-for="(item, index) in sessionList" 
            :key="item.id || index"
            :session="item"
            :is-selected="selectedUserId === item.peerId"
            @click="handleChatItemClick(item.peerId)"
            @session-operated="handleSessionOperated"
          />
        </div>
      </div>
      
      <!-- 右侧聊天详情  -->
      <div class="chat-detail">
        <ChatUserView 
          :peer-id="selectedUserId" 
          @update-session-preview="handleUpdateSessionPreview"
        />
      </div>
    </div>
    
    <!-- 未选择用户时，只显示完整的私信列表 -->
    <div v-else class="full-width-private-list">
      <!-- 固定的私信列表标题行 -->
      <div class="private-list-header" style="position: sticky; top: 0; z-index: 100; background-color: #fafafa;">
        <h3>私信列表</h3>
        <button class="clear-unread-btn" @click="clearAllUnread" :disabled="!hasUnreadMessages">清空已读</button>
      </div>
      <!-- 固定大小的会话容器，内部可滚动 -->
      <div class="private-list-content" style="height: calc(100% - 60px); overflow-y: auto;">
        <div v-if="loading" class="loading">加载中...</div>
        <div v-else-if="error" class="error">加载失败，请重试</div>
        <div v-else-if="sessionList.length === 0" class="empty-message">暂无聊天会话</div>
        <ChatSessionItem 
            v-for="(item, index) in sessionList" 
            :key="item.id || index"
            :session="item"
            :is-selected="selectedUserId === item.peerId"
            @click="handleChatItemClick(item.peerId)"
            @session-operated="handleSessionOperated"
          />
      </div>
    </div>
  <!-- 底部信息 -->
    </div>
   <Footer></Footer>
   <LoginDialog :clicked="loginDialogClicked"></LoginDialog>
  <!-- </div> -->
</template>

<script setup lang="ts">
import type { BasicPageType } from '@/http/ResponseTypes/PageType/BasicPageType'
import ChatUserView from '@/views/chat-home/ChatUserView.vue'
import ChatSessionItem from '@/views/chat-home/ChatSessionItem.vue'
import { ref, onMounted, provide } from 'vue'
import { doGet, doPost } from '@/http/BackendRequests'
import { USER_CHAT_SESSION_URL, USER_CHAT_UNREAD_CLEAR_URL } from '@/http/URL'
import type { ChatSessionItemType, ChatSessionResponse } from '@/http/ResponseTypes/ChatType/ChatSessionType'
import type { CommonResponse } from '@/http/ResponseTypes/CommonResponseType'
import HeaderBar from '@/components/layout/HeaderBar.vue'
import Footer from '@/components/layout/Footer.vue'
import { useRoute } from 'vue-router'
import { ChatUserSessionOperationType } from '@/constants/ChatUserSessionOperationTypes'

// 响应式数据：会话列表
const sessionList = ref<ChatSessionResponse['chatUserItems']>([])
const loading = ref(false)
const error = ref(false)
const hasMore = ref(false)
const route = useRoute()
const hasUnreadMessages = ref(false)

// 获取会话列表数据
const fetchSessionList = async (targetPeerId?: number) => {
  loading.value = true
  error.value = false
  
  try {
    // 第一次请求不需要提供cursor和cursorId
    const response = await doGet<ChatSessionResponse>(USER_CHAT_SESSION_URL, {
      limit: 10
    })
    // console.log('获取会话列表响应:', response)
    hasMore.value = response.data.result.hasMore
    
    // 确保数据类型正确转换
    let sessions = response.data.result.chatUserSessionItemDTOS.map((session: { peerId: any; userId: any; peerName: any; username: any; peerPhoto: any; snapshot: any; lastMsgTime: any; unreadCount: any }) => ({
      ...session,
      peerId: Number(session.peerId) || Number(session.userId) || 0,
      peerName: String(session.peerName || session.username || '未知用户'),
      peerPhoto: String(session.peerPhoto || ''),
      snapshot: String(session.snapshot || ''),
      lastMsgTime: String(session.lastMsgTime || ''),
      unreadCount: Number(session.unreadCount || 0)
    }))
    
    // 如果有目标peerId，将对应会话置顶或放在合适位置
    if (targetPeerId) {
      const targetSession = sessions.find((s: { peerId: number }) => s.peerId === targetPeerId)
      if (targetSession) {
        // 从原始数组中移除
        sessions = sessions.filter((s: { peerId: number }) => s.peerId !== targetPeerId)
        
        // 根据会话置顶状态决定放置位置
        if (targetSession.isTop === 1) {
          // 已置顶：直接放到所有会话最上面
          sessions.unshift(targetSession)
        } else {
          // 未置顶：找到最后一个置顶状态会话，放在它后面
          // 从后往前查找最后一个置顶会话的索引
          const lastTopIndex = sessions.findLastIndex((s: { isTop: number }) => s.isTop === 1)
          if (lastTopIndex === -1) {
            // 如果没有置顶会话，直接添加到开头
            sessions.unshift(targetSession)
          } else {
            // 放在最后一个置顶会话的后面
            sessions.splice(lastTopIndex + 1, 0, targetSession)
          }
        }
      }
    }
    
    sessionList.value = sessions
    // 检查是否有未读消息
    hasUnreadMessages.value = sessionList.value.some(session => session.unreadCount > 0)
    console.log('会话列表:', sessionList.value)
  } catch (err) {
    console.error('获取会话列表失败:', err)
    error.value = true
  } finally {
    loading.value = false
  }
}

// 组件挂载时获取会话列表
onMounted(async () => {
  // console.log('ChatUserSession mounted')
  
  // 检查URL参数中是否有peerId
  const peerIdParam = route.query.peerId
  const targetPeerId = peerIdParam ? Number(peerIdParam) : undefined
  
  // 获取会话列表，如果有peerId则置顶
  await fetchSessionList(targetPeerId) // 可以把targetPeerId传进去，置顶该会话 用于发送信息自动打开会话
  
  // 如果有peerId参数，自动选中该会话
  if (targetPeerId && targetPeerId > 0) {
    // 确保在DOM更新后执行选择操作
    setTimeout(() => {
      handleChatItemClick(targetPeerId)
    }, 100)
  }
})

// 响应式数据：当前选中的聊天用户ID
const selectedUserId = ref<number | null>(null)

// 处理聊天项点击事件
const handleChatItemClick = (peerId: number) => {
  selectedUserId.value = peerId
  // 将选中会话的未读消息数设为0，移除红点提示
  const session = sessionList.value.find(item => item.peerId === peerId)
  if (session) {
    session.unreadCount = 0
  }
}

// 关闭聊天，返回到私信列表视图
const closeChat = () => {
  selectedUserId.value = null
}

// 清空所有已读消息
const clearAllUnread = async () => {
  try {
    const response = await doPost<CommonResponse>(USER_CHAT_UNREAD_CLEAR_URL, {})
    if (response.data.status.code === 0) {
      // 清空所有会话的未读消息数
      sessionList.value.forEach(session => {
        session.unreadCount = 0
      })
      // 更新未读消息状态
      hasUnreadMessages.value = false
      console.log('已清空所有未读消息')
    }
  } catch (error) {
    console.error('清空未读消息失败:', error)
  }
}

// 处理会话预览更新
const handleUpdateSessionPreview = (data: { 
  peerId: number; 
  preview: string; 
  time: string; 
  userName?: string; 
  userPhoto?: string 
}) => {
  // 查找对应peerId的会话
  const sessionIndex = sessionList.value.findIndex(item => item.peerId === data.peerId)
  if (sessionIndex !== -1) {
    // 更新预览信息和时间
    sessionList.value[sessionIndex].snapshot = data.preview
    sessionList.value[sessionIndex].lastMsgTime = data.time
    
    // 如果有用户信息，更新用户信息
    if (data.userName) {
      sessionList.value[sessionIndex].peerName = data.userName
    }
    if (data.userPhoto) {
      sessionList.value[sessionIndex].peerPhoto = data.userPhoto
    }
    
    // 获取当前会话是否置顶
    const isTopSession = sessionList.value[sessionIndex].isTop === 1
    
    // 如果会话位置需要调整
    if (sessionIndex > 0) {
      // 移除当前会话
      const updatedSession = sessionList.value.splice(sessionIndex, 1)[0]
      
      // 查找最后一个置顶会话的索引
      const lastTopSessionIndex = sessionList.value.findLastIndex(s => s.isTop === 1)
      
      if (isTopSession) {
        // 如果是置顶会话，放到所有置顶会话的最上面（即列表最前面）
        sessionList.value.unshift(updatedSession)
      } else {
        // 如果不是置顶会话，放到最后一个置顶会话的后面
        // 如果没有置顶会话，放到第一个位置
        sessionList.value.splice(lastTopSessionIndex + 1, 0, updatedSession)
      }
    }
  } else {
    // 如果会话不存在，创建一个新的会话
    const newSession: ChatSessionItemType = {
      peerId: data.peerId,
      peerName: data.userName || `用户${data.peerId}`, // 使用真实用户名或临时名称
      peerPhoto: data.userPhoto || '', // 使用真实头像或空头像
      snapshot: data.preview,
      lastMsgTime: data.time,
      unreadCount: 0,
      id: 0,
      userId: 0,
      isTop: 0,
      isMute: 0,
      isDelete: 0,
      sessionId: undefined
    }
    
    // 查找最后一个置顶会话的索引
    const lastTopSessionIndex = sessionList.value.findLastIndex(s => s.isTop === 1)
    
    // 新会话默认是非置顶的，放到最后一个置顶会话的后面
    // 如果没有置顶会话，放到第一个位置
    sessionList.value.splice(lastTopSessionIndex + 1, 0, newSession)
    console.log('创建了新会话:', newSession)
  }
}

// 处理会话操作（删除、置顶等）
const handleSessionOperated = (sessionId: number, operationType: number) => {
  // 根据操作类型执行相应操作
  switch (operationType) {
    case ChatUserSessionOperationType.DELETE:
      // 删除会话
      const index = sessionList.value.findIndex(item => item.sessionId === sessionId)
      if (index !== -1) {
        // 获取要删除的会话的peerId
        const peerIdToDelete = sessionList.value[index].peerId
        // 从列表中移除会话
        sessionList.value.splice(index, 1)
        // 如果删除的是当前选中的会话，关闭聊天
        if (selectedUserId.value === peerIdToDelete) {
          selectedUserId.value = null
        }
      }
      break
    case ChatUserSessionOperationType.TOP:
      // 置顶会话
      const topIndex = sessionList.value.findIndex(item => item.sessionId === sessionId)
      if (topIndex !== -1) {
        const session = sessionList.value.splice(topIndex, 1)[0]
        // 更新置顶状态
        session.isTop = 1
        // 添加到列表顶部
        sessionList.value.unshift(session)
      }
      break
    case ChatUserSessionOperationType.CANCEL_TOP:
      // 取消置顶会话
      const cancelTopIndex = sessionList.value.findIndex(item => item.sessionId === sessionId)
      if (cancelTopIndex !== -1) {
        const session = sessionList.value.splice(cancelTopIndex, 1)[0]
        // 更新置顶状态
        session.isTop = 0
        // 简单实现：添加到列表末尾
        // 实际应用中可能需要根据时间排序
        sessionList.value.push(session)
      }
      break
    default:
      break
  }
}

// 登录框
const changeClicked = () => {
  loginDialogClicked.value = !loginDialogClicked.value
  console.log("clicked: ", loginDialogClicked.value)
}

provide('loginDialogClicked', changeClicked)
const loginDialogClicked = ref(false)
</script>



<style scoped>
/* 主容器样式 - 固定高度并隐藏溢出 */
.chat-main-container {
  position: relative;
  height: calc(100vh - 130px); /* 减去头部和底部的高度，留出一些空间 */
  overflow: hidden;
  width: 100%;
  max-width: 1800px;
  margin: 0 auto;
}

/* 分栏布局 */
.chat-layout {
  display: flex;
  height: 100%;
  width: 100%;
  background-color: #f5f5f5;
  overflow: hidden;
  max-width: 1700px;
  margin: 0 auto;
}

/* 左侧聊天列表 */
.chat-list-sidebar {
  width: 400px;
  background-color: #fff;
  border-right: 1px solid #e0e0e0;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.05);
}

.chat-list-header {
  padding: 16px 20px;
  border-bottom: 1px solid #e0e0e0;
  background-color: #fafafa;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chat-list-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.clear-unread-btn {
  background-color: #67c23a;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.2s;
}

.clear-unread-btn:hover {
  background-color: #5aaf31;
}

.clear-unread-btn:disabled {
  background-color: #c0c4cc;
  cursor: not-allowed;
}

.clear-unread-btn:disabled:hover {
  background-color: #c0c4cc;
}

.close-chat-btn {
  background-color: #f56c6c;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.2s;
}

.close-chat-btn:hover {
  background-color: #e64949;
}

.private-list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chat-list-content {
  flex: 1;
  overflow-y: auto;
}

.chat-list-content > div {
  cursor: pointer;
}

.chat-list-content > div.selected {
  background-color: #f0f0f0;
}

/* 会话省略号鼠标悬停才显示 */
.chat-list-content > div .session-actions {
  opacity: 0;
  transition: opacity 0.2s ease;
}

.chat-list-content > div:hover .session-actions {
  opacity: 1;
}

/* 确保ChatSessionItem组件中的操作按钮也应用此样式 */
:deep(.chat-session-item) .session-actions {
  opacity: 0;
  transition: opacity 0.2s ease;
}

:deep(.chat-session-item):hover .session-actions {
  opacity: 1;
}

.loading, .error, .empty-message {
  padding: 20px;
  text-align: center;
  color: #666;
}

.error {
  color: #f56c6c;
}

/* 未选择用户时的完整私信列表样式 */
.full-width-private-list {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  background-color: #fff;
  overflow: hidden;
  max-width: 900px;
  margin: 0 auto;
}

.private-list-header {
  padding: 16px 20px;
  border-bottom: 1px solid #e0e0e0;
  background-color: #fafafa;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.private-list-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.private-list-content {
  flex: 1;
  overflow-y: auto;
}

.private-item-wrapper {
  cursor: pointer;
  transition: background-color 0.2s;
}

.private-item-wrapper:hover {
  background-color: #f9f9f9;
}

.chat-detail {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #fafafa;
  overflow: hidden; /* 防止整个聊天区域溢出 */
  position: relative;
  height: 100%;
  min-height: 400px;
  max-width: 1200px;
  /* 设置最小高度，确保在小屏幕上也有合理的显示 */
}

/* 确保ChatUserView组件正确显示 */
:deep(.chat-container) {
  height: 100%;
  display: flex;
  flex-direction: column;
}

/* 确保ChatUserView组件内容区域在固定容器内滚动 */
:deep(.chat-user-view-container) {
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: #fff;
  overflow: hidden;
}

:deep(.chat-messages) {
  flex: 1;
  overflow-y: auto;
  min-height: 0; /* 确保内容足够时可以滚动 */
}

/* 响应式布局 */
@media (max-width: 1400px) {
  .chat-main-container {
    max-width: 100%;
  }
  
  .chat-layout {
    max-width: 100%;
  }
  
  .chat-list-sidebar {
    width: 360px;
  }
}

@media (max-width: 768px) {
  .chat-main-container {
    height: calc(100vh - 140px); /* 移动端可能需要调整高度 */
    max-width: 100%;
  }
  
  .chat-layout {
    flex-direction: column;
    height: 100%;
    max-width: 100%;
  }
  
  .chat-list-sidebar {
    width: 100%;
    height: 300px;
    border-right: none;
    border-bottom: 1px solid #e0e0e0;
  }
  
  .chat-detail {
    width: 100%;
    flex: 1;
    min-height: 400px;
    max-width: 100%;
  }
  
  .full-width-private-list {
    height: 100%;
    max-width: 100%;
  }
  
  /* 移动端始终显示操作按钮 */
  :deep(.chat-session-item) .session-actions {
    opacity: 1;
  }
}

@media (max-width: 480px) {
  .chat-list-header,
  .private-list-header {
    padding: 12px 16px;
  }
  
  .chat-list-header h3,
  .private-list-header h3 {
    font-size: 16px;
  }
  
  .close-chat-btn {
    padding: 4px 8px;
    font-size: 12px;
  }
}
</style>
