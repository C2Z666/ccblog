-- V1.6.0__
-- 1.6之后基本在做一些效率优化,主模块暂时开发完毕

-- 1. 给评论计数加冗余字段:回复数,砍掉查询是否回复需要的子查询
ALTER TABLE comment_count
    ADD COLUMN reply_cnt INT NOT NULL DEFAULT 0
    COMMENT '总回复数';