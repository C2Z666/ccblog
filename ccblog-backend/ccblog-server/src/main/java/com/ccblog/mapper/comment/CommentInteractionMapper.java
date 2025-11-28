package com.ccblog.mapper.comment;

import com.ccblog.entity.comment.CommentInteraction;
import io.vavr.Tuple2;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 和用户评论操作相关
 * @author czc
 * @date 2025-10-04
 */
@Mapper
public interface CommentInteractionMapper {

    /**
     * 插入/更新一条用户评论交互信息
     */
    void saveOrUpdateCommentInteraction(CommentInteraction commentInteraction);

    /**
     * 查找用户对评论的状态
     */
    @Select("SELECT like_stat likeStat," +
            "dislike_stat dislikeStat,report_stat reportStat" +
            " FROM comment_interaction WHERE user_id=#{userId} AND comment_id=#{commentId}")
    CommentInteraction getUserStatusByCommentId(Long userId, Long commentId);

    /**
     * 批量更新评论交互
     * @param commentInteractionList
     */
    void upsertInteractionBatch(List<CommentInteraction> commentInteractionList);

    /**
     * 批量查询评论交互
     * @param idxPairs
     * @return
     */
    List<CommentInteraction> getUserStatusBatch(List<Tuple2<Long, Long>> idxPairs);
}