package com.ccblog.entity.article;

import com.ccblog.entity.BaseInfo;
import lombok.*;

import java.io.Serializable;

/**
 * 文章类别表
 * @author czc
 * @date 2025-09-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 类目名称 */
    private String categoryName;
    /* 状态：0-未发布，1-已发布 */
    private Integer status;
    /* 排序字段 */
    private Integer rank;
    /* 是否删除：0-正常，1-已删除 */
    private Integer deleted;
}