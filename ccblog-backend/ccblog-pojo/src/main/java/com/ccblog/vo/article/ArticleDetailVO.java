package com.ccblog.vo.article;

import com.ccblog.dto.article.ArticleDTO;
import com.ccblog.dto.comment.CommentItemDTO;
import com.ccblog.dto.comment.TopCommentDTO;
import com.ccblog.vo.comment.CommentCursorVO;
import com.ccblog.vo.user.UserBaseInfoVo;
import lombok.Data;

import java.util.List;

/**
 * @author czc
 * @date 2025-09-30
 */
@Data
public class ArticleDetailVO {
    /**
     * 文章信息
     */
    private ArticleDTO article;

    /**
     * 评论信息后续不通过文章详情返回,在评论页自动拉取,放着防止后续用
     */
    /**
     * 评论信息(顶级+子评论)
     */
    private List<TopCommentDTO> comments;

    /**
     * 热门评论(点赞最多)
     */
    private TopCommentDTO hotComment;

    /**
     * 游标方式获得的结果
     */
    CommentCursorVO commentCursorVO;

    // =================  下面的两个尽在需要回复的时候跳转评论需要用到 =============
    /**
     * 评论的祖先节点序列
     */
    List<Long> commentParents;

    /**
     * 顶级评论信息
     */
    private CommentItemDTO topCommentInfo;

    /**
     * 作者相关信息
     */
    private UserBaseInfoVo author;

    /**
     * 所属专栏id
     */
//    private Long columnId;
}
