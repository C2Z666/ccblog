package com.ccblog.vo.common;

import lombok.Data;

/**
 * 举报条目信息
 * @author czc
 * @date 2025/12/20
 */
@Data
public class ReportTypeVO {
    /**
     * id
     */
    private Long id;

    /**
     * 举报原因
     */
    private String reason;
}