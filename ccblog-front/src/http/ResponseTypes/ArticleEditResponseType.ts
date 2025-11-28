import type { ArticleType } from '@/http/ResponseTypes/ArticleType/ArticleType'
import type { ArticleCategoryType } from '@/http/ResponseTypes/CategoryType/ArticleCategoryType'
import type { ArticleTagType } from '@/http/ResponseTypes/TagType/ArticleTagType'

export interface ArticleEditResponseType{
  articleId: string;
  title: string;
  shortTitle: string;
  summary: string;
  cover: string; // 封面
  content: string;
  sourceType: string; // 来源相关
  sourceUrl: string;
  version: number;
  category: ArticleCategoryType;

  categories: ArticleCategoryType[],
  tags: ArticleTagType[]
}
