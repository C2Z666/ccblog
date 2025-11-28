package com.ccblog.dto.article;

import com.ccblog.enumeration.article.PushStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author XuYifei
 * @date 2024-07-12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Tag(name = "文章分类DTO", description = "文章分类DTO")
public class CategoryDTO implements Serializable {
    public static final String DEFAULT_TOTAL_CATEGORY = "全部";
    public static final CategoryDTO DEFAULT_CATEGORY = new CategoryDTO(0L, "全部");

    private static final long serialVersionUID = 8272116638231812207L;
    public static CategoryDTO EMPTY = new CategoryDTO(-1L, "illegal");

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String category;

    /**
     * 排序
     */
    private Integer rank;

    /**
     * 分类状态
     */
    private Integer status;


    // 这个方法供查全部(category=null)调用
    public CategoryDTO(Long categoryId, String category) {
        this(categoryId, category, 0);
    }

    public CategoryDTO(Long categoryId, String category, Integer rank) {
        this.categoryId = categoryId;
        this.category = category;
        this.status = PushStatusEnum.ONLINE.getCode();
        this.rank = rank;
    }
}
