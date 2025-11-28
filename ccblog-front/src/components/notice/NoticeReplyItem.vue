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
            :href="'/user/' + msg.operateUserId"
            class=""
            target="_blank"
          >
            <span class="name">{{msg.operateUserName}}</span>
          </a>
          回复了你在
          
          <!-- 优先使用extendId和extendInfo -->
          <template v-if="msg.extendId!=0 && msg.extendInfo">
            文章
            <a :href="getArticleUrlByExtend()">
              <span class="title-article">{{msg.extendInfo}}</span>
            </a>
            发表的评论
          </template>
          <template v-else>
            <!-- 根据relatedType显示不同类型 -->
            <template v-if="msg.relatedType === 1">
              文章
              <a :href="getTargetUrl()" v-if="msg.relatedId">
                <span class="title-article">{{msg.relatedInfo}}</span>
              </a>
            </template>
            <template v-else-if="msg.relatedType === 2">
              评论
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
          </template>
          
          <span v-if="msg.relatedType === 1 || msg.relatedType === 3">的评论</span>
        </div>
      </div>
      
      <!-- 使用relatedInfo作为评论内容 -->
      <div class="notification-comment">{{msg.relatedInfo}}</div>

      <div class="notification-bottom">
        <span
          class="notification-time"
        >
          {{format(new Date(msg.createTime), 'yyyy-MM-dd HH:mm:ss')}}
        </span>
        <div class="notification-action">
          <!-- <a :href="getTargetUrl()" class="notification-action-item">
            <img class="read-comment-praise"
                 src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAACXBIWXMAABYlAAAWJQFJUiTwAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAJ9SURBVHgB7VZNbtNQEP7GP7AkN8DcoJyA5gRNTwCR2kqsUm9YEKEaoZRl0hUSBLWcAHOCpjdIT1AfIewgTjzM+AccxwHXLRYS/STnvbyxZ743b34e8L+DcEMMhu+fERk7DLTAuAwRjjz3eVD1+xsROD75eARmr7AczDlsVyVhoCbeDt85mXHmqLtkbst0Ko9jk31aVU9tAgvYW7FxYNJ3D85eufuTOZtKYiZu3X4z/LBdRU9tAia4E0+YL7I1z+3OmPEpkWOrip7aBED0RAcb7K8KeBb/alD+LQIa+TI48kxfuAfTVSlVMlybgAafpN1RYgujopwIOzpGEhuogGuloTc8bd2j5TmS8/VfHu7t5uUaeCaRygORPaqi00JFJAVn2UuNa667xXfEeJZ+rePR+Kool7gIQg67+RpBJTvo5QMoZHPXxFLW8Tld2lhoUqMOfg/JlKjdT2Pnpwf0bDlx3worG1EnPlENF0m5OayO5+7NyjRLHXgMfCsNQhumrBviRfRAhnqyu0KAYXmpcX/BfKKekL+dvBIheOUddkuNK7QO6A43ycXDvolYr5OtGTnlD3VU41rVZLdfccvIipMYDdYINAHZZJyicgR+4wQ0hbVH6Px7RBeNE9BM0lGbVxorTRNImhcxf8mvNxcDafOywBM0TUCqqEa/I09QbF6NECDQdjKuN6hGCJSlX2MENqXfGgFKS6hlmA9wi7hvLJ/qWEy/DL96AfMlEXWkU/mD0XiCkjud7kRk56iOltwRYz3ShM7KXlhpx4PheBR3qxzS6zbSi0YtyOZe9919D38ioNC2vIDl6NyQC2bWtzWVomvf93gWwg7KXH+HfwY/AGsn+Lf3Dim6AAAAAElFTkSuQmCC"/>
            <span class="action-text">点赞</span>
          </a> -->
          
          <!-- 加上锚点直接跳转到评论区 -->
          <a :href="getTargetUrl(true)" class="notification-action-item">
            <img class="read-comment-praise"
                 src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAACXBIWXMAABYlAAAWJQFJUiTwAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAKRSURBVHgB7VZNbtpQEJ55BlR15RvUOUHTG5ATJD1BQ9NU6gq8JKiKoyrJ0u2qUmkFOUGaE5TegJwg7gnqLRh7MvMwwiDbYHCUDZ9kHph5M9+bN38AO+zwzEAoiEv3xz4v/KAFiK/0S6J/qPCBoui+Y38cFlC3HgHH7Zk1FTXZUIt/mivEPVY6GFFw4difPNiWwNW3X+dLhj1iA3JqProXq7EQ8TVoz4A1147OWfPkAjYhcO1+twirt7FSEexPiG4+26cDyMEXt1s3AI7Z+LsZ4TEFB1newBzjf2B6Gi8kaqwyvEpHFgmVtjmxcTgm401R44I2G0M2KjpEV5W9KbG0koC+8znrt47d8GFDCAk+gJCQwNyvwqS1LLNwBbHbHuQ7G99bJ4rXgY4LRPGqz4T2koda8EAERl2zIrory7hArlBnDmdSDSbHyf8WrwDVNHKV0YOyQdGNXhAPMwlgnHKjaHQPJUNBOEjamL9fhI7SMt0/Q3uu08wjoJGWLk+FZQKefLyEwIKSETcxwTCbANFfWQLAIygZCFifrjkEQq73WgixCaUziHWi+p1JIC65Hj/mpdt1oCQkq2u7+f4uk4BAGo8minguFQy2hFRXvlonqTuXgK5aRLqHc/m8vXK7LdgCE6hYskolTGtqqWnYsU+dmITJrnCvv/7sOXKSDVCBiScrJgeVBHInIokDuYqEsB5KQqgMl7uk1A4DwrpCkFLLExL0x1HAWfXCr2H4X2TOWh+wEAGB7pBQcRITzgwymvmxEilcVtp+cX1cfs20Drv2VKyDSVUPI4Ij3lRPEfFJclzXEvIQlXhioZ4QYaNjn/Q3IrAMiQmDA0wB+QGEflr/ENK6xXOXFS8QRQdFx/YdnhyP1D0hcwr1KvEAAAAASUVORK5CYII="/>
            <span class="action-text">查看</span>
          </a>
        </div>
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

// 使用extendId生成文章URL
const getArticleUrlByExtend = (toComment = false) => {
  const { extendId } = props.msg;
  if (!extendId && typeof extendId !== 'number') return '#';
  
  // 使用extendId生成符合要求的文章链接格式
  const baseUrl = `http://localhost:5173/article/detail/${extendId}`;
  
  // 如果需要跳转到评论区，添加锚点
  const { relatedType, relatedId } = props.msg;
  const commentId = relatedType === 2 ? relatedId : '';
  return toComment ? `${baseUrl}#comment-${commentId}` : baseUrl;
}

// 根据relatedType和relatedId生成目标URL
const getTargetUrl = (toComment = false) => {
  const { relatedType, relatedId, extendId } = props.msg;
  
  // 优先使用extendId生成文章链接
  if (extendId) {
    return getArticleUrlByExtend(toComment);
  }
  
  // 确保relatedId存在且为数字类型
  if (!relatedId && typeof relatedId !== 'number') return '#';
  
  let baseUrl = '#';
  
  // 根据relatedType生成对应的URL
  switch (relatedType) {
    case 1: // 文章
      baseUrl = `http://localhost:5173/article/detail/${relatedId}`;
      break;
    case 2: // 评论
      const commentId = relatedType === 2 ? relatedId : '';
      baseUrl = `http://localhost:5173/article/detail/${relatedId}#comment-${commentId}`;
      break;
    case 3: // 专栏
      baseUrl = `http://localhost:5173/column/detail/${relatedId}`;
      break;
  }
  
  // 如果是文章且需要跳转到评论区，添加锚点
  // if (relatedType === 1 && toComment && baseUrl !== '#') {
  //   baseUrl += '#commentList';
  // }
  
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
