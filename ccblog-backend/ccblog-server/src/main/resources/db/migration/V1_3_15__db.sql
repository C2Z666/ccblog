-- V1.3.15__
-- 评论模块
-- 修改article_count结构

-- 1. 给article_count加一列report_cnt
ALTER TABLE article_count
    ADD COLUMN report_cnt INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '举报数';