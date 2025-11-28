-- V1.5_4__
-- 聊天模块
-- 修改表

-- 修改session_id为id
ALTER TABLE chat_ai_session
    RENAME COLUMN session_id TO id;

-- 改为自增
ALTER TABLE chat_ai_session
    MODIFY id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT;

-- 去掉chat_ai_history的paren_id
ALTER TABLE chat_ai_history
    DROP COLUMN parent_id;

