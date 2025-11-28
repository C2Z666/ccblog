-- V1.3.3__
-- 评论模块

-- 更新一些表属性
-- 设置article_user_id可以为空方便插入(更新的时候比如更新点赞是不需要这个条件的)
ALTER TABLE `user_article_foot`
  MODIFY `article_user_id` INT UNSIGNED NULL DEFAULT 0 COMMENT '发布该文章的用户ID';

-- 2. 给 article_count 新增举报计数列
ALTER TABLE `article_count`
  ADD COLUMN `report_cnt` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '举报计数'
  AFTER `comment_cnt`;
