package com.ccblog.dto.comment;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论树状结构
 *
 * @author XuYifei
 * @since 2024-07-12
 */
@Data
public class TopCommentDTO extends BaseCommentDTO {
    /**
     * 评论数量
     */
    private Integer commentCount;

    /**
     * 子评论
     */
    private List<SubCommentDTO> childComments;

    public List<SubCommentDTO> getChildComments() {
        if (childComments == null) {
            childComments = new ArrayList<>();
        }
        return childComments;
    }

    @Override
    // 保证时间越早越靠前
    public int compareTo(@NotNull BaseCommentDTO o) {
        return Long.compare(o.getCommentTime().getTime(), this.getCommentTime().getTime());
    }
}
