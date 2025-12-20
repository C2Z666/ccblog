package com.ccblog.service.user.helper;

import com.ccblog.cfg.MailProps;
import com.ccblog.enumeration.StatusEnum;
import com.ccblog.exception.ExceptionUtil;
import com.ccblog.redis.user.VerifyRedis;
import jakarta.annotation.Resource;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * 邮箱相关功能
 * @author czc
 * @date 2025/12/20
 */
@Component
public class EmailHelper {
    @Autowired
    private VerifyRedis verifyRedis;

    /**
     * 资源文件
     */
    @Resource
    private JavaMailSender mailSender;

    @Resource
    private MailProps mailProps;

    /**
     * 邮箱校验
     */
    private static final Pattern PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    /**
     * 发送验证码
     * @param email
     */
    public void sendVerifyCode(String email) {
        if(!isValid(email)){
            throw ExceptionUtil.of(StatusEnum.VERIFY_EMAIL_FORMAT_INVALID);
        }
        String code = UserRandomGenHelper.genNumericCode(6); // 生成六位验证码
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
            helper.setTo(email);
            helper.setFrom(mailProps.getUsername());
            helper.setSubject("CCBlog注册验证码");
            helper.setText("您的验证码是：" + code + "，5 分钟内有效。", false);
            mailSender.send(msg);
        } catch (MessagingException | jakarta.mail.MessagingException e) {
            throw new RuntimeException("邮件发送失败", e);
        }
        verifyRedis.saveCode(email,code);
    }

    /**
     * 校验邮箱格式是否合法
     * @param email 待校验字符串
     * @return true=格式正确；false=格式错误
     */
    private static boolean isValid(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return PATTERN.matcher(email).matches();
    }
}