-- V1.5_2__
-- 聊天模块
-- 添加列

-- 给chat_ai_history加上token和结束标记
ALTER TABLE chat_ai_history
    ADD COLUMN token int(10) unsigned NOT NULL DEFAULT 0 COMMENT '消耗token',
    ADD COLUMN error tinyint NOT NULL DEFAULT 0 COMMENT '0:正常 1:用户中止 2:其他异常'

