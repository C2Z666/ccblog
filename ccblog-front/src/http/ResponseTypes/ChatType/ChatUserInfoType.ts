// 通用用户信息DTO
export interface CommonInfoDTO {
  /**
   * 用户id
   */
  userId: number;

  /**
   * 用户名
   */
  userName: string;

  /**
   * 用户图像
   */
  photo: string;
}

// 聊天用户信息VO
export interface ChatUserInfoVO {
  result: any;
  /**
   * 自己的信息
   */
  selfInfo: CommonInfoDTO;

  /**
   * 聊天对象信息
   */
  peerInfo: CommonInfoDTO;
}