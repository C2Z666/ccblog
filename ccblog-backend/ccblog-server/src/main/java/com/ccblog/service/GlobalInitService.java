package com.ccblog.service;

import com.ccblog.config.GlobalViewConfig;
import com.ccblog.context.ReqInfoContext;
import com.ccblog.dto.user.BaseUserInfoDTO;
import com.ccblog.localCache.user.GlobalViewCache;
import com.ccblog.redis.chat.ChatUserUnreadRedis;
import com.ccblog.redis.notice.NoticeCountRedis;
import com.ccblog.service.user.LoginService;
import com.ccblog.utils.NumUtil;
import com.ccblog.utils.SessionUtil;
import com.ccblog.vo.global.GlobalViewVo;
import com.ccblog.vo.global.GlobalVo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * @author XuYifei
 * @date 2024-07-12
 * added by czc 2025.09.24
 * updated by czc 2025.11.7
 */
@Slf4j
@Service
public class GlobalInitService {
    @Value("${spring.profiles.active}")
    private String env;

    @Resource
    private GlobalViewConfig globalViewConfig;

    @Autowired
    private LoginService loginService;

    @Autowired
    private GlobalViewCache globalViewCache;

    @Autowired
    private ChatUserUnreadRedis chatUserUnreadRedis;

    @Autowired
    private NoticeCountRedis noticeCountRedis;

    /**
     * 全局属性配置
     */
    public GlobalVo globalAttr() {
        GlobalVo vo = new GlobalVo();
        vo.setEnv(env);
        // 加上两步复制属性(为了兼容原本代码的)
        GlobalViewVo globalViewVo = new GlobalViewVo();
        BeanUtils.copyProperties(globalViewConfig,globalViewVo);
        vo.setSiteInfo(globalViewVo); // 因为在vo里面无法再嵌套引用config的,所以直接在那里创建了一个对应的跟config一样的vo

        try {
            if (ReqInfoContext.getReqInfo() != null && NumUtil.upZero(ReqInfoContext.getReqInfo().getUserId())) {
                vo.setIsLogin(true);
                vo.setUser(ReqInfoContext.getReqInfo().getUser());
                Long userId = ReqInfoContext.getReqInfo().getUserId();
                // 加入更新的信息
                GlobalVo globalVo = globalViewCache.get(userId);
                if(globalVo!=null){
                    vo.setUserMsgNum(globalVo.getUserMsgNum());
                    vo.setNoticeMsgNum(globalVo.getNoticeMsgNum());
                }
                else{
                    int unreadUserMsg = chatUserUnreadRedis.getTotalUnread(userId);
                    vo.setUserMsgNum(unreadUserMsg);
                    int unreadNoticeMsg = noticeCountRedis.getTotolUnread(userId);
                    vo.setNoticeMsgNum(unreadNoticeMsg);
                    // 只缓存必要的
                    GlobalVo voCache = GlobalVo.builder()
                            .userMsgNum(unreadUserMsg)
                            .noticeMsgNum(unreadNoticeMsg).build();
                    globalViewCache.put(userId,voCache);
                }
            } else {
                vo.setIsLogin(false);
            }
        } catch (Exception e) {
            log.error("loginCheckError:", e);
        }
        return vo;
    }

    /**
     * 初始化用户信息
     *
     * @param reqInfo
     */
    public void initLoginUser(ReqInfoContext.ReqInfo reqInfo) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        // 优先查看session-key
        Optional.ofNullable(SessionUtil.findCookieByName(request, LoginService.SESSION_KEY))
                .ifPresent(cookie -> initLoginUser(cookie.getValue(), reqInfo));
        if(reqInfo.getUser()!=null)return;
        if (request.getCookies() == null) {
            Optional.ofNullable(request.getHeader("Authorization"))
                    .ifPresent(cookie -> initLoginUser(request.getHeader("Authorization"), reqInfo));
            return;
        }
        Optional.ofNullable(request.getHeader("Authorization"))
                .ifPresent(token -> initLoginUser(token, reqInfo));
        if(reqInfo.getUser()!=null)return;
        if(request.getHeader("Authorization") != null){
            String token = request.getHeader("Authorization");
            initLoginUser(token, reqInfo);
        }
    }

    /**
     * 初始化用户信息
     * @param session
     * @param reqInfo
     */
    public void initLoginUser(String session, ReqInfoContext.ReqInfo reqInfo) {
        BaseUserInfoDTO user = loginService.getAndUpdateUserIpInfoBySessionId(session, reqInfo.getClientIp());
        reqInfo.setSession(session);
        if (user != null) {
            reqInfo.setUserId(user.getUserId());
            reqInfo.setUser(user); // 设置用户信息
        }
    }
}
