package com.ccblog.entity.article;

import com.ccblog.entity.BaseInfo;
import lombok.*;

/**
 * @author czc
 * @date 2025-09-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag extends BaseInfo {

    private static final long serialVersionUID = 1L;

    /* 标签名称 */
    private String tagName;
    /* 标签类型：1-系统标签，2-自定义标签 */
    private Integer tagType;
    /* 所属类目ID */
    private Long categoryId;
    /* 状态：0-未发布，1-已发布 */
    private Integer status;
    /* 是否删除：0-正常，1-已删除 */
    private Integer deleted;
}