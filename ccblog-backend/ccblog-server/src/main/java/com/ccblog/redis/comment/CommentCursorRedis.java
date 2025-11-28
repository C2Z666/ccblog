package com.ccblog.redis.comment;

import com.ccblog.constant.RedisPrefixConstant;
import com.ccblog.template.SimpleObjectRedisTemplate;
import com.ccblog.vo.comment.CommentCursorVO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 评论
 * @author czc
 * @date 2025/10/18
 */
@Component
public class CommentCursorRedis extends SimpleObjectRedisTemplate<CommentCursorVO> {
    /**
     * 生成key
     */
    protected String buildKey(Object... args) {
        Long articleId = (Long) args[0];
        Long parentId = (Long) args[1];
        Long seconds = LocalDateTime.parse(args[2].toString()).toEpochSecond(ZoneOffset.ofHours(8)); // 东八区
        Integer limit = (Integer) args[3];
        return String.format(RedisPrefixConstant.COMMENT_CURSOR,articleId,parentId,seconds,limit);
    }

    /**
     * 删除pattern生成
     */
    protected String buildRemovePattern(Object... args) {
        Long articleId = (Long) args[0];
        Long parentId = (Long) args[1];
        return String.format(RedisPrefixConstant.COMMENT_CURSOR,articleId,parentId,"*","*");
    }

    /**
     * 类型
     */
    protected Class<CommentCursorVO> getRedisClass() {
        return CommentCursorVO.class;
    }

    /**
     * 拿评论
     */
    public CommentCursorVO getComment(Long articleId, Long parentId, LocalDateTime cursor, Integer limit){
        return super.getContent(articleId,parentId,cursor,limit);
    }

    /**
     * 存评论
     */
    public boolean setComment(Long articleId, Long parentId, LocalDateTime cursor, Integer limit,CommentCursorVO commentCursorVO){
        return super.setContent(commentCursorVO,articleId,parentId,cursor,limit);
    }

    /**
     * 主动失效
     */
    public void removeComment(Long articleId,Long parentId){
        super.removeContent(articleId,parentId);
    }

}