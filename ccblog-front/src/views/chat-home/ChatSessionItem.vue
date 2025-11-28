

<template>
  <div
    class="chat-session-item" 
    :class="{ 
      'selected': isSelected,
      'top-session': session.isTop
    }"
    @click="handleClick"
  >
    <!-- 用户头像区域 -->
    <div class="session-avatar">
      <img 
        v-if="session.peerPhoto" 
        :src="session.peerPhoto as string"
        alt="用户头像" 
        class="avatar-image"
      >
      <div v-else class="avatar-placeholder">
        {{ session.peerName?.charAt(0) || '用' }}
      </div>
    </div>
    
    <!-- 会话信息区域 -->
    <div class="session-info">
      <div class="session-header">
        <h4 class="session-name">{{ session.peerName || '未知用户' }}</h4>
        <div class="session-actions">
          <!-- 置顶图标 -->
          <div v-if="session.isTop" class="top-icon">
            <StarFilled size="16" color="#409EFF" />
          </div>
          
          <!-- 下拉菜单按钮 - 点击显示菜单，鼠标悬停显示"操作" -->
          <el-dropdown trigger="click" @command="handleSessionOperation">
            <button class="more-btn" title="操作" @click="handleMenuClick">
              ...
            </button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item 
                  :command="session.isTop ? ChatUserSessionOperationType.CANCEL_TOP : ChatUserSessionOperationType.TOP"
                >
                  <template #icon>
                    <Star size="16" />
                  </template>
                  {{ session.isTop ? '取消置顶' : '置顶会话' }}
                </el-dropdown-item>
                <el-dropdown-item 
                  divided
                ></el-dropdown-item>
                <el-dropdown-item 
                  :command="ChatUserSessionOperationType.DELETE"
                  class="delete-item"
                >
                  删除会话
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
      <div class="session-preview">
        <span class="preview-text" :class="{ 'unread': session.unreadCount && session.unreadCount > 0 }">
          {{ session.snapshot || '暂无消息' }}
        </span>
        <span class="unread-badge" v-if="session.unreadCount && session.unreadCount > 0">
          {{ session.unreadCount > 99 ? '99+' : session.unreadCount }}
        </span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { ChatSessionItemType, ChatSessionResponse } from '@/http/ResponseTypes/ChatType/ChatSessionType'
import { ChatUserSessionOperationType } from '@/constants/ChatUserSessionOperationTypes'
import { USER_CHAT_SESSION_OPERATE_URL } from '@/http/URL'
import { doGet, doPost } from '@/http/BackendRequests'
import { ElMessage } from 'element-plus'
import { Star, StarFilled } from '@element-plus/icons-vue'

const props = defineProps<{
  session: ChatSessionItemType;
  isSelected?: boolean;
}>()

const emit = defineEmits<{
  (e: 'click', peerId: number): void;
  (e: 'sessionOperated', sessionId: number, type: number): void;
}>()

const handleClick = () => {
  // 直接传递peerId参数
  emit('click', props.session.peerId)
}

// 处理会话操作
const handleSessionOperation = (command: number) => {
  // 确保只处理有效的操作类型
  const validTypes = [
    ChatUserSessionOperationType.DELETE,
    ChatUserSessionOperationType.TOP,
    ChatUserSessionOperationType.CANCEL_TOP
  ];
  
  if (!validTypes.includes(command)) {
    return;
  }
  
  const sessionId = props.session.sessionId

  
  doPost(`${USER_CHAT_SESSION_OPERATE_URL}/${command}?sessionId=${sessionId}`, {})
    .then((response) => {
      if (response.status === 200 && response.data) {
        ElMessage.success(
          command === ChatUserSessionOperationType.DELETE ? '删除会话成功' : 
          command === ChatUserSessionOperationType.TOP ? '置顶会话成功' : 
          command === ChatUserSessionOperationType.CANCEL_TOP ? '取消置顶成功' : '操作成功'
        )
        // 已经在前端进行了即时更新，这里不再需要通知
      } else {
        ElMessage.error('操作失败，请稍后重试')
        // 如果操作失败，可以考虑在父组件中恢复状态
      }
        // 前端即时更新UI
  if (command === ChatUserSessionOperationType.DELETE) {
    // 立即从UI中移除会话
    emit('sessionOperated', sessionId, command)
  } else if (command === ChatUserSessionOperationType.TOP) {
    // 立即将会话置顶
    emit('sessionOperated', sessionId, command)
  } else if (command === ChatUserSessionOperationType.CANCEL_TOP) {
    // 立即取消置顶
    emit('sessionOperated', sessionId, command)
  }
    })
    .catch((error) => {
      console.error('会话操作失败:', error)
      ElMessage.error('操作失败，请稍后重试')
      // 如果操作失败，可以考虑在父组件中恢复状态
    })
}

// 点击按钮时阻止冒泡，避免触发会话选择
const handleMenuClick = (event: Event) => {
  event.stopPropagation()
}
</script>

<style scoped>
.chat-session-item {
  display: flex;
  align-items: flex-start;
  padding: 12px 16px;
  cursor: pointer;
  transition: background-color 0.2s;
  border-bottom: 1px solid #f0f0f0;
}

/* 置顶会话样式 */
.chat-session-item.top-session {
  background-color: #f0f9ff;
  border-left: 3px solid #409EFF;
}

.chat-session-item.top-session:hover {
  background-color: #e6f7ff;
}

.chat-session-item.top-session.selected {
  background-color: #bae7ff;
}

.chat-session-item:hover {
  background-color: #f9f9f9;
}

.chat-session-item.selected {
  background-color: #f0f0f0;
}

.session-avatar {
  margin-right: 12px;
}

.avatar-placeholder {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background-color: #409eff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: 500;
}

.avatar-image {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
  border: 1px solid #e0e0e0;
}

.session-info {
  flex: 1;
  min-width: 0;
}

.session-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
  position: relative;
}

.session-actions {
  display: flex;
  align-items: center;
  gap: 4px;
}

.more-btn {
  background: none;
  border: none;
  cursor: pointer;
  padding: 6px 10px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #999;
  opacity: 0;
  transition: all 0.2s;
  margin-left: 8px;
  font-size: 18px;
  font-weight: 600;
  line-height: 1;
  transform: scale(1);
  transition: opacity 0.2s, background-color 0.2s, transform 0.1s;
}

.more-btn:hover {
  background-color: #f0f0f0;
  color: #666;
  transform: scale(1.05);
}

.chat-session-item:hover .more-btn {
  opacity: 1;
}

.more-btn:hover {
  background-color: #f0f0f0;
  color: #666;
}

.top-icon {
  color: #409EFF;
  margin-right: 4px;
  font-weight: bold;
}

.delete-item {
  color: #f56c6c;
}

/* 美化下拉菜单 */
:deep(.el-dropdown-menu) {
  margin-top: 6px;
  min-width: 140px;
  border-radius: 8px;
  border: 1px solid #e4e7ed;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  background-color: #fff;
  padding: 4px 0;
  overflow: hidden;
}

:deep(.el-dropdown-menu__item) {
  padding: 10px 20px;
  font-size: 14px;
  color: #606266;
  transition: all 0.2s;
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  gap: 8px;
}

:deep(.el-dropdown-menu__item:hover) {
  background-color: #f0f9ff;
  color: #409eff;
  padding-left: 24px;
}

:deep(.el-dropdown-menu__item.is-disabled) {
  color: #c0c4cc;
  background-color: #f5f7fa;
}

:deep(.el-dropdown-menu__item.divided) {
  border-top: 1px solid #ebeef5;
  margin-top: 4px;
  padding-top: 4px;
}

/* 删除选项特殊样式 */
:deep(.el-dropdown-menu__item.delete-item) {
  color: #f56c6c;
}

:deep(.el-dropdown-menu__item.delete-item:hover) {
  background-color: #fef0f0;
  color: #f56c6c;
}

/* 图标样式优化 */
:deep(.el-dropdown-menu__item .el-icon) {
  font-size: 16px;
  color: inherit;
}

/* 解决下拉菜单点击穿透问题 */
:deep(.el-dropdown-menu) {
  pointer-events: auto;
  z-index: 1000;
}

/* 下拉菜单项动画 */
:deep(.el-dropdown-menu__item) {
  animation: dropdown-item-fade-in 0.2s ease;
}

@keyframes dropdown-item-fade-in {
  0% {
    opacity: 0;
    transform: translateY(-5px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

.session-name {
  margin: 0;
  font-size: 16px;
  font-weight: 500;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 180px;
}

.session-time {
  font-size: 12px;
  color: #999;
}

.session-preview {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.preview-text {
  font-size: 14px;
  color: #666;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  flex: 1;
  margin-right: 8px;
}

.preview-text.unread {
  color: #333;
  font-weight: 500;
}

.unread-badge {
  background-color: #f56c6c;
  color: white;
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 10px;
  min-width: 20px;
  text-align: center;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .session-name {
    max-width: 120px;
  }
  
  .avatar-placeholder,
  .avatar-image {
    width: 40px;
    height: 40px;
    font-size: 16px;
  }
  
  /* 移动端始终显示操作按钮 */
  .more-btn {
    opacity: 1;
  }
}
</style>