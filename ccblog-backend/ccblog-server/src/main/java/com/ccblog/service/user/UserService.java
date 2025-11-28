package com.ccblog.service.user;

import com.ccblog.dto.user.UserSaveInfoDTO;
import com.ccblog.vo.user.SimpleUserInfoVO;
import com.ccblog.vo.user.UserBaseInfoVo;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

/**
 * @author czc
 * @date 2025-09-26
 */
public interface UserService {

    /**
     * 根据用户id获取主页相关信息
     * @param userId
     * @return
     */
    UserBaseInfoVo getUserHomeInfo(Long userId) throws JsonProcessingException;

    /**
     * 更新用户详细信息
     * @param userSaveInfoDTO
     */
    void saveUserInfo(UserSaveInfoDTO userSaveInfoDTO);

    /**
     * 获取热门用户,目前按照粉丝(后续可能改)
     * @param limit 数量
     * @return
     */
    List<SimpleUserInfoVO> getHotUsersByFanCnt(Integer limit);
}