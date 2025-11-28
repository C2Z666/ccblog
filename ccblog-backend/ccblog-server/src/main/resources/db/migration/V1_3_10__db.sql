-- V1.3.10__
-- 评论模块
-- 改回count表的id为普通自增

-- 1. 删除重建
DROP TABLE IF EXISTS`comment_count`;
DROP TABLE IF EXISTS `article_count`;
CREATE TABLE comment_count (
  id          int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  comment_id  int UNSIGNED NOT NULL COMMENT '评论ID',
  like_cnt    int UNSIGNED NOT NULL DEFAULT 0,
  dislike_cnt int UNSIGNED NOT NULL DEFAULT 0,
  report_cnt  int UNSIGNED NOT NULL DEFAULT 0,
  update_time timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_comment_id (comment_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论计数表（自增）';

CREATE TABLE article_count (
  id          int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  article_id  int UNSIGNED NOT NULL COMMENT '文章ID',
  read_cnt    int UNSIGNED NOT NULL DEFAULT 0,
  like_cnt    int UNSIGNED NOT NULL DEFAULT 0,
  star_cnt    int UNSIGNED NOT NULL DEFAULT 0,
  comment_cnt int UNSIGNED NOT NULL DEFAULT 0,
  update_time timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_article_id (article_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章统计计数表（自增）';