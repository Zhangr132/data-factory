package com.data.controller;


import com.data.dto.LoginDto;
import com.data.dto.RegisterDto;
import com.data.service.UserService;
import com.data.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
@RestController
@RequestMapping("/user")
@Api("用户管理模块")
public class UserController {
    private Logger logger= LoggerFactory.getLogger(getClass());
    @Autowired
    private UserService userService;

    @ApiOperation("登录")
    @PostMapping("/login")
    public R login(@Valid @RequestBody LoginDto loginDto, HttpSession session) throws Exception {
        logger.info("正在进行登录");
        String result=userService.login(loginDto);
        if(result!=null){
            //登陆成功则保存用户信息到session
            session.setAttribute("result", result);
            return R.Success("登录成功！",result);
        }
        System.out.println("登陆失败！");
        return R.Failed("登录失败！");

    }

    @ApiOperation("注册")
    @PostMapping("/register")
    public R register(@Valid @RequestBody RegisterDto registerDto) throws Exception {
        logger.info("正在进行注册");
        boolean result=userService.register(registerDto);
        if(result){
            return R.Success("注册成功");
        }
        return R.Failed("用户名已注册");
    }

    @ApiOperation("注销")
    @PostMapping("/logout")
    public R logout(HttpSession session){
        logger.info("正在注销用户");

        // 获取当前会话是否已经登录，返回true=已登录，false=未登录
        boolean res = true;
        if(res){
            //退出登录则清除session中的用户信息
            session.removeAttribute("result");
            logger.info("用户已注销");
            return R.Success("用户已注销");
        }
        return R.Failed("用户未登录");
    }

}
