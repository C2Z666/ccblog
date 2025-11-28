-- V1.3.21__
-- 评论模块
-- 修改user表增加fan_cnt和follow_cnt冗余

-- 1.文章主表增加冗余
ALTER TABLE `user`
    ADD COLUMN fan_cnt INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '粉丝数',
    ADD COLUMN follow_cnt INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '关注数';