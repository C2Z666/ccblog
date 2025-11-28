package com.ccblog.mapper.user;

import com.ccblog.entity.user.User;
import com.ccblog.entity.user.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 关于用户登录
 * @author czc
 * @date 2025-09-26
 */
@Mapper
public interface LoginMapper {

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    @Select("select * from user where user_name=#{userName}")
    User getUserByUserName(String userName);

    /**
     * 插入user
     * @param user
     */
    void saveUser(User user);

    /**
     * 插入user_info
     * @param userInfo
     */
    void saveUserInfo(UserInfo userInfo);
}