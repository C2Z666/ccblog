package com.ccblog.service.comment.helper;

import com.ccblog.context.ReqInfoContext;
import com.ccblog.dto.comment.CommentItemDTO;
import com.ccblog.enumeration.YesOrNoEnum;
import com.ccblog.enumeration.user.FieldInfoEnum;
import com.ccblog.redis.comment.CommentCountRedis;
import com.ccblog.redis.comment.CommentInteractionRedis;
import com.ccblog.redis.user.UserInfoRedis;
import com.ccblog.utils.NumUtil;
import com.ccblog.vo.comment.CommentCursorVO;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.ccblog.enumeration.comment.CommentOperateFieldEnum.COL_DISLIKE;
import static com.ccblog.enumeration.comment.CommentOperateFieldEnum.COL_LIKE;
import static com.ccblog.enumeration.comment.CommentOperateFieldEnum.COL_REPLY;
import static com.ccblog.enumeration.user.FieldInfoEnum.COL_PHOTO;
import static com.ccblog.enumeration.user.FieldInfoEnum.COL_USER_NAME;

/**
 * 文章工具类
 * @author czc
 * @date 2025/11/27
 */
@Component
public class CommentHelper {
    @Autowired
    private CommentCountRedis commentCountRedis;

    @Autowired
    private CommentInteractionRedis commentInteractionRedis;

    @Autowired
    private UserInfoRedis userInfoRedis;


    /**
     * 从redis查询数据更新评论数据(单个评论数据)
     */
    public CommentItemDTO commentItemUpdAttr(CommentItemDTO commentItemDTO){
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        Long commentId = commentItemDTO.getCommentId();
        // 评论本身
        Map<String, Integer> totalCounts =
                commentCountRedis.getTotalCounts(commentId, List.of(COL_LIKE, COL_REPLY, COL_DISLIKE));
        commentItemDTO.setLikeCnt(totalCounts.get(COL_LIKE));
        commentItemDTO.setDislikeCnt(totalCounts.get(COL_DISLIKE));
        commentItemDTO.setReplyCnt(totalCounts.get(COL_REPLY));
        // 用户交互
        if(userId!=null){
            commentItemDTO.setLike( // 是否点赞
                    commentInteractionRedis.getUserCommentStaus(userId,commentId,COL_LIKE)== YesOrNoEnum.YES.getCode());
            commentItemDTO.setDislike( // 是否点踩
                    commentInteractionRedis.getUserCommentStaus(userId,commentId,COL_DISLIKE)== YesOrNoEnum.YES.getCode());
        }
        else{
            commentItemDTO.setLike(false);
            commentItemDTO.setDislike(false);
        }
        return commentItemDTO;
    }

    /**
     * 从redis查询数据更新评论数据
     * return是方便对对象处理完直接返回
     * @return 更新属性后的评论数据
     */
    public CommentCursorVO cursorCommentUpdAttr(CommentCursorVO commentCursorVO){
        boolean isLogin = ( ReqInfoContext.getReqInfo()!=null);
        Long userId = null;
        if(isLogin) userId = ReqInfoContext.getReqInfo().getUserId(); // 要获取当前用户对评论的状态
        // 批量获得用户基本信息
        Set<Long> userIdSet = new HashSet<>();
        Set<Long> commentIdSet = new HashSet<>();
        Set<Tuple2<Long,Long>> userCommentSet = new HashSet<>();
        for(CommentItemDTO dto:commentCursorVO.getComments()){
            Long commentAuthorId = dto.getUserId();
            Long commentId = dto.getCommentId();
            commentIdSet.add(commentId);
            userIdSet.add(commentAuthorId);
            if(isLogin) userCommentSet.add(Tuple.of(userId,commentId));
        }
        Map<Long, Map<String, String>> infos =
                userInfoRedis.getInfosBatch(userIdSet, List.of(COL_USER_NAME, FieldInfoEnum.COL_PHOTO));
        Map<Long,Map<String,String>> commentCounts =
                commentCountRedis.getTotalCountsBatch(commentIdSet,List.of(COL_LIKE,COL_REPLY,COL_DISLIKE));
        Map<Tuple2<Long,Long>,Map<String,String>> commentStaus = null;
        if(isLogin){
            commentStaus = commentInteractionRedis.getStatusBatch(userCommentSet,List.of(COL_LIKE,COL_DISLIKE));
        }
        final int YES = YesOrNoEnum.YES.getCode();
        for(CommentItemDTO dto:commentCursorVO.getComments()){
            Long commentId = dto.getCommentId();
            Long commentAuthorId = dto.getUserId();
            // 添加评论计数信息
            Map<String,String> commentCount = commentCounts.getOrDefault(commentId,Map.of());
            dto.setReplyCnt(NumUtil.null2Zero(commentCount.get(COL_REPLY)));
            dto.setLikeCnt(NumUtil.null2Zero(commentCount.get(COL_LIKE)));
            dto.setDislikeCnt(NumUtil.null2Zero(commentCount.get(COL_DISLIKE)));
            // 添加用户信息
            Map<String,String> info = infos.getOrDefault(commentAuthorId, Map.of());
            dto.setUserName(info.getOrDefault(COL_USER_NAME,""));
            dto.setUserPhoto(info.getOrDefault(COL_PHOTO,""));
            // 添加评论交互信息
            if(isLogin){
                Tuple2<Long,Long> idx = Tuple.of(userId,commentId);
                Map<String,String> statusMap = commentStaus.get(idx);
                dto.setLike(statusMap.get(COL_LIKE)!=null // 为null就是没记录那就是没点赞(false)
                        &&Integer.parseInt(statusMap.getOrDefault(COL_LIKE,"0"))==YES);
                dto.setDislike(statusMap.get(COL_DISLIKE)!=null
                        &&Integer.parseInt(statusMap.getOrDefault(COL_DISLIKE,"0"))==YES);
            }
            else{
                dto.setLike(false);
                dto.setDislike(false);
            }
        }
        return commentCursorVO;
    }
}