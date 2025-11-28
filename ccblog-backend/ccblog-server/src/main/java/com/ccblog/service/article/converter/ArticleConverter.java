package com.ccblog.service.article.converter;

import com.ccblog.dto.article.ArticleDTO;
import com.ccblog.dto.article.CategoryDTO;
import com.ccblog.dto.article.TagDTO;
import com.ccblog.enumeration.article.SourceTypeEnum;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 用于article转为其他类型  目前已被遗弃
 * @author czc
 * @date 2025-09-28
 */
@Mapper(componentModel = "spring", imports = {SourceTypeEnum.class}) // // 交给容器处理
public interface ArticleConverter {

    /**
     * 外部唯一入口
     */
    default List<ArticleDTO> toDTOList(List<ArticleDTO> articles,
                                       List<CategoryDTO> categories,
                                       List<List<TagDTO>> tags) {
        return IntStream.range(0, articles.size())
                .mapToObj(i -> {
                    ArticleDTO dto = entityToDTO(articles.get(i));
                    dto.setCategory(categories.get(i));
                    dto.setTags(tags.get(i));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /* ====== 下面都是 MapStruct 自动生成的 ====== */
    // 同名的会自动迁移,标注了ingore=True的不会自动生成
    @Mapping(target = "category", ignore = true)   // 手动设
    @Mapping(target = "tags", ignore = true)       // 手动设
    // 这些本来是间接查询用的
//    @Mapping(target = "author", source = "authorName")
//    @Mapping(target = "authorAvatar", source = "photo")
//    @Mapping(target = "userId", source = "user_id")
//    @Mapping(target = "sourceType", source = "source")
//    @Mapping(target = "lastUpdateTime", source = "updateTime")
    ArticleDTO entityToDTO(ArticleDTO article);
}