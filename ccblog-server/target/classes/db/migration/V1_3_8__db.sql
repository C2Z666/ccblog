-- V1.3.8__
-- 评论模块
-- 拆comment表,把高并发读的计数部分拆出来,并且把article_count的主键变为bigint(雪花主键,适合分布式高并发)

-- 1.分出comment里面的举报和点赞数量
ALTER TABLE `comment`
  DROP COLUMN `like_cnt`,
  DROP COLUMN `report_cnt`;

CREATE TABLE `comment_count` (
  `id`          bigint unsigned NOT NULL COMMENT '雪花ID',
  `comment_id`  int  unsigned NOT NULL COMMENT '评论ID',
  `like_cnt`    int  unsigned NOT NULL DEFAULT 0 COMMENT '点赞数',
  `dislike_cnt` int  unsigned NOT NULL DEFAULT 0 COMMENT '点踩数',
  `report_cnt`  int  unsigned NOT NULL DEFAULT 0 COMMENT '举报数',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_comment_id` (`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论计数表';

-- 2.修改article_count的主键类型
DROP TABLE IF EXISTS `article_count`;
CREATE TABLE `article_count` (
  `id`          bigint unsigned NOT NULL COMMENT '雪花ID',
  `article_id`  int(10) unsigned NOT NULL COMMENT '文档ID（文章）',
  `read_cnt`    int(10) unsigned NOT NULL DEFAULT 0 COMMENT '访问计数',
  `like_cnt`    int(10) unsigned NOT NULL DEFAULT 0 COMMENT '点赞计数',
  `star_cnt`    int(10) unsigned NOT NULL DEFAULT 0 COMMENT '收藏计数',
  `comment_cnt` int(10) unsigned NOT NULL DEFAULT 0 COMMENT '评论计数',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_article_id` (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章统计计数表';
