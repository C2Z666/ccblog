-- V1.7.0__
-- 添加用户实名、日志追踪等功能,方便追踪

-- 把third_account_id改为邮箱
ALTER TABLE user
    CHANGE COLUMN third_account_id email VARCHAR(80) NOT NULL COMMENT '邮箱';