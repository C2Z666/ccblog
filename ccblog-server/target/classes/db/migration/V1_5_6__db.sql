-- V1.5_6__
-- 聊天模块
-- 修改命名

-- 1. 重命名
ALTER TABLE chat_ai_history
    RENAME COLUMN error TO finish_reason;