-- V1.3.6__
-- 评论模块
-- 更新一些表属性

-- 1.删除评论的父评论内容,后续更新困难
ALTER TABLE `comment`
DROP COLUMN `parent_content`;