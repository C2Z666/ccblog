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
     * 根据用户邮箱查询用户
     * @param email
     * @return
     */
    @Select("select * from user where email=#{email}")
    User getUserByEmail(String email);

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