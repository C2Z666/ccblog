-- V1.6.2__
-- 1.6之后基本在做一些效率优化,主模块暂时开发完毕

-- 文章标签表加索引:保证连接文章表可以很快得到
ALTER TABLE article_tag
ADD INDEX idx_aid_ver_covered (article_id, version, tag_id);