package com.ccblog.controller.user.article;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccblog.dto.article.ArticleDTO;
import com.ccblog.dto.article.TagDTO;
import com.ccblog.service.article.TagService;
import com.ccblog.vo.global.ResVo;
import com.ccblog.vo.global.ResultVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author czc
 * @date 2025-09-29
 */
@Slf4j
@Tag(name="标签接口",description = "标签接口")
@RequestMapping("/tag")
@RestController
public class TagController {
    @Autowired
    private TagService tagService;

    /**
     * 查询所有标签
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有标签")
    public ResultVo<List<TagDTO>> listAllTags(){
        List<TagDTO> tagDTOS = tagService.listAllTags();
        return ResultVo.ok(tagDTOS);
    }

    /**
     * 分页获取指定标签的文章
     * @param tagId         标签id
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping("/articles")
    @Operation(summary = "标签查询文章")
    public ResultVo<IPage<ArticleDTO>> getArticlesByCategory(@RequestParam(name = "tagId", required = false) Long tagId,
                                                             @RequestParam(name = "currentPage", required = false, defaultValue = "1") int currentPage,
                                                             @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        IPage<ArticleDTO> tagArticles =
                tagService.queryArticleByTagId(tagId,currentPage,pageSize);
        return ResultVo.ok(tagArticles);
    }

    /**
     * 获取标签信息(首页热门标签)
     * @return
     */
    @GetMapping("/hot")
    @Operation(summary = "热门标签")
    public ResVo<List<TagDTO>> getHotTags(@RequestParam(name = "limit", required = false, defaultValue = "15") int limit) {
        List<TagDTO> tagDTOS = tagService.getHotTag(limit);
        return ResVo.ok(tagDTOS);
    }


}