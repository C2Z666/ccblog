package com.ccblog.service.article.helper;

import com.ccblog.concurrentCache.CategoryCache;
import com.ccblog.concurrentCache.TagCache;
import com.ccblog.constant.RedisPrefixConstant;
import com.ccblog.context.ReqInfoContext;
import com.ccblog.disruptor.readEvent.ReadEventQueue;
import com.ccblog.dto.article.ArticleCountDTO;
import com.ccblog.dto.article.ArticleDTO;
import com.ccblog.dto.article.CategoryDTO;
import com.ccblog.dto.article.TagDTO;
import com.ccblog.entity.article.Category;
import com.ccblog.entity.article.Tag;
import com.ccblog.enumeration.YesOrNoEnum;
import com.ccblog.enumeration.user.FieldInfoEnum;
import com.ccblog.enumeration.user.UserOperateTypeEnum;
import com.ccblog.event.OperationEvent;
import com.ccblog.redis.article.ArticleCountRedis;
import com.ccblog.redis.article.ArticleInteractionRedis;
import com.ccblog.redis.article.ArticleTagRedis;
import com.ccblog.redis.user.UserCountRedis;
import com.ccblog.redis.user.UserInfoRedis;
import com.ccblog.utils.RedisUtil;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

import static com.ccblog.constant.RedisPrefixConstant.*;
import static com.ccblog.enumeration.article.ArticleOperateFieldEnum.COL_COLLECT;
import static com.ccblog.enumeration.article.ArticleOperateFieldEnum.COL_COMMENT;
import static com.ccblog.enumeration.article.ArticleOperateFieldEnum.COL_READ;
import static com.ccblog.enumeration.article.ArticleOperateTypeEnum.READ;
import static com.ccblog.enumeration.comment.CommentOperateFieldEnum.COL_LIKE;
import static com.ccblog.enumeration.user.FieldInfoEnum.COL_PHOTO;
import static com.ccblog.enumeration.user.FieldInfoEnum.COL_USER_NAME;

/**
 * 一些实现类
 * @author czc
 * @date 2025/11/13
 */
@Slf4j
@Component
public class ArticleHelper {
    @Autowired
    private ArticleCountRedis articleCountRedis;

    @Autowired
    private UserCountRedis userCountRedis;

    @Autowired
    private CategoryCache categoryCache;

    @Autowired
    private TagCache tagCache;

    @Autowired
    private ArticleTagRedis articleTagRedis;

    @Autowired
    private UserInfoRedis userInfoRedis;

    @Autowired
    private ArticleInteractionRedis articleInteractionRedis;

    @Autowired
    private ReadEventQueue readEventQueue;


    /**
     * 批量给文章数据添加额外信息
     * 测试10用户越需要20ms(10000文章+1000用户)
     * @param articleDTOS
     * @param addFix      是否更新固定信息(一般固定信息在缓存存了,命中不更新)
     */
    public void addAttrBatch(List<ArticleDTO> articleDTOS, boolean addFix){
        Set<Long> userIdSet = new HashSet<>();
        Set<Long> articleIdSet = new HashSet<>();
        Set<Tuple2<Long,Integer>> articleIdVersionSet = new HashSet<>();
        for(ArticleDTO dto:articleDTOS){
            userIdSet.add(dto.getAuthor());
            articleIdSet.add(dto.getArticleId());
            articleIdVersionSet.add(Tuple.of(dto.getArticleId(),dto.getVersion()));
        }
        Map<Long, Map<String, String>> infos = null;
        if(addFix){
            infos = userInfoRedis.getInfosBatch(userIdSet, List.of(COL_USER_NAME, FieldInfoEnum.COL_PHOTO));
        }
        Map<Long, Map<String, String>> articleInfos =
                articleCountRedis.getTotalCountsBatch(articleIdSet,
                        List.of(COL_LIKE, COL_COMMENT, COL_READ, COL_COLLECT));
        for(ArticleDTO dto:articleDTOS){
            // 添加文章信息
            Map<String, String> articleInfo = articleInfos.get(dto.getArticleId());
            ArticleCountDTO articleCountDTO = ArticleCountDTO.builder()
                    .readCnt(Integer.parseInt(Optional.ofNullable(articleInfo.get(COL_READ)).orElse("0")))
                    .likeCnt(Integer.parseInt(Optional.ofNullable(articleInfo.get(COL_LIKE)).orElse("0")))
                    .commentCnt(Integer.parseInt(Optional.ofNullable(articleInfo.get(COL_COMMENT)).orElse("0")))
                    .collectCnt(Integer.parseInt(Optional.ofNullable(articleInfo.get(COL_COLLECT)).orElse("0")))
                    .build();
            dto.setCount(articleCountDTO); // 加入计数信息
            if(addFix){
                if(infos!=null){ // 防止为空
                    // 添加用户信息
                    Long authorId = dto.getAuthor();
                    dto.setAuthorName(infos.get(authorId).get(COL_USER_NAME));
                    dto.setAuthorAvatar(infos.get(authorId).get(COL_PHOTO));
                }
                // 理论上是批量拿最好,不过concurrentHashMap应该是很快的
                // 文章类别
                dto.setCategory(getCategoryDTO(dto.getCategoryId()));
                // 文章标签
                dto.setTags(getTagDTO(dto.getArticleId()));
            }
        }
    }

    /**
     * 单独给文章数据添加额外信息
     * 经常变动的信息(状态、计数)
     * @param articleDTO 文章信息
     * @param addFix     是否更新固定信息
     */
    public void addAttr(ArticleDTO articleDTO, boolean addFix){
        // 可以考虑加一个二级缓存固定间隔刷新查数据，然后添加的时候直接拿就可以
        articleDTO.setCount(articleCount(articleDTO.getArticleId())); // 加入计数信息
        // 用户交互
        if(ReqInfoContext.getReqInfo().getUser()!=null){
            Long userId = ReqInfoContext.getReqInfo().getUser().getUserId();
            Map<String, Integer> totalStatus =
                    articleInteractionRedis.getTotalStatus(userId, articleDTO.getArticleId(),
                            List.of(COL_LIKE, COL_COMMENT, COL_COLLECT));
            articleDTO.setLiked(totalStatus.get(COL_LIKE) == YesOrNoEnum.YES.getCode());
            articleDTO.setCommented(totalStatus.get(COL_COMMENT)== YesOrNoEnum.YES.getCode());
            articleDTO.setCollected(totalStatus.get(COL_COLLECT)== YesOrNoEnum.YES.getCode());
        }

        if(addFix){
            // 作者信息
            Long authorId = articleDTO.getAuthor();
            Map<String, String> info =
                    userInfoRedis.getInfos(authorId, List.of(COL_USER_NAME, FieldInfoEnum.COL_PHOTO));
            articleDTO.setAuthorName(info.get(COL_USER_NAME));
            articleDTO.setAuthorAvatar(info.get(COL_PHOTO));
            // 文章类别
            articleDTO.setCategory(getCategoryDTO(articleDTO.getCategoryId()));
            // 文章标签
            articleDTO.setTags(getTagDTO(articleDTO.getArticleId()));
        }
    }


    /**
     * 读取redis的数据给文章计数
     * 这个函数可有可无,就是增加可读性,没有简化原本的代码
     */
    private ArticleCountDTO articleCount(Long articleId)  {
        ArticleCountDTO articleCountDTO = new ArticleCountDTO();
        Map<String, Integer> totalCounts =
                articleCountRedis.getTotalCounts(articleId, List.of(COL_LIKE, COL_COMMENT, COL_READ, COL_COLLECT));
        articleCountDTO.setLikeCnt(totalCounts.get(COL_LIKE));
        articleCountDTO.setCommentCnt(totalCounts.get(COL_COMMENT));
        articleCountDTO.setReadCnt(totalCounts.get(COL_READ));
        articleCountDTO.setCollectCnt(totalCounts.get(COL_COLLECT));
        return articleCountDTO;
    }

    /**
     * 根据类别获得文章信息
     * @param categoryId
     * @return
     */
    public CategoryDTO getCategoryDTO(Long categoryId){
        Category category = categoryCache.getCategory(categoryId);
        return CategoryDTO.builder()
                .categoryId(categoryId)
                .category(category.getCategoryName())
                .rank(category.getRank())
                .status(category.getStatus()).build();
    }

    /**
     * 根据tagIds获得TagDTO
     * @param articleId
     * @return
     */
    public List<TagDTO> getTagDTO(Long articleId){
        List<Long> tagIds = articleTagRedis.getTag(articleId).getTagIds();
        List<TagDTO> tagDTOS = new ArrayList<>();
        if(!tagIds.isEmpty()){
            Map<Long, Tag> tagMap = tagCache.getTagBatch(new HashSet<>(tagIds));
            for(Long tagId:tagIds){
                Tag tag = tagMap.get(tagId);
                TagDTO dto = new TagDTO();
                dto.setTag(tag.getTagName());
                dto.setStatus(tag.getStatus());
                dto.setTagId(tagId);
                tagDTOS.add(dto);
            }
        }
        return tagDTOS;
    }

    /**
     * 用户详情聚合事件
     * 内存聚合,然后批量分发批量聚合
     * @param articleId
     * @param userId
     * @param authorId
     */
    @Async(value = "eventExecutor")
    public void asyncArticleAggregateBatch(Long articleId, Long userId, Long authorId){
        if(userId!=null){ // 登录用户才存这个用户相关的信息
            // 先存到内存然后批量分发聚合
            readEventQueue.publish(userId, authorId, articleId);
        }
    }
}