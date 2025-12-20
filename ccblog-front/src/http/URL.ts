// ============= 后端请求的地址 =============
// 前端接口地址,要跟后端匹配
// export const BASE_URL = "http://172.24.54.219:8005"
// export const WS_URL = "ws://172.24.54.219:8005"

export const BASE_URL = "http://localhost:8005"
export const WS_URL = "ws://localhost:8005"

export const API_PREFIX = '/api';
export const WS_PREFIX = '/ws';  


// 获得global信息
export const INDEX_URL = "/index"

// ============ 用户模块 ============
// 用户名密码登录
export const LOGIN_USER_NAME_URL = `${API_PREFIX}/login/username`
// 用户注册
export const REGISTER_USER_NAME_URL = `${API_PREFIX}/register/username`
// 退出登录
export const LOGOUT_URL = `${API_PREFIX}/logout`
// 发送验证邮件
export const SEND_VERIFY_EMAIL_URL = `${API_PREFIX}/sendVerifyEmail`
// 获取用户信息
export const USER_INFO_URL = `${API_PREFIX}/user/userHome`
// 获取热门作者
export const USER_HOT_USER_URL = `${API_PREFIX}/user/hotUser`
// 分页获取用户关注的用户的列表
export const USER_FOLLOW_LIST_URL = `${API_PREFIX}/user/follows`
// 分页获取用户粉丝列表
export const USER_FANS_LIST_URL = `${API_PREFIX}/user/fans`
// 获取所有用户关注用户id
export const USER_FOLLOW_ID_LIST_URL = `${API_PREFIX}/user/followIds`   
// 保存用户信息
export const USER_INFO_SAVE_URL = `${API_PREFIX}/user/saveUserInfo`
// 关注/取关用户
export const USER_FOLLOW_URL = `${API_PREFIX}/user/saveUserRelation`

// ============ 文章模块 ============
// 获取指定category下的文章列表
export const CATEGORY_ARTICLE_LIST_URL = `${API_PREFIX}/category/articles`
// 获取指定tag下的文章列表
export const TAG_ARTICLE_LIST_URL = `${API_PREFIX}/tag/articles`
// 获取热门标签
export const TAG_HOT_URL = `${API_PREFIX}/tag/hot`
// 获取指定文章的详情
export const ARTICLE_DETAIL_URL = `${API_PREFIX}/article/detail`
// 获取指定文章的更多详情
export const ARTICLE_DETAIL_MORE_URL = `${API_PREFIX}/article/detail/more`
// 根据查询条件获取文章列表
export const ARTICLE_LIST_URL = `${API_PREFIX}/article/query`
// 获取最新文章草稿
export const ARTICLE_LATEST_DRAFT_URL = `${API_PREFIX}/article/draft/latest`
// 文章点赞、收藏、举报
export const ARTICLE_LIKE_COLLECT_URL = `${API_PREFIX}/article/articleOperation`    
// 获取文章分类列表
export const CATEGORY_LIST_URL = `${API_PREFIX}/category/list`
// 获取文章标签列表(未被标记为删除的)
export const TAG_LIST_URL = `${API_PREFIX}/tag/list`    
// 保存文章
export const ARTICLE_UPLOAD_URL = `${API_PREFIX}/article/save`
// 更新（编辑）文章时获取的文章详情
export const ARTICLE_EDIT_URL = `${API_PREFIX}/article/edit`
// 删除文章
export const ARTICLE_DELETE_URL = `${API_PREFIX}/article/delete`
// 获取用户文章列表
export const USER_ARTICLE_LIST_URL = `${API_PREFIX}/article/userId`
// 获取用户浏览记录列表
export const USER_HISTORY_LIST_URL = `${API_PREFIX}/article/history`
// 获取用户收藏列表
export const USER_STAR_LIST_URL = `${API_PREFIX}/article/star`
// 获取搜索文章列表
export const SEARCH_ARTICLE_LIST_URL = `${API_PREFIX}/article/query`

// ============ 评论模块 ============
// 这三个实际是一样的...
// 评论操作
export const COMMENT_LIKE_URL = `${API_PREFIX}/comment/commentOperation`
// 评论点踩
export const COMMENT_DISLIKE_URL = `${API_PREFIX}/comment/commentOperation`
// 删除评论
export const COMMENT_DELETE_URL = `${API_PREFIX}/comment/commentOperation`
// 提交评论
export const COMMENT_SUBMIT_URL = `${API_PREFIX}/comment/save`
// 游标形式获取评论
export const COMMENT_CURSOR_URL = `${API_PREFIX}/comment/getCommentCursor`

// ============ 通知模块 ============
// 分页获取通知(已读+未读)
export const UNREAD_NOTICE_URL = `${API_PREFIX}/notice/messages`
// 一键清除通知
export const UNREAD_NOTICE_CLEAR_URL = `${API_PREFIX}/notice/clearUnread`

// ============ 聊天模块 ============
// 获取用户聊天会话列表
export const USER_CHAT_SESSION_URL = `${API_PREFIX}/chat/user/session`
// 会话操作
export const USER_CHAT_SESSION_OPERATE_URL = `${API_PREFIX}/chat/user/session/operate`
// 心跳延续
export const USER_CHAT_PING_URL = `${API_PREFIX}/chat/user/ping`
// 获取ticket
export const USER_CHAT_TICKET_URL = `${API_PREFIX}/chat/user/ticket`
// 获取用户聊天记录
export const USER_CHAT_HISTORY_URL = `${API_PREFIX}/chat/user/history`
// 获取用户聊天信息
export const USER_CHAT_INFO_URL = `${API_PREFIX}/chat/user/chatInfo`
// 获取AI聊天记录
export const AI_CHAT_HISTORY_URL = `${API_PREFIX}/chat/ai/history`
// 清空用户已读消息
export const USER_CHAT_UNREAD_CLEAR_URL = `${API_PREFIX}/chat/user/unread/clear`
// 获取AI会话
export const AI_CHAT_SESSION_URL = `${API_PREFIX}/chat/ai/sessions`
// 获取AI回答
export const AI_CHAT_ANSWER_URL = `${API_PREFIX}/chat/ai/answer`
// 删除AI会话
export const AI_CHAT_SESSION_DELETE_URL = `${API_PREFIX}/chat/ai/session/delete`
// 置顶AI会话
export const AI_CHAT_SESSION_TOP_URL = `${API_PREFIX}/chat/ai/session/top`
// 重命名AI会话
export const AI_CHAT_SESSION_RENAME_URL = `${API_PREFIX}/chat/ai/session/rename`
// 删除AI消息
export const AI_CHAT_MESSAGE_DELETE_URL = `${API_PREFIX}/chat/ai/message/delete`
// 撤回消息
export const USER_CHAT_RECALL_URL = `${API_PREFIX}/chat/user/recall`
// 删除消息
export const USER_CHAT_DELETE_URL = `${API_PREFIX}/chat/user/delete`

// ============ 通用模块 ============
// 上传图片
export const FILE_UPLOAD_URL = `${API_PREFIX}/common/upload`
// 获取举报列表
export const REPORT_TYPE_URL = `${API_PREFIX}/common/report/list`
// 举报
export const REPORT_URL = `${API_PREFIX}/common/report/save`
// 获取全局信息
export const GLOBAL_INFO_URL = `${API_PREFIX}/global/info`

// ============= 前端跳转的地址 =============
export const WRITE_ARTICLE_URL = '/article/edit'
export const ARTICLE_DETAIL_URL_FRONT = '/article/detail'

