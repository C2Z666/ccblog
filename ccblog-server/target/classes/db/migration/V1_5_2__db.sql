-- V1.5_2__
-- 聊天模块
-- 重构通知模块的私信为系统通知

-- 1. 重命名
ALTER TABLE notice_count
    RENAME COLUMN unread_private TO unread_system;