// ChatAiRequestDTO 类型定义
export interface ChatAiRequestDTO {
  /**
   * 模型名称
   */
  aiType: string;

  /**
   * 内容
   */
  content: string;

  /**
   * 创建时间
   */
  createTime: string;
}

// ChatAiResponseDTO 类型定义
export interface ChatAiResponseDTO {
  choices: {
    delta?: {
      content?: string;
    };
    message?: {
      content?: string;
    };
  }[];
}

// SSE 事件数据类型
export interface SSEEventData {
  event: string;
  id: string;
  data: any;
}