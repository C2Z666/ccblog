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
      <div class="notification-content">
        <div class="profile">
          <a
            :href="'/user/home?userId=' + msg.operateUserId"
            class=""
            target="_blank"
          >
            <span class="name">{{msg.operateUserName}}</span>
          </a>
          赞了你
          <!-- 根据relatedType显示不同类型 -->
          <template v-if="msg.relatedType === 1">
            的文章
            <a :href="getTargetUrl()">
              <span class="title-article">{{msg.relatedInfo}}</span>
            </a>
          </template>
          <template v-else-if="msg.relatedType === 2">
            在
            <a :href="ARTICLE_DETAIL_URL_FRONT+`/${props.msg.extendId || props.msg.relatedId}`">
              <span class="title-article">{{msg.extendInfo}}</span>
            </a>
            发表的评论
            <a :href="getTargetUrl()" v-if="msg.relatedId">
              <span class="title-article">{{msg.relatedInfo}}</span>
            </a>
          </template>
          <template v-else-if="msg.relatedType === 3">
            专栏
            <a :href="getTargetUrl()" v-if="msg.relatedId">
              <span class="title-article">{{msg.relatedInfo}}</span>
            </a>
          </template>
          <template v-else>
            内容
          </template>
        </div>
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
import { ARTICLE_DETAIL_URL } from '@/http/URL';
import { format } from 'date-fns'
import { ARTICLE_DETAIL_URL_FRONT } from '@/http/URL';

const props = defineProps<{
  msg: NoticeMsgType
}>()

// 根据relatedType和relatedId生成目标URL
const getTargetUrl = () => {
  const { extendId,relatedType, relatedId } = props.msg;
  const commentId = relatedType === 2 ? relatedId : '';
  
  // 确保relatedId存在
  // if (!relatedId && typeof relatedId !== 'number') return '#';
  
  let baseUrl = '#';
  
  // 根据relatedType生成对应的URL
  switch (relatedType) {
    case 1: // 文章
      baseUrl = ARTICLE_DETAIL_URL_FRONT+`/${relatedId}`;
      break;
    case 2: // 评论
      baseUrl = ARTICLE_DETAIL_URL_FRONT+`/${extendId}#comment-${commentId}`;
      break;
    case 3: // 专栏
      baseUrl = `/column/detail/${relatedId}`;
      break;
    case 4: // 用户
      baseUrl = `/user/${relatedId}`;
      break;
  }
  
  return baseUrl;
}
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
</style>
