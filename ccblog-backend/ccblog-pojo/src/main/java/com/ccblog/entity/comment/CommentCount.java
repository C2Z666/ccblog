package com.ccblog.entity.comment;

import com.ccblog.entity.BaseInfo;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 *
 * @author czc
 * @date 2025/10/7
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentCount {
    /** ID */
    private Long id;

    /** 评论ID */
    private Long commentId;

    /** 点赞数 */
    private Integer likeCnt;

    /** 点踩数 */
    private Integer dislikeCnt;

    /** 举报数 */
    private Integer reportCnt;

    /** 回复数 */
    private Integer replyCnt;

    /** 最后更新时间 */
    private LocalDateTime updateTime;
}