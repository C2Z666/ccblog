/**
 * AI聊天记录项DTO
 * 对应后端ChatAiItemDTO类
 */
export interface ChatAiItemDTO {
  /**
   * 序列号
   */
  seq: number;

  /**
   * 发送者  user/...(model_name)
   */
  sender: number;

  /**
   * 内容
   */
  content: string;

  /**
   * 时间
   */
  createTime: string;
}

/**
 * AI聊天记录游标VO
 * 对应后端ChatAiCursorVO类
 */
export interface ChatAiCursorVO {
  /**
   * 是否还有更多(消息记录是否有更多展开)
   */
  hasMore: boolean;

  /**
   * AI聊天记录列表
   */
  chatAiItems: ChatAiItemDTO[];
}

/**
 * AI聊天历史响应类型
 */
export interface ChatAiHistoryResponse {
  status: number;
  /**
   * 响应码
   */
  code: number;

  /**
   * 响应消息
   */
  message: string;

  result: ChatAiCursorVO;
}