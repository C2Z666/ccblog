package com.ccblog.service.user.Impl;

import com.ccblog.context.ReqInfoContext;
import com.ccblog.dto.user.BaseUserInfoDTO;
import com.ccblog.dto.user.CommonInfoDTO;
import com.ccblog.dto.user.LoginSuccDTO;
import com.ccblog.dto.user.UserRegisterDTO;
import com.ccblog.entity.user.IpInfo;
import com.ccblog.entity.user.User;
import com.ccblog.entity.user.UserInfo;
import com.ccblog.enumeration.user.LoginTypeEnum;
import com.ccblog.enumeration.StatusEnum;
import com.ccblog.mapper.user.LoginMapper;
import com.ccblog.mapper.user.UserMapper;
import com.ccblog.service.user.LoginService;
import com.ccblog.service.user.helper.UserPwdHelper;
import com.ccblog.service.user.helper.UserRandomGenHelper;
import com.ccblog.service.user.helper.UserSessionHelper;
import com.ccblog.exception.ExceptionUtil;
import com.ccblog.utils.ip.IpUtil;
import com.ccblog.vo.user.LoginInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 处理跟鉴权、账户加密相关服务
 * @author czc
 * @date 2025-09-25
 *
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private UserSessionHelper userSessionHelper;

    @Autowired
    private LoginMapper userLoginMapper;

    @Autowired
    private UserPwdHelper userPwdHelper;

    @Autowired
    private UserMapper userMapper;


    /**
     * 用户密码登录
     * @param username
     * @param password
     * @return
     */
    public LoginInfoVO loginByUserPwd(String username, String password) {
        User user = userLoginMapper.getUserByUserName(username); // 先查询是否有用户
        if (user == null) {
            throw ExceptionUtil.of(StatusEnum.USER_NOT_EXISTS, "userName=" + username);
        }
        if (!userPwdHelper.match(password, user.getPassword())) {
            throw ExceptionUtil.of(StatusEnum.USER_PWD_ERROR);
        }
        Long userId = user.getId();
        // 1. 为了兼容历史数据，对于首次登录成功的用户，初始化ai信息
        // todo: 目前没有考虑ai,所以先注释
//        userAiService.initOrUpdateAiInfo(new UserPwdLoginReq().setUserId(userId).setUsername(username).setPassword(password));
        // 查出用户头像和昵称信息
        LoginInfoVO loginInfoVO = userMapper.getLoginInfoByUserId(userId);

        // 登录成功，返回对应的session
        ReqInfoContext.getReqInfo().setUserId(userId);
        return loginInfoVO;
    }

    /**
     * 注册用户
     * @param userRegisterDTO
     * @return
     */
    @Transactional // 事务原子性
    public LoginInfoVO registerByUserPwd(UserRegisterDTO userRegisterDTO) {
        // 前置校验
        registerPreCheck(userRegisterDTO);
        // 注册用户  1.插入user表  2.插入user_info表
        User user = new User();
        BeanUtils.copyProperties(userRegisterDTO,user);
        user.setPassword(userPwdHelper.encPwd(user.getPassword())); // 密码加密后存储
        user.setLoginType(LoginTypeEnum.USER_PWD.getType());
        userLoginMapper.saveUser(user); // 注意注册之后要把id传回
        UserInfo userInfo = new UserInfo();
        Long userId = user.getId(); // 得到id
        BeanUtils.copyProperties(userRegisterDTO, userInfo);
        userInfo.setUserId(userId);
        userInfo.setPhoto(UserRandomGenHelper.genAvatar()); // 随机生成一张图片
        userLoginMapper.saveUserInfo(userInfo);

        LoginInfoVO loginInfoVO = LoginInfoVO.builder()
                .userId(userId)
                .photo(userInfo.getPhoto())
                .build();
        // 注册，返回对应的session
//        ReqInfoContext.getReqInfo().setUserId(userId);
        return loginInfoVO;
    }

    /**
     * 用户登出
     * @param session
     */
    public void logout(String session) {
        userSessionHelper.removeSession(session); // 移除session,其实就是删除缓存redis里面的
    }

    private void registerPreCheck(UserRegisterDTO userRegisterDTO){
        // 检测是否为空
        if (StringUtils.isBlank(userRegisterDTO.getUserName()) || StringUtils.isBlank(userRegisterDTO.getPassword())) {
            throw ExceptionUtil.of(StatusEnum.USER_NAME_PWD_BLANK);
        }
        // 查询用户是否存在
        User user = userLoginMapper.getUserByUserName(userRegisterDTO.getUserName()); // 先查询是否有用户
        if(user!=null){
            throw ExceptionUtil.of(StatusEnum.USER_EXISTS,userRegisterDTO.getUserName());
        }
    }

    /**
     * 更新用户基础信息
     * @param session 用户session
     * @param clientIp ip
     * @return
     */
    public BaseUserInfoDTO getAndUpdateUserIpInfoBySessionId(String session, String clientIp) {
        if (StringUtils.isBlank(session)) {
            return null;
        }

        Long userId = userSessionHelper.getUserIdBySession(session);
        if (userId == null) {
            return null;
        }

        // 查询用户信息，并更新最后一次使用的ip
        UserInfo userInfo = userMapper.getByUserId(userId);
        if (userInfo == null) {
            throw ExceptionUtil.of(StatusEnum.USER_NOT_EXISTS, "userId=" + userId);
        }

        IpInfo ip = userInfo.getIp();
        if (clientIp != null && !Objects.equals(ip.getLatestIp(), clientIp)) {
            // ip不同，需要更新
            ip.setLatestIp(clientIp);
            ip.setLatestRegion(IpUtil.getLocationByIp(clientIp).toRegionStr());

            if (ip.getFirstIp() == null) {
                ip.setFirstIp(clientIp);
                ip.setFirstRegion(ip.getLatestRegion());
            }
            userMapper.updateUserInfoById(userInfo);
        }
        BaseUserInfoDTO baseUserInfoDTO = new BaseUserInfoDTO();
        BeanUtils.copyProperties(userInfo,baseUserInfoDTO);

        //todo:【未完成】需要完成ai初始化
//        UserAiDO userAiDO = userAiDao.getByUserId(userId);
//        dto = UserConverter.toDTO(user, userAiDO);
        return baseUserInfoDTO;
    }
}