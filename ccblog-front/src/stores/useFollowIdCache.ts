// src/stores/userFollowIdCache.ts
import { ref } from 'vue'
import { doGet } from '@/http/BackendRequests'          // 你自己的 axios 封装
import { USER_FOLLOW_ID_LIST_URL } from '@/http/URL'
import { USER_FOLLOW_ID_CACHE } from '@/constants/LocalStorageConstants'
import type { CommonResponse } from '@/http/ResponseTypes/CommonResponseType'

// 从localStorage读取持久化数据或创建新Set
const loadFromLocalStorage = (): Set<number> => {
  try {
    const stored = localStorage.getItem(USER_FOLLOW_ID_CACHE)
    if (stored) {
      const ids = JSON.parse(stored)
      return new Set(ids.map((id: any) => Number(id)))
    }
  } catch (e) {
    console.error('读取本地缓存失败', e)
  }
  return new Set()
}

// 缓存本体 - 带持久化
const followIdSet = ref<Set<number>>(loadFromLocalStorage())
let timer: number | null = null

// 读缓存（外部组件用）
export function useFollowIdCache() {
  return followIdSet
}

// 添加更新缓存的方法，供组件使用
export function updateFollowCache(userId: number, followed: boolean) {
  if (followed) {
    followIdSet.value.add(userId)
  } else {
    followIdSet.value.delete(userId)
  }
  // 更新持久化存储
  try {
    localStorage.setItem(
      USER_FOLLOW_ID_CACHE,
      JSON.stringify(Array.from(followIdSet.value))
    )
  } catch (e) {
    console.error('更新本地缓存失败', e)
  }
}

// 清除定时器的方法
export function clearFollowCacheTimer() {
  if (timer) {
    clearTimeout(timer)
    timer = null
  }
}

// 立即加载 + 5 分钟定时刷新
export function startCacheRefresh(userId: number | null) {
  if (!userId) return;          // 没登录就不拉

  // 先清除可能存在的旧定时器
  clearFollowCacheTimer()

  const load = async () => {
    try {
      const response = await doGet<CommonResponse<number[]>>(USER_FOLLOW_ID_LIST_URL, { userId });
      // 确保result存在且为数组，否则使用空数组
      const resultArray = response.data.result && Array.isArray(response.data.result) ? response.data.result : [];
      const numericIds = resultArray.map((id: any) => Number(id));
      followIdSet.value = new Set(numericIds); // 转为number存入
      // 持久化到localStorage
      try {
        localStorage.setItem(
    USER_FOLLOW_ID_CACHE,
    JSON.stringify(Array.from(followIdSet.value))
  )
      } catch (e) {
        console.error('保存本地缓存失败', e)
      }
      console.log("获得用户关注列表");
      console.log(numericIds);
    } catch (e) {
      console.error('拉取关注列表失败', e);
    }
  };

  const schedule = () => {
    if (timer) clearTimeout(timer);
    timer = window.setTimeout(() => load().finally(schedule), 5*60*1000); // 5min
  };

  load()   // 首次立即加载
  schedule()
}