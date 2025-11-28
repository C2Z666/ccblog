<template>
  <HeaderBar></HeaderBar>

  <!-- 正文内容 -->
  <div class="user-div">
    <UserHomeInfo :vo="userInfo.userHome"></UserHomeInfo>
    <div class="user-wrap">
      <div class="user-content">
        <!-- 主要内容 -->
        <div class="user-body">
          <!-- 选择标签列表 -->
          <UserHomeNavBar></UserHomeNavBar>

        </div>
        <!-- 右侧内容 -->
        <div class="user-left hidden-when-screen-small">
          <UserAchievement :user="userInfo.userHome"></UserAchievement>
          <UserHistory :user="userInfo.userHome"></UserHistory>
        </div>
      </div>
    </div>
  </div>
  <Footer></Footer>
  
  <LoginDialog :clicked="loginDialogClicked"></LoginDialog>
  <RegisterDialog :clicked="registerDialogClicked"></RegisterDialog>

</template>

<script setup lang="ts">

import HeaderBar from '@/components/layout/HeaderBar.vue'
import Footer from '@/components/layout/Footer.vue'
import { onMounted, ref, provide } from 'vue'
import { doGet } from '@/http/BackendRequests'
import type { CommonResponse } from '@/http/ResponseTypes/CommonResponseType'
import { USER_INFO_URL } from '@/http/URL'
import { useRoute } from 'vue-router'
import { useGlobalStore } from '@/stores/global'
import { defaultUserHomeInfo, type UserHomeInfoResponseType } from '@/http/ResponseTypes/UserHomeInfoResponseType'
import UserHomeInfo from '@/views/user/UserHomeInfo.vue'
import UserHomeNavBar from '@/views/user/UserHomeNavBar.vue'
import UserAchievement from '@/views/user/UserAchievement.vue'
import UserHistory from '@/views/user/UserHistory.vue'
import LoginDialog from '@/components/dialog/LoginDialog.vue'
import RegisterDialog from '@/components/dialog/RegisterDialog.vue'

const globalStore = useGlobalStore()
const global = globalStore.global

// 登录框
const loginDialogClicked = ref(false)
const changeClicked = () => {
  loginDialogClicked.value = !loginDialogClicked.value
  console.log("clicked: ", loginDialogClicked.value)
}
provide('loginDialogClicked', changeClicked)

// 注册框
const registerDialogClicked = ref(false)
const changeRegisterClicked = () => {
  registerDialogClicked.value = !registerDialogClicked.value
  console.log("clicked: ", registerDialogClicked.value)
}
provide('registerDialogClicked', changeRegisterClicked)

const route = useRoute()

const userInfo = ref<UserHomeInfoResponseType>({...defaultUserHomeInfo})
onMounted(() => {
  doGet<CommonResponse>(USER_INFO_URL, {
    userId: route.params.userId
  })
    .then((res) => {
      globalStore.setGlobal(res.data.global)

      Object.assign(userInfo.value.userHome, res.data.result)
      // console.log(userInfo)
    })
    .catch((err) => {
      console.log(err)
    })
})


</script>



<style scoped>
/* 整体布局 */
.user-div {
  overflow: auto;
  min-height: calc(100vh - var(--footer-height) - var(--header-height));
  background-color: #f7f8f9;
}
</style>
