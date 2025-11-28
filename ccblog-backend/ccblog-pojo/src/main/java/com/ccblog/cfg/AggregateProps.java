package com.ccblog.cfg;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 聚合配置类
 * @author czc
 */
@Data
@ConfigurationProperties(prefix = "aggregate") // 对应根节点
@Component
public class AggregateProps {

    /** 对应 YAML 中的 none（全局默认） */
    private Item none = new Item();

    /** 各业务节点 */
    private Item user   = new Item();
    private Item article= new Item();
    private Item comment= new Item();
    private Item notice = new Item();
    private Item chat   = new Item();

    /**
     * 默认配置
     */
    @Data
    public static class Item {
        private int maxCount = 10;
        private boolean redisLog = false;
        private boolean mqLog    = false;
    }

    /** 访问接口 */
    public Item of(String module) {
        return switch (module) {
            case "user" -> user;
            case "article" -> article;
            case "comment" -> comment;
            case "notice" -> notice;
            case "chat" -> chat;
            default -> none;
        };
    }
}