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
          关注了你
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
    <el-button @click="follow" :disabled="followBtnDisabled" class="w-20" v-if="global.isLogin && global.user.userId != String(msg.operateUserId)">
      {{ userFollowed == true ? '取消关注' : '关注'}}
    </el-button>

  </div>
</template>
<script setup lang="ts">
import type { NoticeMsgType } from '@/http/ResponseTypes/NoticeType/NoticeMsgType'
import { format } from 'date-fns'
import { useGlobalStore } from '@/stores/global'
import { ref, watch } from 'vue'
import { doPost } from '@/http/BackendRequests'
import type { CommonResponse } from '@/http/ResponseTypes/CommonResponseType'
import { USER_FOLLOW_URL } from '@/http/URL'
import { useFollowIdCache, updateFollowCache } from '@/stores/useFollowIdCache'
const globalStore = useGlobalStore()
const global = globalStore.global
const props = defineProps<{
  msg: NoticeMsgType
}>()

// 关注/取消关注
const followBtnDisabled = ref(false)
const userFollowed = ref(props.msg.relatedInfo != 'false')

// 通过watch监听缓存，实时更新关注状态
watch(
  [() => props.msg.operateUserId, useFollowIdCache()],
  ([targetUid, followIdSet]) => {
    if (!global.isLogin) {
      userFollowed.value = false
      return
    }
    userFollowed.value = followIdSet.has(Number(targetUid))
  },
  { immediate: true }   // 首次渲染就执行一次
)

const follow = () => {
  followBtnDisabled.value = true
  doPost<CommonResponse>(USER_FOLLOW_URL, {
    followId:props.msg.operateUserId,
    followed: !userFollowed.value
  })
    .then((res) => {
      userFollowed.value = !userFollowed.value
      // 更新持久化缓存
      const targetUserId = Number(props.msg.operateUserId)
      updateFollowCache(targetUserId, userFollowed.value)
    })
    .catch((err) => {
      console.log(err)
    })
    .finally(() => {
      followBtnDisabled.value = false
    })
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
