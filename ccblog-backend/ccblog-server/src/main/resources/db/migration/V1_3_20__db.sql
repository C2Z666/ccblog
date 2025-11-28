-- V1.3.20__
-- 评论模块
-- 修改article列定义(19的反向操作)

-- 1.文章主表
ALTER TABLE article
  MODIFY short_title  varchar(120) NOT NULL DEFAULT '' COMMENT '短标题',
  MODIFY picture      varchar(128) NOT NULL DEFAULT '' COMMENT '文章头图',
  MODIFY source_url   varchar(128) NOT NULL DEFAULT '' COMMENT '原文链接',
  MODIFY version      INT UNSIGNED NOT NULL DEFAULT 1 COMMENT '版本号',
  MODIFY deleted      tinyint      NOT NULL DEFAULT 0 COMMENT '是否删除',
  MODIFY offical_stat int unsigned NOT NULL DEFAULT 0 COMMENT '0非官方 1官方',
  MODIFY cream_stat   int unsigned NOT NULL DEFAULT 0 COMMENT '0不置顶 1置顶',
  MODIFY topping_stat int unsigned NOT NULL DEFAULT 0 COMMENT '0不加精 1加精';
