package com.ccblog.mapper.article;

import com.ccblog.dto.article.TagCategoryDTO;
import com.ccblog.dto.article.TagDTO;
import com.ccblog.entity.article.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 标签有关查询
 * @author czc
 * @date 2025-09-27
 */
@Mapper
public interface TagMapper {

    /**
     * 查询所有TagDTO
     * @return
     */
    @Select("SELECT id tagId,tag_name tag,`status` FROM tag WHERE deleted=0")
    List<TagDTO> queryAllTag();

    /**
     * 查询所有tag entity
     * @return
     */
    @Select("select * from tag where deleted=0")
    List<Tag> listAllTag();


}
