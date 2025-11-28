/**
 * AI聊天会话项接口，对应Java的ChatAiSessionItemDTO类
 */
export interface ChatAiSessionItemDTO {
  /** 会话id */
  sessionId: number;
  
  /** 会话名 */
  title: string;
    
  /** 最后信息的时间 */
  lastMsgTime: string;
}

/**
 * AI聊天会话游标接口，对应Java的ChatAiSessionCursorVO类
 */
export interface ChatAiSessionCursorVO {
  /** 是否还有更多(聊天会话是否有更多展开) */
  hasMore: boolean;
  
  /** 聊天会话列表 */
  chatAiSessionItems: ChatAiSessionItemDTO[];
}

/**
 * AI聊天历史响应类型
 */
export interface ChatAiSessionResponse {
  /**
   * 响应码
   */
  code: number;

  /**
   * 响应消息
   */
  message: string;

  result: ChatAiSessionCursorVO;
}