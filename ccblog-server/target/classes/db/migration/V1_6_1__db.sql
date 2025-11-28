-- V1.6.1__
-- 1.6之后基本在做一些效率优化,主模块暂时开发完毕

-- 文章表加索引:用户id,创建时间等
ALTER TABLE article
  ADD INDEX idx_user_ct      (user_id, create_time),      -- 用户时间线
  ADD INDEX idx_top_ct       (topping_stat, create_time),     -- 置顶+时间排序
  ADD INDEX idx_create_time  (create_time);               -- 纯时间范围

-- 文章互动表:按照最近阅读排序,用于查询历史文章排序
ALTER TABLE article_interaction
  ADD INDEX idx_last_read_time (last_read_time);           -- 最近阅读查询