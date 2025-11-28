package com.ccblog.mapper.comment;

import com.ccblog.entity.article.ArticleCount;
import com.ccblog.entity.comment.CommentCount;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 * @author czc
 * @date 2025/10/7
 */
@Mapper
public interface CommentCountMapper {

    /**
     * 保存一条计数信息(初始化)
     * @param commentCount
     */
    @Insert("INSERT INTO comment_count(comment_id) values(#{commentId})")
    void save(CommentCount commentCount);

    /**
     * 根据评论id获得计数信息
     * @param commentId
     * @return
     */
    @Select("SELECT like_cnt AS likeCnt,dislike_cnt AS dislikeCnt," +
            "report_cnt AS reportCnt,reply_cnt AS replyCnt " +
            "FROM comment_count WHERE comment_id=#{commentId}")
    CommentCount getCountByCommentId(Long commentId);

    /**
     * 批量新增/更新评论增量计数
     * @param commentCountList
     */
    void upsertDeltaCountBatch(List<CommentCount> commentCountList);

    /**
     * 批量查询计数信息
     * @param commentIds
     * @return
     */
    List<CommentCount> getCommentCountBatch(List<Long> commentIds);
}