-- V1.6.4__
-- 1.6之后基本在做一些效率优化,主模块暂时开发完毕

-- 修改计数为带符号(适应增量入库改造)
-- 1. 用户综合计数表
ALTER TABLE user_count
  MODIFY fan_cnt      int      NOT NULL DEFAULT 0 COMMENT '粉丝数',
  MODIFY follow_cnt   int      NOT NULL DEFAULT 0 COMMENT '关注数',
  MODIFY read_cnt     int      NOT NULL DEFAULT 0 COMMENT '被阅读总数',
  MODIFY comment_cnt  int      NOT NULL DEFAULT 0 COMMENT '被评论总数',
  MODIFY like_cnt     int      NOT NULL DEFAULT 0 COMMENT '被点赞总数',
  MODIFY collect_cnt  int      NOT NULL DEFAULT 0 COMMENT '被收藏总数';

-- 2. 文章统计计数表
ALTER TABLE article_count
  MODIFY read_cnt     int      NOT NULL DEFAULT 0,
  MODIFY like_cnt     int      NOT NULL DEFAULT 0,
  MODIFY collect_cnt  int      NOT NULL DEFAULT 0 COMMENT '收藏计数',
  MODIFY comment_cnt  int      NOT NULL DEFAULT 0,
  MODIFY report_cnt   int      NOT NULL DEFAULT 0 COMMENT '举报数';

-- 3. 评论计数表
ALTER TABLE comment_count
  MODIFY like_cnt     int      NOT NULL DEFAULT 0,
  MODIFY dislike_cnt  int      NOT NULL DEFAULT 0,
  MODIFY report_cnt   int      NOT NULL DEFAULT 0,
  MODIFY reply_cnt    int      NOT NULL DEFAULT 0 COMMENT '总回复数';

-- 4. 通知未读计数表
ALTER TABLE notice_count
  MODIFY unread_system   int      NOT NULL DEFAULT 0,
  MODIFY unread_reply    int      NOT NULL DEFAULT 0,
  MODIFY unread_comment  int      NOT NULL DEFAULT 0,
  MODIFY unread_like     int      NOT NULL DEFAULT 0,
  MODIFY unread_collect  int      NOT NULL DEFAULT 0,
  MODIFY unread_follow   int      NOT NULL DEFAULT 0;