package com.ccblog.service.comment.Impl;

import com.ccblog.context.ReqInfoContext;
import com.ccblog.dto.comment.*;
import com.ccblog.dto.notice.NoticeContentAggDTO;
import com.ccblog.entity.comment.Comment;
import com.ccblog.enumeration.article.ArticleCommentStatusEnum;
import com.ccblog.enumeration.article.ArticleOperateTypeEnum;
import com.ccblog.enumeration.comment.CommentOperateTypeEnum;
import com.ccblog.enumeration.notice.NoticeTargetTypeEnum;
import com.ccblog.enumeration.notice.NoticeTypeEnum;
import com.ccblog.enumeration.user.UserOperateTypeEnum;
import com.ccblog.event.OperationEvent;
import com.ccblog.localCache.comment.CommentCursorCache;
import com.ccblog.mapper.article.ArticleMapper;
import com.ccblog.mapper.comment.CommentCountMapper;
import com.ccblog.mapper.comment.CommentInteractionMapper;
import com.ccblog.mapper.comment.CommentMapper;
import com.ccblog.mapper.notice.NoticeContentMapper;
import com.ccblog.redis.comment.CommentCountRedis;
import com.ccblog.redis.comment.CommentCursorRedis;
import com.ccblog.redis.comment.CommentInteractionRedis;
import com.ccblog.redis.notice.NoticeContentRedis;
import com.ccblog.redis.notice.NoticeCountRedis;
import com.ccblog.redis.user.UserCountRedis;
import com.ccblog.service.article.ArticleService;
import com.ccblog.service.comment.CommentService;
import com.ccblog.redis.article.ArticleCountRedis;
import com.ccblog.redis.article.ArticleInteractionRedis;
import com.ccblog.service.comment.helper.CommentHelper;
import com.ccblog.utils.TruncateUtil;
import com.ccblog.vo.comment.CommentCursorVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static com.ccblog.enumeration.article.ArticleOperateFieldEnum.COL_COMMENT;
import static com.ccblog.enumeration.comment.CommentOperateFieldEnum.*;
import static com.ccblog.enumeration.comment.CommentOperateTypeEnum.*;
import static com.ccblog.enumeration.comment.CommentOperateTypeEnum.DISLIKE;
import static com.ccblog.enumeration.comment.CommentOperateTypeEnum.LIKE;


/**
 * @author czc
 * @date 2025-10-03
 */
@Service
public class commentServiceImpl implements CommentService {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CommentInteractionMapper commentInteractionMapper;

    @Autowired
    private CommentCountMapper commentCountMapper;

    @Autowired
    private ArticleCountRedis articleCountRedis;

    @Autowired
    private ArticleInteractionRedis articleInteractionRedis;

    @Autowired
    private CommentCountRedis commentCountRedis;

    @Autowired
    private CommentInteractionRedis commentInteractionRedis;

    @Autowired
    private UserCountRedis userCountRedis;

    @Autowired
    private CommentCursorRedis commentCursorRedis;

    @Autowired
    private CommentCursorCache commentCursorCache;

    @Autowired
    private NoticeContentRedis noticeContentRedis;

    @Autowired
    private NoticeCountRedis noticeCountRedis;

    @Autowired
    private NoticeContentMapper noticeContentMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CommentHelper commentHelper;


    /**
     * 插入一条评论信息
     * @param newCommentDTO
     * @return
     */
    @Transactional
    public CommentItemDTO saveComment(NewCommentDTO newCommentDTO) {
        // 插入评论表格
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        Long articleId = newCommentDTO.getArticleId();
        Comment comment = Comment.builder()
                .userId(userId)
                .articleId(articleId)
                .content(newCommentDTO.getCommentContent())
                .topCommentId(newCommentDTO.getTopCommentId())
                .parentCommentId(newCommentDTO.getParentCommentId()).build();
        commentMapper.saveComment(comment);
        // 获得新的评论信息并返回供前端渲染
        CommentItemDTO commentItemDTO = commentMapper.getCommentByCommentId(userId,comment.getId()); // 使用游标插入之后,评论区单独维护,插入之后直接返回新的数据评论数据
        commentItemDTO.setReplyCnt(0); // 新发布的默认没有回复
        // 因为是新发布的,肯定是没有点赞和举报的
        commentItemDTO.setReportCnt(0);
        commentItemDTO.setLikeCnt(0);
        Long commentId = comment.getId();

        // redis更新
        // 更新文章redis相关信息(interaction和count)
        articleCountRedis.incrTotalCount(articleId, COL_COMMENT,1);
        articleInteractionRedis.setStatusSingle(userId,articleId, COL_COMMENT, ArticleCommentStatusEnum.COMMENT.getCode());
        articleCountRedis.aggregate(new OperationEvent(articleId, ArticleOperateTypeEnum.COMMENT.getCode())); // 评论事件
        articleInteractionRedis.aggregate(userId,articleId); // 对文章评论状态发生改变
        // 更新评论redis相关信息(interaction和count,还有cursor),也包括一些caffeine缓存的
        // 插入初始信息到计数redis,后续定时任务会自动新增一条记录(否则后续查询redis查不到又会查数据库)
        Map<String,String> map = Map.of(
                COL_DISLIKE, "0",
                COL_LIKE, "0"
        );
        commentCountRedis.setCount(commentId, map);
        commentCountRedis.aggregate(new OperationEvent(commentId, CommentOperateTypeEnum.NEW.getCode()));
        // 失效两级缓存(当前层+父层,失效父层是因为当前层的展开回复取决于父层的hasReply)
        Long parentId = (commentItemDTO.getParentCommentId()!=null?commentItemDTO.getParentCommentId():0L);
        commentCursorRedis.removeComment(articleId, parentId); // 主动失效redis缓存
        commentCursorCache.evictById(articleId, parentId); // 主动失效caffeine缓存
        if(parentId!=0){ // =0=>回复顶级不需要向上了
            Long pParentId = commentMapper.getParentIdByCommentId(parentId);
            commentCursorRedis.removeComment(articleId, pParentId); // 主动失效redis缓存
            commentCursorCache.evictById(articleId, pParentId); // 主动失效caffeine缓存
        }
        // 用户文章被评论数+1,需要后续更新用户被评论数
        Long authorId = newCommentDTO.getAuthorId();
        userCountRedis.incrTotalCount(authorId, COL_REPLY,1);
        userCountRedis.aggregate(new OperationEvent(authorId, UserOperateTypeEnum.COMMENT.getCode())); // 被评论数+1
        // 父评论回复数+1
        if(parentId!=0){ // 回复评论
            commentCountRedis.incrTotalCount(parentId, COL_REPLY,1);
            commentCountRedis.aggregate(new OperationEvent(parentId,CommentOperateTypeEnum.REPLY.getCode()));
        }
        // 更新通知相关redis(内容落库+计数落库)
        Long recvUserId = authorId;
        int type; // 通知对象
        if(parentId==0){ // 此时为评论文章/专栏,目前只有文章
            type = NoticeTargetTypeEnum.ARTICLE.getType();
        }
        else{
            recvUserId =  commentMapper.getUserIdbyCommentId(parentId); // 子级评论对象是评论作者
            type = NoticeTargetTypeEnum.COMMENT.getType();
        }
        if(recvUserId != userId){ // 自己给自己发的不通知
            // - 内容落库相关
            TruncateUtil.Result trucatedTxt = TruncateUtil.truncate(newCommentDTO.getCommentContent());
            String relatedInfo = trucatedTxt.getText();
            boolean trucate = trucatedTxt.isTruncated();
            NoticeContentAggDTO event = NoticeContentAggDTO.builder()
                    .userId(recvUserId)
                    .operateUserId(userId)
                    .targetType(NoticeTargetTypeEnum.COMMENT.getType()) // 评论
                    .targetId(commentId)
                    .relatedInfo(relatedInfo)
                    .type(type) // 点赞
                    .readFlag(false)
                    .truncated(trucate)
                    .status(true) // 默认有效
                    .createTime(LocalDateTime.now())
                    .build();
            noticeContentRedis.aggregate(event); // 聚合事件(一条评论消息)
            // - 未读计数相关(操作对象需要增加未读通知)
            if(parentId==0){
                noticeCountRedis.incrTotalCount(recvUserId,NoticeTypeEnum.COL_COMMENT,1);
            }
            else{
                noticeCountRedis.incrTotalCount(recvUserId,NoticeTypeEnum.COL_REPLY,1);
            }
            noticeCountRedis.aggregate(userId);
        }
        return commentItemDTO;
    }

    /**
     * 获取展示文章需要的评论详情(平铺展开类型)
     * 注意这里也是要从redis读取点赞数的,但是这个api遗弃,暂时不维护
     * @param articleId
     * @return
     */
    public CommentShowDTO getCommentShow(Long articleId,Long userId) {
        CommentShowDTO commentShowDTO = new CommentShowDTO();
        List<CommentItemDTO> commentItemDTOS = commentMapper.listCommentByArticleId(articleId,userId);
        // 1.处理comments+找出热评位置(回复最多)
        // 建立映射+生成顶级评论序列
        Map<Long,String> contentMap = new HashMap<>(); // Map<comment_id,content>评论内容
        Map<Long,Integer> indexMap = new HashMap<>(); // Map<comment_id,index>顶级评论的索引位置
        List<TopCommentDTO> topCommentDTOS = new ArrayList<>();
        for(CommentItemDTO commentItemDTO:commentItemDTOS){
            if(commentItemDTO.getParentCommentId()==0){ // 顶级评论
                TopCommentDTO topCommentDTO = new TopCommentDTO();
                BeanUtils.copyProperties(commentItemDTO,topCommentDTO);
                topCommentDTOS.add(topCommentDTO);
                indexMap.put(commentItemDTO.getCommentId(),  topCommentDTOS.size() -1); // 数量-1才是索引
            }
            contentMap.put(commentItemDTO.getCommentId(),commentItemDTO.getCommentContent());
        }
        // 建立子回复
        for(CommentItemDTO commentItemDTO:commentItemDTOS){
            if(commentItemDTO.getParentCommentId()==0){ // 顶级评论已处理
                continue;
            }
            SubCommentDTO subCommentDTO = new SubCommentDTO();
            BeanUtils.copyProperties(commentItemDTO,subCommentDTO);
            subCommentDTO.setParentContent(contentMap.get(commentItemDTO.getParentCommentId())); // 设置父评论内容
            int topDTOIdx = indexMap.get(commentItemDTO.getTopCommentId()); // 取出顶级评论在list中的位置
            topCommentDTOS.get(topDTOIdx).getChildComments().add(subCommentDTO); // 加入子节点
        }
        // 完善子回复+排序(子回复和顶级回复)
        int hot_idx = 0; // 记录热评位置(点赞数量最多)
        for(int i =0;i<topCommentDTOS.size();i++){
            TopCommentDTO topCommentDTO = topCommentDTOS.get(i);
            topCommentDTO.setCommentCount(topCommentDTO.getChildComments().size()); // 设置回复数量
            if(topCommentDTO.getLikeCnt()> topCommentDTOS.get(hot_idx).getLikeCnt()){
                hot_idx = i;
            }
            topCommentDTO.getChildComments().sort(SubCommentDTO::compareTo);
        }
        // 2.处理hotComment(要注意点赞数至少为1)
        if(topCommentDTOS!=null&& !topCommentDTOS.isEmpty() &&topCommentDTOS.get(hot_idx).getLikeCnt()>0) {
            TopCommentDTO hotCommentDTO = topCommentDTOS.get(hot_idx);
            commentShowDTO.setHotComment(hotCommentDTO);
            // 移掉主评论的热评
            topCommentDTOS.remove(hot_idx);
        }
        // 排序
        topCommentDTOS.sort(TopCommentDTO::compareTo);
        commentShowDTO.setComments(topCommentDTOS);
        return commentShowDTO;
    }

    /**
     * 根据游标查询评论数据
     * @param articleId 文章id(仅用于查找顶级评论,因为所有顶级评论的父评论都是统一的0,不加文章id那么无法区分)
     * @param parentId  需要展开的评论id
     * @param cursor    展开的最后一条时间
     * @param cursorSeq 展开的最后一条的评论id
     * @param limit     获取数量
     * @return 游标评论体
     */
    public CommentCursorVO getCursorComments(Long articleId,Long parentId, LocalDateTime cursor, Long cursorSeq, Integer limit) {
        // 为空就设置为最大值,相当于没有条件(这个代码应该不会用到1000年后吧?)
        cursor = (cursor == null ? LocalDateTime.of(2970, 1, 1, 0, 0):cursor);

        // 先查询本地缓存
        CommentCursorVO commentCursorVO = commentCursorCache.get(articleId,parentId,cursor,limit);

        // miss,尝试从Redis获取,否则直接返回
        if(commentCursorVO==null){
            commentCursorVO = commentCursorRedis.getComment(articleId,parentId,cursor,limit);
        }
        else{
            return commentHelper.cursorCommentUpdAttr(commentCursorVO);
        }

        // 尝试从数据库获取
        if(commentCursorVO==null){
            // 查询数据库(游标分页)
            Long userId = ReqInfoContext.getReqInfo().getUserId(); // 用于获取当前用户和评论的状态
            List<CommentItemDTO> comments = commentMapper.listByArticleIdAndCommentId(articleId,userId,parentId,cursor,cursorSeq,limit+1);
            boolean hasMore = true;
            if(comments.size()<=limit){
                hasMore=false;
            }
            else{
                comments.remove(comments.size() - 1); // 删除最后一个元素
            }
            for(CommentItemDTO comment:comments){
                // 如果是已删除那么需要标记内容为特殊标记
                if(comment.getDeleted()){
                    comment.setCommentContent("该评论已被删除"); // 删除评论
                }
            }
            commentCursorVO = CommentCursorVO.builder()
                    .comments(comments)
                    .hasMore(hasMore).build();
            commentCursorRedis.setComment(articleId,parentId,cursor,limit,commentCursorVO); // redis
        }
        // 缓存结果
        commentCursorCache.put(articleId,parentId,cursor,limit,commentCursorVO); // caffeine
        return commentHelper.cursorCommentUpdAttr(commentCursorVO); // 更新状态并返回(从redis读点赞、点踩数据)
    }

    /**
     * 获得评论祖先序列
     * @param commentId 评论id
     * @return
     */
    public List<Long> getCommentParentList(Long commentId) {
        return commentMapper.getAllParentIdByCommentId(commentId);
    }

    /**
     * 获得一条评论的信息(游标评论单元),这个主要在评论置顶用到
     * @param commentId
     * @return
     */
    public CommentItemDTO getTopComment(Long commentId) {
        CommentItemDTO commentItemDTO = commentMapper.getCommentCursorItem(commentId);
        commentItemDTO.setReplyCnt(commentCountRedis.getTotalCount(commentId,COL_REPLY)); // 补充回复信息
        return commentHelper.commentItemUpdAttr(commentItemDTO); // 添加点赞数据传回去
    }

    /**
     * 处理对评论的操作(举报/点赞/点踩/删除)
     * @param commentId
     * @param operationType
     */
    public void operateComment(Long commentId, Integer operationType) {
        if(operationType==null)return;
        if(operationType.equals(REPORT.getCode())) { // 举报
            ;
        }
        if(operationType.equals(DELETE.getCode())){ // 删除
            commentMapper.deleteByCommentId(commentId);
        }
        else{ // 点赞/点踩
            Long userId = ReqInfoContext.getReqInfo().getUserId();
            // 定义通知消息相关(只需要记录点赞不记录点踩)
            Long recvUserId =  commentMapper.getUserIdbyCommentId(commentId); // 评论作者
            if(operationType.equals(LIKE.getCode())){
                commentCountRedis.incrTotalCount(commentId, COL_LIKE,1);
                commentInteractionRedis.setSingleStatus(userId,commentId,COL_LIKE,LIKE.getDbStatCode());
                if(recvUserId!=userId){ // 自己点赞不考虑
                    // 消息内容(只考虑点赞)
                    String content = commentMapper.getContentByCommentId(commentId);
                    TruncateUtil.Result truncatedTxt = TruncateUtil.truncate(content);
                    NoticeContentAggDTO event = NoticeContentAggDTO.builder()
                            .userId(recvUserId)
                            .operateUserId(userId)
                            .targetType(NoticeTargetTypeEnum.COMMENT.getType())
                            .targetId(commentId)
                            .relatedInfo(truncatedTxt.getText())
                            .readFlag(false)
                            .truncated(truncatedTxt.isTruncated())
                            .type(NoticeTypeEnum.LIKE.getType())
                            .status(true)
                            .createTime(LocalDateTime.now())
                            .build();
                    noticeContentRedis.aggregate(event);
                    // 未读消息计数
                    noticeCountRedis.incrTotalCount(recvUserId,NoticeTypeEnum.COL_LIKE,1);// 点赞未读+1
                    noticeCountRedis.aggregate(recvUserId); // 点赞需要更新未读数,其他都不更新
                }
                commentCountRedis.aggregate(new OperationEvent(commentId, LIKE.getCode()));
            }
            else if(operationType.equals(DISLIKE.getCode())){
                commentCountRedis.incrTotalCount(commentId, COL_DISLIKE,1);
                commentInteractionRedis.setSingleStatus(userId,commentId, COL_DISLIKE,DISLIKE.getDbStatCode());
                commentCountRedis.aggregate(new OperationEvent(commentId, DISLIKE.getCode()));
            }
            else if(operationType.equals(CANCEL_DISLIKE.getCode())){
                commentCountRedis.incrTotalCount(commentId, COL_DISLIKE,-1);
                commentInteractionRedis.setSingleStatus(userId,commentId, COL_DISLIKE,CANCEL_DISLIKE.getDbStatCode());
                commentCountRedis.aggregate(new OperationEvent(commentId, CANCEL_DISLIKE.getCode()));
            }
            else if(operationType.equals(CANCEL_LIKE.getCode())){
                commentCountRedis.incrTotalCount(commentId, COL_LIKE,-1);
                commentInteractionRedis.setSingleStatus(userId,commentId, COL_LIKE,CANCEL_LIKE.getDbStatCode());
                commentCountRedis.aggregate(new OperationEvent(commentId, CANCEL_LIKE.getCode()));
            }
            else{
                ;
            }
            // 评论相关聚合(写在后面防止redis更新值前就执行聚合)
            commentInteractionRedis.aggregate(userId,commentId);
        }
    }
}