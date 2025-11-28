-- V1.6.5__
-- 1.6之后基本在做一些效率优化,主模块暂时开发完毕

-- 加搜索分词,简单的ADD FULLTEXT(summary)对中文会直接一整句分词,没用
ALTER TABLE article
  ADD FULLTEXT INDEX summary (summary) WITH PARSER ngram;