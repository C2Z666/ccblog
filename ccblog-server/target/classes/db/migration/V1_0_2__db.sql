-- V1.0.2__
-- 用户模块

-- 设置user_info的ip列可以为空
ALTER TABLE `user_info`
MODIFY COLUMN `ip` json NULL COMMENT '用户的ip信息';