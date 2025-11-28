package com.ccblog.constant;

/**
 * 消息常量
 * @author czc
 * @date 2025/10/21
 */
public class NoticeMsgConstant {
    public static final Long ADMIN_USER_ID = 9L; // 管理员id

    /** 评论相关 */
    public static final String COMMENT_DELETED_BY_USER = "用户已删除该评论";
    public static final String COMMENT_DELETED_BY_ADMIN = "该评论因违规已被管理员删除";

    /** 关注相关 */
    public static final String FOLLOWEE_PUBLISH_POST = "您最近关注的用户 %s 发表了新文章"; // 用户名

    /** 点赞相关 */
    public static final String POST_LIKED = "有人点赞了您的文章";
    public static final String COMMENT_LIKED = "有人点赞了您的评论";

    private NoticeMsgConstant() {}
}