-- V1.3.4__
-- 评论模块

-- 更新一些表属性

-- 1. 把user_article_foot的article_user_id重新设为可以为空
ALTER TABLE `user_article_foot`
  MODIFY `article_user_id` INT UNSIGNED NULL DEFAULT 0 COMMENT '发布该文章的用户ID';
