-- V1.3.0__
-- 评论模块

-- 先删除历史遗留
DROP TABLE IF EXISTS user_foot;

-- 1. 用户对文章互动表（阅读、收藏、点赞、评分等）
CREATE TABLE `user_article_foot` (
  `id`              int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id`         int unsigned NOT NULL DEFAULT 0 COMMENT '用户ID',
  `article_id`      int unsigned NOT NULL DEFAULT 0 COMMENT '文章ID',
  `article_user_id` int unsigned NOT NULL DEFAULT 0 COMMENT '发布该文章的用户ID',

  `collection_stat` tinyint(3)   NOT NULL DEFAULT 0 COMMENT '收藏状态：0-未收藏，1-已收藏，2-取消收藏',
  `read_stat`       tinyint(3)   NOT NULL DEFAULT 0 COMMENT '阅读状态：0-未读，1-已读',
  `comment_stat`    tinyint(3)   NOT NULL DEFAULT 0 COMMENT '评论状态：0-未评论，1-已评论，2-删除评论',
  `like_status`     tinyint(3)   NOT NULL DEFAULT 0 COMMENT '点赞状态：0-未点赞，1-已点赞，2-取消点赞',
  `score`           tinyint      DEFAULT NULL COMMENT '用户打分：1-5',
  `read_duration`   int unsigned NOT NULL DEFAULT 0 COMMENT '阅读时长（秒）',
  `first_read_time` datetime     DEFAULT NULL COMMENT '首次阅读时间',
  `last_read_time`  datetime     DEFAULT NULL COMMENT '末次阅读时间',

  `deleted`         tinyint      NOT NULL DEFAULT 0 COMMENT '0-正常，1-已删',
  `create_time`     timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`     timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',

  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_article` (`user_id`,`article_id`),
  KEY `idx_article_user` (`article_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户对文章互动足迹表';

-- =====================================================
-- 2. 用户对评论互动表（点赞、点踩、举报等）
-- =====================================================
CREATE TABLE `user_comment_foot` (
  `id`              int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id`         int unsigned NOT NULL DEFAULT 0 COMMENT '用户ID',
  `comment_id`      int unsigned NOT NULL DEFAULT 0 COMMENT '评论ID',
  `comment_user_id` int unsigned NOT NULL DEFAULT 0 COMMENT '发布该评论的用户ID',

  `like_status`     tinyint(3)   NOT NULL DEFAULT 0 COMMENT '点赞状态：0-未点赞，1-已点赞，2-取消点赞',
  `dislike_status`  tinyint(3)   NOT NULL DEFAULT 0 COMMENT '点踩状态：0-无，1-已踩，2-取消',
  `report_cnt`      tinyint      NOT NULL DEFAULT 0 COMMENT '累计举报次数',

  `deleted`         tinyint      NOT NULL DEFAULT 0 COMMENT '0-正常，1-已删',
  `create_time`     timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`     timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',

  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_comment` (`user_id`,`comment_id`),
  KEY `idx_comment_user` (`comment_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户对评论互动足迹表';