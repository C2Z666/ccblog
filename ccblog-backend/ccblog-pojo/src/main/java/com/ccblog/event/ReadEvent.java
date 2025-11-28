package com.ccblog.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章阅读事件
 * @author czc
 * @date 2025/11/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadEvent {

    private long userId;

    private long authorId;

    private long articleId;

    private long readTime;
}