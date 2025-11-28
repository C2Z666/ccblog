package com.ccblog.event;

import com.ccblog.entity.comment.CommentCount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 发送给rabbitmq更新文章计数的信息
 * @author czc
 * @date 2025/10/11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentCountUpdEvent {
    private String version;

    private List<String> commentIdList; // 需要更新的commentId

}