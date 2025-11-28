import type { BasicPageType } from '@/http/ResponseTypes/PageType/BasicPageType'
import type { SideBarItem } from '@/http/ResponseTypes/SideBarItemType'

export interface ColumnVoType {
  columnId: string
  column: string
  introduction: string
  cover: string
  publishTime: string
  section: number
  state: number
  nums: number
  type: number
  freeStartTime: string
  freeEndTime: string
  author: string
  authorName: string
  authorAvatar: string
  authorProfile: string
  count: {
    likeCount: number
    readCount: number
    collectCount: number
    commentCount: number
    articleCount: number
    totalNums: number
  }
}

export interface ColumnListVoTypeResponse {
  columns?: any
  columnPage: BasicPageType<ColumnVoType>
  sideBarItems: SideBarItem[]
}

export const  defaultColumnVoResponse: ColumnListVoTypeResponse = {
  columns: [],
  columnPage: {
    records: [],
    total: 0,
    size: 0,
    current: 0,
    orders: [],
    optimizeCountSql: false,
    searchCount: false,
    maxLimit: 0,
    countId: '',
    pages: 0
  },
  sideBarItems: []
}
