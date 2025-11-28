package com.ccblog.service.comment;

import com.ccblog.dto.comment.CommentItemDTO;
import com.ccblog.dto.comment.CommentShowDTO;
import com.ccblog.dto.comment.NewCommentDTO;
import com.ccblog.vo.comment.CommentCursorVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论相关接口
 * @author czc
 * @date 2025-10-03
 */
public interface CommentService {

    /**
     * 新增一条评论
     *
     * @return
     */
    CommentItemDTO saveComment(NewCommentDTO newCommentDTO);

    /**
     * 获取展示需要的评论相关信息
     * @param articleId
     * @return
     */
    CommentShowDTO getCommentShow(Long articleId,Long userId);

    /**
     * 处理对评论的操作
     * @param commentId
     * @param opeatationType
     */
    void operateComment(Long commentId, Integer opeatationType);

    /**
     * 根据游标获取评论
     * @param articleId 文章id(和父评论id任选1)
     * @param parentId  父评论id
     * @param cursor    展开的最后一条评论的时间
     * @param cursorId  展开的最后一条评论的id
     * @param limit     数量
     * @return
     */
    CommentCursorVO getCursorComments(Long articleId, Long parentId, LocalDateTime cursor, Long cursorId, Integer limit);

    /**
     * 获得评论祖先序列
     * @param commentId
     * @return
     */
    List<Long> getCommentParentList(Long commentId);

    /**
     * 获得一条评论的信息(游标评论单元)
     * @param commentId
     * @return
     */
    CommentItemDTO getTopComment(Long commentId);
}