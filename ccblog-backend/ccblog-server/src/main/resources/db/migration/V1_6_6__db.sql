-- V1.6.6__
-- 1.6之后基本在做一些效率优化,主模块暂时开发完毕

-- 加入一些关注信息
-- 1. 初始化用户计数（ignore 防止重复插入）
INSERT IGNORE INTO user_count(user_id, fan_cnt, follow_cnt) VALUES
(1,0,0),(2,0,0),(3,0,0),(4,0,0),(5,0,0);

-- 事务
START TRANSACTION;

-- 插入关注记录
INSERT INTO user_relation(user_id, follow_user_id, follow_state, last_follow_time) VALUES
(1,2,1,NOW()),   -- 1 关注 2
(1,3,1,NOW()),   -- 1 关注 3
(2,1,1,NOW()),   -- 2 关注 1
(3,1,1,NOW()),   -- 3 关注 1
(3,2,1,NOW()),   -- 3 关注 2
(3,4,1,NOW()),   -- 3 关注 4
(3,5,1,NOW()),   -- 3 关注 5
(4,5,1,NOW()),   -- 4 关注 5
(5,1,1,NOW()),   -- 5 关注 1
(2,5,1,NOW()),   -- 2 关注 5
(1,5,0,NOW()),   -- 1 曾经关注 5 又取消（follow_state=0）
(4,1,0,NOW());   -- 4 曾经关注 1 又取消（follow_state=0）

-- 根据上面数据，重新计算并更新 fan_cnt / follow_cnt
-- 只统计 follow_state=1
INSERT INTO user_count(user_id, fan_cnt, follow_cnt)
SELECT
    u.user_id,
    COALESCE(fan.cnt,0) AS fan_cnt,
    COALESCE(fol.cnt,0) AS follow_cnt
FROM
    (SELECT 1 AS user_id UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5) u
LEFT JOIN (SELECT follow_user_id AS user_id, COUNT(*) AS cnt
           FROM user_relation WHERE follow_state=1 GROUP BY follow_user_id) fan
          ON u.user_id = fan.user_id
LEFT JOIN (SELECT user_id, COUNT(*) AS cnt
           FROM user_relation WHERE follow_state=1 GROUP BY user_id) fol
          ON u.user_id = fol.user_id
ON DUPLICATE KEY UPDATE
    fan_cnt  = VALUES(fan_cnt),
    follow_cnt = VALUES(follow_cnt);

COMMIT;
