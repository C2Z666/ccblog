-- V1.3.5__
-- 评论模块
-- 更新一些表属性

-- 1.改user_comment_foot为comment_foot,归到comment模块下
-- 2.改user_article_foot为article_foot,归到article模块下
RENAME TABLE user_article_foot TO article_interaction,
             user_comment_foot TO comment_interaction;