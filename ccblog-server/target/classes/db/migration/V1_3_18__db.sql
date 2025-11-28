-- V1.3.17__
-- 评论模块
-- 修改article_tag和article结构,改为version对齐(原来是deleted标记)

-- 1.文章主表
-- article
ALTER TABLE article
  ADD COLUMN version INT UNSIGNED NOT NULL DEFAULT 1 COMMENT '版本号';

-- 2.文章-标签关系表
-- article_tag
ALTER TABLE article_tag
  ADD COLUMN version INT UNSIGNED NOT NULL DEFAULT 1 COMMENT '版本号';

-- 3.文章详情表(已经有version)
-- 修改article_detail的默认值为1,并且把当前已有的全部设置为1
ALTER TABLE article_detail
    ALTER COLUMN version SET DEFAULT 1;
UPDATE article_detail SET version = 1 WHERE 1= 1;