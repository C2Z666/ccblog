-- V1.3.19__
-- 评论模块
-- 修改article列定义,方便插入代码的编写

-- 1.文章主表
ALTER TABLE article
  MODIFY short_title  varchar(120) NULL,
  MODIFY picture      varchar(128) NULL,
  MODIFY source_url   varchar(128) NULL,
  MODIFY version      int unsigned NULL,
  MODIFY deleted      tinyint      NULL,
  MODIFY offical_stat int unsigned NULL,
  MODIFY cream_stat   int unsigned NULL,
  MODIFY topping_stat int unsigned NULL;