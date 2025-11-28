-- V1.3.14__
-- 评论模块
-- 修改comment_interaction的列名

-- 1. 修改comment_interaction的相关字段,跟article_interaction保持一致
ALTER TABLE comment_interaction
    CHANGE COLUMN like_status     like_stat     TINYINT NOT NULL DEFAULT 0 COMMENT '点赞状态：0-未点，1-已点，2-取消点赞',
    CHANGE COLUMN dislike_status  dislike_stat  TINYINT NOT NULL DEFAULT 0 COMMENT '点踩状态：0-未点，1-已点，2-取消点踩',
    CHANGE COLUMN report_status   report_stat   TINYINT NOT NULL DEFAULT 0 COMMENT '举报状态：0-未举报，1-已举报';