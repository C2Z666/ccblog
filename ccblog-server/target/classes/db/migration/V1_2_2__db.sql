-- -- V1.2.2__
-- 文章模块

-- 添加文章数据统计表
CREATE TABLE `article_count` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `article_id` int(10) unsigned NOT NULL COMMENT '文档ID（文章）',
  `read_cnt` int(10) unsigned NOT NULL DEFAULT 0 COMMENT '访问计数',
  `like_cnt` int(10) unsigned NOT NULL DEFAULT 0 COMMENT '点赞计数',
  `star_cnt` int(10) unsigned NOT NULL DEFAULT 0 COMMENT '收藏计数',
  `comment_cnt` int(10) unsigned NOT NULL DEFAULT 0 COMMENT '评论计数',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_article_id` (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章统计计数表';