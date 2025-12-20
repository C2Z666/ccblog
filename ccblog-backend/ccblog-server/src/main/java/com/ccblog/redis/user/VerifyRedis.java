package com.ccblog.redis.user;

import com.ccblog.constant.RedisPrefixConstant;
import com.ccblog.template.SimpleStringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 用户验证缓存
 * @author czc
 * @date 2025/12/20
 */
@Component
public class VerifyRedis extends SimpleStringRedisTemplate {

    /**
     * 查询验证码有效性
     * @param email    邮箱
     * @param userCode 用户填写的验证码
     * @return
     */
    public boolean checkIfCorrect(String email,String userCode){
        String code = super.getContent(email);
        return userCode.equals(code);
    }

    /**
     * 存储验证码
     * @param email
     * @param code   系统生成的验证码
     * @return
     */
    public void saveCode(String email,String code){
        super.setContent(code,email); // key参数在后面
    }

    /**
     * 强制失效验证码
     * @param email
     */
    public void removeCode(String email){
        super.removeContent(email);
    }

    public VerifyRedis(){
        super(5); // 五分钟有效
    }

    @Override
    protected String buildKey(Object... args) {
        String email = (String)args[0];
        return String.format(RedisPrefixConstant.USER_VERIFY_CODE,email);
    }


    @Override
    protected String buildRemovePattern(Object... args) {
        String email = (String)args[0];
        return String.format(RedisPrefixConstant.USER_VERIFY_CODE,email);
    }
}