<template>
  <div class="chat-ai-view-container">
    <!-- 顶部信息栏 -->
    <div class="chat-header">
      <h3>AI 聊天</h3>
    </div>

    <!-- 聊天消息区域 -->
    <div class="chat-messages" ref="chatMessagesContainer">
      <div v-if="loading && messages.length === 0" class="loading">加载中...</div>
      <div v-else-if="error" class="error">加载失败，请重试</div>
      <div v-else-if="messages.length === 0" class="empty-chat">
        <p>开始与AI对话吧！</p>
      </div>
      
      <!-- 加载更多按钮 -->
      <div v-if="hasMore && messages.length > 0" class="load-more-container">
        <button 
          class="load-more-btn" 
          @click="loadMoreHistory" 
          :disabled="loading"
        >
          {{ loading ? '加载中...' : '展开更多' }}
        </button>
      </div>
      
      <!-- 消息列表 -->
      <div v-for="(message, index) in messages" :key="`${message.seq}-${index}`" :class="['message-item', message.sender === 0 ? 'user-message' : 'ai-message']"
           @contextmenu.prevent="showContextMenu($event, message)">
        <template v-if="message.sender === 0">
          <div class="message-wrapper">
            <div class="message-bubble">
              <div class="message-text">
                <MdPreview 
                  :model-value="message.content" 
                  :toolbars="false" 
                  :copy-code="false"
                  :preview-class="'chat-md-preview'"
                />
              </div>
            </div>
            <div class="message-time">
              {{ message.createTime }}
            </div>
          </div>
          <div class="message-avatar">
            用户
          </div>
        </template>
        <template v-else>
          <div class="message-avatar">
            AI
          </div>
          <div class="message-wrapper">
            <div class="message-bubble">
              <div class="message-text">
                <MdPreview 
                  :model-value="message.content" 
                  :toolbars="false" 
                  :copy-code="false"
                  :preview-class="'chat-md-preview'"
                />
              </div>
            </div>
            <div class="message-time">
              {{ message.createTime }}
            </div>
          </div>
        </template>
      </div>

      <!-- 正在输入提示 -->
      <div v-if="isTyping" class="message-item ai-message">
        <div class="message-avatar">
          <div class="avatar-circle ai-avatar">🤖</div>
        </div>
        <div class="message-content ai-content">
          <div class="message-text" v-if="currentAiResponse">
            <MdPreview 
              :model-value="currentAiResponse" 
              :toolbars="false" 
              :copy-code="false"
              :preview-class="'chat-md-preview ai-preview'"
            />
          </div>
          <div class="typing-indicator">
            <span class="dot"></span>
            <span class="dot"></span>
            <span class="dot"></span>
          </div>
        </div>
      </div>
    </div>

        <!-- 右键菜单 -->
    <div 
      v-if="contextMenuVisible" 
      class="context-menu"
      :style="{
        left: contextMenuPosition.x + 'px',
        top: contextMenuPosition.y + 'px'
      }"
      @click.stop
    >
      <div class="menu-item" @click="deleteMessage">删除</div>
      
    </div>

        <!-- 点击其他区域关闭右键菜单 -->
    <div 
      v-if="contextMenuVisible" 
      class="context-menu-backdrop" 
      @click="closeContextMenu"
    ></div>

    <!-- 输入区域 -->
    <div class="chat-input-area">
      <div class="input-container">
        <textarea 
          v-model="inputMessage" 
          ref="chatInput"
          @keydown.enter.prevent="handleEnterKey" 
          placeholder="输入您的问题..."
          :disabled="isTyping"
        ></textarea>
        <div class="input-actions">
          <button 
            v-if="isTyping" 
            class="stop-btn" 
            @click="stopGeneration"
          >
            停止
          </button>
          <button 
            v-else 
            class="send-btn" 
            @click="sendMessage" 
            :disabled="!inputMessage.trim()"
          >
            发送
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch, onUnmounted } from 'vue'
import { doDelete, doGet } from '@/http/BackendRequests'
import { AI_CHAT_HISTORY_URL, AI_CHAT_ANSWER_URL, BASE_URL, AI_CHAT_MESSAGE_DELETE_URL } from '@/http/URL'
import { useGlobalStore } from '@/stores/global'
import type { ChatAiItemDTO } from '@/http/ResponseTypes/ChatType/ChatAiHistoryType'
import type { ChatAiHistoryResponse } from '@/http/ResponseTypes/ChatType/ChatAiHistoryType'
import type { ChatAiRequestDTO, ChatAiResponseDTO } from '@/http/ResponseTypes/ChatType/ChatAiRequestType'
import { messageTip } from '@/util/utils'
import { MdPreview } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'

// 右键菜单相关状态
const contextMenuVisible = ref(false);
const contextMenuPosition = ref({ x: 0, y: 0 });
const selectedMessage = ref<any>(null);

// Props
interface Props {
  sessionId: string
  aiModel?: string
}

const props = withDefaults(defineProps<Props>(), {
  aiModel: 'ai1'
})

// Emits
const emit = defineEmits<{
  'update-session-preview': [data: { sessionId: string; preview: string; createTime: string; model: string }]
  'update-session-id': [sessionId: string]
}>()

// 常量定义
const LIMIT = 5 // 每次请求的条数

// 路由和全局状态
const globalStore = useGlobalStore();

// 响应式数据
const inputMessage = ref('')
const messages = ref<ChatAiItemDTO[]>([])
const isTyping = ref(false)
const currentAiModel = ref(props.aiModel)
const loading = ref(false)
const error = ref(false)
const hasMore = ref(true) // 是否还有更多历史记录
const currentCursorSeq = ref<number | undefined>(undefined) // 当前游标seq
const currentCursorTime = ref<string | undefined>(undefined) // 当前游标时间
const currentAiResponse = ref('') // 当前AI回复内容（流式）
const chatMessagesContainer = ref<HTMLElement | null>(null) // 聊天消息容器引用
const chatInput = ref<HTMLTextAreaElement | null>(null) // 输入框引用

// 聚焦输入框的方法
defineExpose({
  focusInput: () => {
    if (chatInput.value) {
      chatInput.value.focus()
    }
  }
})
let eventSource: EventSource | null = null

// 存储当前会话的seq信息
const currentUserSeq = ref<number | null>(null)
const currentAiSeq = ref<number | null>(null)

// 滚动到聊天容器底部
const scrollToBottom = () => {
  if (chatMessagesContainer.value) {
    // 添加一个小延迟，确保DOM更新完成
    setTimeout(() => {
      chatMessagesContainer.value!.scrollTop = chatMessagesContainer.value!.scrollHeight
    }, 100)
  }
}

// 滚动到聊天容器顶部
const scrollToTop = () => {
  if (chatMessagesContainer.value) {
    // 添加一个小延迟，确保DOM更新完成
    setTimeout(() => {
      chatMessagesContainer.value!.scrollTop = 0
    }, 100)
  }
}

const MessageSender = {
  USER: 0,
  AI: 1
}

// 格式化消息时间
const formatMessageTime = (timeString: string) => {
  const date = new Date(timeString);
  const now = new Date();
  const diffMs = now.getTime() - date.getTime();
  const diffDays = Math.floor(diffMs / (1000 * 60 * 60 * 24));
  
  if (diffDays === 0) {
    return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' });
  } else if (diffDays === 1) {
    return '昨天 ' + date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' });
  } else if (diffDays < 7) {
    const days = ['日', '一', '二', '三', '四', '五', '六'];
    return '周' + days[date.getDay()] + ' ' + date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' });
  } else {
    return date.toLocaleDateString('zh-CN', { month: '2-digit', day: '2-digit' }) + ' ' + 
           date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' });
  }
};

// 获取聊天历史
const fetchChatHistory = async (sessionId: string, cursorSeq?: number, cursorTime?: string) => {
  try {
    const params = {
      sessionId: Number(sessionId),
      cursor: cursorTime,
      cursorSeq: cursorSeq,
      limit: LIMIT
    }
    
    const response = await doGet<ChatAiHistoryResponse>(AI_CHAT_HISTORY_URL, params)
    
    if (response.data.result) {
      const cursorData = response.data.result
      
      // 转换后端数据格式，并根据sender判断角色
      const newMessages = cursorData.chatAiItems.map((item: ChatAiItemDTO) => ({
        seq: item.seq,
        sender: item.sender,
        content: item.content,
        createTime: item.createTime
      }))
      
      // 翻转数据，因为后端返回的是从晚到早的顺序
      const reversedMessages = newMessages.reverse()
      
      // 添加到消息列表
      messages.value = [...reversedMessages,...messages.value]
      
      // 更新是否有更多数据的状态
      hasMore.value = cursorData.hasMore
      
      // 如果有数据，更新游标为第一条数据的seq和时间（因为我们已经翻转了，所以取第一条）
      if (reversedMessages.length > 0 && hasMore.value) {
        currentCursorSeq.value = reversedMessages[0].seq
        currentCursorTime.value = reversedMessages[0].createTime
      }
      newMessages.forEach(item => {
        item.createTime = formatMessageTime(item.createTime)
      })
      
      // 更新会话预览信息
      updateSessionInfo()
      
      // 加载历史数据后滚动到顶部
      scrollToTop()
    }
  } catch (err) {
    error.value = true
    console.error('获取聊天历史失败:', err)
    messageTip('获取聊天历史失败，请稍后重试', '')
  }
}

// 加载会话消息
const loadSessionMessages = async (sessionId: string) => {
  try {
    loading.value = true
    error.value = false
    messages.value = [] // 清空现有消息
    hasMore.value = true // 重置是否有更多数据的状态
    currentCursorSeq.value = undefined // 重置游标
    currentCursorTime.value = undefined
    
    await fetchChatHistory(sessionId, undefined, undefined)
  } catch (err) {
    error.value = true
    console.error('加载会话消息失败:', err)
  } finally {
    loading.value = false
    // 加载会话消息后滚动到底部
    scrollToBottom()
  }
}

// 监听sessionId变化，加载对应会话的消息
watch(() => props.sessionId, (newSessionId) => {
  if (newSessionId) {
    loadSessionMessages(newSessionId)
  }
}, { immediate: true })

// 监听aiModel变化，确保始终使用父组件传递的最新模型
watch(() => props.aiModel, (newModel) => {
  if (newModel) {
    currentAiModel.value = newModel
    // console.log('模型已更新为:', newModel)
  }
})

// 加载更多历史记录
const loadMoreHistory = async () => {
  if (loading.value || !hasMore.value || !props.sessionId) return
  
  try {
    loading.value = true
    await fetchChatHistory(props.sessionId, currentCursorSeq.value, currentCursorTime.value)
    // 加载更多历史记录后滚动到底部
    // scrollToBottom() // 注释掉滚动到底部，因为在fetchChatHistory中已调用scrollToTop
  } finally {
    loading.value = false
  }
}

// 处理AI模型切换
const handleModelChange = () => {
  // 通知父组件模型已更改
  updateSessionInfo()
}

// 更新会话信息
const updateSessionInfo = () => {
  const latestMessage = messages.value[messages.value.length - 1]
  emit('update-session-preview', {
    sessionId: props.sessionId,
    preview: latestMessage ? latestMessage.content.substring(0, 30) + (latestMessage.content.length > 30 ? '...' : '') : '暂无消息',
    createTime: latestMessage ? latestMessage.createTime : formatMessageTime(new Date().toISOString()),
    model: currentAiModel.value
  })
}

// 存储当前的reader引用，用于停止生成
let currentReader: ReadableStreamDefaultReader | null = null

// 停止生成AI回复
const stopGeneration = () => {
  if (currentReader) {
    // 取消读取器，这会触发连接关闭，让后端的onClientCancel能够处理
    currentReader.cancel()
    currentReader = null
    
    // 如果当前已经有部分AI回复内容，将其添加到消息列表
    if (currentAiResponse.value) {
      const now = new Date()
      const aiMessage = {
        seq: currentAiSeq.value || Date.now(), // 使用后端返回的aiSeq
        sender: 1, // AI是发送者1
        content: currentAiResponse.value,
        createTime: formatMessageTime(now.toISOString()),
      }
      messages.value.push(aiMessage)
      
      // 更新会话预览
      updateSessionInfo()
      
      // 滚动到底部
      scrollToBottom()
    }
    
    // 重置状态
    isTyping.value = false
    currentAiResponse.value = ''
  }
}

// 发送消息
const sendMessage = async () => {
  const content = inputMessage.value.trim()
  if (!content || isTyping.value || loading.value) return
  
  // 如果正在加载会话消息，短暂等待后重试
  if (loading.value) {
    setTimeout(() => {
      sendMessage()
    }, 100)
    return
  }

  // 取消之前的连接（如果存在）
  if (currentReader) {
    currentReader.cancel()
    currentReader = null
  }
  
  if (eventSource) {
    eventSource.close()
    eventSource = null
  }
  
  // 重置当前seq信息
  currentUserSeq.value = null
  currentAiSeq.value = null

  // 添加用户消息（暂时使用临时seq，稍后会被后端返回的seq替换）
  const now = new Date()
  const userMessage = { 
    seq: Date.now(), // 临时seq，稍后会被后端返回的userSeq替换
    sender: 0, // 用户是发送者0
    content,
    createTime: formatMessageTime(now.toISOString()),
  }
  messages.value.push(userMessage)
  // console.log('发送用户消息:', userMessage,"全部消息:",messages.value)

  // 清空输入框
  inputMessage.value = ''

  // 更新会话预览
  updateSessionInfo()
  
  // 用户消息发送后滚动到底部
  scrollToBottom()

  // 开始SSE请求
  isTyping.value = true
  currentAiResponse.value = ''
  
  // 构建请求数据
  const requestData = {
    aiType: currentAiModel.value,
    content,
    createTime: new Date().toLocaleString('sv-SE', { timeZone: 'Asia/Shanghai' }).replace(' ', 'T')
    // stream: true // 启用流式响应
  }
  
  // 处理SSE连接
  try {
    // 使用fetch API实现POST请求的SSE
    // 使用完整URL确保端口一致
    const fullUrl = `${BASE_URL}${AI_CHAT_ANSWER_URL}`
    const response = await fetch(fullUrl, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'text/event-stream'
      },
      body: JSON.stringify({
        ...requestData,
        sessionId: props.sessionId // 加上sessionId
      }),
      credentials: 'include' // 携带凭证
    })

    if (!response.ok || !response.body) {
      throw new Error('获取AI回复失败: ' + response.statusText)
    }

    currentReader = response.body.getReader()
    let buffer = ''

    // 处理SSE流
    const processStream = async (): Promise<void> => {
      const { done, value } = await currentReader!.read()
      if (done) {
        return
      }

      // 将新读取的值转换为文本并追加到buffer
      if (value) {
        const decoder = new TextDecoder()
        buffer += decoder.decode(value)
      }

      const events = buffer.split('\n\n')
      buffer = events.pop() || ''

      for (const event of events) {
        if (!event.trim()) continue

        const eventLines = event.split('\n')
        let eventName = 'message'
        let eventData = ''

        for (const line of eventLines) {
          if (line.startsWith('event:')) {
            eventName = line.substring(6).trim()
          } else if (line.startsWith('data:')) {
            eventData = line.substring(5).trim()
          }
        }

        // 处理后端发送的[DONE]标志
        // console.log('seq值:',currentAiSeq.value)

        // 处理seq事件，获取并存储用户和AI的seq值
        if (eventName === 'seq' && eventData) {
          try {
            const seqData = JSON.parse(eventData)
            currentUserSeq.value = seqData.userSeq
            currentAiSeq.value = seqData.aiSeq
            // 仅在新建会话时（sessionId为'0'）通过emit更新sessionId
            if (props.sessionId === '0'&&seqData.sessionId) {
              emit('update-session-id', seqData.sessionId)
            }
            // console.log('收到seq事件:', seqData)
            
            // 更新最后一条用户消息的seq为后端返回的userSeq
            const lastMessage = messages.value[messages.value.length - 1]
            if (lastMessage && lastMessage.sender === 0) {
              if (currentUserSeq.value !== null) {
                lastMessage.seq = currentUserSeq.value
              }
            }
          } catch (error) {
            console.error('解析SSE seq消息失败:', error)
          }
        }
        
        // 处理add事件
        if (eventName === 'add' && eventData) {
          try {
            const data = JSON.parse(eventData)
            
            // 从delta或message中获取内容
            let newContent = ''
            if (data.choices && data.choices[0]) {
              if (data.choices[0].delta && data.choices[0].delta.content) {
                newContent = data.choices[0].delta.content
              } else if (data.choices[0].message && data.choices[0].message.content) {
                newContent = data.choices[0].message.content
              }
            }
            
            // 累积AI回复内容
            if (newContent) {
              currentAiResponse.value += newContent
              // AI回复内容更新时滚动到底部
              scrollToBottom()
            }
          } catch (error) {
            console.error('解析SSE消息失败:', error)
          }
        }

        // 处理done事件
        if (eventName === 'done') {
          // console.log('hello:收到done事件:', eventData)
          // 添加完整的AI回复到消息列表
          const now = new Date()
          const aiMessage = {
            seq: currentAiSeq.value || 0, // 使用后端返回的aiSeq
            sender: 1, // AI是发送者1
            content: currentAiResponse.value,
            createTime: formatMessageTime(now.toISOString()),
          }
          messages.value.push(aiMessage)
          
          // 更新会话预览
          updateSessionInfo()
          
          // 重置状态
          isTyping.value = false
          currentAiResponse.value = ''
          
          return
        }
      }

      // 继续处理流
      return processStream()
    }

    // 开始处理流
    await processStream()

    // 确保done事件被处理
    if (currentAiResponse.value && isTyping.value) {
      // 添加完整的AI回复到消息列表
      const now = new Date()
      const aiMessage = {
        seq: currentAiSeq.value || 0, // 使用后端返回的aiSeq
        sender: 1, // AI是发送者1
        content: currentAiResponse.value,
        createTime: now.toISOString(),
      }
      messages.value.push(aiMessage)
      
      // 更新会话预览
      updateSessionInfo()
      
      // AI消息接收后滚动到底部
      scrollToBottom()
    }

    // 重置状态
    isTyping.value = false
    currentAiResponse.value = ''
  } catch (error) {
    console.error('SSE连接错误:', error)
    messageTip('获取AI回复失败，请稍后重试', '')
    
    // 重置状态
    isTyping.value = false
    currentAiResponse.value = ''
  }
}

// 处理回车键发送
const handleEnterKey = (event: KeyboardEvent) => {
  // 如果按住Shift键，则换行而不发送
  if (!event.shiftKey) {
    sendMessage()
  }
}


// 删除消息
async function deleteMessage() {
  if (selectedMessage.value) {
    try {
      // 调用后端删除接口
      const response = await doDelete(AI_CHAT_MESSAGE_DELETE_URL, {
        sessionId: Number(props.sessionId),
        seq: Number(selectedMessage.value.seq)
      })
      
      if (response && response.status === 200 && response.data) {
        // 删除成功后，更新本地消息列表
        const index = messages.value.findIndex(
          msg => msg.seq === selectedMessage.value.seq
        )
        if (index > -1) {
          messages.value.splice(index, 1)
        }
        
        // 更新会话预览
        updateSessionInfo()
      } else {
        console.error('删除失败:', response)
        messageTip('删除失败，请稍后重试', '')
      }
    } catch (error) {
      console.error('删除请求异常:', error)
      messageTip('删除失败，请稍后重试', '')
    } finally {
      closeContextMenu()
    }
  }
}

// 显示右键菜单
function showContextMenu(event: MouseEvent, message: any) {
  contextMenuPosition.value = { x: event.clientX, y: event.clientY };
  selectedMessage.value = message;
  contextMenuVisible.value = true;
}

// 关闭右键菜单
function closeContextMenu() {
  contextMenuVisible.value = false;
  selectedMessage.value = null;
}


// 组件卸载时清理资源
onUnmounted(() => {
  if (eventSource) {
    eventSource.close()
  }
  if (currentReader) {
    currentReader.cancel()
    currentReader = null
  }
})
</script>

<style scoped>
.chat-ai-view-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: #fff;
  position: relative;
}

/* 顶部信息栏 */
.chat-header {
  padding: 12px 16px;
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

.chat-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.chat-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

/* AI模型选择器 */
.model-selector {
  display: flex;
  align-items: center;
  gap: 8px;
}

.model-selector label {
  font-size: 14px;
  color: #666;
}

.model-selector select {
  padding: 4px 8px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 14px;
  color: #606266;
  background-color: #fff;
  cursor: pointer;
}

.model-selector select:focus {
  outline: none;
  border-color: #409eff;
}

/* 聊天消息区域 */
.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 0;
  scroll-behavior: smooth;
  position: relative;
  margin-bottom: 100px; /* 为固定定位的输入栏留出空间 */
}

.empty-chat,
.loading,
.error {
  text-align: center;
  padding: 40px 0;
}

.empty-chat {
  color: #909399;
}

.loading {
  color: #606266;
}

.error {
  color: #f56c6c;
}

/* 加载更多按钮样式 */
.load-more-container {
  text-align: center;
  margin: 16px 0;
}

.load-more-btn {
  background-color: #f5f5f5;
  color: #606266;
  border: 1px solid #dcdfe6;
  padding: 6px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}

.load-more-btn:hover:not(:disabled) {
  background-color: #ecf5ff;
  color: #409eff;
  border-color: #c6e2ff;
}

.load-more-btn:disabled {
  cursor: not-allowed;
  opacity: 0.6;
}

/* 消息项 */
.message-item {
  display: flex;
  margin-bottom: 16px;
  animation: fadeIn 0.3s ease-in-out;
}

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

/* 用户消息 */
.message-item.user {
  justify-content: flex-end;
}

.message-item.user .message-content {
  background-color: #9fe870;
  color: #333;
  margin-left: 16px;
  margin-right: 0;
}

/* AI消息 */
.message-item.ai {
  justify-content: flex-start;
}

.message-item.ai .message-content {
  background-color: #fff;
  color: #333;
  margin-left: 0;
  margin-right: 16px;
}

/* 消息头像 */
.message-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background-color: #dcdfe6;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  color: #606266;
  flex-shrink: 0;
}

/* 消息内容 */
.message-content {
  max-width: 70%;
  padding: 8px 12px;
  border-radius: 8px;
  position: relative;
}

.message-text {
  font-size: 14px;
  line-height: 1.5;
  word-wrap: break-word;
  word-break: break-word;
}

/* 自定义Markdown预览样式 */
:deep(.chat-md-preview) {
  background-color: transparent !important;
  padding: 0 !important;
  margin: 0 !important;
  font-size: 14px;
  line-height: 1.5;
}

:deep(.chat-md-preview .editor-preview-code-block) {
  margin: 8px 0;
}

:deep(.chat-md-preview pre) {
  background-color: #f5f5f5 !important;
  border-radius: 4px;
  padding: 12px;
  overflow-x: auto;
}

:deep(.chat-md-preview code) {
  background-color: #f5f5f5 !important;
  padding: 2px 4px;
  border-radius: 3px;
  font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
}

.message-time {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
  text-align: right;
}

/* Markdown预览样式 */
.chat-md-preview {
  background-color: transparent !important;
  padding: 0 !important;
  margin: 0 !important;
}

.chat-md-preview :deep(.md-editor-v3__preview) {
  background-color: transparent !important;
  padding: 0 !important;
}

.chat-md-preview :deep(pre) {
  background-color: #f5f5f5 !important;
  border-radius: 4px !important;
  padding: 12px !important;
  overflow-x: auto !important;
}

.chat-md-preview :deep(code) {
  background-color: #f5f5f5 !important;
  padding: 2px 4px !important;
  border-radius: 3px !important;
  font-family: 'Consolas', 'Monaco', 'Courier New', monospace !important;
}

/* 消息样式 */
.message-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 16px;
  padding: 0 16px;
}

/* AI消息 - 左对齐 */
.ai-message {
  justify-content: flex-start;
}

/* 用户消息 - 右对齐 */
.user-message {
  justify-content: flex-end;
}

.message-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background-color: #e0e0e0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 500;
  margin: 0 8px;
  flex-shrink: 0;
}

.user-message .message-avatar {
  background-color: #409eff;
  color: white;
}

.ai-message .message-avatar {
  background-color: #67c23a;
  color: white;
}

/* 消息包装器 */
.message-wrapper {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  max-width: 70%;
  min-width: 100px;
  position: relative;
}

.user-message .message-wrapper {
  align-items: flex-end;
  position: relative;
}

/* 气泡样式 */
.message-bubble {
  padding: 18px 22px;
  border-radius: 20px;
  word-wrap: break-word;
  border: none;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
  color: #303133;
  position: relative;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  animation: messageSlideIn 0.3s ease-out;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
  font-size: 15px;
  line-height: 1.5;
}

/* 消息滑入动画 */
@keyframes messageSlideIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* AI消息气泡样式 */
.ai-message .message-bubble {
  background: linear-gradient(135deg, #ffffff 0%, #f0f9ff 100%);
  border: 1px solid rgba(64, 158, 255, 0.1);
  border-bottom-left-radius: 4px;
  color: #303133;
}

/* 用户消息气泡样式 */
.user-message .message-bubble {
  background: linear-gradient(135deg, #73d13d 0%, #52c41a 100%); /* 更浅的绿色 */
  color: white !important;
  border-radius: 20px;
  border-bottom-right-radius: 4px;
  box-shadow: 0 4px 12px rgba(115, 209, 61, 0.3);
  position: relative;
  overflow: hidden;
  padding: 12px 16px; /* 减小内边距降低高度 */
}

/* 消息时间样式 - 基础 */
.message-time {
  color: #909399;
  font-size: 12px;
  margin-top: 6px;
  opacity: 1;
  transition: all 0.2s ease;
  display: block;
  position: relative;
  z-index: 1;
}

/* 增强选择器特异性 - 与AI时间颜色保持一致 */
.message-item.user-message .message-wrapper .message-time {
  text-align: right !important;
  padding-right: 8px !important;
  color: #909399 !important; /* 与AI消息时间颜色保持一致 */
  font-weight: 500 !important;
  letter-spacing: 0.3px !important;
  font-size: 12px !important;
  opacity: 1 !important;
  margin-top: 6px !important;
  display: block !important;
  visibility: visible !important;
  line-height: 1.4 !important;
  position: relative !important;
  z-index: 999 !important;
  background-color: transparent !important;
  padding: 2px 4px !important;
}

.ai-message .message-time {
  text-align: left;
  padding-left: 4px;
  color: #909399;
  margin-top: 4px;
}

/* 鼠标悬停效果 */
.message-item:hover .message-time {
  opacity: 1;
}

.user-message:hover .message-bubble {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(82, 196, 26, 0.4);
}

.ai-message:hover .message-bubble {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
}

/* 用户气泡光晕效果 */
.user-message .message-bubble::before {
  content: '';
  position: absolute;
  top: -2px;
  left: -2px;
  right: -2px;
  bottom: -2px;
  background: linear-gradient(135deg, rgba(115, 209, 61, 0.3) 0%, transparent 100%);
  border-radius: 22px;
  z-index: -1;
  pointer-events: none;
}

/* AI气泡效果 */
.ai-message .message-bubble::before {
  content: '';
  position: absolute;
  top: -2px;
  left: -2px;
  right: -2px;
  bottom: -2px;
  background: linear-gradient(135deg, rgba(64, 158, 255, 0.1) 0%, transparent 100%);
  border-radius: 22px;
  z-index: -1;
  pointer-events: none;
}

.chat-md-preview :deep(.md-editor-v3__preview) {
  background-color: transparent !important;
  color: inherit !important;
  border: none !important;
  padding: 0 !important;
  margin: 0 !important;
  box-shadow: none !important;
}

/* 确保MdPreview组件不会产生额外的背景 */
.user-message .message-text :deep(.md-editor-v3__preview),
.ai-message .message-text :deep(.md-editor-v3__preview) {
  background-color: transparent !important;
  border: none !important;
  box-shadow: none !important;
}

/* 确保消息文本容器没有额外背景 */
.message-text {
  background-color: transparent !important;
  border: none !important;
  padding: 0 !important;
  box-shadow: none !important;
}

/* 移除所有可能的白色背景和边框 */
.user-message .message-content *,
.user-message .message-text *,
.user-message .chat-md-preview * {
  background-color: transparent !important;
  border: none !important;
  box-shadow: none !important;
  font-family: inherit;
  font-size: inherit;
}

/* 确保消息内容直接使用父容器背景 */
.user-message .message-content {
  position: relative;
  z-index: 1;
}

/* 强制覆盖所有可能的默认样式 */
:deep(.md-editor-v3__preview) {
  background: transparent !important;
  background-color: transparent !important;
  border: none !important;
  outline: none !important;
  box-shadow: none !important;
}

/* 确保用户消息中的文本颜色与蓝色背景形成对比 */
.user-message .message-text,
.user-message .message-text :deep(*) {
  color: white !important;
}

/* 确保用户消息中的时间也显示为白色 */
/* 已合并到上面的样式中 */

/* 正在输入指示器 - 美化版 */
.typing-indicator {
  display: flex;
  gap: 6px;
  padding: 16px 20px;
  background: linear-gradient(135deg, #ffffff 0%, #f0f9ff 100%);
  border-radius: 18px;
  border-bottom-left-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  max-width: 180px;
  margin-bottom: 8px;
  margin-left: 60px;
}

.typing-indicator .dot {
  width: 10px;
  height: 10px;
  background-color: #409eff;
  border-radius: 50%;
  animation: typing 1.4s infinite ease-in-out both;
  box-shadow: 0 0 4px rgba(64, 158, 255, 0.5);
}

.typing-indicator .dot:nth-child(1) {
  animation-delay: -0.32s;
}

.typing-indicator .dot:nth-child(2) {
  animation-delay: -0.16s;
}

@keyframes typing {
  0%, 80%, 100% {
    transform: scale(0);
    opacity: 0.3;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}

/* 输入区域 - 美化版 */
.chat-input-area {
  padding: 20px;
  border-top: 1px solid #e0e0e0;
  background-color: #ffffff;
  flex-shrink: 0;
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: 100; /* 提高z-index确保输入栏在最上层 */
  box-shadow: 0 -2px 12px rgba(0, 0, 0, 0.06);
  backdrop-filter: blur(8px);
  background-color: rgba(255, 255, 255, 0.95);
  transition: all 0.3s ease;
}

.input-container {
  display: flex;
  gap: 12px;
  align-items: flex-end;
  width: 100%;
}

.input-container textarea {
  flex: 1;
  min-height: 80px;
  max-height: 180px;
  padding: 16px 20px; /* 增加内边距 */
  border: 2px solid #e0e0e0;
  border-radius: 18px; /* 更大的圆角 */
  font-size: 14px;
  line-height: 1.6;
  resize: none;
  font-family: inherit;
  width: calc(100% - 80px);
  transition: all 0.3s ease;
  background-color: #fafafa;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.05);
}

.input-container textarea:focus {
  outline: none;
  border-color: #409eff;
  box-shadow: 0 0 0 3px rgba(64, 158, 255, 0.1), inset 0 2px 4px rgba(0, 0, 0, 0.05);
  background-color: #ffffff;
}

.input-container textarea:hover:not(:disabled) {
  border-color: #c0c4cc;
}

/* 确保消息不会紧贴输入框 - 增强版本 */
.chat-messages .message-item:last-child {
  margin-bottom: 30px; /* 增加底部间距，配合容器的内边距使用 */
}

.input-container textarea:focus {
  outline: none;
  border-color: #409eff;
}

.input-container textarea:disabled {
  background-color: #f5f7fa;
  cursor: not-allowed;
}

.input-actions {
  display: flex;
  gap: 8px;
}

.send-btn {
    padding: 8px 16px;
    background-color: #409eff;
    color: white;
    border: none;
    border-radius: 4px;
    font-size: 14px;
    cursor: pointer;
    transition: background-color 0.2s;
    height: 40px;
    width: 60px;
    white-space: nowrap;
  }
  
  .send-btn:hover:not(:disabled) {
    background-color: #66b1ff;
  }
  
  .send-btn:disabled {
    background-color: #c0c4cc;
    cursor: not-allowed;
  }
  
  .stop-btn {
    padding: 8px 16px;
    background-color: #f56c6c;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
    transition: background-color 0.2s;
    height: 40px;
    width: 60px;
    white-space: nowrap;
  }
  
  .stop-btn:hover {
    background-color: #f78989;
  }

/* 简化模型选择器样式 */
.model-selector {
  display: flex;
  align-items: center;
  gap: 12px;
  background-color: #f8f9fa;
  padding: 8px 12px;
  border-radius: 8px;
  border: 1px solid #e9ecef;
  transition: all 0.3s ease;
}

.model-selector.compact {
  gap: 8px;
  padding: 6px 10px;
  background-color: transparent;
  border: none;
}

.model-selector:hover {
  background-color: #e9ecef;
  border-color: #409eff;
}

.model-selector.compact:hover {
  background-color: #f0f7ff;
}

.model-label {
  font-size: 14px;
  color: #606266;
  font-weight: 500;
  white-space: nowrap;
}

.model-select {
  flex: 1;
  padding: 8px 12px;
  border: 1px solid #dcdfe6;
  border-radius: 6px;
  background-color: white;
  font-size: 14px;
  color: #303133;
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 160px;
}

.model-select.compact {
  min-width: 140px;
  padding: 6px 10px;
  border-radius: 6px;
  background-color: #fff;
  border: 1px solid #dcdfe6;
  font-size: 13px;
}

.model-select:focus {
  outline: none;
  border-color: #409eff;
  box-shadow: 0 0 0 3px rgba(64, 158, 255, 0.1);
}

.model-select:hover {
  border-color: #c0c4cc;
}

.model-select.compact:hover {
  border-color: #409eff;
}

/* 右键菜单样式 */
.context-menu {
  position: fixed;
  background-color: white;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  min-width: 140px;
  overflow: hidden;
  animation: slideDown 0.2s ease-out;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.context-menu .menu-item {
  padding: 10px 16px;
  cursor: pointer;
  font-size: 14px;
  color: #606266;
  transition: all 0.3s ease;
  border-bottom: 1px solid #f0f0f0;
}

.context-menu .menu-item:last-child {
  border-bottom: none;
}

.context-menu .menu-item:hover {
  background-color: #f5f7fa;
  color: #409eff;
  padding-left: 20px;
}

.context-menu-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 999;
  animation: fadeIn 0.2s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

/* 响应式布局 */
@media (max-width: 768px) {
  .chat-header {
    flex-direction: column;
    gap: 16px;
    align-items: stretch;
    padding: 12px 16px;
  }
  
  .model-selector {
    width: 100%;
    flex-direction: column;
    align-items: stretch;
    gap: 8px;
    padding: 12px;
  }
  
  .model-label {
    text-align: center;
  }
  
  .model-select {
    min-width: auto;
  }
  
  .message-item {
    margin: 10px 12px;
  }
  
  .message-content {
    max-width: 85%;
    padding: 12px 16px;
    border-radius: 10px;
  }
  
  .avatar-circle {
    width: 36px;
    height: 36px;
    font-size: 18px;
  }
  
  .input-container {
    flex-direction: column;
    align-items: stretch;
    gap: 12px;
  }
  
  .input-actions {
    justify-content: center;
    flex-wrap: wrap;
  }
  
  .send-btn,
  .stop-btn {
    flex: 1;
    min-width: 120px;
  }
  
  .chat-input-area {
    padding: 16px;
  }
  
  .message-textarea {
    min-height: 80px;
    padding: 12px 14px;
    font-size: 14px;
  }
  
  /* 优化移动端状态显示 */
  .loading,
  .error,
  .empty-chat {
    padding: 40px 16px;
  }
  
  .empty-icon {
    font-size: 48px;
  }
  
  .error-icon {
    font-size: 36px;
  }
  
  /* 优化滚动条显示 */
  .chat-messages::-webkit-scrollbar {
    width: 6px;
  }
}

/* 平板设备优化 */
@media (max-width: 1024px) {
  .chat-messages {
    padding: 16px;
  }
  
  .message-content {
    max-width: 75%;
  }
}

/* 深色模式适配准备 */
@media (prefers-color-scheme: dark) {
  /* 预留深色模式样式 */
}
</style>