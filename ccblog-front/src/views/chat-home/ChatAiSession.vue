<template>
  <HeaderBar></HeaderBar>
  <!-- 正文内容 -->
  <div class="chat-main-container">
    <!-- 分情况显示：选择了AI会话时使用三栏布局，否则只显示AI会话列表 -->
    <div v-if="selectedSessionId" class="chat-layout">
    <!-- 左侧AI会话列表 -->
    <div class="chat-list-sidebar">
      <div class="chat-list-header">
        <h3>AI 会话列表</h3>
        <button class="close-chat-btn" @click="closeChat" title="退出聊天">
          <span class="icon">×</span>
          <span>退出聊天</span>
        </button>
      </div>
      <div class="chat-list-content">
        <div v-if="loading" class="loading">
          <div class="loading-spinner"></div>
          <span>加载中...</span>
        </div>
        <div v-else-if="error" class="error">
          <span class="error-icon">⚠</span>
          <span>加载失败，请重试</span>
        </div>
        <div v-else-if="sessionList.length === 0" class="empty-message">
          <div class="empty-icon">💬</div>
          <p>暂无AI会话</p>
          <button class="create-first-chat-btn" @click="createNewSession">创建第一个会话</button>
        </div>
        <div 
          v-for="(item, index) in sessionList" 
          :key="item.sessionId || index"
          :class="['ai-session-item', { 'selected': selectedSessionId === String(item.sessionId) }]"
          @click="handleSessionItemClick(item.sessionId)"
        >
          <div class="session-info">
            <div class="session-name">{{ item.title }}</div>
            <div v-if="item.lastMsgTime" class="session-time">{{ formatSessionTime(item.lastMsgTime) }}</div>
          </div>
          <div class="session-actions">
            <button 
              class="action-btn" 
              @click.stop="showContextMenu($event, item)"
              title="操作"
            >
              <span class="icon-dots">•••</span>
            </button>
          </div>
        </div>
        <!-- 加载更多按钮 -->
        <div v-if="hasMore && sessionList.length > 0" class="load-more-container">
          <button 
            class="load-more-btn"
            :disabled="loading"
            @click="loadMoreSessions"
          >
            {{ loading ? '加载中...' : '展开更多' }}
          </button>
        </div>
      </div>
    </div>
    
    <!-- 中间聊天详情 -->
    <div class="chat-detail">
      <ChatAiView 
        ref="chatAiViewRef"
        :session-id="selectedSessionId || ''"
        :ai-model="selectedAiModel"
        @update-session-preview="handleUpdateSessionPreview"
        @update-session-id="handleUpdateSessionId"
      />
    </div>
    
    <!-- 右侧AI模型信息栏 -->
    <div class="ai-info-sidebar">
      <div class="ai-info-header">
        <h4>AI模型信息</h4>
      </div>
      <div class="ai-info-content">
        <div class="model-card" :class="{ active: selectedAiModel === 'ai1' }" @click="selectModel('ai1')">
          <div class="model-icon">🧠</div>
          <div class="model-name">QWEN 1.7B</div>
          <div class="model-desc">轻量级中文大语言模型，响应迅速</div>
          <div class="model-stats">
            <span class="stat-item">参数：1.7B</span>
            <span class="stat-item">擅长：日常对话</span>
          </div>
          <div class="select-indicator" v-if="selectedAiModel === 'ai1'">✓</div>
        </div>
        
        <div class="model-card" :class="{ active: selectedAiModel === 'ai2' }" @click="selectModel('ai2')">
          <div class="model-icon">🤖</div>
          <div class="model-name">DEEPSEEK r1</div>
          <div class="model-desc">代码与推理能力出色的模型</div>
          <div class="model-stats">
            <span class="stat-item">参数：7B</span>
            <span class="stat-item">擅长：编程辅助</span>
          </div>
          <div class="select-indicator" v-if="selectedAiModel === 'ai2'">✓</div>
        </div>
        
        <div class="model-tips">
          <h5>💡 使用提示</h5>
          <ul>
            <li>清晰描述您的问题</li>
            <li>提供足够的上下文信息</li>
            <li>对复杂问题分步提问</li>
            <li>使用简洁准确的语言</li>
          </ul>
        </div>
      </div>
    </div>
  </div>
  
  <!-- 未选择会话时，只显示完整的AI会话列表 -->
    <div v-else class="full-width-ai-list">
    <div class="ai-list-header">
      <h3>AI 会话列表</h3>
      <button class="new-chat-btn" @click="createNewSession" title="创建新会话">
        <span class="icon">+</span>
        <span>新会话</span>
      </button>
    </div>
    <div class="ai-list-content">
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else-if="error" class="error">加载失败，请重试</div>
      <div v-else-if="sessionList.length === 0" class="empty-message">暂无AI会话</div>
      <div 
          v-for="(item, index) in sessionList" 
          :key="item.sessionId || index"
          class="ai-session-item"
          @click="handleSessionItemClick(item.sessionId)"
        >
          <div class="session-info">
            <div class="session-name">{{ item.title }}</div>
            <div v-if="item.lastMsgTime" class="session-time">{{ formatSessionTime(item.lastMsgTime) }}</div>
          </div>
          <div class="session-actions">
            <button 
              class="action-btn" 
              @click.stop="showContextMenu($event, item)"
              title="操作"
            >
              <span class="icon-dots">•••</span>
            </button>
          </div>
        </div>
      <!-- 加载更多按钮 -->
      <div v-if="hasMore && sessionList.length > 0" class="load-more-container">
        <button 
          class="load-more-btn"
          :disabled="loading"
          @click="loadMoreSessions"
        >
          {{ loading ? '加载中...' : '展开更多' }}
        </button>
      </div>
    </div>
    </div>
  </div>
  <!-- 底部信息 -->
  <Footer></Footer>
  <LoginDialog :clicked="loginDialogClicked"></LoginDialog>
  
  <!-- 操作菜单 -->
  <div 
    v-if="showMenu" 
    class="context-menu" 
    :style="{ left: menuPosition.x + 'px', top: menuPosition.y + 'px' }"
    @click.stop
    @mouseenter="handleMenuEnter"
    @mouseleave="handleMenuLeave"
  >
    <div class="menu-item" @click="handleRenameClick">
      <span class="menu-icon">✏️</span>
      <span>编辑标题</span>
    </div>
    <div class="menu-divider"></div>
    <div class="menu-item delete" @click="deleteSession">
      <span class="menu-icon">🗑️</span>
      <span>删除</span>
    </div>
  </div>
  
  <!-- 重命名对话框 -->
  <div v-if="showRenameDialog" class="rename-dialog-overlay" @click="cancelRename">
    <transition name="dialog-fade">
      <div class="rename-dialog" @click.stop>
        <div class="dialog-header">
          <h3>编辑标题</h3>
          <button class="dialog-close" @click="cancelRename">×</button>
        </div>
        <div class="dialog-content">
          <input 
            v-model="newSessionName" 
            type="text" 
            class="rename-input"
            placeholder="请输入新标题"
            @keyup.enter="confirmRename"
            ref="renameInputRef"
          />
        </div>
        <div class="dialog-buttons">
          <button class="cancel-btn" @click="cancelRename">取消</button>
          <button class="confirm-btn" @click="confirmRename" :disabled="!newSessionName.trim()">确认</button>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, provide, nextTick } from 'vue'
import HeaderBar from '@/components/layout/HeaderBar.vue'
import Footer from '@/components/layout/Footer.vue'
import ChatAiView from '@/views/chat-home/ChatAiView.vue'
import { doGet, doDelete, doPut, doPost } from '@/http/BackendRequests'
import { AI_CHAT_SESSION_URL, AI_CHAT_SESSION_DELETE_URL, AI_CHAT_SESSION_RENAME_URL } from '@/http/URL'
import type { ChatAiSessionItemDTO } from '@/http/ResponseTypes/ChatType/ChatAiSessionType'
import { messageTip } from '@/util/utils'
import type { ChatAiSessionResponse } from '@/http/ResponseTypes/ChatType/ChatAiSessionType'

// 响应式数据
const sessionList = ref<ChatAiSessionItemDTO[]>([])
const selectedSessionId = ref<string | null>(null)
const selectedAiModel = ref('ai1') // 默认选择第一个AI模型

// 选择AI模型
const selectModel = (model: string) => {
  selectedAiModel.value = model
  console.log('已选择AI模型:', model)
  // 如果当前有聊天会话，通知用户模型已切换
  if (selectedSessionId.value) {
    messageTip(`已切换到${model === 'ai1' ? 'QWEN 1.7B模型' : 'DEEPSEEK r1模型(敬请期待)'}`, '')
  }
}
const loading = ref(false)
const error = ref(false)
const loginDialogClicked = ref(false)
const hasMore = ref(true) // 是否还有更多数据
const lastCursorId = ref<number | undefined>(undefined) // 最后一条数据的sessionId，作为游标
// 操作菜单相关
const showMenu = ref(false)
const menuPosition = ref({ x: 0, y: 0 })
const currentSession = ref<ChatAiSessionItemDTO | null>(null)
// 重命名相关
const showRenameDialog = ref(false)
const newSessionName = ref('')
// ChatAiView组件引用
const chatAiViewRef = ref<InstanceType<typeof ChatAiView> | null>(null)
// 重命名输入框引用
const renameInputRef = ref<HTMLInputElement | null>(null)
// 菜单动画相关
const menuTimer = ref<number | null>(null)


// 从后端获取AI会话列表
const fetchSessionList = async (cursorId?: number, isLoadMore = false) => {
  try {
    // 如果是加载更多且没有更多数据，则不再请求
    if (isLoadMore && !hasMore.value) {
      return
    }
    
    // 如果是加载更多，保持原有数据；否则清空列表并重置游标
    if (!isLoadMore) {
      loading.value = true
      error.value = false
      sessionList.value = []
      lastCursorId.value = undefined
    } else {
      // 加载更多时显示加载状态
      loading.value = true
    }
    
    const params = {
      cursorId: cursorId,
      limit: 10
    }
    
    const response = await doGet<ChatAiSessionResponse>(AI_CHAT_SESSION_URL, params)
    
    if (response.data.result) {
      const cursorData = response.data.result
      // console.log("获取到数据:",cursorData)
      
      // 将后端返回的数据转换为前端需要的格式
      const newSessions: ChatAiSessionItemDTO[] = cursorData.chatAiSessionItems.map(item => ({
        sessionId: item.sessionId,
        title: item.title,
        lastMsgTime: item.lastMsgTime
      }))
      
      // 根据是否为加载更多来决定如何处理数据
      if (isLoadMore) {
        sessionList.value = [...sessionList.value, ...newSessions]
      } else {
        sessionList.value = newSessions
      }
      
      // 更新游标和是否有更多数据的标志
      hasMore.value = cursorData.hasMore
      if (newSessions.length > 0) {
        lastCursorId.value = newSessions[newSessions.length - 1].sessionId
      }
    }
    
    // 添加淡入动画效果
    nextTick(() => {
      const items = document.querySelectorAll('.ai-session-item')
      items.forEach((item, index) => {
        setTimeout(() => {
          (item as HTMLElement).style.opacity = '1';
          (item as HTMLElement).style.transform = 'translateY(0)';
        }, index * 50)
      })
    })
  } catch (err) {
    error.value = true
    console.error('获取AI会话列表失败:', err)
    messageTip('获取AI会话列表失败，请稍后重试',"")
  } finally {
    loading.value = false
  }
}

// 初始化会话列表（调用fetchSessionList的包装函数）
const initializeSessionList = () => {
  fetchSessionList()
}

// 加载更多会话
const loadMoreSessions = () => {
  fetchSessionList(lastCursorId.value, true)
}

// 创建新会话
const createNewSession = () => {
  const now = new Date()
  const newSession: ChatAiSessionItemDTO = {
    sessionId: 0, 
    title: `新对话 ${now.toLocaleString('zh-CN', {
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    })}`,
    lastMsgTime: now.toLocaleString('zh-CN', {
      year: 'numeric', 
      month: '2-digit', 
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    }) // 占位
  }
  
  // 添加新会话并设置初始动画状态
  sessionList.value.unshift(newSession)
  
  // 触发重排，然后设置动画
  nextTick(() => {
    const firstItem = document.querySelector('.ai-session-item') as HTMLElement | null
    if (firstItem) {
      firstItem.style.opacity = '0'
      firstItem.style.transform = 'translateY(-20px)'
      setTimeout(() => {
        firstItem.style.opacity = '1'
        firstItem.style.transform = 'translateY(0)'
      }, 10)
    }
  })
  
  handleSessionItemClick(0)
}

// 处理会话项点击事件
const handleSessionItemClick = (sessionId: number | string) => {
  selectedSessionId.value = String(sessionId)
  // 短暂延迟后聚焦到输入框，确保组件已完成更新
  setTimeout(() => {
    if (chatAiViewRef.value) {
      chatAiViewRef.value.focusInput()
    }
  }, 100)
}

// 关闭聊天，返回到会话列表视图
const closeChat = () => {
  selectedSessionId.value = null
}

// 处理会话预览更新
const handleUpdateSessionPreview = (data: { 
  sessionId: string; 
  preview: string; 
  time?: string; 
  createTime?: string; 
  model?: string
}) => {
  // 查找对应sessionId的会话
  const sessionIndex = sessionList.value.findIndex(item => String(item.sessionId) === data.sessionId)
  if (sessionIndex !== -1) {
    // 如果当前会话不是置顶的，将其移到列表顶部
    if (sessionIndex > 0) {
      const updatedSession = sessionList.value.splice(sessionIndex, 1)[0]
      sessionList.value.unshift(updatedSession)
    }
  }
}

// 处理子组件传递的sessionId更新
const handleUpdateSessionId = (newSessionId: string) => {
  // console.log('更新会话ID:', newSessionId)
  // 在会话列表中找到当前会话（根据selectedSessionId）并更新其sessionId
  const currentSessionIndex = sessionList.value.findIndex(
    session => String(session.sessionId) === selectedSessionId.value
  )
  
  if (currentSessionIndex !== -1) {
    // 更新会话列表中当前会话的sessionId
    sessionList.value[currentSessionIndex].sessionId = Number(newSessionId)
    // sessionList.value[currentSessionIndex].title = newTitle
  }
  
  // 更新选中的会话ID，保持当前会话为选中状态
  selectedSessionId.value = newSessionId
}

// 显示操作菜单
const showContextMenu = (event: MouseEvent, item: ChatAiSessionItemDTO) => {
  event.preventDefault()
  
  // 清除之前的定时器
  if (menuTimer.value) {
    clearTimeout(menuTimer.value)
    menuTimer.value = null
  }
  
  // 获取菜单元素和视口宽度
  const menuWidth = 180 // 调整为更舒适的宽度
  const menuHeight = 120 // 预估高度
  const viewportWidth = window.innerWidth
  const viewportHeight = window.innerHeight
  const clickX = event.clientX
  const clickY = event.clientY
  
  // 计算X位置，确保不超出屏幕
  const xPosition = (clickX + menuWidth > viewportWidth) ? 
    clickX - menuWidth : 
    clickX
  
  // 计算Y位置，确保不超出屏幕
  const yPosition = (clickY + menuHeight > viewportHeight) ? 
    Math.max(0, viewportHeight - menuHeight) : 
    clickY
  
  menuPosition.value = { x: xPosition, y: yPosition }
  currentSession.value = item
  showMenu.value = true
}

// 隐藏操作菜单
const hideContextMenu = () => {
  showMenu.value = false
  currentSession.value = null
}

// 处理重命名点击
const handleRenameClick = () => {
  if (currentSession.value) {
    newSessionName.value = currentSession.value.title
    showRenameDialog.value = true
    showMenu.value = false
    // 延迟聚焦到输入框
    nextTick(() => {
      setTimeout(() => {
        if (renameInputRef.value) {
          renameInputRef.value.focus()
          renameInputRef.value.select()
        }
      }, 100)
    })
  }
}

// 处理菜单鼠标进入
const handleMenuEnter = () => {
  if (menuTimer.value) {
    clearTimeout(menuTimer.value)
    menuTimer.value = null
  }
}

// 处理菜单鼠标离开
const handleMenuLeave = () => {
  if (!menuTimer.value) {
    menuTimer.value = window.setTimeout(() => {
      hideContextMenu()
    }, 300)
  }
}

// 格式化会话时间
const formatSessionTime = (timeStr: string) => {
  try {
    const date = new Date(timeStr)
    const now = new Date()
    const diffMs = now.getTime() - date.getTime()
    const diffMins = Math.floor(diffMs / 60000)
    const diffHours = Math.floor(diffMs / 3600000)
    const diffDays = Math.floor(diffMs / 86400000)
    
    if (diffMins < 1) {
      return '刚刚'
    } else if (diffMins < 60) {
      return `${diffMins}分钟前`
    } else if (diffHours < 24) {
      return `${diffHours}小时前`
    } else if (diffDays < 7) {
      return `${diffDays}天前`
    } else {
      return `${date.getMonth() + 1}月${date.getDate()}日`
    }
  } catch (e) {
    return timeStr
  }
}

// 确认重命名
const confirmRename = async () => {
    if (!currentSession.value || !newSessionName.value.trim()) return
    
    const sessionId = currentSession.value.sessionId
    const newName = newSessionName.value.trim()
    
    // 更新前端显示
    const index = sessionList.value.findIndex(item => item.sessionId === sessionId)
    if (index !== -1) {
      sessionList.value[index].title = newName
    }
    
    // 发送重命名请求到后端 - 使用POST方法，参数通过URL查询参数传递
    try {
      // 按照后端@RequestParam要求，将参数作为URL查询参数传递
      await doPut(`${AI_CHAT_SESSION_RENAME_URL}?sessionId=${Number(sessionId)}&title=${encodeURIComponent(newName)}`)
      messageTip('重命名成功', 'success')
    } catch (error) {
      console.error('重命名会话失败:', error)
      messageTip('重命名失败，请重试', 'error')
      // 如果失败，可以选择回滚前端更新
    } finally {
      showRenameDialog.value = false
      newSessionName.value = ''
    }
  }

// 取消重命名
const cancelRename = () => {
  showRenameDialog.value = false
  newSessionName.value = ''
}

// 删除会话
  const deleteSession = async () => {
    if (!currentSession.value) return
    
    const sessionId = currentSession.value.sessionId
    showMenu.value = false
    
    // 前端先移除会话
    const index = sessionList.value.findIndex(item => item.sessionId === sessionId)
    if (index !== -1) {
      sessionList.value.splice(index, 1)
    }
    
    // 如果删除的是当前选中的会话，关闭聊天
    if (selectedSessionId.value === String(sessionId)) {
      selectedSessionId.value = null
    }
    
    // 发送删除请求到后端
    try {
      // 参数通过URL查询参数传递
      await doDelete(`${AI_CHAT_SESSION_DELETE_URL}?sessionId=${Number(sessionId)}`)
      messageTip('删除成功', 'success')
    } catch (error) {
      console.error('删除会话失败:', error)
      messageTip('删除失败，请重试', 'error')
      // 可以选择回滚前端删除操作
    } finally {
      currentSession.value = null
    }
  }

// 全局点击关闭菜单
document.addEventListener('click', (e) => {
  // 如果点击的不是操作按钮和菜单本身，关闭菜单
  if (!e.target || 
      !(e.target as Element).closest('.action-btn') && 
      !(e.target as Element).closest('.context-menu')) {
    if (!menuTimer.value) {
      menuTimer.value = window.setTimeout(() => {
        hideContextMenu()
      }, 100)
    }
  }
})

// 登录框切换
const changeClicked = () => {
  loginDialogClicked.value = !loginDialogClicked.value
  console.log("clicked: ", loginDialogClicked.value)
}

provide('loginDialogClicked', changeClicked)


// 移除重复的事件监听器

// 组件挂载时初始化会话列表
onMounted(() => {
  initializeSessionList()
})

// 在组件卸载时移除事件监听器，防止内存泄漏
onUnmounted(() => {
  document.removeEventListener('click', (e) => {
    if (!e.target || !(e.target as Element).closest('.action-btn')) {
      hideContextMenu()
    }
  })
})
</script>

<style scoped>
/* 主容器样式 - 固定高度并隐藏溢出 */
.chat-main-container {
  position: relative;
  height: calc(100vh - 130px); /* 减去头部和底部的高度，留出一些空间 */
  overflow: hidden;
  width: 100%;
  background-color: #f8f9fa;
  max-width: 1800px; /* 增加最大宽度 */
  margin: 0 auto;
}

/* 分栏布局 - 三栏布局 */
.chat-layout {
  display: flex;
  height: 100%;
  width: 100%;
  background-color: #f8f9fa;
  overflow: hidden;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  margin: 0 auto;
  max-width: 1700px; /* 增加最大宽度 */
  width: calc(100% - 20px); /* 减少边距 */
  align-items: stretch;
}

/* 操作菜单样式 */
.context-menu {
  position: fixed;
  background: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
  z-index: 9999;
  padding: 8px 0;
  min-width: 160px;
  transition: opacity 0.2s ease, transform 0.2s ease;
  opacity: 1;
  transform: translateY(0);
}

.menu-item {
  padding: 10px 20px;
  cursor: pointer;
  font-size: 14px;
  color: #333;
  transition: all 0.2s ease;
  user-select: none;
  display: flex;
  align-items: center;
  gap: 8px;
}

.menu-item:hover {
  background-color: #f5f7fa;
  color: #409eff;
  padding-left: 22px;
}

.menu-item.delete {
  color: #f56c6c;
}

.menu-item.delete:hover {
  background-color: #fef0f0;
  color: #f56c6c;
}

.menu-icon {
  font-size: 16px;
  width: 20px;
  text-align: center;
}

.menu-divider {
  height: 1px;
  background-color: #f0f0f0;
  margin: 4px 0;
}

/* 重命名对话框样式 */
.rename-dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 10000;
  backdrop-filter: blur(4px);
}

.rename-dialog {
  background: #fff;
  border-radius: 12px;
  width: 400px;
  max-width: 90%;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
  overflow: hidden;
}

.dialog-header {
  padding: 20px 24px 16px;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dialog-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.dialog-close {
  background: none;
  border: none;
  font-size: 24px;
  color: #909399;
  cursor: pointer;
  padding: 0;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  transition: all 0.2s;
}

.dialog-close:hover {
  background-color: #f5f7fa;
  color: #606266;
}

.dialog-content {
  padding: 20px 24px;
}

.rename-input {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  font-size: 14px;
  box-sizing: border-box;
  transition: border-color 0.2s, box-shadow 0.2s;
  font-family: inherit;
}

.rename-input:focus {
  outline: none;
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.1);
}

.dialog-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 16px 24px 20px;
  border-top: 1px solid #f0f0f0;
  background-color: #fafafa;
  border-radius: 0 0 12px 12px;
}

.cancel-btn, .confirm-btn {
  padding: 10px 20px;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s;
  min-width: 80px;
}

.cancel-btn {
  background-color: #fff;
  color: #606266;
}

.cancel-btn:hover {
  background-color: #f5f5f5;
  border-color: #c6e2ff;
}

.confirm-btn {
  background-color: #409eff;
  color: #fff;
  border-color: #409eff;
}

.confirm-btn:hover:not(:disabled) {
  background-color: #66b1ff;
  border-color: #66b1ff;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.3);
}

.confirm-btn:disabled {
  background-color: #c0c4cc;
  border-color: #c0c4cc;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

/* 对话框动画 */
.dialog-fade-enter-active,
.dialog-fade-leave-active {
  transition: opacity 0.3s ease;
}

.dialog-fade-enter-from,
.dialog-fade-leave-to {
  opacity: 0;
}

.dialog-fade-enter-active .rename-dialog,
.dialog-fade-leave-active .rename-dialog {
  transition: transform 0.3s ease;
}

.dialog-fade-enter-from .rename-dialog {
  transform: scale(0.9);
}

.dialog-fade-leave-to .rename-dialog {
  transform: scale(0.9);
}

/* 会话操作按钮样式 - 优化交互体验 */
.session-actions {
  display: flex;
  align-items: center;
}

.action-btn {
  width: 32px;
  height: 32px;
  border: none;
  background: none;
  cursor: pointer;
  border-radius: 6px;
  color: #909399;
  font-size: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
  opacity: 0;
}

.ai-session-item:hover .action-btn {
  opacity: 1;
}

.action-btn:hover {
  background-color: #f0f0f0;
  color: #606266;
  transform: scale(1.1);
}

.icon-dots {
  letter-spacing: 1px;
  font-size: 16px;
  font-weight: bold;
}

/* 左侧AI会话列表 */
.chat-list-sidebar {
  width: 360px; /* 增加宽度 */
  background-color: #fff;
  border-right: 1px solid #e0e0e0;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  border-radius: 8px 0 0 8px;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
}

.chat-list-header {
  padding: 16px 20px;
  border-bottom: 1px solid #e0e0e0;
  background-color: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: sticky;
  top: 0;
  z-index: 100;
  backdrop-filter: blur(8px);
  background-color: rgba(255, 255, 255, 0.95);
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

.chat-list-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #333;
  letter-spacing: 0.3px;
}

.close-chat-btn {
  background-color: #f56c6c;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  gap: 6px;
}

.close-chat-btn:hover {
  background-color: #e64949;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(245, 108, 108, 0.3);
}

.close-chat-btn .icon {
  font-size: 16px;
  line-height: 1;
}

.chat-list-content {
  flex: 1;
  overflow-y: auto;
}

/* 美化滚动条 */
.chat-list-content::-webkit-scrollbar {
  width: 6px;
}

.chat-list-content::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.chat-list-content::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.chat-list-content::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 加载、错误、空状态样式 */
.loading {
  padding: 40px 20px;
  text-align: center;
  color: #666;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.loading-spinner {
  width: 32px;
  height: 32px;
  border: 3px solid #f3f3f3;
  border-top: 3px solid #409eff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.error {
  padding: 40px 20px;
  text-align: center;
  color: #f56c6c;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.error-icon {
  font-size: 32px;
}

.empty-message {
  padding: 60px 20px;
  text-align: center;
  color: #909399;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.empty-icon {
  font-size: 64px;
  opacity: 0.6;
}

.empty-message p {
  margin: 0;
  font-size: 16px;
  color: #606266;
}

.create-first-chat-btn {
  background-color: #409eff;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s ease;
  margin-top: 8px;
}

.create-first-chat-btn:hover {
  background-color: #66b1ff;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.3);
}

/* AI会话项样式 */
.ai-session-item {
  padding: 16px 20px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: all 0.25s ease;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  overflow: hidden;
  opacity: 1;
  transform: translateY(0);
}

.ai-session-item::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  height: 100%;
  width: 3px;
  background-color: transparent;
  transition: all 0.25s ease;
}

.ai-session-item:hover {
  background-color: #f9f9f9;
  padding-left: 22px;
}

.ai-session-item.selected {
  background-color: #f0f7ff;
  padding-left: 22px;
}

.ai-session-item.selected::before {
  background-color: #409eff;
}

.session-info {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.session-name {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  transition: color 0.2s ease;
}

.ai-session-item.selected .session-name {
  color: #409eff;
}

.session-time {
  font-size: 12px;
  color: #909399;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 未选择会话时的完整AI会话列表样式 */
.full-width-ai-list {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  background-color: #fff;
  overflow: hidden;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  margin: 0 auto;
  max-width: 900px; /* 增加最大宽度 */
  width: calc(100% - 32px);
}

.ai-list-header {
  padding: 20px 24px;
  border-bottom: 1px solid #e0e0e0;
  background-color: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: sticky;
  top: 0;
  z-index: 100;
  backdrop-filter: blur(8px);
  background-color: rgba(255, 255, 255, 0.95);
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

.ai-list-header h3 {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: #333;
  letter-spacing: 0.3px;
}

.new-chat-btn {
  background-color: #409eff;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  gap: 6px;
}

.new-chat-btn:hover {
  background-color: #66b1ff;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.3);
}

.new-chat-btn .icon {
  font-size: 16px;
  font-weight: bold;
  line-height: 1;
}

.ai-list-content {
  flex: 1;
  overflow-y: auto;
}

/* 美化滚动条 */
.ai-list-content::-webkit-scrollbar {
  width: 6px;
}

.ai-list-content::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.ai-list-content::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.ai-list-content::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

.chat-detail {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #fafafa;
  overflow: hidden;
  position: relative;
  height: 100%;
  min-height: 400px;
  max-width: 1100px; /* 增加最大宽度 */
  margin: 0 auto;
}

/* 右侧AI模型信息栏 */
.ai-info-sidebar {
  width: 320px; /* 增加宽度 */
  background-color: #fff;
  border-left: 1px solid #e0e0e0;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  box-shadow: -2px 0 8px rgba(0, 0, 0, 0.05);
}

.ai-info-header {
  padding: 16px 20px;
  border-bottom: 1px solid #e0e0e0;
  background-color: #fff;
  position: sticky;
  top: 0;
  z-index: 100;
  backdrop-filter: blur(8px);
  background-color: rgba(255, 255, 255, 0.95);
}

.ai-info-header h4 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.ai-info-content {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.model-card {
  background-color: #f8f9fa;
  border-radius: 12px;
  padding: 16px;
  transition: all 0.3s ease;
  cursor: pointer;
  border: 2px solid transparent;
}

.model-card:hover {
  background-color: #e9ecef;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.model-card.active {
  border-color: #409eff;
  background-color: #ecf5ff;
  box-shadow: 0 0 0 3px rgba(64, 158, 255, 0.1);
}

.model-icon {
  font-size: 32px;
  margin-bottom: 8px;
}

.model-name {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 6px;
}

.model-desc {
  font-size: 14px;
  color: #606266;
  line-height: 1.5;
  margin-bottom: 12px;
}

.model-stats {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.stat-item {
  font-size: 12px;
  color: #909399;
  background-color: rgba(0, 0, 0, 0.03);
  padding: 4px 8px;
  border-radius: 4px;
  display: inline-block;
}

.model-tips {
  background-color: #f0f7ff;
  border-radius: 12px;
  padding: 16px;
  border-left: 4px solid #409eff;
}

.model-tips h5 {
  margin: 0 0 12px 0;
  font-size: 15px;
  font-weight: 600;
  color: #333;
}

.model-tips ul {
  margin: 0;
  padding-left: 20px;
}

.model-tips li {
  font-size: 13px;
  color: #606266;
  line-height: 1.6;
  margin-bottom: 6px;
}

.model-tips li:last-child {
  margin-bottom: 0;
}

/* 加载更多按钮样式 */
.load-more-container {
  padding: 16px;
  text-align: center;
  border-top: 1px solid #f0f0f0;
}

.load-more-btn {
  background-color: transparent;
  color: #409eff;
  border: 1px solid #dcdfe6;
  padding: 8px 20px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s ease;
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.load-more-btn:hover:not(:disabled) {
  background-color: #ecf5ff;
  color: #409eff;
  border-color: #409eff;
  transform: translateY(-1px);
  box-shadow: 0 2px 6px rgba(64, 158, 255, 0.2);
}

.load-more-btn:disabled {
  cursor: not-allowed;
  opacity: 0.6;
  transform: none;
  box-shadow: none;
}

/* 确保ChatAiView组件正确显示 */
:deep(.chat-container) {
  height: 100%;
  display: flex;
  flex-direction: column;
}

/* 确保ChatAiView组件内容区域在固定容器内滚动 */
:deep(.chat-ai-view-container) {
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: #fff;
  overflow: hidden;
  border-radius: 0 8px 8px 0;
}

:deep(.chat-messages) {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background-color: #fafafa;
}

/* 美化ChatAiView中的滚动条 */
:deep(.chat-messages::-webkit-scrollbar) {
  width: 6px;
}

:deep(.chat-messages::-webkit-scrollbar-track) {
  background: #f1f1f1;
}

:deep(.chat-messages::-webkit-scrollbar-thumb) {
  background: #c1c1c1;
  border-radius: 3px;
}

:deep(.chat-messages::-webkit-scrollbar-thumb:hover) {
  background: #a8a8a8;
}

/* 平滑滚动效果 */
* {
  scroll-behavior: smooth;
}

/* 响应式布局 */
@media (max-width: 1400px) {
  /* 在中等屏幕上隐藏右侧信息栏 */
  .ai-info-sidebar {
    display: none;
  }
  
  .chat-detail {
    max-width: none;
    margin: 0;
  }
  
  .chat-main-container {
    max-width: 1400px;
  }
  
  .chat-layout {
    max-width: 1300px;
  }
}

@media (max-width: 768px) {
  .chat-main-container {
    height: calc(100vh - 140px); /* 移动端可能需要调整高度 */
    padding: 12px;
    max-width: none;
  }
  
  .chat-layout {
    flex-direction: column;
    height: 100%;
    width: 100%;
    border-radius: 8px;
    max-width: none;
  }
  
  .chat-list-sidebar {
    width: 100%;
    height: 300px;
    border-right: none;
    border-bottom: 1px solid #e0e0e0;
    border-radius: 8px 8px 0 0;
    box-shadow: none;
  }
  
  .chat-detail {
    width: 100%;
    flex: 1;
    max-width: none;
    margin: 0;
  }
  
  :deep(.chat-ai-view-container) {
    border-radius: 0 0 8px 8px;
  }
  
  .full-width-ai-list {
    height: 100%;
    width: 100%;
    max-width: none;
  }
  
  .chat-list-header,
  .ai-list-header {
    padding: 16px;
  }
  
  .ai-session-item {
    padding: 14px 16px;
  }
  
  .rename-dialog {
    width: 90%;
    max-width: 340px;
  }
}

@media (max-width: 480px) {
  .chat-main-container {
    padding: 8px;
    height: calc(100vh - 120px);
  }
  
  .chat-list-sidebar {
    height: 250px;
  }
  
  .chat-layout {
    height: calc(100vh - 136px);
  }
}
</style>
