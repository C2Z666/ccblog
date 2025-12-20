package com.ccblog.entity.common;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 举报记录实体
 *
 * @author czc
 * @date 2025/12/20
 */
@Data
@TableName("report_record")
public class ReportRecord {
    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 举报人用户ID */
    private Long reporterUid;

    /** 被举报对象类型：1文章 2评论 4用户 */
    private Integer targetType;

    /** 被举报对象主键ID */
    private Long targetId;

    /** 举报类型ID → report_type.id */
    private Integer reasonId;

    /** 补充文字 */
    private String reasonText;

    /** 证据图片URL数组（JSON） */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> proofUrls;

    /** 处理状态：0未处理 1已处理 */
    private Integer status;

    /** 处理结果：0忽略 1接受 2拒绝 */
    private String resultAction;

    /** 管理员备注 */
    private String resultNote;

    /** 处理管理员ID */
    private Long auditorId;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}