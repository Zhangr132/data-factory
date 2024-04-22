package com.data.controller;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.data.dto.Email.EmailLoginDto;
import com.data.dto.LoginDto;
import com.data.dto.RegisterDto;
import com.data.dto.Email.SendEmailCodeDto;
import com.data.service.UserService;
import com.data.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * 用户管理
 * <p>
 * 用户表 前端控制器
 * </p>
 * @module 数据工厂用户管理
 * @author zhangr132
 * @since 2024-03-14
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Api("用户管理")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("密码登录")
    @PostMapping("/login")
    public R login(@Valid @RequestBody LoginDto loginDto, HttpSession session, HttpServletRequest request) throws Exception {
        log.info("正在进行密码登录");
        String result=userService.login(loginDto,request);
        if(result!="false"){
            //登陆成功则保存用户信息到session
            session.setAttribute("result", result);
            return R.Success("登录成功！",result);
        }
       log.info("登陆失败！");
        return R.Failed("登录失败！");

    }

    @ApiOperation("邮箱登录")
    @PostMapping("/emailLogin")
    public R emailLogin(@Valid @RequestBody EmailLoginDto emailLoginDto, HttpServletRequest request)  {
        log.info("正在进行邮箱登录");
        R result = userService.emailLogin(emailLoginDto, request);
        return result;
    }

    @ApiOperation("用户数据")
    @PostMapping("/getUserData")
    public R getUserData(HttpServletRequest request) {
        log.info("正在获取用户数据");
        //从Header中获取token
        String token=request.getHeader("Authorization");
        log.info("token:"+ token);
        if (token != null) {
            R result=userService.getUserData(token);
            return result;
        }
        log.info("获取失败,请先登录！");
        return R.Failed("获取失败,请先登录！");


    }

    @ApiOperation("注册")
    @PostMapping("/register")
    public R register(MultipartFile file, @Valid RegisterDto registerDto, HttpServletRequest request) throws Exception {
        log.info("正在进行注册");
        if (ObjectUtils.isNotEmpty(registerDto)){
            boolean result=userService.register(registerDto,request);
            if(result){
                log.info("注册成功");
                return R.Success("注册成功");
            }
            log.info("用户名已注册");
            return R.Failed("用户名已注册");
        }
        log.info("注册用户数据为空");
       return R.Failed("注册用户数据为空");
    }

    @ApiOperation("注销")
    @PostMapping("/logout")
    public R logout(HttpSession session){
        log.info("正在注销用户");

        // 获取当前会话是否已经登录，返回true=已登录，false=未登录
        boolean res = true;
        if(res){
            //退出登录则清除session中的用户信息
            session.removeAttribute("result");
            log.info("用户已注销");
            return R.Success("用户已注销");
        }
        log.info("用户未登录");
        return R.Failed("用户未登录");
    }

    @ApiOperation("发送邮箱验证码")
    @PostMapping("/sendEmailCode")
    public R sendEmailCode(@Valid @RequestBody SendEmailCodeDto sendEmailCodeDto, HttpSession session) {
        log.info("正在发送邮箱验证码");
        R result = userService.sendEmailCode(sendEmailCodeDto, session);
        return result;
    }

}
