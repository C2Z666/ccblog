package com.ccblog.dto.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 *举报内容详情
 * @author czc
 * @date 2025/12/20
 */
@Data
public class ReportDetailDTO {
    /** 被举报对象类型：ARTICLE-文章 1  COMMENT-评论 2 USER-用户 4 */
    @NotBlank
    private int targetType;

    /** 被举报对象主键ID */
    @NotNull
    private Long targetId;

    /** 举报理由ID（report_type.id） */
    @NotNull
    private Integer reasonId;

    /** 补充说明（可选） */
    private String reasonText;

    /** 证据图片URL列表（可选，最多6张） */
    private List<String> proofImages;
}