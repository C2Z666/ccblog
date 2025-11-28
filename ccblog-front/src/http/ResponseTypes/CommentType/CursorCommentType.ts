export interface CommentItemDTO {
  commentId:    number | string   // 后端 Long→前端 number 或 string 均可
  userId:       number | string
  userName:     string
  userPhoto:    string
  commentTime:  string            // 后端 Date→序列化为 ISO 字符串
  commentContent: string
  likeCnt:    number
  dislikeCnt: number
  reportCnt:  number
  like:         boolean
  dislike:      boolean
  report:       boolean
  parentCommentId: number | string
  topCommentId: number | string
  replyCnt: number
}

export interface CommentCursorVO {
  hasMore: boolean
  comments: CommentItemDTO[]
}