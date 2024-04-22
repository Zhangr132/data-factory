package com.data.service;

import com.data.dto.LoginDto;
import com.data.dto.RegisterDto;
import com.data.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.data.utils.R;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-14
 */
public interface UserService extends IService<User> {

    String login(LoginDto loginDto) throws Exception;

    boolean register(RegisterDto registerDto) throws Exception;

    R getUserData(String token);
}
