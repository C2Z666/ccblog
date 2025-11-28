// ========== 文章类型 =============
import type { UserBaseInfo } from '@/http/ResponseTypes/UserInfoType/UserStatisticInfoType'
import type { ArticleOtherType } from '@/http/ResponseTypes/ArticleType/ArticleOtherType'
import type { SideBarItem } from '@/http/ResponseTypes/SideBarItemType'
import type { ArticleCommentType } from '@/http/ResponseTypes/CommentType/ArticleCommentType'
import type { ArticleType } from '@/http/ResponseTypes/ArticleType/ArticleType'
import type { CommentCursorVO, CommentItemDTO } from './CommentType/CursorCommentType'


// 将主要对象封装在一个更大的接口中
export interface ArticleDetailResponse {
  article: ArticleType;
  comments: ArticleCommentType[]; // 类型未指定
  hotComment: ArticleCommentType; // 类型未指定
  commentCursorVO?: CommentCursorVO; // 游标类型评论需要的
  commentParents: number[]; // 评论的父评论id列表
  topCommentInfo: CommentItemDTO | null; // 置顶评论信息，可为null
  author: UserBaseInfo; // 类型未指定
  other: ArticleOtherType; // 类型未指定
  // sideBarItems: SideBarItem[]; // 类型未指定

  /**
   * 所属专栏id
   */
  columnId: number;

  /**
   * 所属专栏中的文章顺序
   */
  sectionId: number;
}

export const defaultArticleDetailResponse: ArticleDetailResponse = {
  author: {
    followCount: 0,
    fansCount: 0,
    joinDayCount: 0,
    articleCount: 0,
    likeCount: 0,
    readCount: 0,
    collectCount: 0,
    followed: false,
    infoPercent: 0,
    yearArticleList:[],
  },
  other: {
    readType: 0,
  },
  // sideBarItems: [],
  article: {
    articleId: '',
    articleType: 0,
    author: '',
    authorName: '',
    authorAvatar: '',
    title: '',
    shortTitle: '',
    summary: '',
    cover: '',
    content: '',
    sourceType: '',
    sourceUrl: '',
    status: 0,
    officalStat: 0,
    toppingStat: 0,
    creamStat: 0,
    createTime: '',
    lastUpdateTime: '',
    category: {
      categoryId: 0,
      category: '',
      rank: 0,
      status: 0,
    },
    tags: [],
    liked: false,
    commented: false,
    collected: false,
    count: {
      likeCnt: 0,
      readCnt: 0,
      collectCnt: 0,
      commentCnt: 0,
    },
    praisedUsers: [],
    categoryId: 0,
    version: 0
  },
  hotComment: {
    commentCount: 0,
    commentContent: '',
    commentId: '',
    commentTime: '',
    likeCount: 0,
    praised: false,
    userId: '',
    userName: '',
    userPhoto: '',
    childComments: [],
  },
  comments: [],
  columnId: 0,
  sectionId: 0,
  commentParents: [],
  topCommentInfo: null,
}

