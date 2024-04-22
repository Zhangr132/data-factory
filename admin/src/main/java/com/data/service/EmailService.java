package com.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.data.dto.Email.EmailDto;
import com.data.entity.Email;

/**
 * @Author zhangr132
 * @Date 2024/4/22 17:40
 * @注释
 */
public interface EmailService extends IService<Email> {
    void send(EmailDto emailDto);
}
