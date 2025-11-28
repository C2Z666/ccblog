-- V1.4.0__
-- 通知模块
-- 开始开发通知模块

--  ====================== 通知未读计数表 ======================
CREATE TABLE IF NOT EXISTS `notice_count` (
  `id`              bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id`         bigint unsigned NOT NULL,
  `unread_private`  int unsigned NOT NULL DEFAULT 0,
  `unread_reply`    int unsigned NOT NULL DEFAULT 0,
  `unread_comment`  int unsigned NOT NULL DEFAULT 0,
  `unread_like`     int unsigned NOT NULL DEFAULT 0,
  `unread_collect`  int unsigned NOT NULL DEFAULT 0,
  `unread_follow`   int unsigned NOT NULL DEFAULT 0,
  `create_time`     timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time`     timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '通知未读计数';

--  ====================== 通知主体表 ======================
CREATE TABLE IF NOT EXISTS `notice_content` (
  `id`              bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id`         bigint unsigned NOT NULL COMMENT '接收者',
  `operate_user_id` bigint unsigned NOT NULL COMMENT '触发者',
  `type`            tinyint(4)  NOT NULL COMMENT '1私信 2回复 3评论 4点赞 5收藏 6关注',
  `target_type`     tinyint(4)  NOT NULL COMMENT '1文章 2评论 3专栏',
  `target_id`       bigint unsigned NOT NULL COMMENT '目标主键',
  `related_info`    varchar(200) NOT NULL DEFAULT '' COMMENT '列表摘要（写200字）',
  `read_flag`       tinyint(1)  NOT NULL DEFAULT 0 COMMENT '0未读 1已读',
  `truncated`       tinyint(1)  NOT NULL DEFAULT 0 COMMENT '0未截断 1已截断',
  `status`          tinyint(1)  NOT NULL DEFAULT 1 COMMENT '0无效 1有效',
  `deleted`         tinyint(1)  NOT NULL DEFAULT 0 COMMENT '0未删除 1已删除',
  `create_time`     timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time`     timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_type` (`user_id`, `type`, `create_time`),
  KEY `idx_target`    (`target_type`, `target_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '通知主体表';

--  ====================== 私信详情表 ======================
CREATE TABLE IF NOT EXISTS `notice_private` (
  `id`            bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `content`       text            NOT NULL COMMENT '私信完整原文',
  `created_time`  timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '私信详情（正文>200字时写入）';