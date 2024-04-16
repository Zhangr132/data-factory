package com.data.service.Impl;

import com.data.dto.LoginDto;
import com.data.dto.RegisterDto;
import com.data.entity.User;
import com.data.mapper.UserMapper;
import com.data.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.utils.JwtTokenUtil;
import com.data.utils.Md5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private Logger logger= LoggerFactory.getLogger(getClass());
    @Autowired
    private UserMapper userMapper;

    /**
     * 登录
     * @param loginDto
     * @return
     * @throws Exception
     */
    @Override
    public String login(LoginDto loginDto) throws Exception {
        logger.info("正在处理登录请求");
        boolean result= Md5Util.passwordVerify(loginDto.getPassword(),userMapper.selectByUserName(loginDto.getUsername()).getPassword());
        if (result){
            //登录生成token
            Map<String,String> map=new HashMap<>();
            map.put("username", loginDto.getUsername());
            map.put("password", Md5Util.md5(loginDto.getPassword()));
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
        logger.info("正在处理注册请求");
        User registerUser = userMapper.selectByUserName(registerDto.getUsername());
            if (registerUser == null) {
                String ps= Md5Util.md5(registerDto.getPassword());
                User user=User.builder()
                        .username(registerDto.getUsername())
                        .password(ps)
                        .nickname(registerDto.getNickname())
                        .picture(registerDto.getPicture())
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
}
