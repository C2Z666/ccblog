-- V1.0.0__
-- 用户模块

-- 创建user相关的四张数据表
CREATE TABLE `user` (
  `id`               bigint unsigned NOT NULL AUTO_INCREMENT,
  `third_account_id` varchar(64)     NOT NULL DEFAULT '',
  `user_name`         varchar(32)     NOT NULL DEFAULT '',
  `password`         varchar(128)    NOT NULL DEFAULT '',
  `login_type`       int             NOT NULL DEFAULT 0 COMMENT '手机号',
  `deleted`          int             NOT NULL DEFAULT 0 COMMENT '1删除 0正常',
  `user_role` int NOT NULL DEFAULT 0,
  `create_time`      datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time`      datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_name` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户基础信息表';

-- 用户个人信息
CREATE TABLE IF NOT EXISTS `user_info` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int unsigned NOT NULL DEFAULT 0,
  `user_name` varchar(50) NOT NULL DEFAULT '',
  `photo` varchar(128) NOT NULL DEFAULT '',
  `position` varchar(50) NOT NULL DEFAULT '',
  `company` varchar(50) NOT NULL DEFAULT '',
  `profile` varchar(225) NOT NULL DEFAULT '',
  `extend` varchar(1024) NOT NULL DEFAULT '',
  `ip` json NOT NULL,
  `deleted` tinyint NOT NULL DEFAULT 0,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `key_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户个人信息表';

-- 用户关系
CREATE TABLE IF NOT EXISTS `user_relation` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int unsigned NOT NULL DEFAULT 0,
  `follow_user_id` int unsigned NOT NULL,
  `follow_state` tinyint unsigned NOT NULL DEFAULT 0,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_follow` (`user_id`,`follow_user_id`),
  KEY `key_follow_user_id` (`follow_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户关系表';

-- 用户足迹
CREATE TABLE IF NOT EXISTS `user_foot` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int unsigned NOT NULL DEFAULT 0,
  `document_id` int unsigned NOT NULL DEFAULT 0,
  `document_type` tinyint NOT NULL DEFAULT 1,
  `document_user_id` int unsigned NOT NULL DEFAULT 0,
  `collection_stat` tinyint unsigned NOT NULL DEFAULT 0,
  `read_stat` tinyint unsigned NOT NULL DEFAULT 0,
  `comment_stat` tinyint unsigned NOT NULL DEFAULT 0,
  `praise_stat` tinyint unsigned NOT NULL DEFAULT 0,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_document` (`user_id`,`document_id`,`document_type`),
  KEY `idx_document_id` (`document_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户足迹表';