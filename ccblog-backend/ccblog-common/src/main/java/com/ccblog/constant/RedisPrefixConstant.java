package com.ccblog.constant;

/**
 * 存储在redis的相关字段前缀
 * 这里存储的是需要对外开放读写的,在redis模块内部调用的不在这里
 * @author czc
 * @date 2025-09-30
 */
public class RedisPrefixConstant {

    public static final String GLOBAL_PREFIX = "cc_"; // 本项目所有存储在redis里面的公共数据前缀

    /**
     * 用户相关 用户相关  USER_*
     */
    public static final String USER_SESSION = "session:%s"; // String,key就是用户session
    public static final String USER_TOTAL_COUNT = "user:count:count:%s"; // hash 用户粉丝/关注/被阅读/被评论/被收藏/被点赞数(都是一张表,都放一起)
    public static final String USER_FOLLOW_TIME = "user:relation:follow_time:%s"; // zset 用户关注列表,按照时间排序
    public static final String USER_FAN_TIME = "user:relation:fan_time:%s"; // zset 用户粉丝列表,按照时间排序,最大数量固定
    public static final String USER_COMMON_INFO = "user:info:%s"; // hash 用户常用信息
    public static final String USER_VERIFY_CODE = "user:verify:%s"; // string,用户验证码 {email:code}



    /**
     * 文章相关  ARTICLE_*
     */
    public static final String ARTICLE_TOTAL_COUNT = "article:count:count:%s"; // hash 文章计数,包含若干文章计数信息,包括READ/LIKE/COLLECT/COMMENT
    public static final String ARTICLE_LAST_READ_TIME = "article:interaction:last_read:%s:%s";
                                        // string 最后阅读时间信息,单列因为高频(不进行滑动窗过滤),其中%s:%s=>{userId}:{articleId}
    public static final String ARTICLE_INTERACTION = "article:interaction:status:%s:%s";
                                        // hash 交互信息,用户交互信息包括点赞/收藏等状态,内含多个hash,每个hash的key为READ/LIKE/...
    public static final String ARTICLE_TAG_CATEGORY = "article:tag_category:%s"; // {articleId} hash 查文章标签和分类

    /**
     * 评论相关 COMMENT_*
     */
    public static final String COMMENT_CURSOR = "comment:content:%s:%s:%s:%s"; // 评论内容,后面跟articleId:parentId:cursor:limit
    public static final String COMMENT_TOTAL_COUNT = "comment:count:%s"; // hash 评论计数信息,包含若干评论计数信息,包括LIKE/DISLIKE
    public static final String COMMENT_INTERACTION = "comment:interaction:%s:%s"; // hash 评论交互信息,%s:%s=>{userId}:{commentId}

    /**
     * 通知相关 NOTICE_*
     */
    public static final String NOTICE_TOTAL_COUNT = "notice:count:%s"; // hash 通知计数信息,包含若干计数信息,包括...

    /**
     * 聊天相关 CHAT_*
     */
    public static final String CHAT_USER_SESSION = "chat:user:session:%s:%s"; // {userId}:{peerId},hash,存储用户会话相关信息
    public static final String CHAT_USER_LATEST_MSG = "chat:user:message:%s:%s:%s"; // {userId}:{peerId}:{limit},obj,存储最新用户一页消息
    public static final String CHAT_USER_TOTAL_UNREAD = "chat:user:unread:%s"; // {sessionId:limit},String 存储用户总未读数
    public static final String CHAT_AI_SESSION = "chat:ai:session:%s"; // {sessionId},hash,存储seq和lastUpdateTime等字段
    public static final String CHAT_AI_LATEST_MSG = "chat:ai:message:%s:%s"; // {sessionId:limit},obj,存储最新一页用户AI聊天消息

}