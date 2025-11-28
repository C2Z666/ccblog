package com.ccblog.entity.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 文章计数实体类
 *
 * @author czc
 * @date 2025/10/7
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleCount {

    /** ID */
    private Long id;

    /** 文章ID */
    private Long articleId;

    /** 访问计数 */
    private Integer readCnt;

    /** 点赞计数 */
    private Integer likeCnt;

    /** 收藏计数 */
    private Integer collectCnt;

    /** 评论计数 */
    private Integer commentCnt;

    /** 文章被举报数 */
    private Integer reportCnt;

    /** 最后更新时间 */
    private LocalDateTime updateTime;

}