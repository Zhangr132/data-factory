package com.data.service;

import com.data.dto.Email.EmailLoginDto;
import com.data.dto.LoginDto;
import com.data.dto.RegisterDto;
import com.data.dto.Email.SendEmailCodeDto;
import com.data.dto.UpdateUserDto;
import com.data.dto.VerifyDto;
import com.data.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.data.utils.R;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-14
 */
public interface UserService extends IService<User> {

    String login(LoginDto loginDto, HttpServletRequest request) throws Exception;

    R emailLogin(EmailLoginDto emailLoginDto, HttpServletRequest request) throws Exception;

    boolean register(RegisterDto registerDto, HttpServletRequest request) throws Exception;

    R getUserData(String token);

    R sendEmailCode(SendEmailCodeDto sendEmailCodeDto, HttpServletRequest request);

    R secondVerify(VerifyDto verifyDto, HttpServletRequest request) throws Exception;

    R updateUserInfo(UpdateUserDto updateUserDto, HttpServletRequest request) throws Exception;


}
