-- V1.3.13__
-- 评论模块
-- 修改数据库信息

-- 1. 修改article_interaction的列名为collect_stat(改回来了)
ALTER TABLE article_interaction CHANGE star_stat collect_stat tinyint(3) NOT NULL DEFAULT 0 COMMENT '收藏状态：0-未收藏，1-已收藏，2-取消收藏';;