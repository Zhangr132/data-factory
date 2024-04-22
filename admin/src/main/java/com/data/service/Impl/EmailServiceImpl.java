package com.data.service.Impl;

import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.dto.Email.EmailDto;
import com.data.entity.Email;
import com.data.mapper.EmailMapper;
import com.data.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author zhangr132
 * @Date 2024/4/22 17:41
 * @注释
 */
@Slf4j
@Service
public class EmailServiceImpl extends ServiceImpl<EmailMapper,Email> implements EmailService {
    @Autowired
    private EmailMapper emailMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${spring.mail.host}")
    private String eHost;
    @Value("${spring.mail.port}")
    private String ePort;
    @Value("${spring.mail.username}")
    private String eUsername;
    @Value("${spring.mail.password}")
    private String ePassword;


    @Override
    public void send(EmailDto emailDto) {
        log.info("正在发送邮件");
        //发送邮件
        // 读取邮箱配置
        if ( eHost == null || ePort == null || eUsername == null || ePassword == null) {
            throw new RuntimeException("邮箱配置异常");
        }

        // 设置
        MailAccount account = new MailAccount();
        account.setHost(eHost);
        account.setPort(Integer.parseInt(ePort));
        // 设置发送人邮箱
        account.setFrom("数据工厂" + "<" + eUsername + ">");
        // 设置发送人名称
        account.setUser(eUsername);
        // 设置发送授权码
        account.setPass(ePassword);
        account.setAuth(true);
        // ssl方式发送
        account.setSslEnable(true);
        // 使用安全连接
        account.setStarttlsEnable(true);

        // 发送邮件
        try {
            int size = emailDto.getTos().size();
            Mail.create(account)
                    .setTos(emailDto.getTos().toArray(new String[size]))
                    .setTitle(emailDto.getSubject())
                    .setContent(emailDto.getContent())
                    .setHtml(true)
                    //关闭session
                    .setUseGlobalSession(false)
                    .send();
            log.info("邮件发送成功");
        } catch (Exception e) {
            log.error("邮件发送失败", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
