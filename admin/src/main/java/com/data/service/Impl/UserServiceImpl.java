package com.data.service.Impl;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.data.controller.FileController;
import com.data.dto.LoginDto;
import com.data.dto.RegisterDto;
import com.data.entity.User;
import com.data.mapper.UserMapper;
import com.data.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.utils.JwtTokenUtil;
import com.data.utils.Md5Util;
import com.data.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-14
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FileController fileController;

    /**
     * 用户密码登录
     * @param loginDto
     * @return
     * @throws Exception
     */
    @Override
    public String login(LoginDto loginDto) throws Exception {
        log.info("正在处理登录请求");
        //查询用户是否存在
        User user=userMapper.selectByUserName(loginDto.getUsername());
        boolean result= Md5Util.passwordVerify(loginDto.getPassword(),user.getPassword());
        if (result){
            //登录生成token
            Map<String,String> map=new HashMap<>();
            map.put("username", loginDto.getUsername());
            String token= JwtTokenUtil.createToken(map);
            System.out.println(loginDto.getUsername()+" 登录成功！生成的Token为："+token);
            return token;
        }
        return "false";
    }

    /**
     * 注册
     * @param registerDto
     * @return
     * @throws Exception
     */
    @Override
    public boolean register(RegisterDto registerDto) throws Exception {
        log.info("正在处理注册请求");
        User registerUser = userMapper.selectByUserName(registerDto.getUsername());
            if (ObjectUtils.isEmpty(registerUser)) {
                String picture=fileController.uploadImage(registerDto.getFile()).getData().toString();

                String ps= Md5Util.md5(registerDto.getPassword());
                User user=User.builder()
                        .username(registerDto.getUsername())
                        .password(ps)
                        .nickname(registerDto.getNickname())
                        .picture(picture)
                        .describtion(registerDto.getDescribtion())
                        .phone(registerDto.getPhone())
                        .email(registerDto.getEmail())
                        .isDelete(false)
                        .build();
                int count=userMapper.insert(user);
                return count>0;
            }
        return false;
    }

    @Override
    public R getUserData(String token) {
        log.info("正在处理用户数据获取请求");
        //解析token
        String username = JwtTokenUtil.getUsername(token);
        log.info("username:"+username);
        //根据用户名查询用户信息
        User user = userMapper.selectByUserName(username);
        if (ObjectUtils.isNotEmpty(user)) {
            //返回用户信息
            return R.Success(user);
        }
        return R.Failed("用户数据不存在！");
    }
}
