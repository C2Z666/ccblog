-- V1.3.0__
-- 评论模块
-- 修改user_comment_foot结构,加入comment表

-- 1. 重建 user_comment_foot（结构更新）
DROP TABLE IF EXISTS `user_comment_foot`;

CREATE TABLE `user_comment_foot` (
  `id`                int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id`           int unsigned NOT NULL DEFAULT 0 COMMENT '用户ID',
  `comment_id`        int unsigned NOT NULL DEFAULT 0 COMMENT '评论ID',
  `comment_user_id`   int unsigned NOT NULL DEFAULT 0 COMMENT '发布该评论的用户ID',

  `like_status`       tinyint(3) NOT NULL DEFAULT 0 COMMENT '点赞状态：0-未点赞，1-已点赞，2-取消点赞',
  `dislike_status`    tinyint(3) NOT NULL DEFAULT 0 COMMENT '点踩状态：0-无，1-已踩，2-取消',
  `report_status`     tinyint(4) NOT NULL DEFAULT 0 COMMENT '举报状态：0-未举报，1-已举报',

  `create_time`       timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`       timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',

  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_comment` (`user_id`,`comment_id`),
  KEY `idx_comment_user` (`comment_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户对评论互动足迹表';

-- 2. 创建评论表 comment
DROP TABLE IF EXISTS `comment`;

CREATE TABLE `comment` (
  `id`                  int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `article_id`          int unsigned NOT NULL DEFAULT 0 COMMENT '文章ID',
  `user_id`             int unsigned NOT NULL DEFAULT 0 COMMENT '用户ID',
  `content`             varchar(300) NOT NULL DEFAULT '' COMMENT '评论内容',
  `top_comment_id`      int          NOT NULL DEFAULT 0 COMMENT '顶级评论ID',
  `parent_comment_id`   int unsigned NOT NULL DEFAULT 0 COMMENT '父评论ID',
  `like_cnt`            int unsigned NOT NULL DEFAULT 0 COMMENT '点赞数',
  `parent_content`      varchar(300) NOT NULL DEFAULT '' COMMENT '父评论内容（冗余，避免回表）',
  `report_cnt`          int unsigned NOT NULL DEFAULT 0 COMMENT '举报数',
  `deleted`             tinyint(3)   NOT NULL DEFAULT 0 COMMENT '0-正常，1-已删',
  `create_time`         timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`         timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',

  PRIMARY KEY (`id`),
  KEY `idx_article_id` (`article_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';