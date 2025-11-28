<template>
  <el-card>
    <template #default>
      <div class="flex text-sm justify-between">
        <a :href="`/user/${user.userId}/articlesTab`" class="flex">
          <el-avatar size="large" class="cursor-pointer">
            <img :src="user.avatar">
          </el-avatar>
          <span class="center-content ml-2 text-lg cursor-pointer text-blue-500 hover:underline">{{user.userName}}</span>
        </a>
        <!-- 只有当前登录用户点进自己主页才能关注/取消关注 -->
        <!-- <span class="center-content" v-if="global.user.id == route.params['userId']"> -->
        <!-- 也可以查看别人的,反正只要是登录用户都可以,但是关注和取关是相对于登录用户自己的 -->
          <span class="center-content" v-if="global.isLogin && props.user.userId != Number(global.user.userId)">
          <el-button @click="follow" :disabled="btnDisabled" round>{{userFollowed? '取消关注': '关注'}}</el-button>
        </span>
      </div>
    </template>
  </el-card>
</template>

<script setup lang="ts">

import type { FollowUserInfoType } from '@/http/ResponseTypes/UserInfoType/FollowUserInfoType'
import { doPost } from '@/http/BackendRequests'
import type { CommonResponse } from '@/http/ResponseTypes/CommonResponseType'
import { USER_FOLLOW_URL } from '@/http/URL'
import { useGlobalStore } from '@/stores/global'
import { useRoute } from 'vue-router'
import { ref, watch } from 'vue'
import { useFollowIdCache, updateFollowCache } from '@/stores/useFollowIdCache'
const globalStore = useGlobalStore()
const global = globalStore.global

const props = defineProps<{
  user: FollowUserInfoType
}>()

const route = useRoute()
// const userFollowed = ref(props.user.followed)
const userFollowed = ref(false)
// 计算或 watch 都可以，这里用 watch 实时同步
watch(
  [() => props.user.userId, useFollowIdCache()],
  ([targetUid, followIdSet]) => { // 这几个分别对应上面几个列表的值 
    if (!global.isLogin) {
      userFollowed.value = false        // 没登录一律当未关注(一般没登录无法到这里)
      return
    }
    // followSet 就是全局缓存的 Set<number>
    userFollowed.value = followIdSet.has(Number(targetUid))
    // console.log("判断一次!!!缓存列表:",followIdSet," 当前对象用户:",props.user.userId," 当前登录用户:",global.user.userId)
  },
  { immediate: true }   // 首次渲染就执行一次
)

// 关注/取消关注
const btnDisabled = ref(false)

const follow = () => {
  btnDisabled.value = true
  doPost<CommonResponse>(USER_FOLLOW_URL, {
    followId: props.user.userId,
    followed: !userFollowed.value
  })
    .then((res) => {
      userFollowed.value = !userFollowed.value
      // 更新持久化缓存
      const targetUserId = Number(props.user.userId)
      updateFollowCache(targetUserId, userFollowed.value)
    })
    .catch((err) => {
      console.log(err)
    })
    .finally(() => {
      btnDisabled.value = false
  })
}


</script>

<style scoped>

</style>
