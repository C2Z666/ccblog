package com.ccblog.constant;

/**
 * 存储和RabbitMQ相关的常量
 * @author czc
 * @date 2025/10/9
 */
public class RabbitMQConstant {
    // 交换机
    public static final String USER_EVENT_EX = "user.event.exchange"; // 用户交换机
    public static final String ARTICLE_EVENT_EX = "article.event.exchange"; // 文章交换机
    public static final String COMMENT_EVENT_EX = "comment.event.exchange"; // 评论交换机
    public static final String NOTICE_EVENT_EX = "notice.event.exchange"; // 通知交换机
    public static final String CHAT_EVENT_EX = "chat.event.exchange"; // 聊天交换机
    public static final String DEAD_LETTER_EX = "dead.letter.exchange"; // 死信交换机

    // 消费者队列
    public static final String USER_COUNT_Q = "user.count.queue"; // 负责接收用户计数相关的
    public static final String USER_FOLLOW_Q = "user.follow.queue"; // 负责接收用户关注相关的
    public static final String ARTICLE_COUNT_Q = "article.count.queue"; // 负责接收文章计数相关的
    public static final String ARTICLE_INTERACTION_Q = "article.interaction.queue"; // 负责接收文章交互相关的
    public static final String COMMENT_COUNT_Q = "comment.count.queue"; // 负责接收评论计数相关的
    public static final String COMMENT_INTERACTION_Q = "comment.interaction.queue"; // 负责接收评论交互相关的
    public static final String NOTICE_COUNT_Q = "notice.count.queue"; // 负责接收通知计数相关的
    public static final String NOTICE_CONTENT_Q = "notice.content.queue"; // 负责接收通知内容相关的
    public static final String CHAT_USER_Q = "chat.user.content.queue"; // 负责接收用户聊天内容相关
    public static final String CHAT_USER_SESSION_Q = "chat.user.session.queue"; // 负责接收用户聊天内容相关
    public static final String CHAT_AI_Q = "chat.ai.content.queue"; // 负责接收AI聊天相关


    // 死信队列
    public static final String USER_DLQ = "user.dlq.queue";    // 用户相关的死信队列
    public static final String ARTICLE_DLQ = "article.dlq.queue"; // 文章相关的死信队列
    public static final String COMMENT_DLQ = "comment.dlq.queue"; // 评论相关的死信队列
    public static final String NOTICE_DLQ = "notice.dlq.queue"; // 通知相关的死信队列
    public static final String CHAT_DLQ = "chat.dlq.queue"; // 聊天相关的死信队列

    // route key
    public static final String USER_COUNT_KEY = "user_count_update";
    public static final String USER_FOLLOW_KEY = "user_follow_update";
    public static final String ARTICLE_COUNT_KEY = "article_count_update";
    public static final String ARTICLE_INTERACTION_KEY = "article_interaction_update";
    public static final String COMMENT_COUNT_KEY = "comment_count_update";
    public static final String COMMENT_INTERACTION_KEY = "comment_interaction_update";
    public static final String NOTICE_COUNT_KEY = "notice_count_update";
    public static final String NOTICE_CONTENT_KEY = "notice_content_update";
    public static final String CHAT_USER_KEY = "chat_user_update";
    public static final String CHAT_USER_SESSION_KEY = "chat_use_session_update";
    public static final String CHAT_AI_KEY = "chat_ai_update";
}