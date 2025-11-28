-- V1.5_1__
-- 聊天模块

-- 1. 新增列
ALTER TABLE chat_user_session
ADD COLUMN display_seq INT UNSIGNED NOT NULL DEFAULT 0
    COMMENT '快照对应消息seq'