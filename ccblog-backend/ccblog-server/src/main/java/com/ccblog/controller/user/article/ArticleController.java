package com.ccblog.controller.user.article;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccblog.annotation.Permission;
import com.ccblog.dto.article.ArticleDTO;
import com.ccblog.dto.article.ArticleEditDTO;
import com.ccblog.dto.article.NewArticleDTO;
import com.ccblog.enumeration.StatusEnum;
import com.ccblog.enumeration.user.UserRole;
import com.ccblog.service.article.ArticleService;
import com.ccblog.vo.article.ArticleDetailVO;
import com.ccblog.vo.global.ResVo;
import com.ccblog.vo.global.ResultVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文章相关
 * @author czc
 * @date 2025-09-27
 */
@Slf4j
@Tag(name="文章接口",description = "文章接口")
@RequestMapping("/article")
@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 新增文章
     * @return
     */
    @PostMapping("/save")
    @Operation(summary = "新增文章")
    public ResultVo saveArticle(@RequestBody NewArticleDTO newArticleDTO){
        articleService.saveArticle(newArticleDTO);
        return ResultVo.ok(true);
    }

    /**
     * 获取编辑文章需要的信息
     * @return
     */
    @GetMapping("/edit/{articleId}")
    @Operation(summary = "添加/更新文章")
    public ResultVo updateArticle(@PathVariable Long articleId){
        ArticleEditDTO articleEditDTO = articleService.queryArticle(articleId);
        return ResultVo.ok(articleEditDTO);
    }

    /**
     * 查询某个用户的文章
     */
    @Operation(summary = "根据用户id查询文章")
    @GetMapping("/userId")
    public ResultVo<IPage<ArticleDTO>> queryArticleOfUser(@RequestParam(name = "userId") Long userId,
                                                          @RequestParam(name = "currentPage", required = false, defaultValue = "1") int currentPage,
                                                          @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize){
        IPage<ArticleDTO> articleDTOIPage = articleService.queryPageArticleByUserId(userId,currentPage,pageSize);
        return ResultVo.ok(articleDTOIPage);
    }

    /**
     * 获取文章详情(框架)
     * @param articleId
     * @return
     */
    @GetMapping("/detail/{articleId}")
    @Operation(summary = "根据id获得文章详情")
    public ResultVo<ArticleDetailVO> detailOriginalMarkdown(@PathVariable(name = "articleId") Long articleId,
                                                            @RequestParam(name="commentId",defaultValue = "-1") Long commentId) throws JsonProcessingException {
        ArticleDetailVO articleDetailVO = articleService.getArticleDetail(articleId,commentId);
        if(articleDetailVO==null){
            return ResultVo.fail(StatusEnum.ARTICLE_NOT_EXISTS); // 文章不存在
        }
        return ResultVo.ok(articleDetailVO);
    }

    /**
     *
     * @param articleId 文章id
     * @return
     */
    @GetMapping("/detail/more/{articleId}")
    @Operation(summary = "获得更多文章信息")
    public ResVo<ArticleDetailVO> getFullDetail(@PathVariable Long articleId,
                                                @RequestParam(name = "queryFix", defaultValue = "false",required = false) boolean queryFix,
                                                @RequestParam(name = "categoryId", defaultValue = "0",required = false) Long categoryId,
                                                @RequestParam(name = "authorId", defaultValue = "0",required = false) Long authorId
                                                ) {
        ArticleDetailVO articleDetailVO = articleService.getFullInfo(articleId,queryFix,categoryId,authorId);
        return ResVo.ok(articleDetailVO);
    }

    /**
     * 用户点赞/收藏/...
     * 之所以用get因为get才能用这种输入变量(属于不太合理的操作)
     * @param articleId
     * @param opeatationType
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "/articleOperation")
    @Operation(summary = "用户操作")
    public ResVo<Boolean> operateArticle(@RequestParam(name = "articleId") Long articleId,
                                        @RequestParam(name = "type") Integer opeatationType,
                                        @RequestParam(name = "authorId") Long authorId){
        articleService.operateArticle(articleId,authorId,opeatationType);
        return ResVo.ok(true);
    }

    /**
     * 删除文章
     * @param articleId
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "/delete")
    @Operation(summary = "删除文章")
    public ResVo<Boolean> deleteArticle(@RequestParam(name = "articleId") Long articleId){
        articleService.deleteArticle(articleId);
        return ResVo.ok(true);
    }

    /**
     * 浏览历史
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "/history")
    @Operation(summary = "用户浏览历史")
    public ResVo<IPage<ArticleDTO>> getReadHistory(@RequestParam(name = "userId") Long userId,
                                                               @RequestParam(name = "currentPage", required = false, defaultValue = "1") int currentPage,
                                                               @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize){
        IPage<ArticleDTO> articleDTOIPage = articleService.getReadHistory(userId,currentPage,pageSize);
        return ResVo.ok(articleDTOIPage);
    }

    /**
     * 收藏文章
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "/star")
    @Operation(summary = "用户收藏")
    public ResVo<IPage<ArticleDTO>> getSatrArticle(@RequestParam(name = "userId") Long userId,
                                                      @RequestParam(name = "currentPage", required = false, defaultValue = "1") int currentPage,
                                                      @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize){
        IPage<ArticleDTO> articleDTOIPage = articleService.getStarArticle(userId,currentPage,pageSize);
        return ResVo.ok(articleDTOIPage);
    }

    /**
     * 搜索文章
     * @param query
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "/query")
    @Operation(summary = "搜索文章")
    public ResultVo<IPage<ArticleDTO>> queryArticle(@RequestParam(name = "query") String query,
                                                 @RequestParam(name = "currentPage", required = false, defaultValue = "1") int currentPage,
                                                 @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize){
        IPage<ArticleDTO> articleDTOIPage = articleService.getArticleByQuery(query,currentPage,pageSize);
        return ResultVo.ok(articleDTOIPage);
    }

    /**
     * 获得最新草稿文章信息
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "/draft/latest")
    @Operation(summary = "获得最新草稿文章信息")
    public ResVo<ArticleEditDTO> getLatestDraft(){
        ArticleEditDTO editDTO = articleService.getLatestDraft();
        return ResVo.ok(editDTO);
    }
}