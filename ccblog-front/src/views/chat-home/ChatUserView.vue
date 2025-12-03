<template>
  <div class="chat-container" style="display: flex; flex-direction: column; height: 100%; position: relative; overflow: hidden;">
    <!-- 聊天头部 -->
    <div class="chat-header">
      <div class="chat-user-info">
        <img 
          v-if="chatUserInfo?.peerInfo?.photo" 
          :src="chatUserInfo?.peerInfo?.photo?.replace(/[`'\s]/g, '')" 
          class="user-avatar cursor-pointer"
          alt="用户头像"
          @click="navigateToUserHome(chatUserInfo?.peerInfo?.userId)"
        />
        <span class="user-name">{{ chatUserInfo?.peerInfo?.userName || '加载中...' }}</span>
      </div>
    </div>

    <!-- 聊天内容区域 - 添加外层容器 -->
    <div class="message-container" style="flex: 1; overflow: hidden; display: flex; flex-direction: column;">
      <div class="chat-messages" ref="chatMessagesRef" style="flex: 1; overflow-y: auto; padding: 10px; padding-bottom: 70px;">
      <!-- 加载更多按钮 -->
      <div v-if="hasMore && !loading" class="load-more-button-container">
        <button 
          class="load-more-button" 
          @click="loadMoreHistory"
          :disabled="loadingMore"
        >
          {{ loadingMore ? '加载中...' : '查看更多' }}
        </button>
      </div>
      
      <div v-if="loading" class="loading">加载中...</div>
      <!-- <div v-else-if="error" class="error">加载失败，请重试</div> -->
      <div v-else-if="chatHistory.length === 0" class="empty-message">暂无聊天记录</div>
      <div 
        v-for="(message, index) in chatHistory" 
        :key="message.seq || index"
        class="message-wrapper"
        :class="{
          'own-message': Number(message.sendUserId) === Number(currentUserId),
          'other-message': Number(message.sendUserId) !== Number(currentUserId)
        }"
        @contextmenu.prevent="showContextMenu($event, message)"
      >
        <img 
          v-if="Number(message.sendUserId) !== Number(currentUserId)" 
          :src="chatUserInfo?.peerInfo?.photo?.replace(/[`'\s]/g, '')" 
          class="message-avatar cursor-pointer"
          alt="头像"
          @click="navigateToUserHome(chatUserInfo?.peerInfo?.userId)"
        />
        <div class="message-content">
          <div 
            class="message-bubble"
            :class="{ 'recalled-message': message.status === 1 }"
          >
            <template v-if="message.status === 1">
              <div class="recalled-icon">✓</div>
              <span class="recalled-text">{{ message.content }}</span>
            </template>
            <template v-else>
              {{ message.content }}
            </template>
          </div>
          <div class="message-time">
            {{ formatMessageTime(message.createTime) }}
          </div>
        </div>
        <img 
          v-if="Number(message.sendUserId) === Number(currentUserId)" 
          :src="chatUserInfo?.selfInfo?.photo?.replace(/[`'\s]/g, '')" 
          class="message-avatar cursor-pointer"
          alt="头像"
          @click="navigateToUserHome(chatUserInfo?.selfInfo?.userId)"
        />
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
      <div 
        v-if="selectedMessage && Number(selectedMessage.sendUserId) === Number(currentUserId) && canRecallMessage(selectedMessage)" 
        class="menu-item" 
        @click="recallMessage"
      >
        撤回
      </div>
    </div>
    
    <!-- 点击其他区域关闭右键菜单 -->
    <div 
      v-if="contextMenuVisible" 
      class="context-menu-backdrop" 
      @click="closeContextMenu"
    ></div>

    <!-- 聊天输入区域 - 固定在底部 -->
    <div class="chat-input-area" style="position: absolute; bottom: 0; left: 0; right: 0; padding: 10px; background-color: #fff; border-top: 1px solid #e0e0e0; z-index: 10;">
      <input 
        v-model="messageText" 
        type="text" 
        placeholder="输入消息..." 
        class="message-input" 
        style="width: calc(100% - 80px); padding: 8px 12px; border: 1px solid #dcdfe6; border-radius: 4px; outline: none;"
        @keyup.enter="sendMessage"
        :disabled="chatInputDisabled"
      />
      <button class="send-button" 
        @click="sendMessage" 
        :disabled="!messageText.trim() || chatInputDisabled"
        style="width: 60px; padding: 8px 0; margin-left: 10px; background-color: #409eff; color: white; border: none; border-radius: 4px; cursor: pointer;"
      >
        {{ chatBtnText }}
      </button>
    </div>
  </div>
</template>


<script setup lang="ts">
import { defineEmits } from 'vue';
import { ref, onMounted, computed, nextTick, watch, onBeforeUnmount } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { doDelete, doGet } from '@/http/BackendRequests';
import { useGlobalStore } from '@/stores/global';
import type { ChatUserCursorVO, ChatUserItemDTO } from '@/http/ResponseTypes/ChatType/ChatUserHistoryType';
import type { ChatUserInfoVO } from '@/http/ResponseTypes/ChatType/ChatUserInfoType';
import { USER_CHAT_HISTORY_URL, USER_CHAT_INFO_URL, USER_CHAT_PING_URL, USER_CHAT_TICKET_URL, WS_URL, USER_CHAT_RECALL_URL, USER_CHAT_DELETE_URL, API_PREFIX } from '@/http/URL';
import Stomp from "stompjs";
import type { CommonResponse } from '@/http/ResponseTypes/CommonResponseType';
import { dayjs } from 'element-plus';
import type { ChatSessionItemType } from '@/http/ResponseTypes/ChatType/ChatSessionType';

// 路由和全局状态
const router = useRouter();
const route = useRoute();
const globalStore = useGlobalStore();

// 接收props参数
const props = defineProps<{
  peerId?: number // 可选，用于在分栏布局中传递用户ID
}>();

// 定义emits
const emit = defineEmits<{
  'update-session-preview': [data: { 
    peerId: number; 
    preview: string; 
    time: string; 
    userName?: string; 
    userPhoto?: string 
  }];
  'updateChatPreview': [data: { 
    peerId: number; 
    lastMsgTime: string; 
    snapshot: string 
  }]
}>();

// 状态管理
const chatHistory = ref<ChatUserItemDTO[]>([]);
const chatUserInfo = ref<ChatUserInfoVO | null>(null);
const messageText = ref('');
const loading = ref(false);
const error = ref(false);
const chatMessagesRef = ref<HTMLElement>();
const chatInputDisabled = ref(true);
const chatBtnText = ref('等待连接');
const hasMore = ref(false); // 是否还有更多历史记录
const cursor = ref(undefined); // 分页游标，初始值设为空
const cursorSeq = ref(undefined); // 第一条消息的seq，初始化为空
const loadingMore = ref(false); // 是否正在加载更多
const limit = 5; // 每次加载的消息数量

let seq = 0;
// 右键菜单相关状态
const contextMenuVisible = ref(false);
const contextMenuPosition = ref({ x: 0, y: 0 });
const selectedMessage = ref<any>(null);

// STOMP协议的客户端
let stompClient: any = null;
// ping定时器
let pingTimer: number | null = null;
// WebSocket ticket
let ticket: string = '';

// 当前用户ID
const currentUserId = computed(() => {
  const id = globalStore.global?.user?.userId;
  return typeof id === 'string' ? parseInt(id) : (id || 0);
});

// 跳转到用户主页
const navigateToUserHome = (userId: number | undefined) => {
  if (userId) {
    // 跳转到用户主页，URL格式为 /user/{userId}/articlesTab
    router.push({
      path: `/user/${userId}/articlesTab`
    });
  }
};

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

// 更新左侧聊天预览
function updateChatPreview(sessionItem: ChatSessionItemType) {
  // 更新lastMsgTime和snapshot到左侧预览
  // 通过emit通知父组件更新聊天预览
  emit('updateChatPreview', {
    peerId: effectivePeerId.value,
    lastMsgTime: sessionItem.lastMsgTime,
    snapshot: sessionItem.snapshot
  });
}

// 删除消息
async function deleteMessage() {
  if (selectedMessage.value) {
    
    try {
      // 构建完整的消息体参数
      const chatUserItemDTO = {
        seq: selectedMessage.value.seq || 0,
        recvUserId: Number(selectedMessage.value.recvUserId), // 保留原接收者ID
        sendUserId: Number(selectedMessage.value.sendUserId), // 保留原发送者ID
        type: selectedMessage.value.type || 0,
        content: selectedMessage.value.content || '',
        readFlag: selectedMessage.value.readFlag || false,
        status: selectedMessage.value.status || 0,
        // 处理时区转换
        createTime: selectedMessage.value.createTime 
      };
      console.log('删除消息参数:', chatUserItemDTO);
      
      // console.log('准备调用删除接口，消息体:', chatUserItemDTO);
      
      // 调用后端删除接口
      const response = await doDelete<CommonResponse<ChatSessionItemType>>(USER_CHAT_DELETE_URL, chatUserItemDTO);
      // console.log('删除接口响应:', response);
      
      if (response && response.status === 200 && response.data) {
        // 更新左侧聊天预览
        if (response.data.result) {
          updateChatPreview(response.data.result);
        }
        // console.log(selectedMessage.value.seq);
        // 删除成功后，更新本地消息状态
        const index = chatHistory.value.findIndex(
          msg => msg.seq === selectedMessage.value.seq
        );
        if (index > -1) {
          chatHistory.value.splice(index, 1);
        }
        // 删除成功后，通知父组件更新会话预览
        // console.log('删除成功后:',response.data.result);
        if (response.data.result) {
          emit('update-session-preview', {
            peerId: effectivePeerId.value,
            preview: response.data.result.snapshot || '', // 使用后端返回的消息快照
            time: response.data.result.lastMsgTime,
            userName: chatUserInfo.value?.peerInfo.userName,
            userPhoto: chatUserInfo.value?.peerInfo.photo
          });
        }
      } else {
        // console.error('删除失败:', response);
      }
    } catch (error) {
      console.error('删除请求异常:', error);
    } finally {
      closeContextMenu();
    }
  } else {
    // console.log('未选中消息');
    closeContextMenu();
  }
}

// 撤回消息
async function recallMessage() {
  if (selectedMessage.value && Number(selectedMessage.value.sendUserId) === Number(currentUserId.value)) {
    
    try {
      // 调用后端撤回接口
            // 构建完整的消息体参数
      const chatUserItemDTO = {
        seq: selectedMessage.value.seq || 0,
        recvUserId: Number(selectedMessage.value.recvUserId), // 保留原接收者ID
        sendUserId: Number(selectedMessage.value.sendUserId), // 保留原发送者ID
        type: selectedMessage.value.type || 0,
        content: selectedMessage.value.content || '',
        readFlag: selectedMessage.value.readFlag || false,
        status: selectedMessage.value.status || 0,
        // 保持原始时间格式，让后端处理时区转换
        createTime: selectedMessage.value.createTime || ''
      };
      
      const response = await doGet<CommonResponse<ChatSessionItemType>>(USER_CHAT_RECALL_URL, chatUserItemDTO);
      // console.log('撤回接口响应:', response);
      
      if (response && response.status === 200 && response.data) {
        // 更新左侧聊天预览
        if (response.data.result) {
          updateChatPreview(response.data.result);
        }
        // console.log('撤回成功');
        // 撤回成功后，更新本地消息状态
        const index = chatHistory.value.findIndex(
          msg => msg.seq === selectedMessage.value.seq
        );
        if (index > -1) {
          // 不删除消息，而是替换为撤回提示
          const recalledMessage = {
            ...selectedMessage.value,
            content: '你撤回了一条消息',
            status: 1, // 标记为已撤回
            originalContent: selectedMessage.value.content // 保存原始内容以备不时之需
          };
          chatHistory.value.splice(index, 1, recalledMessage);
          
          // 检查是否是最后一条消息，如果是则使用撤回消息更新预览
          if (index === chatHistory.value.length - 1) {
            emit('update-session-preview', {
              peerId: effectivePeerId.value,
              preview: recalledMessage.content,
              time: recalledMessage.createTime || response.data.result.lastMsgTime,
              userName: chatUserInfo.value?.peerInfo.userName,
              userPhoto: chatUserInfo.value?.peerInfo.photo
            });
          } else {
            // 不是最后一条消息，使用后端返回的快照
            emit('update-session-preview', {
              peerId: effectivePeerId.value,
              preview: response.data.result.snapshot || '',
              time: response.data.result.lastMsgTime,
              userName: chatUserInfo.value?.peerInfo.userName,
              userPhoto: chatUserInfo.value?.peerInfo.photo
            });
          }
        } else {
          // 未找到消息但撤回成功，使用后端返回的快照
          emit('update-session-preview', {
            peerId: effectivePeerId.value,
            preview: response.data.result.snapshot || '',
            time: response.data.result.lastMsgTime,
            userName: chatUserInfo.value?.peerInfo.userName,
            userPhoto: chatUserInfo.value?.peerInfo.photo
          });
        }
      } else {
        console.error('撤回失败:', response);
      }
    } catch (error) {
      console.error('撤回请求异常:', error);
    } finally {
      closeContextMenu();
    }
  }
}

// 有效聊天对象ID
const effectivePeerId = computed(() => {
  if (props.peerId !== undefined) {
    return props.peerId;
  }
  return Number(route.params.peerId) || 0;
});

// 监听peerId变化，重新加载聊天数据
watch(effectivePeerId, async (newPeerId, oldPeerId) => {
  // 只有当peerId变化时才重置状态
  if (newPeerId !== oldPeerId) {
    // 重置cursor相关状态
    hasMore.value = false;
    cursor.value = undefined;
    cursorSeq.value = undefined;
  }
  
  if (newPeerId && newPeerId !== 0) {
    // 确保连接已建立
    if (!stompClient || !stompClient.connected) {
      initWs();
    }
    
    await Promise.all([
      fetchChatHistory(),
      fetchChatUserInfo()
    ]);
    
    // 数据加载完成后滚动到底部
    await nextTick();
    scrollToBottom();
  }
}, { immediate: true });

// 检查消息是否可以撤回（发送时间不超过两分钟）
const canRecallMessage = (message: any) => {
  if (!message.createTime) return false;
  
  const messageTime = new Date(message.createTime);
  const now = new Date();
  const diffMinutes = (now.getTime() - messageTime.getTime()) / (1000 * 60);
  
  // 消息发送时间不超过两分钟且未被撤回
  return diffMinutes <= 2 && message.status !== 1;
};

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

// 获取聊天历史记录
const fetchChatHistory = async (isLoadMore = false) => {
  if (!effectivePeerId.value) return;
  
  // 根据是初次加载还是加载更多来设置loading状态
  if (isLoadMore) {
    loadingMore.value = true;
  } else {
    loading.value = true;
    error.value = false;
  }
  
  try {
    // 准备请求参数
    const params: any = {
      peerId: effectivePeerId.value,
      limit: limit
    };
    
    // 如果是加载更多或者不是第一次加载，添加cursor和cursorSeq参数
    if (isLoadMore || (cursor.value || cursorSeq.value !== 999999999)) {
      params.cursor = cursor.value;
      params.cursorSeq = cursorSeq.value;
    }
      // console.log('请求参数:', params);
    
    const response = await doGet<ChatUserCursorVO>(USER_CHAT_HISTORY_URL, params);
    
    if (response.data.result) {
      const newMessages = response.data.result.chatUserItems || [];
    
      // 备注:回复的消息从晚到早
      
      // 更新cursor和cursorSeq（使用新获取消息的第一条）
      if (newMessages.length > 0) {
        const lastMessage = newMessages[newMessages.length - 1]; // 最后一条是最早的消息
        if (lastMessage.createTime) {
          cursor.value = lastMessage.createTime;
        }
        if (lastMessage.seq) {
          cursorSeq.value = lastMessage.seq;
        }
      }
      
      // console.log('获取到的新消息:', newMessages);
      if (isLoadMore) {
        // 加载更多时，将新消息添加到现有消息的前面
        chatHistory.value = [...newMessages.reverse(), ...chatHistory.value];
      } else {
        seq = newMessages[0].seq || 0;
        // 初次加载时，直接替换消息列表
        chatHistory.value = newMessages.reverse();
      }
      
      // 更新hasMore状态
      hasMore.value = response.data.result.hasMore || false;
      
    }
  } catch (err) {
    console.error('获取聊天历史失败:', err);
    if (!isLoadMore) {
      error.value = true;
    }
  } finally {
    // 重置loading状态
    if (isLoadMore) {
      loadingMore.value = false;
    } else {
      loading.value = false;
    }
  }
};

// 获取聊天用户信息(头像,用户名...)
const fetchChatUserInfo = async () => {
  if (!effectivePeerId.value) return;
  
  try {
    const response = await doGet<CommonResponse>(USER_CHAT_INFO_URL, {
      peerId: effectivePeerId.value
    });
    
    if (response.data.result) {
      chatUserInfo.value = response.data.result;
     globalStore.setGlobal(response.data.global)
    }
    // 设置全局信息

  } catch (err) {
    console.error('获取聊天用户信息失败:', err);
  }
};

// 初始化WebSocket连接
const initWs = async () => {
  // console.log("初始化用户私信WebSocket连接");
  
  try {
    // 1. 先请求获取ticket
    // console.log('请求获取聊天ticket...');
    const ticketResponse = await doGet<any>(USER_CHAT_TICKET_URL, {});
    if (ticketResponse && ticketResponse.data && ticketResponse.data.result) {
      // 使用全局ticket变量
      ticket = ticketResponse.data.result;
      // console.log('成功获取到ticket:', ticket);
    } else {
      console.error('获取ticket失败:', ticketResponse);
      return;
    }
    
    // 使用标准WebSocket API创建连接
    // 根据后端配置，正确的WebSocket端点是/chat
    const wsUrl = `${WS_URL}${API_PREFIX}/chat/${ticket}`; // 定义在后端的addEndpoint
    // console.log('连接WebSocket URL:', wsUrl);

    
    const socket = new WebSocket(wsUrl);
    stompClient = Stomp.over(socket);
    
    // 启用调试模式以便于调试
    stompClient.debug = (str: string) => {
      // console.log('STOMP调试信息:', str);
    };
    
    // 设置STOMP心跳 - 方案A
    stompClient.heartbeat.outgoing = 20000; // 20秒发送一次心跳
    stompClient.heartbeat.incoming = 20000; // 20秒接收一次心跳
    
    // 设置连接参数(只在连接头有效)
    // console.log('连接头 ticket:', ticket);
    stompClient.connectHeaders = {
      'heart-beat': '20000,20000', // 心跳配置与上面保持一致
      'ticket': ticket // 将ticket放入连接头中
    };
    
    // 连接STOMP服务器
    stompClient.connect(stompClient.connectHeaders, 
      function(frame: any) {
        console.log('成功连接到STOMP服务器:', frame);
        // 开放按钮和输入框
        chatInputDisabled.value = false;
        chatBtnText.value = '发送';
        
        // 启动ping定时器，保持连接活跃
        startPingTimer();
        
        // 订阅自己的消息队列
        // todo:这里后端可以把消息传过来,可以考虑历史记录在这里请求
        // console.log("订阅路径:", `/user/${effectivePeerId.value}/chat/user`);
        stompClient.subscribe(`/user/${currentUserId.value}/chat/user`, function(message: any) {
          console.log('收到私信/回执:', message.body);            
          
          try {
            const messageData = JSON.parse(message.body);
            
            // 判断消息类型
            if (typeof messageData === 'object' && messageData !== null) {
              // 类型1: 发送给自己的回执 (包含seq和type='receipt')
              if (messageData.type === 'receipt' && messageData.seq !== undefined) {
                const serverSeq = messageData.seq;
                
                // 查找最后一条发送的临时消息（seq为0且发送者为当前用户的消息）
                const lastSentIndex = chatHistory.value.findLastIndex(
                  msg => msg.seq === 0 && Number(msg.sendUserId) === Number(currentUserId.value)
                );
                
                if (lastSentIndex > -1) {
                  // 更新消息的seq值，移除临时ID
                  const updatedMessage = {
                    ...chatHistory.value[lastSentIndex],
                    seq: serverSeq
                  };

                  // 更新本地消息列表
                  chatHistory.value.splice(lastSentIndex, 1, updatedMessage);
                  console.log('已更新本地消息的seq值:', serverSeq);
                }
              }
              // 类型2: 撤回消息 (包含seq和type='recall')
              else if (messageData.type === 'recall' && messageData.seq !== undefined) {
                const recallSeq = messageData.seq;
                
                // 查找需要撤回的消息
                const targetMessageIndex = chatHistory.value.findIndex(
                  msg => msg.seq === recallSeq
                );
                
                if (targetMessageIndex > -1) {
                  // 标记消息为已撤回
                  const targetMessage = chatHistory.value[targetMessageIndex];
                  // 判断是自己撤回还是对方撤回
                  const isSelfRecall = Number(targetMessage.sendUserId) === Number(currentUserId.value);
                  
                  const updatedMessage = {
                    ...targetMessage,
                    status: 1, // 1表示已撤回
                    content: isSelfRecall ? '您撤回了一条消息' : '对方撤回了一条消息'
                  };

                  // 更新本地消息列表
                  chatHistory.value.splice(targetMessageIndex, 1, updatedMessage);
                  // console.log('已标记消息为撤回状态:', recallSeq);
                    
                  // 检查是否是最后一条消息，如果是则更新会话预览
                  if (targetMessageIndex === chatHistory.value.length - 1) {
                    // 通知父组件更新会话预览为撤回消息
                    emit('update-session-preview', {
                      peerId: effectivePeerId.value,
                      preview: updatedMessage.content,
                      time: updatedMessage.createTime || formatMessageTime(new Date().toISOString()),
                      userName: chatUserInfo.value?.peerInfo.userName,
                      userPhoto: chatUserInfo.value?.peerInfo.photo
                    });
                  }
                }
              }
              // 类型3: 新格式消息 (type='msg'，消息内容在data字段中)
              else if (messageData.type === 'msg' && messageData.data) {
                const actualMessage = messageData.data;
                
                if (actualMessage.content && actualMessage.seq) {
                  // 检查是否已经存在相同seq的消息，避免重复添加
                  const existingMessageIndex = chatHistory.value.findIndex(
                    msg => msg.seq === actualMessage.seq
                  );
                  
                  if (existingMessageIndex === -1) {
                    // 确保消息属于当前聊天对象
                    const isFromCurrentChat = 
                      Number(actualMessage.sendUserId) === Number(effectivePeerId.value) ||
                      Number(actualMessage.recvUserId) === Number(effectivePeerId.value);
                      
                    if (isFromCurrentChat) {
                      // 添加新消息到聊天历史
                      chatHistory.value.push(actualMessage);
                      console.log('接收到新格式消息并添加到聊天历史');
                      
                      // 通知父组件更新会话预览
                      emit('update-session-preview', {
                        peerId: effectivePeerId.value,
                        preview: actualMessage.content.substring(0, 30) + (actualMessage.content.length > 30 ? '...' : ''),
                        time: actualMessage.createTime || formatMessageTime(new Date().toISOString()),
                        userName: chatUserInfo.value?.peerInfo.userName,
                        userPhoto: chatUserInfo.value?.peerInfo.photo
                      });
                    }
                  }
                }
              }
              // 类型4: 旧格式完整消息体（从其他用户发来的）- 兼容旧格式
              else if (messageData.content && messageData.seq) {
                // 检查是否已经存在相同seq的消息，避免重复添加
                const existingMessageIndex = chatHistory.value.findIndex(
                  msg => msg.seq === messageData.seq
                );
                
                if (existingMessageIndex === -1) {
                  // 确保消息属于当前聊天对象
                  const isFromCurrentChat = 
                    Number(messageData.sendUserId) === Number(effectivePeerId.value) ||
                    Number(messageData.recvUserId) === Number(effectivePeerId.value);
                    
                  if (isFromCurrentChat) {
                    // 添加新消息到聊天历史
                    chatHistory.value.push(messageData);
                    console.log('接收到旧格式消息并添加到聊天历史');
                    
                    // 通知父组件更新会话预览
                    emit('update-session-preview', {
                      peerId: effectivePeerId.value,
                      preview: messageData.content.substring(0, 30) + (messageData.content.length > 30 ? '...' : ''),
                      time: messageData.createTime || formatMessageTime(new Date().toISOString()),
                      userName: chatUserInfo.value?.peerInfo.userName,
                      userPhoto: chatUserInfo.value?.peerInfo.photo
                    });
                  }
                }
              }
            }
            // 兼容旧格式: 纯数字seq值
            else if (typeof messageData === 'string') {
              const serverSeq = parseInt(messageData.trim(), 10);
              if (!isNaN(serverSeq)) {
                // 查找最后一条发送的临时消息（seq为0且发送者为当前用户的消息）
                const lastSentIndex = chatHistory.value.findLastIndex(
                  msg => msg.seq === 0 && Number(msg.sendUserId) === Number(currentUserId.value)
                );
                
                if (lastSentIndex > -1) {
                  // 更新消息的seq值，移除临时ID
                  const updatedMessage = {
                    ...chatHistory.value[lastSentIndex],
                    seq: serverSeq
                  };

                  // 更新本地消息列表
                  chatHistory.value.splice(lastSentIndex, 1, updatedMessage);
                }
              }
            }
          } catch (parseError) {
            console.error('解析消息失败:', parseError, '原始消息:', message.body);
          }
          finally {
            // 滚动到底部
            nextTick(() => {
              scrollToBottom();
            });
          }
        });
      
        // console.log('已订阅私信队列');
      },
      function(error: any) {
        console.error('STOMP连接失败:', error);
        // 停止ping定时器
        stopPingTimer();
        // 连接失败时的状态管理
        chatInputDisabled.value = true;
        chatBtnText.value = '重连';
        // 如果是token过期或无效，可以尝试重新连接
        setTimeout(() => {
          if (!stompClient || !stompClient.connected) {
            console.log('尝试重新连接WebSocket...');
            initWs();
          }
        }, 5000);
      }
    );
    
    // 关闭链接时的处理
    socket.onclose = disconnect;
  } catch (err) {
    console.error('创建WebSocket连接失败:', err);
    chatInputDisabled.value = true;
    chatBtnText.value = '连接失败';
  }
};

// 发送消息
const sendMessage = async () => {
  if (!messageText.value.trim() || !effectivePeerId.value) return;
  
  const message = messageText.value.trim();
  messageText.value = '';
  
  // 记录发送的消息到本地，保证用户体验的即时性
  const newMessage: ChatUserItemDTO = {
    seq: 0,
    recvUserId: Number(effectivePeerId.value),
    sendUserId: Number(currentUserId.value),
    type: 0,
    content: message,
    readFlag: false,
    status: 0,
    createTime: new Date().toLocaleString('sv-SE', { timeZone: 'Asia/Shanghai' }).replace(' ', 'T')
  };
  
  chatHistory.value.push(newMessage);
  
  // 滚动到底部
  await nextTick();
  scrollToBottom();
  
  // 通过WebSocket发送消息
  if (stompClient && stompClient.connected) {
    try {
      // console.log('通过WebSocket发送消息给用户:', effectivePeerId.value, '内容:', newMessage);
      
      // 后续 SEND 需要把ticket带上  注意前端发消息不管后端的RequestMapping,并且要加上/app,这个是配置的
      stompClient.send(`/app/private/${effectivePeerId.value}`, {ticket: ticket}, JSON.stringify(newMessage));
      
      // console.log('消息发送成功');
      
      // 发送成功后，通知父组件更新会话预览
      emit('update-session-preview', {
        peerId: Number(effectivePeerId.value),
        preview: message, // 使用消息内容作为预览
        time: newMessage.createTime,
        userName: chatUserInfo.value?.peerInfo.userName, // 传递用户名
        userPhoto: chatUserInfo.value?.peerInfo.photo  // 传递头像 || '' // 传递头像
      });
    } catch (err) {
      console.error('WebSocket发送消息失败:', err);
      // 发送失败时可以更新消息状态或提示用户
      chatBtnText.value = '发送失败';
    }
  } else {
    // console.log('WebSocket未连接，尝试重连...');
    // 尝试重新连接
    initWs();
    chatBtnText.value = '发送失败';
  }
};

// 发送ping消息保持连接
const sendPing = () => {
  if (stompClient && stompClient.connected) {
    try {
      // 发送ping消息到后端的ping端点，同时在header中传递ticket
      stompClient.send(USER_CHAT_PING_URL, {ticket: ticket}, '');
      // console.log('发送ping消息，保持连接活跃，携带ticket:', ticket);
    } catch (err) {
      console.error('发送ping消息失败:', err);
    }
  }
};

// 启动ping定时器
const startPingTimer = () => {
  // 清除已存在的定时器
  if (pingTimer !== null) {
    clearInterval(pingTimer);
  }
  
  // 每10秒发送一次ping消息
  pingTimer = window.setInterval(sendPing, 60000);
  // console.log('已启动ping定时器，每60秒发送一次ping消息');
};

// 停止ping定时器
const stopPingTimer = () => {
  if (pingTimer !== null) {
    clearInterval(pingTimer);
    pingTimer = null;
    // console.log('已停止ping定时器');
  }
};

// 断开WebSocket连接
function disconnect() {
  // 停止ping定时器
  stopPingTimer();
  
  if (stompClient !== null) {
    stompClient.disconnect(() => {});
  }
  // console.log("WebSocket连接已断开");
  stompClient = null;
  
  // 更新UI状态
  chatInputDisabled.value = true;
  chatBtnText.value = '重连';
}


// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (chatMessagesRef.value) {
      chatMessagesRef.value.scrollTop = chatMessagesRef.value.scrollHeight;
      chatMessagesRef.value.scroll({
        top: chatMessagesRef.value.scrollHeight + 100,
        behavior: 'smooth'
      });
    }
  });
};

// 加载更多历史消息
const loadMoreHistory = async () => {
  if (hasMore.value && !loadingMore.value) {
    await fetchChatHistory(true);
    
    // 等待DOM更新后，直接滚动到顶部，显示新加载的历史消息
    nextTick(() => {
      if (chatMessagesRef.value) {
        chatMessagesRef.value.scrollTop = 0;
      }
    });
  }
};

// 组件挂载时初始化
onMounted(async () => {
  // 当有peerId时立即加载数据和建立连接
  if (effectivePeerId.value && effectivePeerId.value !== 0) {
    // 初始化WebSocket连接
    if (globalStore.global?.isLogin && currentUserId.value) {
      initWs();
    }
    
    // 加载聊天数据
    await Promise.all([
      fetchChatHistory(),
      fetchChatUserInfo()
    ]);
    
    // 数据加载完成后滚动到底部
    await nextTick();
    scrollToBottom();
  }
});

// 组件卸载时清理资源
onBeforeUnmount(() => {
  console.log('组件即将卸载，开始清理资源');
  // 停止ping定时器
  stopPingTimer();
  // 断开WebSocket连接
  disconnect();
  console.log('组件卸载完成');
});
</script>

<style scoped>
/* 可点击元素样式 */
.cursor-pointer {
  cursor: pointer;
  transition: transform 0.2s ease, opacity 0.2s ease;
}

.cursor-pointer:hover {
  transform: scale(1.05);
  opacity: 0.9;
}

/* 右键菜单样式 */
.context-menu {
  position: fixed;
  background: white;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  z-index: 1000;
  min-width: 100px;
}

.menu-item {
  padding: 8px 16px;
  cursor: pointer;
  font-size: 14px;
}

.menu-item:hover {
  background-color: #f5f5f5;
}

.context-menu-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 999;
}

.chat-container {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 60px); /* 减去顶部导航栏高度 */
  background-color: #f5f5f5;
  overflow: hidden;
  position: relative;
}

/* 聊天消息区域容器 */
.message-container {
  flex: 1;
  overflow: hidden;
  position: relative;
  display: flex;
  flex-direction: column;
  min-height: 0; /* 允许容器收缩以适应内容 */
  height: calc(100vh - 120px); /* 固定高度，减去头部和输入区域的高度 */
}

.chat-header {
  display: flex;
  align-items: center;
  padding: 10px 16px;
  background-color: #fff;
  border-bottom: 1px solid #e0e0e0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  flex-shrink: 0; /* 防止头部被压缩 */
}

.back-button {
  padding: 6px 12px;
  background-color: transparent;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  margin-right: 16px;
  transition: all 0.3s;
}

.back-button:hover {
  background-color: #f5f5f5;
}

.chat-user-info {
  display: flex;
  align-items: center;
  flex: 1;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 12px;
  object-fit: cover;
}

.user-name {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
  position: relative;
  /* 确保滚动条正常显示 */
  -ms-overflow-style: auto;
  scrollbar-width: auto;
  /* 确保内容足够时可以滚动 */
  min-height: 0;
  /* 确保不会超出容器 */
  max-height: 100%;
  /* 固定高度 */
  height: calc(100% - 80px);
}

.loading, .error, .empty-message {
  text-align: center;
  padding: 40px;
  color: #999;
}

.message-wrapper {
  display: flex;
  align-items: flex-start;
  margin-bottom: 15px;
  width: 100%;
}

/* 自己的消息 - 右侧显示 */
.own-message {
  justify-content: flex-end;
}

/* 对方的消息 - 左侧显示 */
.other-message {
  justify-content: flex-start;
}

.message-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin: 0 10px;
  object-fit: cover;
}

.message-content {
  max-width: 70%;
  display: flex;
  flex-direction: column;
}

.own-message .message-content {
  align-items: flex-end;
}

.other-message .message-content {
  align-items: flex-start;
}

.message-bubble {
  padding: 10px 15px;
  border-radius: 18px;
  word-wrap: break-word;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.own-message .message-bubble {
  background-color: #9EEA6A;
  color: #000;
  margin-right: 0;
  margin-left: auto;
}

.other-message .message-bubble {
  background-color: #ffffff;
  color: #333;
  margin-left: 0;
  margin-right: auto;
}

/* 撤回消息样式 - 类似微信效果 */
.message-bubble.recalled-message {
  background-color: transparent !important;
  padding: 4px 8px;
  cursor: default;
  box-shadow: none;
}

.recalled-text {
  font-size: 12px;
  color: #888;
  font-style: italic;
  user-select: none;
}

.recalled-icon {
  display: inline-block;
  width: 14px;
  height: 14px;
  line-height: 14px;
  text-align: center;
  background-color: #ccc;
  color: white;
  border-radius: 50%;
  font-size: 10px;
  margin-right: 4px;
  vertical-align: middle;
}

/* 禁用撤回消息的右键菜单 */
.message-wrapper:has(.recalled-message) {
  pointer-events: none;
}

.message-time {
  font-size: 12px;
  color: #999;
  padding: 0 6px;
}

/* 撤回消息样式 - 类似微信效果 */
.message-bubble.recalled-message {
  background-color: transparent !important;
  padding: 6px 10px;
  cursor: default;
  border: none;
  box-shadow: none;
  color: #999;
}

.recalled-text {
  font-size: 12px;
  color: #999;
  font-style: normal;
  user-select: none;
  letter-spacing: 0.5px;
}

.recalled-icon {
  display: inline-block;
  width: 16px;
  height: 16px;
  line-height: 16px;
  text-align: center;
  background-color: #d8d8d8;
  color: #666;
  border-radius: 50%;
  font-size: 11px;
  margin-right: 5px;
  vertical-align: middle;
  font-weight: bold;
}

/* 禁用撤回消息的右键菜单 */
.message-wrapper:has(.recalled-message) {
  pointer-events: none;
}

/* 确保在不同屏幕尺寸下的响应式显示 */
@media screen and (max-width: 768px) {
  .recalled-text {
    font-size: 11px;
  }
  
  .recalled-icon {
    width: 14px;
    height: 14px;
    line-height: 14px;
    font-size: 10px;
  }
}

/* 确保撤回消息的时间显示保持一致 */
.message-wrapper:has(.recalled-message) .message-time {
  opacity: 0.7;
}

.chat-input-area {
  display: flex;
  padding: 16px;
  background: white;
  border-top: 1px solid #e0e0e0;
  gap: 10px;
  flex-shrink: 0;
  /* 固定在底部 */
  position: sticky;
  bottom: 0;
  z-index: 10;
}

.message-input {
  flex: 1;
  padding: 10px 16px;
  border: 1px solid #e0e0e0;
  border-radius: 20px;
  outline: none;
  font-size: 14px;
  resize: none;
}

.message-input:focus {
  border-color: #409eff;
}

.send-button {
  padding: 10px 24px;
  background-color: #409eff;
  color: #fff;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
}

.send-button:hover:not(:disabled) {
  background-color: #66b1ff;
}

.send-button:disabled {
  background-color: #dcdfe6;
  cursor: not-allowed;
}

.load-more-button-container {
  display: flex;
  justify-content: center;
  padding: 10px 0;
}

.load-more-button {
  padding: 6px 16px;
  background-color: #f0f0f0;
  color: #606266;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
}

.load-more-button:hover:not(:disabled) {
  background-color: #e6f2ff;
  border-color: #409eff;
  color: #409eff;
}

.load-more-button:disabled {
  background-color: #f5f7fa;
  color: #c0c4cc;
  cursor: not-allowed;
}

.loading-more {
  text-align: center;
  padding: 10px;
  color: #909399;
  font-size: 14px;
}

/* 滚动条样式 */
.chat-messages::-webkit-scrollbar {
  width: 6px;
}

.chat-messages::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.chat-messages::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.chat-messages::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>