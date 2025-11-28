package com.ccblog.service.article.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccblog.constant.NoticeMsgConstant;
import com.ccblog.context.ReqInfoContext;
import com.ccblog.dto.article.*;
import com.ccblog.entity.article.*;
import com.ccblog.enumeration.article.PushStatusEnum;
import com.ccblog.dto.comment.CommentItemDTO;
import com.ccblog.dto.notice.NoticeContentAggDTO;
import com.ccblog.enumeration.article.*;
import com.ccblog.enumeration.notice.NoticeTargetTypeEnum;
import com.ccblog.enumeration.notice.NoticeTypeEnum;
import com.ccblog.enumeration.user.UserOperateFieldEnum;
import com.ccblog.event.OperationEvent;
import com.ccblog.localCache.article.ArticleDetailCache;
import com.ccblog.localCache.article.CateArticlePageCahce;
import com.ccblog.localCache.user.GlobalViewCache;
import com.ccblog.mapper.article.*;
import com.ccblog.mapper.comment.CommentMapper;
import com.ccblog.mapper.user.UserMapper;
import com.ccblog.mapper.user.UserRelationMapper;
import com.ccblog.redis.article.ArticleTagRedis;
import com.ccblog.redis.notice.NoticeContentRedis;
import com.ccblog.redis.notice.NoticeCountRedis;
import com.ccblog.redis.user.UserCountRedis;
import com.ccblog.result.PageResult;
import com.ccblog.service.article.ArticleService;
import com.ccblog.service.article.CategoryService;
import com.ccblog.service.article.converter.ArticleConverter;
import com.ccblog.redis.article.ArticleCountRedis;
import com.ccblog.redis.article.ArticleInteractionRedis;
import com.ccblog.service.article.helper.ArticleHelper;
import com.ccblog.service.comment.CommentService;
import com.ccblog.service.user.UserService;
import com.ccblog.utils.PageUtil;
import com.ccblog.vo.article.ArticleDetailVO;
import com.ccblog.vo.user.UserBaseInfoVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.ccblog.enumeration.article.ArticleOperateFieldEnum.*;
import static com.ccblog.enumeration.article.ArticleOperateTypeEnum.*;
import static com.ccblog.enumeration.article.ArticleOperateTypeEnum.COLLECT;
import static com.ccblog.enumeration.article.ArticleOperateTypeEnum.LIKE;

/**
 * 文章服务
 * @author czc
 * @date 2025-09-27
 */
@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private ArticleConverter articleConverter;

    @Autowired
    private ArticleCountRedis articleCountRedis;

    @Autowired
    private UserCountRedis userCountRedis;

    @Autowired
    private CatgeoryMapper categoryMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleInteractionRedis articleInteractionRedis;

    @Autowired
    private ArticleInteractionMapper articleInteractionMapper;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ArticleTagRedis articleTagRedis;


    @Autowired
    private ArticleCountMapper articleCountMapper;

    @Autowired
    private ArticleDetailCache articleDetailCache;

    @Autowired
    private NoticeContentRedis noticeContentRedis;

    @Autowired
    private NoticeCountRedis noticeCountRedis;

    @Autowired
    private UserRelationMapper userRelationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CateArticlePageCahce cateArticlePageCahce;

    @Autowired
    private GlobalViewCache globalViewCache;

    @Autowired
    private ArticleHelper articleHelper;

    /**
     * 新增文章(也包括编辑过的文章,更新的文章不变覆盖article,追加detail和tag)
     * @param newArticleDTO
     */
    @Transactional
    public void saveArticle(NewArticleDTO newArticleDTO) {
        newArticleDTO.setVersion(newArticleDTO.getVersion()+1); // 在后端更新版本,新文章那么是0+1,旧文章是旧版本+1(可以定义其他规则)
        Long userId = ReqInfoContext.getReqInfo().getUserId(); // 获得用户id
        // todo:这边最好写一个conveter转为article(应该很多设为可以为空,默认有值)
        // 创建文章 article
        Article article = new Article();
        BeanUtils.copyProperties(newArticleDTO, article);
        article.setVersion(newArticleDTO.getVersion());
        article.setId(newArticleDTO.getArticleId());
        article.setPicture(newArticleDTO.getCover());
        article.setUserId(userId); // 设置文章userId
        String articleType = newArticleDTO.getArticleType();
        article.setArticleType(ArticleTypeEnum.valueOf(articleType).getCode()); // 得到文章类型
        if (newArticleDTO.getStatus() != null) {
            article.setStatus(newArticleDTO.getStatus()); // 设置文章状态
        } else {
            switch (newArticleDTO.getActionType()) {
                case "POST": // 发布
                    article.setStatus(PushStatusEnum.ONLINE.getCode()); // 本来是需要转审核,但是没有写管理系统所以跳过
                    break;
                case "SAVE": // 草稿
                    // fixme:草稿是已发布还是未发布? 列为未发布
                    article.setStatus(PushStatusEnum.OFFLINE.getCode());
                    break;
                case "DELETE": // 删除
                    article.setDeleted(1);
                    break;
                default: // 其他类型不管
                    break;
            }
        }
        // 插入文章(文章采用upsert方式)
        articleMapper.upsertArticle(article); // 需要传回id否则下面无法插入其他信息
        Long articleId = article.getId(); // 获得文章id(新插入)

        // 插入文章详情article_detail
        ArticleDetail articleDetail = new ArticleDetail();
        BeanUtils.copyProperties(newArticleDTO, articleDetail);
        articleDetail.setArticleId(articleId);
        articleMapper.saveArticleDetail(articleDetail); // 插入文章详情
        // 插入标签,创建tag和文章映射
        List<ArticleTag> articleTags = new ArrayList<>();
        for(Long tagId:newArticleDTO.getTagIds()){
            articleTags.add(ArticleTag.builder().
                    tagId(tagId).
                    articleId(articleId)
                    .version(newArticleDTO.getVersion()).build());
        }
        articleTagMapper.addByTagIdAndArticleId(articleTags); // 根据用户id和tagId插入数据
        // 这里不进行类似插入评论的初始化: 1.这里可能是更新可能是更新那么不需要初始化  2.新文章的频率远低于新评论,后面查询差不到也会自然初始化
        articleCountRedis.aggregate(new OperationEvent(articleId,NEW.getCode())); // 空操作(初始化)

        // 如果是编辑文章那么失效对应的缓存
        if(newArticleDTO.getArticleId()!=0){
            articleTagRedis.removeTag(articleId); // 标签缓存失效
            articleDetailCache.evict(articleId);  // 文章页失效
            cateArticlePageCahce.removeIfExist(articleId); // 如果在首页展示列表那么失效缓存
            ;  // 首页失效(一般要置顶才失效吧?或者维护一下前三页缓存都有哪些文章)
        }

        // 发表新文章需要给新粉丝推送消息(三天内关注的)
        // 备注:这里要是粉丝量很多会造成redis的冲击(如果有粉丝量很多的应该优化)
        if(newArticleDTO.getArticleId()==0&&newArticleDTO.getActionType().equals("POST")){
            List<Long> recentFanIds = userRelationMapper.listRecentFanIds(userId,3);
            for(Long id:recentFanIds){
                String relatedInfo = String.format(
                        NoticeMsgConstant.FOLLOWEE_PUBLISH_POST,userMapper.getUserNameByUserId(article.getUserId()));
                NoticeContentAggDTO event = NoticeContentAggDTO.builder()
                        .userId(id)
                        .operateUserId(NoticeMsgConstant.ADMIN_USER_ID)
                        .type(NoticeTypeEnum.SYSTEM.getType())
                        .targetId(NoticeMsgConstant.ADMIN_USER_ID)
                        .targetType(NoticeTargetTypeEnum.USER.getType())
                        .relatedInfo(relatedInfo)
                        .readFlag(false)
                        .status(true)
                        .createTime(LocalDateTime.now()).build();
                noticeContentRedis.aggregate(event);
                noticeCountRedis.incrTotalCount(id,NoticeTypeEnum.COL_SYSTEM,1); //私信数+1
                noticeCountRedis.aggregate(id);
            }
        }
    }

    /**
     * 根据类别分页查询文章(主页展示)
     * @param currentPage
     * @param pageSize
     * @param categoryId
     * @return
     */
    public IPage<ArticleDTO> queryPageArticlesByCategory(int currentPage, int pageSize, Long categoryId) {
        IPage<ArticleDTO> result = cateArticlePageCahce.get(categoryId,currentPage,pageSize);
        if(result!=null){
            articleHelper.addAttrBatch(result.getRecords(),false);
            return result;
        }
        PageHelper.startPage(currentPage,pageSize);
        Page<ArticleDTO> page = articleMapper.queryPageArticleByCategory(categoryId);
        articleHelper.addAttrBatch(page.getResult(),true);
        // 转为IPage类型,这是为了跟原来的代码兼容(原来是用的mybatis-plus,本项目不使用)
        result = PageUtil.toIPage(new PageResult<ArticleDTO>(page.getTotal(),page.getResult()),currentPage,pageSize);
        cateArticlePageCahce.put(categoryId,currentPage,pageSize,result);
        return result;
    }

    /**
     * 根据标签分页查询文章
     * @param currentPage
     * @param pageSize
     * @param tagId
     * @return
     */
    public IPage<ArticleDTO> queryPageArticlesByTagId(int currentPage, int pageSize, Long tagId) {
        PageHelper.startPage(currentPage,pageSize);
        Page<ArticleDTO> page = articleMapper.queryPageArticleByTagId(tagId);
        articleHelper.addAttrBatch(page.getResult(),true);
        // 转为IPage类型,这是为了跟原来的代码兼容(原来是用的mybatis-plus,本项目不使用)
        IPage<ArticleDTO> result = PageUtil.toIPage(new PageResult<ArticleDTO>(page.getTotal(),page.getResult()),currentPage,pageSize);
        cateArticlePageCahce.put(tagId,currentPage,pageSize,result);
        return result;
    }

    /**
     * 根据查询条件获取文章
     *
     * @param query       查询条件
     * @param currentPage
     * @param pageSize
     * @return
     */
    public IPage<ArticleDTO> getArticleByQuery(String query, int currentPage, int pageSize) {
        PageHelper.startPage(currentPage,pageSize);
        Page<ArticleDTO> page = articleMapper.queryPageArticleByQuery(query);
        articleHelper.addAttrBatch(page.getResult(),true);
        IPage<ArticleDTO> result = PageUtil.toIPage(new PageResult<ArticleDTO>(page.getTotal(),page.getResult()),currentPage,pageSize);
        return result;
    }

    /**
     * 获得草稿文章信息
     * @return
     */
    public ArticleEditDTO getLatestDraft() {
        ArticleEditDTO editDTO = null;
        // 先查到最后一篇草稿id
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        Long articleId = articleMapper.getLastDraftArticleId(userId);
        if(articleId!=null){
            editDTO = articleMapper.getArticleEdit(articleId);
            // 添加类别
            CategoryDTO categoryDTO = articleHelper.getCategoryDTO(editDTO.getCategoryId());
            editDTO.setCategory(categoryDTO);
            // 添加标签
            editDTO.setTags(articleHelper.getTagDTO(articleId));
        }
        return editDTO;
    }



    /**
     * 查询所有类别id和对应的文章数量
     * @return
     */
    public Map<Long, Long> getArticleNumOfCategory() {
        List<ArticleNumberOfCategoryDTO> categroyNum = categoryMapper.queryArticleNumberByCategory();
        // 转为Map,不会出现重复categoryId问题不用考虑
        Map<Long, Long> idNumMap = categroyNum.stream()
                .collect(Collectors.toMap(
                        ArticleNumberOfCategoryDTO::getCategoryId,
                        ArticleNumberOfCategoryDTO::getArticleNum));

        return idNumMap;
    }

    /**
     * 根据用户id分页查询文章(用户主页)
     * @return
     * @param userId
     * @param currentPage
     * @param pageSize
     */
    public IPage<ArticleDTO> queryPageArticleByUserId(Long userId, int currentPage, int pageSize) {
        PageHelper.startPage(currentPage,pageSize);
        Page<ArticleDTO> page= articleMapper.queryPageArticleByUserId(userId);
        List<ArticleDTO> articleDTOList= page.getResult();
        articleHelper.addAttrBatch(page.getResult(),true);
//        articleDTOList = addAdtionalAttr(articleDTOList);
        // 转为IPage类型,这是为了跟原来的代码兼容(原来是用的mybytis-plus,本项目不使用)
        IPage<ArticleDTO> articleDTOIPage = PageUtil.toIPage(new PageResult<ArticleDTO>(page.getTotal(),articleDTOList),currentPage,pageSize);
        return articleDTOIPage;
    }

    /**
     * 根据文章id获取文章详情(阅读文章)
     * @param articleId 文章id
     * @return 文章详细信息
     */
    public ArticleDetailVO getArticleDetail(Long articleId,Long commentId) throws JsonProcessingException {
        ArticleDetailVO articleDetailVO;
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        // 先查缓存
        if(commentId==-1){ // 如果不需要评论信息才查缓存
            articleDetailVO = articleDetailCache.get(articleId);
            if(articleDetailVO!=null) {
//                articleHelper.addAttr(articleDetailVO.getArticle(),false); // 多了这句话让返回时间翻了7倍...这个单独加载
                articleHelper.asyncArticleAggregateBatch(articleId,userId,articleDetailVO.getArticle().getAuthor());
                return articleDetailVO;
            }
        }
        articleDetailVO = new ArticleDetailVO(); // 重新分配否则是null
        ArticleDTO articleDTO = articleMapper.getArticleDetailByArticleId(articleId);
        if(articleDTO==null){
            return null;
        }
        // 扩充article信息(简单信息,耗时信息可以放到第二次请求)
        articleDetailVO.setArticle(articleDTO);
        Long authorId = articleDTO.getAuthor();
        UserBaseInfoVo userBaseInfoVo = userService.getUserHomeInfo(authorId);
        articleDetailVO.setAuthor(userBaseInfoVo);
        // 评论信息
        // 如果是回复消息,需要目标评论的对应评论祖先序列
        if(commentId!=-1){ // -1表示不需要
            // 获得祖先序列链
            List<Long> commentParents = commentService.getCommentParentList(commentId);
            articleDetailVO.setCommentParents(commentParents);
            CommentItemDTO  topCommentInfo = commentService.getTopComment(commentParents.get(0));
            articleDetailVO.setTopCommentInfo(topCommentInfo);
        }
        articleHelper.asyncArticleAggregateBatch(articleId,userId,authorId);
        if(commentId==-1){ // 跳转评论区请求不缓存会携带评论相关信息(其实可以把评论相关消息抹掉存进去刷新缓存,不过这类请求不多无所谓)
            articleDetailCache.put(articleId,articleDetailVO); // 写入caffeine
        }
        return articleDetailVO;
    }

    /**
     * 获得文章更多数据(用户状态,文章计数)
     *
     * @param articleId
     * @param queryFix   是否需要一些固定数据(冷数据)
     * @param categoryId
     * @param authorId
     * @return
     */
    public ArticleDetailVO getFullInfo(Long articleId, boolean queryFix, Long categoryId, Long authorId) {
        ArticleDetailVO articleDetailVO = new ArticleDetailVO();
        ArticleDTO articleDTO  = new ArticleDTO();
        articleDTO.setArticleId(articleId);
        articleDTO.setAuthor(authorId);
        if(queryFix){
            articleDTO.setCategoryId(categoryId);
        }
        articleDetailVO.setArticle(articleDTO);
        articleHelper.addAttr(articleDetailVO.getArticle(),queryFix);
        return articleDetailVO;
    }

    /**
     * 根据操作代码更新文章点赞/收藏/举报等状态
     *
     * @param articleId
     * @param authorId
     * @param opeatationType 操作代码
     */
    public void operateArticle(Long articleId, Long authorId, Integer opeatationType) {
        if(opeatationType==null) return;
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        if(userId==null) return; // 未登录不处理
        if(opeatationType.equals(REPORT.getCode())) {
            ;
        }
        else{
            boolean noticeFlag = (!Objects.equals(authorId, userId)); // 是否需要更新消息相关内容(自己给自己/取消都不更新)
            int type = NoticeTypeEnum.LIKE.getType(); // 默认点赞
            if(opeatationType.equals(LIKE.getCode())){
                articleCountRedis.incrTotalCount(articleId, UserOperateFieldEnum.COL_LIKE,1); // 文章被点赞数
                userCountRedis.incrTotalCount(authorId,UserOperateFieldEnum.COL_LIKE,1); // 用户被点赞数
                articleInteractionRedis.setStatusSingle(userId,articleId,UserOperateFieldEnum.COL_LIKE,LIKE.getDbStatCode());
                articleCountRedis.aggregate(new OperationEvent(articleId, LIKE.getCode())); // 点赞
                userCountRedis.aggregate(new OperationEvent(authorId, LIKE.getCode()));
            }
            else if(opeatationType.equals(COLLECT.getCode())){
                articleCountRedis.incrTotalCount(articleId,UserOperateFieldEnum.COL_COLLECT,1);
                userCountRedis.incrTotalCount(authorId,UserOperateFieldEnum.COL_COLLECT,1);
                articleInteractionRedis.setStatusSingle(userId,articleId,UserOperateFieldEnum.COL_COLLECT,COLLECT.getDbStatCode());
                articleCountRedis.aggregate(new OperationEvent(articleId, COLLECT.getCode())); // 收藏量
                userCountRedis.aggregate(new OperationEvent(authorId, COLLECT.getCode())); // 收藏量
                type = NoticeTypeEnum.COLLECT.getType();
            }
            else if(opeatationType.equals(CANCEL_COLLECT.getCode())){
                articleCountRedis.incrTotalCount(articleId,UserOperateFieldEnum.COL_COLLECT,-1);
                userCountRedis.incrTotalCount(authorId,UserOperateFieldEnum.COL_COLLECT,-1);
                articleInteractionRedis.setStatusSingle(userId,articleId,UserOperateFieldEnum.COL_COLLECT,CANCEL_COLLECT.getDbStatCode());
                articleCountRedis.aggregate(new OperationEvent(articleId, CANCEL_COLLECT.getCode())); // 取消收藏
                userCountRedis.aggregate(new OperationEvent(authorId, CANCEL_COLLECT.getCode())); // 取消收藏
                noticeFlag = false;
            }
            else if(opeatationType.equals(CANCEL_LIKE.getCode())){
                articleCountRedis.incrTotalCount(articleId,UserOperateFieldEnum.COL_LIKE,-1);
                userCountRedis.incrTotalCount(authorId,UserOperateFieldEnum.COL_LIKE,-1);
                articleInteractionRedis.setStatusSingle(userId,articleId,UserOperateFieldEnum.COL_LIKE,CANCEL_LIKE.getDbStatCode());
                articleCountRedis.aggregate(new OperationEvent(articleId, CANCEL_LIKE.getCode())); // 取消点赞
                userCountRedis.aggregate(new OperationEvent(authorId, CANCEL_LIKE.getCode())); // 取消点赞
                noticeFlag = false;
            }
            else{
                ;
            }
//          列清单,注意要放在后面,否则如果触发更新用的是旧数据(聚合)
            articleInteractionRedis.aggregate(userId,articleId); // 更新交互
            // 处理通知
            if(noticeFlag){
                if(opeatationType.equals(LIKE.getCode())){
                    noticeCountRedis.incrTotalCount(authorId,NoticeTypeEnum.COL_LIKE,1);// 点赞通知
                }
                else if(opeatationType.equals(COLLECT.getCode())){
                    noticeCountRedis.incrTotalCount(authorId, COL_COLLECT,1);// 收藏通知
                }
                // 消息内容(只考虑点赞)
                String title = articleMapper.getTitleByArticleId(articleId);
                NoticeContentAggDTO event = NoticeContentAggDTO.builder()
                        .userId(authorId)
                        .operateUserId(userId)
                        .type(type) // 点赞/收藏
                        .targetType(NoticeTargetTypeEnum.ARTICLE.getType()) // 文章
                        .targetId(articleId)
                        .relatedInfo(title)
                        .readFlag(false)
                        .truncated(false) // 标题的长度不会被截断
                        .status(true)
                        .createTime(LocalDateTime.now())
                        .build();
                noticeContentRedis.aggregate(event);
                // 未读消息计数
                noticeCountRedis.aggregate(authorId);
                globalViewCache.evict(articleId); // 失效视图
            }
        }
    }

    /**
     * 获取文章浏览历史
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     */
    public IPage<ArticleDTO> getReadHistory(Long userId, int currentPage, int pageSize) {
        PageHelper.startPage(currentPage,pageSize);
        Page<ArticleDTO> page= articleMapper.listReadHistoryByUserId(userId);
        // 添加额外信息
        articleHelper.addAttrBatch(page.getResult(),true);
        List<ArticleDTO> articleDTOList= page.getResult();
        // 转为IPage类型,这是为了跟原来的代码兼容(原来是用的mybytis-plus,本项目不使用)
        IPage<ArticleDTO> articleDTOIPage = PageUtil.toIPage(new PageResult<ArticleDTO>(page.getTotal(),articleDTOList),currentPage,pageSize);
        return articleDTOIPage;
    }

    /**
     * 获取用户收藏文章
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     */
    public IPage<ArticleDTO> getStarArticle(Long userId, int currentPage, int pageSize) {

        PageHelper.startPage(currentPage,pageSize);
        Page<ArticleDTO> page= articleMapper.listStarByUserId(userId);
        // 从缓存查询额外信息信息
        articleHelper.addAttrBatch(page.getResult(),true);
        List<ArticleDTO> articleDTOList= page.getResult();
        // 转为IPage类型,这是为了跟原来的代码兼容(原来是用的mybytis-plus,本项目不使用)
        IPage<ArticleDTO> articleDTOIPage = PageUtil.toIPage(new PageResult<ArticleDTO>(page.getTotal(),articleDTOList),currentPage,pageSize);
        return articleDTOIPage;
    }

    /**
     * 删除文章
     * @param articleId 文章id
     */
    public void deleteArticle(Long articleId) {
        articleMapper.deleteByArticleId(articleId);
        // 删除首页缓存
        cateArticlePageCahce.removeIfExist(articleId);
    }

    /**
     * 获取文章信息
     * @param articleId
     * @return 编辑文章相关信息
     */
    public ArticleEditDTO queryArticle(Long articleId) {
        ArticleEditDTO articleEditDTO = articleMapper.getArticleEdit(articleId); // 获取关于文章编辑的信息
        // 添加类别
        CategoryDTO categoryDTO = articleHelper.getCategoryDTO(articleEditDTO.getCategoryId());
        articleEditDTO.setCategory(categoryDTO);
        // 添加标签
        articleEditDTO.setTags(articleHelper.getTagDTO(articleId));
        return articleEditDTO;
    }
}