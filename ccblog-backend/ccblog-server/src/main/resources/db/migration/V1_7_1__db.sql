-- V1.7.1__
-- 添加用户实名、日志追踪等功能,方便追踪
-- 更新用户字段,修改唯一索引为邮箱,之后采用邮箱+密码登录

-- 1. 去掉 user_name 唯一索引
ALTER TABLE `user`
    DROP INDEX `uk_user_name`;

-- 2.更新所有当前还没有邮箱的账号,邮箱为user_name@test.com
UPDATE `user`
    SET `email` = CONCAT(`user_name`, '@test.com')
    WHERE `email` = ''
      AND `user_name` <> '';   -- 防止 user_name 也是空串

-- 3. 给 email 加唯一索引
ALTER TABLE `user`
    ADD UNIQUE KEY `uk_email` (`email`);

-- 4. 修正 login_type 字段注释
ALTER TABLE `user`
    MODIFY COLUMN `login_type` int NOT NULL DEFAULT '0' COMMENT '登录类型';
