-- V1.3.23__
-- 评论模块
-- 加入最后关注时间

-- 加入最后关注时间
ALTER TABLE user_relation
ADD COLUMN last_follow_time DATETIME NOT NULL DEFAULT NOW()
    COMMENT '用户最后关注（业务时间）'
    AFTER follow_state