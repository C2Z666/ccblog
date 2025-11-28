package com.ccblog.vo.comment;

import com.ccblog.dto.comment.CommentItemDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 评论返回类
 * @author czc
 * @date 2025-10-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentCursorVO {
    /**
     * 是否还有更多(本条评论是是否有更多展开)
     */
    private Boolean hasMore;

    List<CommentItemDTO> comments;
}