package com.example.service.impl;

import com.example.entity.auth.Account;
import com.example.mapper.UserMapper;
import com.example.mytools.PasswordUtil;
import com.example.service.AuthorizeService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.example.mytools.CheckTools.checkUsername;

@Service
public class AuthorizeServiceImpl implements AuthorizeService {
    @Resource
    UserMapper userMapper;

    @Resource
    MailSender mailSender;

    @Value("${spring.mail.username}")
    String from;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    // 获取用户登录信息
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || !checkUsername(username)) throw new UsernameNotFoundException("用户名非法");
        Account account = userMapper.getAccountByUsernameOrEmail(username);
        if (account == null) throw new UsernameNotFoundException("用户不存在");
        return User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles("user")
                .build();
    }

    // 获取验证码，若获取成功则返回 null，否则返回错误 message
    private String getValidCode(String email) {

        // 取出 redis 里的验证邮箱，验证剩余时间，防止重复发送验证码
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(email))) {
            Long expireTime = Optional.ofNullable(stringRedisTemplate.getExpire(email, TimeUnit.SECONDS)).orElse(0L);
            if (expireTime >= 120) return "请求频繁，请等待" + String.valueOf(expireTime - 120) + "秒后重新发送验证码。";
        }

        // 生成 6 位数随机验证码
        SecureRandom secureRandom = new SecureRandom();
        int validCode = secureRandom.nextInt(900000) + 100000;

        // 设置验证短信消息
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("您的邮件");
        simpleMailMessage.setText("您的验证码是：" + validCode);

        // 发送短信
        try {
            mailSender.send(simpleMailMessage);
            // 将验证信息存入数据库，过期时间 3 分钟
            stringRedisTemplate.opsForValue().set(email, String.valueOf(validCode), 3, TimeUnit.MINUTES);
            return null;
        } catch (MailException e) {
            e.printStackTrace();
            return "邮件发送失败，请检查邮箱地址是否有效";
        }
    }

    @Override
    public String sendValidateRegisterEmail(String email, String sessionId) {
        // 先查询邮箱是否被注册过
        if (userMapper.getAccountByEmail(email) != null) return "邮箱已被注册，请更换邮箱后重试";
        // 发送验证码
        return getValidCode(email);
    }

    @Override
    public String validateAndRegister(String username, String password, String email, String code, String sessionId) {
        String redisKey = email;
        if(Boolean.TRUE.equals(stringRedisTemplate.hasKey(redisKey))) {
            String message = stringRedisTemplate.opsForValue().get(redisKey);
            if(message == null) return "验证码失效，请重新请求";
            if(message.equals(code)) {
                Account account = userMapper.getAccountByUsername(username);
                if(account != null) return "此用户名已被注册，请更换用户名";
                stringRedisTemplate.delete(redisKey);
                password = PasswordUtil.bCryptPasswordEncoder.encode(password);
                if (userMapper.createAccount(username, password, email)) {
                    return null;
                } else {
                    return "内部错误，请联系管理员";
                }
            } else {
                return "验证码错误，请检查后再提交";
            }
        } else {
            return "请先获取验证码";
        }
    }

    @Override
    public String sendValidateResetEmail(String email) {
        if (userMapper.getAccountByEmail(email) == null) return "查询不到该用户信息，请先注册";
        return getValidCode(email);
    }

    @Override
    public boolean resetPassword(String password, String email) {
        // 加密 password
        password = PasswordUtil.bCryptPasswordEncoder.encode(password);
        return userMapper.updatePassword(password, email);
    }

    @Override
    public String validateCode(String email, String code) {
        String redisKey = email;
        if(Boolean.TRUE.equals(stringRedisTemplate.hasKey(redisKey))) {
            String message = stringRedisTemplate.opsForValue().get(redisKey);
            if(message == null) return "验证码失效，请重新请求";
            if(message.equals(code)) {
                return null;
            } else {
                return "验证码错误，请检查后再提交";
            }
        } else {
            return "请先获取验证码";
        }
    }
}
