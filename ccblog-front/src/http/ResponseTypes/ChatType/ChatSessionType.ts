/**
 * 聊天会话快照类型定义
 * 对应后端的ChatUserSession类
 */
export interface ChatSessionItemType {
  sessionId: any;
  /** 主键ID */
  id: number;
  
  /** 当前用户ID */
  userId: number;
  
  /** 对方用户ID */
  peerId: number;

      /**
     * 对方用户头像
     */
    peerPhoto:String ;
    
    /**
     * 对方用户名
     */
    peerName:String;
  
  /** 最新消息快照（前50字） */
  snapshot: string;
  
  /** 最后一条消息发送时间 */
  lastMsgTime: string; // 后端使用LocalDateTime，前端使用字符串格式
  
  /** 未读消息数量 */
  unreadCount: number;
  
  /** 是否置顶：0不置顶 1置顶 */
  isTop: number;
  
  /** 是否关闭通知：0接收 1不接收 */
  isMute: number;
  
  /** 是否删除会话：0正常 1已删除 */
  isDelete: number;
  
  /** 其他可能的基础字段（继承自BaseInfo） */
  createTime?: string;
  updateTime?: string;
}

/**
 * 聊天会话列表响应类型
 */
export interface ChatSessionResponse {
  result: any;

  /**
   * 是否还有更多(消息记录是否有更多展开)
   */
  hasMore: boolean;

    /**
   * 会话信息列表
   */
  chatUserItems: ChatSessionItemType[];
}