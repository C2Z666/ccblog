
export enum ChatUserSessionOperationType{
    EMPTY = 0,
    // 删除会话
    DELETE = 1,
    // 置顶会话
    TOP = 2,
    // 取消置顶会话
    CANCEL_TOP = 3,
    // 不再通知
    MUTE = 4,
    // 取消不再通知
    CANCEL_MUTE = 5,
}
