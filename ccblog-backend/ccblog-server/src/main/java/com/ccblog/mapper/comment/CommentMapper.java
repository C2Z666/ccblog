package com.ccblog.mapper.comment;

import com.ccblog.dto.comment.CommentInfoDTO;
import com.ccblog.dto.comment.CommentItemDTO;
import com.ccblog.entity.comment.Comment;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 评论信息相关的查询
 * @author czc
 * @date 2025-10-03
 */
@Mapper
public interface CommentMapper {

    /**
     * 插入一条评论信息
     * @param comment
     */
    void saveComment(Comment comment);

    /**
     * 获得一篇文章的所有评论和相关信息
     * @param articleId 文章id
     * @param userId 当前用户id
     * @return
     */
    List<CommentItemDTO> listCommentByArticleId(Long articleId, Long userId);

    /**
     * 游标查询评论数据
     * @param articleId 文章id(和父评论id任选1)
     * @param parentId  父评论id
     * @param cursor    展开的最后一条评论的时间
     * @param cursorId  展开的最后一条评论的id
     * @param limit     数量
     * @return
     */
    List<CommentItemDTO> listByArticleIdAndCommentId(Long articleId, Long userId,Long parentId, LocalDateTime cursor,Long cursorId, Integer limit);

    /**
     * 根据commentId获得评论对象
     * @param userId 当前登录用户
     * @param commentId 评论id
     * @return
     */
    CommentItemDTO getCommentByCommentId(Long userId,Long commentId);

    /**
     * 根据评论id删除评论
     * @param commentId
     */
    @Update("UPDATE comment SET deleted=1 WHERE id=#{commentId}")
    void deleteByCommentId(Long commentId);

    /**
     * commentId查parentId
     * @return
     */
    @Select("SELECT parent_comment_id FROM comment WHERE id=#{commentId}")
    Long getParentIdByCommentId(Long commentId);

    /**
     * commentId查parentId链
     * @return
     */
    List<Long> getAllParentIdByCommentId(Long commentId);


    /**
     * 根据commendId查询用户id
     * @param parentId
     * @return
     */
    @Select("SELECT user_id FROM comment WHERE id=#{parentId}")
    Long getUserIdbyCommentId(Long parentId);

    /**
     * 根据id获得评论内容
     * @param commentId
     * @return
     */
    @Select("SELECT content FROM comment WHERE id=#{commentId}")
    String getContentByCommentId(Long commentId);

    /**
     * 查询评论的更多关联信息
     * @param commentIds
     * @return
     */
    List<CommentInfoDTO> getCommInfoByCommId(List<Long> commentIds);

    /**
     * 获得一个游标评论单元信息
     */
    CommentItemDTO getCommentCursorItem(Long commentId);
}