-- V1.3.0__
-- 评论模块

-- 更新一些表的属性名

-- 1. 重建 user_comment_foot（结构更新）
-- 重命名 collection_stat -> star_stat
ALTER TABLE user_article_foot
  CHANGE collection_stat star_stat tinyint(3) NOT NULL DEFAULT 0 COMMENT '收藏状态：0-未收藏，1-已收藏，2-取消收藏';

-- 重命名 like_status -> like_stat
ALTER TABLE user_article_foot
  CHANGE like_status like_stat tinyint(3) NOT NULL DEFAULT 0 COMMENT '点赞状态：0-未点赞，1-已点赞，2-取消点赞';