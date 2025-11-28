package com.ccblog.service.user.Impl;

import com.ccblog.dto.user.UserSaveInfoDTO;
import com.ccblog.dto.user.YearArticleDTO;
import com.ccblog.entity.user.User;
import com.ccblog.entity.user.UserInfo;
import com.ccblog.enumeration.StatusEnum;
import com.ccblog.enumeration.user.UserOperateFieldEnum;
import com.ccblog.exception.ExceptionUtil;
import com.ccblog.localCache.user.HotUserCache;
import com.ccblog.localCache.user.UserInfoCache;
import com.ccblog.mapper.article.ArticleMapper;
import com.ccblog.mapper.user.UserCountMapper;
import com.ccblog.mapper.user.UserMapper;
import com.ccblog.redis.user.UserCountRedis;
import com.ccblog.redis.user.UserInfoRedis;
import com.ccblog.service.user.UserRelationService;
import com.ccblog.service.user.UserService;
import com.ccblog.service.user.helper.UserSessionHelper;
import com.ccblog.utils.NumUtil;
import com.ccblog.vo.user.SimpleUserInfoVO;
import com.ccblog.vo.user.UserBaseInfoVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static com.ccblog.constant.UserConstant.UNKNOW_USER;
import static com.ccblog.constant.UserConstant.UNKNOW_USER_PHOTO;
import static com.ccblog.enumeration.user.FieldInfoEnum.COL_PHOTO;
import static com.ccblog.enumeration.user.FieldInfoEnum.COL_USER_NAME;
import static com.ccblog.enumeration.user.UserOperateFieldEnum.*;

/**
 * 用户服务
 * @author czc
 * @date 2025-09-25
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserSessionHelper userSessionHelper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private UserRelationService userRelationService;

    @Autowired
    private UserCountRedis userCountRedis;

    @Autowired
    private UserInfoCache userInfoCache;

    @Autowired
    private UserInfoRedis userInfoRedis;

    @Autowired
    private UserCountMapper userCountMapper;

    @Autowired
    private HotUserCache hotUserCache;



    /**
     * 根据用户id获取主页相关信息(文章详情也需要)
     * @param userId
     * @return
     */
    public UserBaseInfoVo getUserHomeInfo(Long userId) throws JsonProcessingException {
        UserBaseInfoVo userBaseInfoVo = userInfoCache.get(userId);
        if(userBaseInfoVo!=null) {
            return userBaseInfoVo;
        }
        userBaseInfoVo = userMapper.getQueryInfo(userId);
        Integer infoFilled = (StringUtils.isBlank(userBaseInfoVo.getCompany())?0:1)+
                              (StringUtils.isBlank(userBaseInfoVo.getCompany())?0:1)+
                              (StringUtils.isBlank(userBaseInfoVo.getCompany())?0:1);
        userBaseInfoVo.setInfoPercent((int) Math.round(infoFilled.doubleValue() / 3)*100);
        // 加入天数
        int joinDayCount = (int) ((System.currentTimeMillis() - userBaseInfoVo.getCreateTime()
                .getTime()) / (1000 * 3600 * 24));
        userBaseInfoVo.setJoinDayCount(Math.max(1, joinDayCount));
        // 每年创作数量
        List<YearArticleDTO> yearArticleDTOList = articleMapper.listYearArticle(userId);
        userBaseInfoVo.setYearArticleList(yearArticleDTOList);
        // 取缓存数据
        Map<String, Integer> allTotalCount = userCountRedis.getTotalCounts(userId, List.of(UserOperateFieldEnum.COLS));
        userBaseInfoVo.setFansCount(allTotalCount.get(COL_FAN));
        userBaseInfoVo.setFollowCount(allTotalCount.get(COL_FOLLOW));
        userBaseInfoVo.setCommentCount(allTotalCount.get(COL_COMMENT));
        userBaseInfoVo.setLikeCount(allTotalCount.get(COL_LIKE));
        userBaseInfoVo.setCollectCount(allTotalCount.get(COL_COLLECT));
        userBaseInfoVo.setReadCount(allTotalCount.get(COL_READ));
        userInfoCache.put(userId,userBaseInfoVo); // 写入caffeine
        return userBaseInfoVo;
    }

    /**
     * 更新用户详细信息
     * @param userSaveInfoDTO
     */
    @Transactional
    public void saveUserInfo(UserSaveInfoDTO userSaveInfoDTO) {
        if(userSaveInfoDTO.getUserId()==null) return;
        String userName = userMapper.getIdByUserName(userSaveInfoDTO.getUserName(),userSaveInfoDTO.getUserId());
        if(userName!=null){
            throw ExceptionUtil.of(StatusEnum.USER_EXISTS,userName);
        }
        User user = new User();
        user.setId(userSaveInfoDTO.getUserId()); // 设置用户名
        BeanUtils.copyProperties(userSaveInfoDTO,user);
        userMapper.updateUserById(user); // 更新user表
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userSaveInfoDTO,userInfo);
        userMapper.updateUserInfoById(userInfo);
        // 失效对应缓存
        userInfoRedis.removeInfo(userInfo.getUserId());
        userInfoCache.evict(userInfo.getUserId());
    }

    /**
     * 获取热门用户,目前按照粉丝(后续可能改)
     * 先读缓存,1h刷新一次
     * @param limit 数量
     * @return
     */
    public List<SimpleUserInfoVO> getHotUsersByFanCnt(Integer limit) {
        List<SimpleUserInfoVO> result = hotUserCache.get(limit);
        if(result != null){
            return result;
        }
        List<Long> userIds = userCountMapper.getUserOrderByFan(limit);
        Map<Long, Map<String, String>> infoMap =
                        userInfoRedis.getInfosBatch(new HashSet<>(userIds), List.of(COL_FAN,COL_PHOTO, COL_USER_NAME));
        result = new ArrayList<>();
        for(Long userId:userIds){
            Map<String,String> info = infoMap.get(userId);
            result.add(SimpleUserInfoVO.builder()
                    .userId(userId)
                    .fansCount(NumUtil.null2Zero(info.getOrDefault(COL_FAN,"0")))
                    .avatar(info.getOrDefault(COL_PHOTO,UNKNOW_USER_PHOTO))
                    .userName(info.getOrDefault(COL_USER_NAME,UNKNOW_USER)).build());
        }
        hotUserCache.put(result,limit); // 放缓存
        return result;
    }
}