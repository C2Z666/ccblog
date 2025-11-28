package com.ccblog.service.user.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccblog.constant.RedisPrefixConstant;
import com.ccblog.context.ReqInfoContext;
import com.ccblog.dto.notice.NoticeContentAggDTO;
import com.ccblog.enumeration.YesOrNoEnum;
import com.ccblog.enumeration.notice.NoticeTargetTypeEnum;
import com.ccblog.enumeration.notice.NoticeTypeEnum;
import com.ccblog.enumeration.user.FieldInfoEnum;
import com.ccblog.enumeration.user.UserOperateFieldEnum;
import com.ccblog.enumeration.user.UserOperateTypeEnum;
import com.ccblog.event.OperationEvent;
import com.ccblog.localCache.user.UserRelationPageCache;
import com.ccblog.mapper.user.UserMapper;
import com.ccblog.redis.notice.NoticeContentRedis;
import com.ccblog.redis.notice.NoticeCountRedis;
import com.ccblog.redis.user.UserCountRedis;
import com.ccblog.redis.user.UserFollowRedis;
import com.ccblog.redis.user.UserInfoRedis;
import com.ccblog.result.PageResult;
import com.ccblog.utils.PageUtil;
import com.ccblog.vo.user.FollowUserInfoVO;
import com.ccblog.dto.user.UserRelationItemDTO;
import com.ccblog.mapper.user.UserRelationMapper;
import com.ccblog.service.user.UserRelationService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.Page;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author czc
 * @date 2025-09-30
 */
@Service
public class UserRelationServiceImpl implements UserRelationService {
    @Autowired
    private UserRelationMapper userRelationMapper;

    @Autowired
    private UserCountRedis userCountRedis;

    @Autowired
    private UserFollowRedis userFollowRedis;

    @Autowired
    private UserInfoRedis userInfoRedis;

    @Autowired
    private UserRelationPageCache userRelationPageCache;

    @Autowired
    private NoticeCountRedis noticeCountRedis;

    @Autowired
    private NoticeContentRedis noticeContentRedis;

    @Autowired
    private UserMapper userMapper;

    /**
     * 保存一条关注关系
     * @param userRelationItemDTO
     */
    public void saveUserRelation(UserRelationItemDTO userRelationItemDTO) {
        // 插入/更新一个关注消息
        Long selfUserId = ReqInfoContext.getReqInfo().getUserId(); // 当前用户
        Long followUserId =  userRelationItemDTO.getFollowId(); // 操作用户
        boolean follow = userRelationItemDTO.getFollowed()==Boolean.TRUE; // true表示关注
        // 用户计数
        if(follow){ // 关注
            userCountRedis.incrTotalCount(followUserId, UserOperateFieldEnum.COL_FAN,1); // 粉丝+1
            userCountRedis.incrTotalCount(selfUserId,UserOperateFieldEnum.COL_FOLLOW,1); // 关注+1
            userCountRedis.aggregate(new OperationEvent(selfUserId, UserOperateTypeEnum.FOLLOW.getCode()));
            userCountRedis.aggregate(new OperationEvent(followUserId,UserOperateTypeEnum.FAN.getCode())); // 两个人的计数都需要更新
        }
        else{
            userCountRedis.incrTotalCount(followUserId, UserOperateFieldEnum.COL_FAN,-1); // 粉丝-1
            userCountRedis.incrTotalCount(selfUserId,UserOperateFieldEnum.COL_FOLLOW,-1); // 关注-1
            userCountRedis.aggregate(new OperationEvent(selfUserId, UserOperateTypeEnum.CANCEL_FOLLOW.getCode()));
            userCountRedis.aggregate(new OperationEvent(followUserId,UserOperateTypeEnum.CANCEL_FAN.getCode())); // 两个人的计数都需要更新
        }
        // 用户关系落库
        if(follow){ // 关注
            Map<String, String> userTimeMap = Collections.singletonMap(
                    String.valueOf(selfUserId),
                    String.valueOf(Instant.now().getEpochSecond()));
            userFollowRedis.addFan(followUserId, userTimeMap); // 粉丝信息
            userTimeMap = Collections.singletonMap(
                    String.valueOf(followUserId),
                    String.valueOf(Instant.now().getEpochSecond()));
            userFollowRedis.addFollow(selfUserId,userTimeMap); // 关注信息
            // 聚合
            userFollowRedis.aggregate(selfUserId,followUserId, YesOrNoEnum.YES.getCode());
            userFollowRedis.aggregateFanIncrease(followUserId); // 该用户粉丝数增加,那么需要加入截断检查清单(防止粉丝数过多占用过多内存)
        }
        else{ // 取关
            // 删除被关注者的粉丝信息和关注者的关注信息
            userFollowRedis.delFan(followUserId,selfUserId); // 从followId的粉丝列表删除selfUserId
            userFollowRedis.delFollow(selfUserId,followUserId); // 从selfUserId的关注列表移除followUserId
            // 聚合
            userFollowRedis.aggregate(selfUserId,followUserId, YesOrNoEnum.NO.getCode());
        }
        // 关注通知
        if(follow){
            String userName = userMapper.getUserNameByUserId(selfUserId);
            NoticeContentAggDTO event = NoticeContentAggDTO.builder()
                    .userId(followUserId)
                    .operateUserId(selfUserId)
                    .targetType(NoticeTargetTypeEnum.USER.getType())
                    .targetId(selfUserId)
                    .type(NoticeTypeEnum.FOLLOW.getType())
                    .relatedInfo(userName)
                    .readFlag(false)
                    .truncated(false)
                    .status(true)
                    .createTime(LocalDateTime.now())
                    .build();
            noticeContentRedis.aggregate(event);
            // 计数
            noticeCountRedis.incrTotalCount(followUserId,NoticeTypeEnum.COL_FOLLOW,1); // 关注消息
            noticeCountRedis.aggregate(followUserId);
        }
    }

    /**
     * 分页 获取用户关注列表
     * @param userId 用户名
     * @param currentPage  页码
     * @param pageSize 数量
     * @return
     */
    public IPage<FollowUserInfoVO> getPageUserFollowList(Long userId, int currentPage, int pageSize) {
        IPage<FollowUserInfoVO> followUserInfoVOIPage;
        // 先查询本地缓存(移除,用本地缓存导致滞后,更新很频繁,清空反而影响效率,后续可能考虑缓存第一页)
//        IPage<FollowUserInfoVO> followUserInfoVOIPage =
//                userRelationPageCache.get(userId,UserRelationPageCache.Type.FOLLOW.val(),currentPage,pageSize);
//        if(followUserInfoVOIPage!=null) return followUserInfoVOIPage; // 命中直接返回
        // 查询redis(redis不存在的查数据库)
        Integer followCnt = userCountRedis.getTotalCount(userId,UserOperateFieldEnum.COL_FOLLOW); // 查询用户粉丝量并修正前端请求值
        if(followCnt>0){ // 关注数大于0那么需要处理
            currentPage = Math.min(currentPage,followCnt/pageSize+1); // 页码最多是总数/每页数量+1
            // 查询redis(一定命中,因为会查询数据库),总的思路是先拿到id然后拿到对应信息
            List<Long> followIdList = userFollowRedis.getFollowList(userId, currentPage, pageSize);
            // 查询用户信息
            Map<Long,Map<String, String>> infoMap  = userInfoRedis.getInfosBatch(new HashSet<>(followIdList),
                    List.of(FieldInfoEnum.COL_USER_NAME, FieldInfoEnum.COL_PHOTO)); // 指定需要的id和域
            // 组装为需要的结果
            List<FollowUserInfoVO> followUserInfoVOS = infoMap2InfoVO(followIdList,infoMap);
            followUserInfoVOIPage = PageUtil.toIPage(
                    new PageResult<FollowUserInfoVO>(followCnt,followUserInfoVOS),currentPage,pageSize);
        }
        else{ // 粉丝为0,返回空列表
            followUserInfoVOIPage=PageUtil.toIPage(
                new PageResult<FollowUserInfoVO>(followCnt,new ArrayList<>()),currentPage,pageSize);
        }
        // 写本地缓存
//        userRelationPageCache.put(userId,UserRelationPageCache.Type.FOLLOW.val(),currentPage,pageSize,followUserInfoVOIPage);
        return followUserInfoVOIPage;
    }

    /**
     * 获取粉丝列表
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     */
    public IPage<FollowUserInfoVO> getPageUserFanList(Long userId, int currentPage, int pageSize) {
        IPage<FollowUserInfoVO> fanUserInfoVOIPage;
        // 先查询本地缓存
//        IPage<FollowUserInfoVO> fanUserInfoVOIPage =
//                    userRelationPageCache.get(userId,UserRelationPageCache.Type.FAN.val(),currentPage,pageSize);
//        if(fanUserInfoVOIPage!=null) return fanUserInfoVOIPage; // 命中直接返回
        // 查询redis(超出redis范围和redis未命中的查数据库)
        Integer fanCnt = userCountRedis.getTotalCount(userId,UserOperateFieldEnum.COL_FAN); // 查询用户粉丝量并修正前端请求值
        if(fanCnt>0){ // 粉丝数大于0那么需要处理
            currentPage = Math.min(currentPage,fanCnt/pageSize+1); // 页码最多是总数/每页数量+1
            // 查询redis(只要在存储范围内一定命中,因为会查询数据库),总的思路是先拿到id然后拿到对应信息
            List<Long> fanIdList = userFollowRedis.getFanList(userId, currentPage, pageSize);
            if(fanIdList!=null){ // redis可获得数据
                Map<Long,Map<String, String>> userInfoMap = userInfoRedis.getInfosBatch(new HashSet<>(fanIdList),
                        List.of(FieldInfoEnum.COL_USER_NAME, FieldInfoEnum.COL_PHOTO)); // 指定需要的id和域
                // 组装为需要的结果
                List<FollowUserInfoVO> followUserInfoVOS = infoMap2InfoVO(fanIdList,userInfoMap);
                fanUserInfoVOIPage = PageUtil.toIPage(
                        new PageResult<FollowUserInfoVO>(fanCnt,followUserInfoVOS),currentPage,pageSize);
            }
            else{ // 超出redis存储范围,需要查询数据库(简单起见直接一次性全部拿到信息了)
                PageHelper.startPage(currentPage,pageSize);
                Page<FollowUserInfoVO> page = userRelationMapper.listFanUser(userId);
                fanUserInfoVOIPage = PageUtil.toIPage(
                        new PageResult<FollowUserInfoVO>(page.getTotal(),page.getResult()),currentPage,pageSize);
            }
        }
        else{ // 粉丝为0,返回空列表
            fanUserInfoVOIPage=PageUtil.toIPage(
                    new PageResult<FollowUserInfoVO>(fanCnt,new ArrayList<>()),currentPage,pageSize);
        }
        // 写本地缓存
//        userRelationPageCache.put(userId,UserRelationPageCache.Type.FAN.val(),currentPage,pageSize,fanUserInfoVOIPage);
        return fanUserInfoVOIPage;
    }

    /**
     * 获取关注列表
     */
    public List<Long> getFollowIdsByUserId(Long userId) {
        if(userCountRedis.getTotalCount(userId, UserOperateFieldEnum.COL_FOLLOW)>0){
            return userFollowRedis.getAllFollowList(userId);
        }
        return null;
    }

    /**
     * FollowUserInfoVO组装器,组装redis返回的结果
     * @param fanIdList 每个用户的id(和用户信息一一对应)
     * @param userInfoMap 每个用户的信息,包括组合返回结果需要用到的信息
     * @return 组合结果
     */
    private List<FollowUserInfoVO> infoMap2InfoVO(List<Long> fanIdList,Map<Long,Map<String, String>> userInfoMap){
        List<FollowUserInfoVO> followUserInfoVOS = new ArrayList<>();
        for(int i=0;i<fanIdList.size();i++){
            Map<String, String> userInfo = userInfoMap.get(fanIdList.get(i));
            followUserInfoVOS.add(FollowUserInfoVO.builder()
                    .userName(userInfo.get(FieldInfoEnum.COL_USER_NAME))
                    .avatar(userInfo.get(FieldInfoEnum.COL_PHOTO))
                    .userId(fanIdList.get(i)).build());
        }
        return followUserInfoVOS;
    }
}