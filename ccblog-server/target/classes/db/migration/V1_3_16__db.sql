-- V1.3.16__
-- 评论模块
-- 修改article_detail结构

-- 1. 给article_detail的deleted列删除
ALTER TABLE article_detail DROP COLUMN deleted;