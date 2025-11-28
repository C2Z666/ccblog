package com.ccblog.event;

import com.ccblog.entity.comment.CommentInteraction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 发送给rabbitmq更新评论交互的信息
 * @author czc
 * @date 2025/10/12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentInteractUpdEvent {
    private String version;

    private Map<String, Set<String>> idxMp; // Map<userId,List<commentId>>

}