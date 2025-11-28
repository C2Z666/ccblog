package com.ccblog.dto.article;

import lombok.Data;

import java.util.List;

/**
 * 用户更新文章id查询得到tagId暂存
 * @author czc
 * @date 2025/11/2
 */
@Data
public class ArticleIdTagDTO {
    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 标签id
     */
    private List<Long> tagIds;

    // 废弃方案(现在不从数据库查询tag详细信息)
//    /**
//     * 对应的tag相关信息
//     */
//    private TagDTO tagDTO;
}