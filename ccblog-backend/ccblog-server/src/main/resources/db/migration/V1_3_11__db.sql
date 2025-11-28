-- V1.3.11__
-- 评论模块
-- 改回count表的id为普通自增

-- 1. 添加一下关于article_count的补充,前面是直接插入的,少了代码层面插入计数信息初始初始化(这些文章都是前面插入的)
INSERT INTO article_count(article_id) VALUES(1);
INSERT INTO article_count(article_id) VALUES(2);
INSERT INTO article_count(article_id) VALUES(3);
INSERT INTO article_count(article_id) VALUES(4);
INSERT INTO article_count(article_id) VALUES(5);
INSERT INTO article_count(article_id) VALUES(6);
INSERT INTO article_count(article_id) VALUES(7);
INSERT INTO article_count(article_id) VALUES(8);
INSERT INTO article_count(article_id) VALUES(9);
INSERT INTO article_count(article_id) VALUES(10);
