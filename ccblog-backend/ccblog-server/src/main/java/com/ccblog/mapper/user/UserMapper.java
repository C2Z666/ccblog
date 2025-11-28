package com.ccblog.mapper.user;

import com.ccblog.annotation.AutoFill;
import com.ccblog.dto.user.CommonInfoDTO;
import com.ccblog.entity.user.User;
import com.ccblog.entity.user.UserInfo;
import com.ccblog.enumeration.OperationType;
import com.ccblog.vo.user.LoginInfoVO;
import com.ccblog.vo.user.UserBaseInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户信息相关
 * @author czc
 * @date 2025-09-29
 */
@Mapper
public interface UserMapper {
    /**
     * 通过userId获取用户详细信息
     * @param userId
     * @return
     */
    @Select("select * from user_info where user_id=#{userId}")
    UserInfo getByUserId(Long userId);

    /**
     * 更新用户详细信息
     * @param userInfo
     */
    @AutoFill(OperationType.UPDATE)
    void updateUserInfoById(UserInfo userInfo);

    /**
     * 更新用户信息
     * @param user
     */
    @AutoFill(OperationType.UPDATE)
    void updateUserById(User user);

    /**
     * 用户主页不存在redis的需要查询的单个属性
     * @param userId
     * @return
     */
    UserBaseInfoVo getQueryInfo(Long userId);

    /**
     * 根据用户名查询用户名(其实是判断是否存在)
     * @param userName
     * @param selfId    自己id
     * @return
     */
    @Select("SELECT user_name FROM `user` WHERE user_name=#{userName} AND id!=#{selfId}")
    String getIdByUserName(String userName,Long selfId);

    /**
     * 根据用户id获取用户登录需要的信息
     * 实际上查询的内容跟常用信息差不多,但是考虑后续常用信息可能扩展
     * @param userId
     * @return
     */
    @Select("SELECT user_id AS userId, user_name AS userName, photo FROM user_info WHERE user_id=#{userId}")
    LoginInfoVO getLoginInfoByUserId(Long userId);

    /**
     * 批量得到用户常用信息
     */
    List<CommonInfoDTO> getCommInfoByUserIds(List<Long> userIds);

    /**
     * 根据用户id获得用户名
     * @param userId
     * @return
     */
    @Select("SELECT user_name FROM user WHERE id=#{userId}")
    String getUserNameByUserId(Long userId);
}