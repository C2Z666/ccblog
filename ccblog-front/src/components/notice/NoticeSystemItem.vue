<template>
  <!-- 消息通知 -->
  <div :class="['notification', { 'unread': msg.state === 0 }]">
    <a
      :href="'/user/' + msg.operateUserId"
      class=""
      target="_blank"
    >
      <img
        :src="msg.operateUserPhoto"
        class="notification-img"
        loading="lazy"
      />
    </a>
    <div class="notification-right">
      <div class="notification-content" style="text-align: left;">
        <div class="profile">
          <a
            :href="'/user/' + msg.operateUserId"
            class=""
            target="_blank"
          >
            <span class="name">{{msg.operateUserName || '系统'}}</span>
          </a>
          <!-- 根据relatedType显示不同类型的系统通知 -->
        </div>
      </div>
      <div class="notification-content" style="text-align: left;">
          <span class="notification-text">
            {{msg.relatedInfo || '系统通知'}}
          </span>
      </div>
      <div class="notification-bottom">
        <span
          class="notification-time"
        >
          {{format(new Date(msg.createTime), 'yyyy-MM-dd HH:mm:ss')}}
        </span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { NoticeMsgType } from '@/http/ResponseTypes/NoticeType/NoticeMsgType'
import { format } from 'date-fns'

const props = defineProps<{
  msg: NoticeMsgType
}>()
</script>


<style scoped>
.notification {
  transition: background-color 0.3s ease;
}

/* 未读消息高亮样式 */
.unread {
  background-color: #f0f7ff;
  border-left: 3px solid #409eff;
}

/* 鼠标悬停效果 */
.notification:hover {
  background-color: #f5f7fa;
}

.unread:hover {
  background-color: #e6f2ff;
}

/* 通知文本样式 */
.notification-text {
  margin-left: 4px;
  margin-right: 4px;
}
</style>