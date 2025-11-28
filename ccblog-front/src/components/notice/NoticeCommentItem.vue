<template>
  <div :class="['notification', { 'unread': props.msg.state === 0 }]">
    <a :href="'/user/' + msg.operateUserId" class="" target="_blank">
      <img :src="msg.operateUserPhoto" class="notification-img" loading="lazy" />
    </a>
    <div class="notification-right">
      <div class="notification-content">
        <div class="profile">
          <a :href="'/user/' + msg.operateUserId" class="" target="_blank">
            <span class="name">{{msg.operateUserName}}</span>
          </a>
          <!-- 评论通知 -->
          评论了你的文章
          <template v-if="(props.msg.extendId!=0) && props.msg.extendInfo">
            <!-- 文章 -->
            <a :href="ARTICLE_DETAIL_URL_FRONT+`/${props.msg.extendId}`" class="title-article-link">
              <span class="title-article">{{props.msg.extendInfo}}</span>
            </a>
          </template>
          <template v-else>
            <!-- 根据relatedType显示不同类型 -->
            <template v-if="props.msg.relatedType === 1">文章</template>
            <template v-else-if="props.msg.relatedType === 2">评论</template>
            <template v-else-if="props.msg.relatedType === 3">专栏</template>
            <template v-else-if="props.msg.relatedType === 4">用户</template>
            <template v-else>内容</template>
          </template>
          <!-- 发表的评论 -->
        </div>
      </div>
      <!-- 显示评论内容 -->
      <div class="notification-comment">
        {{props.msg.relatedInfo || ''}}
      </div>
      <div class="notification-bottom">
        <span class="notification-time">
          {{format(new Date(props.msg.createTime), 'yyyy-MM-dd HH:mm:ss')}}
        </span>
        <div class="notification-action">
          <!-- 显示回复按钮 -->
          <!-- <a :href="getTargetUrl(true)" class="notification-action-item">
            <img
              class="read-comment-praise"
              src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAACXBIWXMAABYlAAAWJQFJUiTwAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAKRSURBVHgB7VZNbtpQEJ55BlR15RvUOUHTG5ATJD1BQ9NU6gq8JKiKoyrJ0u2qUmkFOUGaE5TegJwg7gnqLRh7MvMwwiDbYHCUDZ9kHph5M9+bN38AO+zwzEAoiEv3xz4v/KAFiK/0S6J/qPCBoui+Y38cFlC3HgHH7Zk1FTXZUIt/mivEPVY6GFFw4difPNiWwNW3X+dLhj1iA3JqProXq7EQ8TVoz4A1147OWfPkAjYhcO1+twirt7FSEexPiG4+26cDyMEXt1s3AI7Z+LsZ4TEFB1newBzjf2B6Gi8kaqwyvEpHFgmVtjmxcTgm401R44I2G0M2KjpEV5W9KbG0koC+8znrt47d8GFDCAk+gJCQwNyvwqS1LLNwBbHbHuQ7G99bJ4rXgY4LRPGqz4T2koda8EAERl2zIrory7hArlBnDmdSDSbHyf8WrwDVNHKV0YOyQdGNXhAPMwlgnHKjaHQPJUNBOEjamL9fhI7SMt0/Q3uu08wjoJGWLk+FZQKefLyEwIKSETcxwTCbANFfWQLAIygZCFifrjkEQq73WgixCaUziHWi+p1JIC65Hj/mpdt1oCQkq2u7+f4uk4BAGo8minguFQy2hFRXvlonqTuXgK5aRLqHc/m8vXK7LdgCE6hYskolTGtqqWnYsU+dmITJrnCvv/7sOXKSDVCBiScrJgeVBHInIokDuYqEsB5KQqgMl7uk1A4DwrpCkFLLExL0x1HAWfXCr2H4X2TOWh+wEAGB7pBQcRITzgwymvmxEilcVtp+cX1cfs20Drv2VKyDSVUPI4Ij3lRPEfFJclzXEvIQlXhioZ4QYaNjn/Q3IrAMiQmDA0wB+QGEflr/ENK6xXOXFS8QRQdFx/YdnhyP1D0hcwr1KvEAAAAASUVORK5CYII=" />
            <span class="action-text">回复</span>
          </a> -->
          <!-- 显示查看按钮 -->
          <img class="read-comment-praise"
                 src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAACXBIWXMAABYlAAAWJQFJUiTwAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAKRSURBVHgB7VZNbtpQEJ55BlR15RvUOUHTG5ATJD1BQ9NU6gq8JKiKoyrJ0u2qUmkFOUGaE5TegJwg7gnqLRh7MvMwwiDbYHCUDZ9kHph5M9+bN38AO+zwzEAoiEv3xz4v/KAFiK/0S6J/qPCBoui+Y38cFlC3HgHH7Zk1FTXZUIt/mivEPVY6GFFw4difPNiWwNW3X+dLhj1iA3JqProXq7EQ8TVoz4A1147OWfPkAjYhcO1+twirt7FSEexPiG4+26cDyMEXt1s3AI7Z+LsZ4TEFB1newBzjf2B6Gi8kaqwyvEpHFgmVtjmxcTgm401R44I2G0M2KjpEV5W9KbG0koC+8znrt47d8GFDCAk+gJCQwNyvwqS1LLNwBbHbHuQ7G99bJ4rXgY4LRPGqz4T2koda8EAERl2zIrory7hArlBnDmdSDSbHyf8WrwDVNHKV0YOyQdGNXhAPMwlgnHKjaHQPJUNBOEjamL9fhI7SMt0/Q3uu08wjoJGWLk+FZQKefLyEwIKSETcxwTCbANFfWQLAIygZCFifrjkEQq73WgixCaUziHWi+p1JIC65Hj/mpdt1oCQkq2u7+f4uk4BAGo8minguFQy2hFRXvlonqTuXgK5aRLqHc/m8vXK7LdgCE6hYskolTGtqqWnYsU+dmITJrnCvv/7sOXKSDVCBiScrJgeVBHInIokDuYqEsB5KQqgMl7uk1A4DwrpCkFLLExL0x1HAWfXCr2H4X2TOWh+wEAGB7pBQcRITzgwymvmxEilcVtp+cX1cfs20Drv2VKyDSVUPI4Ij3lRPEfFJclzXEvIQlXhioZ4QYaNjn/Q3IrAMiQmDA0wB+QGEflr/ENK6xXOXFS8QRQdFx/YdnhyP1D0hcwr1KvEAAAAASUVORK5CYII="/>
          
          <a :href="getTargetUrl(true)" class="notification-action-item" v-if="props.msg.relatedType !== undefined">
            
            <span class="action-text">查看</span>
          </a>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">

import type { NoticeMsgType } from '@/http/ResponseTypes/NoticeType/NoticeMsgType'
import { ARTICLE_DETAIL_URL, ARTICLE_DETAIL_URL_FRONT } from '@/http/URL';
import { format } from 'date-fns'

const props = defineProps<{
  msg: NoticeMsgType
}>()

console.log("消息:",props.msg)


// 根据relatedType和relatedId生成目标URL
const getTargetUrl = (toComment = false) => {
  const { relatedType, relatedId, extendId } = props.msg;
  // console.log(relatedType, relatedId, extendId)
  
  // 确保有ID可用于生成链接
  if (!extendId && !relatedId) return '#';
  
  let baseUrl = '#';
  // console.log("查看",relatedType, relatedId, extendId)
  
  // 根据relatedType生成对应的URL
  switch (relatedType) {
    case 1: // 文章
      baseUrl = ARTICLE_DETAIL_URL_FRONT + `/${extendId || relatedId}`;
      break;
    case 2: // 评论 - 跳转到指定评论ID
      // 当relatedType=2时，relatedId就是commentId
      const articleId = extendId || (relatedType !== 2 ? relatedId : '');
      const commentId = relatedType === 2 ? relatedId : '';
      baseUrl = ARTICLE_DETAIL_URL_FRONT + `/${articleId}#comment-${commentId}`;
      break;
    case 3: // 专栏
      baseUrl = `/column/detail/${extendId || relatedId}`;
      break;
    case 4: // 用户
      baseUrl = `/user/${extendId || relatedId}`;
      break;
    default:
      // 默认使用extendId或relatedId生成文章链接
      if (extendId || relatedId) {
        baseUrl = ARTICLE_DETAIL_URL_FRONT + `/${extendId || relatedId}`;
        // 如果需要跳转到评论区，添加锚点
        if (toComment) {
          baseUrl += '#commentList';
        }
      }
      break;
  }
  
  return baseUrl;
}

// 导入computed
import { computed } from 'vue';
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
