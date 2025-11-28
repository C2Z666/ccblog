-- V1.1.0__
-- 文章模块

-- 插入文章有关的表

-- 1. 文章表
CREATE TABLE `article` (
  `id`              int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id`         int unsigned NOT NULL DEFAULT 0 COMMENT '作者用户ID',
  `article_type`    tinyint      NOT NULL DEFAULT 1 COMMENT '1博文 2问答',
  `title`           varchar(120) NOT NULL DEFAULT '' COMMENT '文章标题',
  `short_title`     varchar(120) NOT NULL DEFAULT '' COMMENT '短标题',
  `picture`         varchar(128) NOT NULL DEFAULT '' COMMENT '文章头图',
  `summary`         varchar(300) NOT NULL DEFAULT '' COMMENT '文章摘要',
  `category_id`     int unsigned NOT NULL DEFAULT 0 COMMENT '类目ID',
  `source`          tinyint      NOT NULL DEFAULT 1 COMMENT '1转载 2原创 3翻译',
  `source_url`      varchar(128) NOT NULL DEFAULT '' COMMENT '原文链接',
  `offical_stat`    int unsigned NOT NULL DEFAULT 0 COMMENT '0非官方 1官方',
  `topping_stat`    int unsigned NOT NULL DEFAULT 0 COMMENT '0不置顶 1置顶',
  `cream_stat`      int unsigned NOT NULL DEFAULT 0 COMMENT '0不加精 1加精',
  `status`          tinyint      NOT NULL DEFAULT 0 COMMENT '0未发布 1已发布',
  `deleted`         tinyint      NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_time`     timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`     timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_title` (`title`),
  KEY `idx_short_title` (`short_title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章表';

-- 2. 文章详情表
CREATE TABLE `article_detail` (
  `id`          int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `article_id`  int unsigned NOT NULL DEFAULT 0 COMMENT '文章ID',
  `version`     int unsigned NOT NULL DEFAULT 0 COMMENT '版本号',
  `content`     longtext     COMMENT '文章内容',
  `deleted`     tinyint      NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_article_version` (`article_id`,`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章详情表';

-- 3. 类目表
CREATE TABLE `category` (
  `id`            int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `category_name` varchar(64)  NOT NULL DEFAULT '' COMMENT '类目名称',
  `status`        tinyint      NOT NULL DEFAULT 0 COMMENT '0未发布 1已发布',
  `rank`          tinyint      NOT NULL DEFAULT 0 COMMENT '排序',
  `deleted`       tinyint      NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_time`   timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`   timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='类目管理表';

-- 4. 标签表
CREATE TABLE `tag` (
  `id`           int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tag_name`     varchar(120) NOT NULL COMMENT '标签名称',
  `tag_type`     tinyint      NOT NULL DEFAULT 1 COMMENT '1系统标签 2自定义标签',
  `category_id`  int unsigned NOT NULL DEFAULT 0 COMMENT '所属类目ID',
  `status`       tinyint      NOT NULL DEFAULT 0 COMMENT '0未发布 1已发布',
  `deleted`      tinyint      NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_time`  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签管理表';

-- 5. 文章-标签关联表
CREATE TABLE `article_tag` (
  `id`          int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `article_id`  int unsigned NOT NULL DEFAULT 0 COMMENT '文章ID',
  `tag_id`      int          NOT NULL DEFAULT 0 COMMENT '标签ID',
  `deleted`     tinyint      NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章标签映射表';