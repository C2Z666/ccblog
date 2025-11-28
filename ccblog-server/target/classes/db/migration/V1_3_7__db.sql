-- V1.3.7__
-- 评论模块
-- 更新一些表属性

-- 1.删除评论交互的评论发布者用户id,可以由comment_id知己诶对应到comment表的user_id
ALTER TABLE `comment_interaction`
DROP COLUMN `comment_user_id`;