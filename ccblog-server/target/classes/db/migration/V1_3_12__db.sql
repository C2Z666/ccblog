-- V1.3.12__
-- 评论模块
-- 修改数据库信息

-- 1. 修改article_count的列名为collect_cnt
ALTER TABLE article_count CHANGE star_cnt collect_cnt int(10) unsigned NOT NULL DEFAULT 0 COMMENT '收藏计数';