-- V1.3.9__
-- 评论模块
-- 拆comment表,把高并发读的计数部分拆出来,并且把article_count的主键变为bigint(雪花主键,适合分布式高并发)

-- 1.去掉计数表里面的create_time,因为每次新增一条文章或者评论都会插入一条
ALTER TABLE `comment_count`
  DROP COLUMN `create_time`;

ALTER TABLE `article_count`
  DROP COLUMN `create_time`;