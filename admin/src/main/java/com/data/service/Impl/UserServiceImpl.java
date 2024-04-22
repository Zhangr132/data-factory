package com.data.service.Impl;


import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.data.controller.FileController;
import com.data.dto.Email.EmailDto;
import com.data.dto.Email.EmailLoginDto;
import com.data.dto.LoginDto;
import com.data.dto.RegisterDto;
import com.data.dto.Email.SendEmailCodeDto;
import com.data.entity.User;
import com.data.mapper.UserMapper;
import com.data.service.EmailService;
import com.data.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.utils.*;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private EmailService emailService;

    @Value("${code.expiration}")
    private Long expiration;


    /**
     * 用户密码登录
     * @param loginDto
     * @return
     * @throws Exception
     */
    @Override
    public String login(LoginDto loginDto, HttpServletRequest request) throws Exception {
        log.info("正在处理登录请求");

        String agent=request.getHeader("User-Agent");
        //解析agent字符串
        UserAgent userAgent = UserAgentUtil.parse(agent);
        //获取操作系统对象
        String os = userAgent.getOs().getName();
        //获取浏览器对象
        String browser = userAgent.getBrowser().getName();


        //查询用户是否存在
        User user=userMapper.selectByUserName(loginDto.getUsername());
        boolean result= Md5Util.passwordVerify(loginDto.getPassword(),user.getPassword());
        if (result){
            //登录生成token
            Map<String,String> map=new HashMap<>();
            map.put("username", loginDto.getUsername());
            String token= JwtTokenUtil.createToken(map);
            log.info("登录IP:"+ IpUtil.getIpAddr(request));
            log.info("登录时间:"+ DateUtils.getCurrentTime());
            log.info("登录设备:"+os+" "+browser);
            log.info(loginDto.getUsername()+" 登录成功！生成的Token为："+token);
            return token;
        }
        return "false";
    }

    @Override
    public R emailLogin(EmailLoginDto emailLoginDto, HttpServletRequest request) {
        log.info("正在处理邮箱登录请求");
        // 查询邮箱是否存在
        MPJLambdaWrapper<User> wrapper = new MPJLambdaWrapper<>();
        wrapper
                .selectAll(User.class)
                .eq(User::getEmail,emailLoginDto.getEmail());
        User userEmail = userMapper.selectOne(wrapper);
        if (ObjectUtils.isEmpty(userEmail)){
            return R.Failed("邮箱不存在");
        }
        //检验验证码是否一致
        String code = stringRedisTemplate.opsForValue().get(RedisConstants.REDIS_KEY_Emai+emailLoginDto.getEmail());
        if (ObjectUtils.isEmpty(code) ||!code.equals(emailLoginDto.getEmailCode())) {
            return R.Failed("验证码错误");
        }
        //登录生成token
        Map<String,String> map=new HashMap<>();
        map.put("username", userEmail.getUsername());
        String token= JwtTokenUtil.createToken(map);
        log.info("登录IP:"+ IpUtil.getIpAddr(request));
        log.info("登录时间:"+ DateUtils.getCurrentTime());
        log.info("登录设备:"+request.getHeader("User-Agent"));
        log.info(userEmail.getUsername()+" 登录成功！生成的Token为："+token);
        return R.Success(token);
    }

    /**
     * 注册
     * @param registerDto
     * @return
     * @throws Exception
     */
    @Override
    public boolean register(RegisterDto registerDto, HttpServletRequest request) throws Exception {
        log.info(IpUtil.getIpAddr(request)+"正在处理注册请求");
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

    /**
     * 获取用户信息
     * @param token
     * @return
     */
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
        return R.Failed(username+"用户数据不存在！");
    }

    /**
     * 发送邮箱验证码
     * @param sendEmailCodeDto
     * @param session
     * @return
     */
    @Override
    public R sendEmailCode(SendEmailCodeDto sendEmailCodeDto, HttpSession session) {
        log.info("正在处理发送邮箱验证码请求");
        String email = sendEmailCodeDto.getEmail();;

        // 查看注册邮箱是否存在
        MPJLambdaWrapper<User> wrapper = new MPJLambdaWrapper<>();
        wrapper
                .selectAll(User.class)
                .eq(User::getEmail,sendEmailCodeDto.getEmail());
        User userEmail = userMapper.selectOne(wrapper);
        if (ObjectUtils.isEmpty(userEmail)){
            return R.Failed("邮箱不存在");
        }

        // 获取发送邮箱验证码的HTML模板
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("email-code.ftl");


        // 验证码
        String code = null;
        // 产生6位随机数，放入缓存中
        code = RandomUtil.randomNumbers(6);
        log.info("验证码：" + code);
        stringRedisTemplate.opsForValue().set(RedisConstants.REDIS_KEY_Emai+email, code, expiration, TimeUnit.SECONDS);
        log.info( email + " 验证码："+ stringRedisTemplate.opsForValue().get(RedisConstants.REDIS_KEY_Emai+email) + " 已存入缓存，有效期" + expiration + "秒");
//            if (!) {
//                throw new RuntimeException("后台缓存服务异常");
//                return null;
//            }
        // 发送验证码
        emailService.send(new EmailDto(Collections.singletonList(email),
                "邮箱验证码", template.render(Dict.create().set("code", code))));
        return R.Success("验证码已发送至邮箱，请注意查收！");

    }


}
