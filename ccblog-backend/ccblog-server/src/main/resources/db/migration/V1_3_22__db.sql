-- V1.3.22__
-- 评论模块
-- 考虑独立出计数表

-- 1.文章主表删除计数冗余,单独列表
ALTER TABLE `user`
    DROP COLUMN `fan_cnt`,
    DROP COLUMN `follow_cnt`;

-- 2.增加用户计数表
CREATE TABLE `user_count` (
    `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`       BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    `fan_cnt`       INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '粉丝数',
    `follow_cnt`    INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '关注数',
    `read_cnt`      INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '被阅读总数',
    `comment_cnt`   INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '被评论总数',
    `like_cnt`      INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '被点赞总数',
    `collect_cnt`   INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '被收藏总数',
    `update_time`   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                              ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户综合计数表';