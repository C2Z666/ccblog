package com.ccblog.event;

import com.ccblog.dto.article.ArticleInteractUpdDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 发送给rabbitmq更新文章交互的信息
 * @author czc
 * @date 2025/10/10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleInteractUpdEvent {
    private String version;
    private long  shardId;

    private Map<String, Set<String>> idMap; // userId:Se<articleId>

}