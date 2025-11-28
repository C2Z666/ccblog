// 聊天用户消息项DTO
export interface ChatUserItemDTO {
  /**
   * 消息id
   */
  // messageId: number;


  /**
   * 序列号
   */
  seq: number;

  /**
   * 发送者id
   */
  recvUserId: number;

  /**
   * 接收者id
   */
  sendUserId: number;

  /**
   * 消息类型
   */
  type: number;

  /**
   * 内容
   */
  content: string;

  /**
   * 已读状态
   */
  readFlag: boolean;

  /**
   * 撤回状态
   */
  status: number;

  /**
   * 时间
   */
  createTime: string; // LocalDateTime在前端通常使用字符串表示
}

// 聊天用户游标VO
export interface ChatUserCursorVO {
  result: any;
  /**
   * 是否还有更多(消息记录是否有更多展开)
   */
  hasMore: boolean;

  /**
   * 聊天用户消息项列表
   */
  chatUserItems: ChatUserItemDTO[];
}