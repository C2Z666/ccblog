-- V1.5_5__
-- 聊天模块
-- 修改表

-- 添加seq和last_message_time到chat_ai_session
ALTER TABLE chat_ai_session
    ADD COLUMN  seq         int(10) unsigned NOT NULL DEFAULT 1,
    ADD COLUMN last_msg_time TIMESTAMP            NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- 添加seq到用户会话表chat_user_session(可能是之前忘记加了)
ALTER TABLE chat_user_session
    ADD COLUMN seq         int(10) unsigned NOT NULL DEFAULT 1;