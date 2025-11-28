-- V1.6.3__
-- 1.6之后基本在做一些效率优化,主模块暂时开发完毕

-- 加长图片链接存储
ALTER TABLE `user_info`
MODIFY COLUMN `photo` varchar(512) NOT NULL DEFAULT '' COMMENT '头像/照片 URL';