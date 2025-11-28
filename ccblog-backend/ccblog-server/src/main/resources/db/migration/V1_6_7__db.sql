-- V1.6.7__
-- 1.6之后基本在做一些效率优化,主模块暂时开发完毕

SET FOREIGN_KEY_CHECKS = 0;
SET SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO';

-- 1. 生成 10 000 篇 article
INSERT INTO article (id,
    user_id, article_type, title, short_title, picture, summary,
    category_id, source, source_url, offical_stat, topping_stat, cream_stat,
    status, deleted, create_time, update_time, version
) SELECT
    100 + seq - 1, -- 文章id
    1 + (seq % 5),                       -- 作者 1-5
    1,                                   -- 1 博文
    CONCAT('占位标题-', seq),              -- title
    CONCAT('短标题-', seq),                -- short_title
    '',                                  -- 无头图
    '这是一段用于占位的摘要文字，内容重复，无实际意义。',
    1 + (seq % 10),                      -- category 1-10
    2, '', 0, 0, 0,                      -- 原创、无原文、非官方、不加精、不置顶
    1, 0,                                -- 已发布、未删除
    NOW() - INTERVAL seq HOUR,           -- 创建时间错开
    NOW() - INTERVAL seq HOUR,           -- 更新时间
    1                                    -- 版本号 1
FROM (
    SELECT @i:=@i+1 AS seq
    FROM (SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4) t1,
         (SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) t2,
         (SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) t3,
         (SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) t4,
         (SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) t5,
         (SELECT @i:=0) t6
    LIMIT 10000
) nums;

-- 2. 生成对应 article_detail
INSERT INTO article_detail (article_id, version, content, create_time, update_time)
SELECT
    a.id,
    1,
    '这是一段用于占位的文章内容，重复出现，无实际意义。可以在这里放长文本，但本次仅用于填充数据。',
    a.create_time,
    a.update_time
FROM (
    SELECT id, create_time, update_time
    FROM article
    WHERE id>99
    ORDER BY id DESC
    LIMIT 10000
) a;

-- 3. 初始化 article_count
INSERT INTO article_count (article_id)
SELECT
    a.id
FROM (
    SELECT id
    FROM article
    WHERE id>99
    ORDER BY id DESC
    LIMIT 10000
) a;

-- 4. 随机打标签（每篇 1-3 个，tag_id 1-97 随机）
INSERT INTO article_tag (article_id, tag_id)
SELECT a.id, FLOOR(1 + RAND() * 97)
FROM (
    SELECT id FROM article WHERE id>99 ORDER BY id DESC LIMIT 10000
) a
CROSS JOIN (SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3) t
ORDER BY RAND()
LIMIT 20000;   -- 约 2 条/篇

SET FOREIGN_KEY_CHECKS = 1;