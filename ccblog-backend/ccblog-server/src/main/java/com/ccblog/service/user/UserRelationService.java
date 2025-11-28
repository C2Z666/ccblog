package com.ccblog.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccblog.dto.user.BaseUserInfoDTO;
import com.ccblog.vo.user.FollowUserInfoVO;
import com.ccblog.dto.user.UserRelationItemDTO;

import java.util.List;

/**
 * @author czc
 * @date 2025-09-30
 */
public interface UserRelationService {
    /**
     * 保存一条关注信息
     * @param userRelationItemDTO
     */
    void saveUserRelation(UserRelationItemDTO userRelationItemDTO);


    /**
     * 获取用户关注列表
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     */
    IPage<FollowUserInfoVO> getPageUserFollowList(Long userId, int currentPage, int pageSize);

    /**
     * 获取用户粉丝列表
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     */
    IPage<FollowUserInfoVO> getPageUserFanList(Long userId, int currentPage, int pageSize);

    /**
     * 获取关注列表
     */
    List<Long> getFollowIdsByUserId(Long userId);
}