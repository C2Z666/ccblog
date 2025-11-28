-- V1.3.17__
-- 评论模块
-- 修改article_tag结构

-- 1. 给article_tag的deleted列删除
ALTER TABLE article_tag DROP COLUMN deleted;