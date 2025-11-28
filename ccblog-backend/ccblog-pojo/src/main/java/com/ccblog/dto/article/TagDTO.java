package com.ccblog.dto.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;



/**
 * @author czc
 * @date 2025-10-30
 */
@Data
@NoArgsConstructor
public class TagDTO implements Serializable {
    private static final long serialVersionUID = -8614833588325787479L;

    /**
     * id
     */
    private Long tagId;

    /**
     * tag名称
     */
    private String tag;


    /**
     * 状态
     */
    private Integer status;

    /**
     * 热度
     */
    private Integer hotScore;
}
