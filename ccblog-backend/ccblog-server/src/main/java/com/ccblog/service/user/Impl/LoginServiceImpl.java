package com.ccblog.service.user.Impl;

import com.ccblog.cfg.MailProps;
import com.ccblog.context.ReqInfoContext;
import com.ccblog.dto.user.BaseUserInfoDTO;
import com.ccblog.dto.user.UserRegisterDTO;
import com.ccblog.entity.user.IpInfo;
import com.ccblog.entity.user.User;
import com.ccblog.entity.user.UserInfo;
import com.ccblog.enumeration.user.LoginTypeEnum;
import com.ccblog.enumeration.StatusEnum;
import com.ccblog.mapper.user.LoginMapper;
import com.ccblog.mapper.user.UserMapper;
import com.ccblog.redis.user.VerifyRedis;
import com.ccblog.service.user.LoginService;
import com.ccblog.service.user.helper.EmailHelper;
import com.ccblog.service.user.helper.UserPwdHelper;
import com.ccblog.service.user.helper.UserRandomGenHelper;
import com.ccblog.service.user.helper.UserSessionHelper;
import com.ccblog.exception.ExceptionUtil;
import com.ccblog.utils.ip.IpUtil;
import com.ccblog.vo.user.LoginInfoVO;
import jakarta.annotation.Resource;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
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

    @Autowired
    private EmailHelper emailHelper;

    @Autowired
    private VerifyRedis verifyRedis;



    /**
     * 用户密码登录
     * @param email
     * @param password
     * @return
     */
    public LoginInfoVO loginByUserPwd(String email, String password) {
        User user = userLoginMapper.getUserByEmail(email); // 先查询是否有用户
        if (user == null) {
            throw ExceptionUtil.of(StatusEnum.USER_NOT_EXISTS, "email=" + email);
        }
        if (!userPwdHelper.match(password, user.getPassword())) {
            throw ExceptionUtil.of(StatusEnum.USER_PWD_ERROR);
        }
        Long userId = user.getId();
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
                .userName(userRegisterDTO.getUserName())
                .photo(userInfo.getPhoto())
                .build();
        // 注册，返回对应的session
//        ReqInfoContext.getReqInfo().setUserId(userId);
        // 失效验证码
        verifyRedis.removeCode(userRegisterDTO.getEmail()); // 失效验证码
        return loginInfoVO;
    }

    /**
     * 用户登出
     * @param session
     */
    public void logout(String session) {
        userSessionHelper.removeSession(session); // 移除session,其实就是删除缓存redis里面的
    }

    /**
     * 发送验证码
     * @param email
     */
    public void sendVerifyCode(String email) {
        emailHelper.sendVerifyCode(email);
    }

    private void registerPreCheck(UserRegisterDTO userRegisterDTO){
        // 检测是否为空
        if (StringUtils.isBlank(userRegisterDTO.getUserName()) || StringUtils.isBlank(userRegisterDTO.getPassword())) {
            throw ExceptionUtil.of(StatusEnum.USER_NAME_PWD_BLANK);
        }
        // 检查验证码格式是否正确(前端会校验,但是从健壮性考虑可以排除其他途径发送的无效请求)
        boolean flag = true;
        String code = userRegisterDTO.getVerifyCode();
        if (code == null || code.length() != 6) flag=false;
        if(flag){
            for (int i = 0; i < 6; i++) {
                if (code.charAt(i) < '0' || code.charAt(i) > '9')
                    flag = false;
            }
        }
        if(!flag){
            throw ExceptionUtil.of(StatusEnum.VERIFY_CODE_FORMAT_INVALID);
        }
        // 查询验证码是否正确
        if(!verifyRedis.checkIfCorrect(userRegisterDTO.getEmail(), code)){
            throw ExceptionUtil.of(StatusEnum.VERIFY_CODE_MISMATCH);
        }

        // 查询邮箱是否已注册
        User user = userLoginMapper.getUserByEmail(userRegisterDTO.getEmail()); // 先查询是否有用户
        if(user!=null){
            throw ExceptionUtil.of(StatusEnum.USER_EMAIL_EXISTS);
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
//            ip.setLatestRegion(IpUtil.getLocationByIp(clientIp).toRegionStr());

            if (ip.getFirstIp() == null) {
                ip.setFirstIp(clientIp);
//                ip.setFirstRegion(ip.getLatestRegion());
            }
            userMapper.updateUserInfoById(userInfo);
        }
        BaseUserInfoDTO baseUserInfoDTO = new BaseUserInfoDTO();
        BeanUtils.copyProperties(userInfo,baseUserInfoDTO);

        return baseUserInfoDTO;
    }
}