package com.data.service.Impl;


import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.data.controller.FileController;
import com.data.dto.Email.EmailDto;
import com.data.dto.Email.EmailLoginDto;
import com.data.dto.LoginDto;
import com.data.dto.RegisterDto;
import com.data.dto.Email.SendEmailCodeDto;
import com.data.dto.UpdateUserDto;
import com.data.entity.User;
import com.data.entity.UserLog;
import com.data.mapper.UserMapper;
import com.data.service.EmailService;
import com.data.service.UserLogService;
import com.data.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.utils.*;
import com.data.utils.constant.CacheConstants;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    @Autowired
    private UserLogService userLogService;

    //文件磁盘路径
    @Value("${file.upload.path}")
    private String fileUploadPath;
    //Redis缓存过期时间
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

        //查询用户是否存在
        User user=userMapper.selectByUserName(loginDto.getUsername());
        boolean result= Md5Util.passwordVerify(loginDto.getPassword(),user.getPassword());
        if (result){
            //记录用户登录日志
            UserLog userLog=userLogService.getUserLoginLog(request);

            //登录生成token
            Map<String,String> map=new HashMap<>();
            map.put("username", loginDto.getUsername());
            String token= JwtTokenUtil.createToken(map);
            //输出日志
            log.info("登录用户:"+loginDto.getUsername());
            log.info("登录IP:"+ userLog.getIp());
            log.info("登录时间:"+ userLog.getLoginTime());
            log.info("登录设备:"+userLog.getOs()+" "+userLog.getBrowser());
            log.info("IP归属地:"+userLog.getIpAttribution());
            log.info("登录成功！生成的Token为："+token);
            return token;
        }
        return "false";
    }

    /**
     * 邮箱登录
     * @param emailLoginDto
     * @param request
     * @return
     */
    @Override
    public R emailLogin(EmailLoginDto emailLoginDto, HttpServletRequest request) throws Exception {
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
        String code = stringRedisTemplate.opsForValue().get(CacheConstants.REDIS_KEY_Emai+emailLoginDto.getEmail());
        if (ObjectUtils.isEmpty(code) ||!code.equals(emailLoginDto.getEmailCode())) {
            return R.Failed("验证码错误");
        }
        //记录用户登录日志
        UserLog userLog=userLogService.getUserLoginLog(request);
        //登录生成token
        Map<String,String> map=new HashMap<>();
        map.put("username", userEmail.getUsername());
        String token= JwtTokenUtil.createToken(map);
        //输出日志
        log.info("登录用户:"+userEmail.getUsername());
        log.info("登录IP:"+ userLog.getIp());
        log.info("登录时间:"+ userLog.getLoginTime());
        log.info("登录设备:"+userLog.getOs()+" "+userLog.getBrowser());
        log.info("IP归属地:"+userLog.getIpAttribution());
        log.info("登录成功！生成的Token为："+token);
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
                        .description(registerDto.getDescription())
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

            // 获取当前工作目录
            Path basePaths = Paths.get(System.getProperty("user.dir"));
            // 解析文件上传路径
            Path filePath = basePaths.resolve(fileUploadPath+ File.separator+"images"+ File.separator);

            Map<String,Object> userData=new HashMap<>();
            userData.put("username",user.getUsername());
            userData.put("nickname",user.getNickname());
            userData.put("picture",user.getPicture());
            userData.put("description",user.getDescription());
            userData.put("phone",user.getPhone());
            userData.put("email",user.getEmail());
            userData.put("isDelete",user.getIsDelete());
            userData.put("createTime",user.getCreateTime());
            userData.put("updateTime",user.getUpdateTime());
            userData.put("path",filePath.toString());
            //返回用户信息
            return R.Success(userData);
        }
        return R.Failed(username+"用户数据不存在！");
    }

    /**
     * 发送邮箱验证码
     * @param sendEmailCodeDto
     * @param
     * @return
     */
    @Override
    public R sendEmailCode(SendEmailCodeDto sendEmailCodeDto, HttpServletRequest request) {
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
        stringRedisTemplate.opsForValue().set(CacheConstants.REDIS_KEY_Emai+email, code, expiration, TimeUnit.SECONDS);
        log.info( email + " 验证码："+ stringRedisTemplate.opsForValue().get(CacheConstants.REDIS_KEY_Emai+email) + " 已存入缓存，有效期" + expiration + "秒");
//            if (!) {
//                throw new RuntimeException("后台缓存服务异常");
//                return null;
//            }
        // 发送验证码
        emailService.send(new EmailDto(Collections.singletonList(email),
                "邮箱验证码", template.render(Dict.create().set("code", code))));
        return R.Success("验证码已发送至邮箱，请注意查收！");

    }

    /**
     * 二次验证
     * @param password
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public R secondVerify(String password, HttpServletRequest request) throws Exception {
        //解析token
        String token = request.getHeader("Authorization");
        String username = JwtTokenUtil.getUsername(token);
        log.info("正在处理 "+username+" 的二次验证请求");
        //根据用户名查询用户信息
        User user = userMapper.selectByUserName(username);
        if (ObjectUtils.isEmpty(user)) {
            return R.Failed("用户 "+username+" 不存在");
        }
        boolean result= Md5Util.passwordVerify(password,user.getPassword());
        if (result){
            return R.Success("用户 "+username+" 二次验证成功");
        }
        return R.Failed("密码错误");
    }

    /**
     * 更新用户信息
     * @param updateUserDto
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public R updateUserInfo(UpdateUserDto updateUserDto, HttpServletRequest request) throws Exception {
        //解析token
        String token = request.getHeader("Authorization");
        String username = JwtTokenUtil.getUsername(token);
        log.info(username+"正在处理用户信息更新请求");

        //根据用户Id查询用户信息
        User user = userMapper.selectById(updateUserDto.getId());
        if (ObjectUtils.isEmpty(user)) {
            return R.Failed("用户 "+username+" 不存在");
        }
        String picture=fileController.uploadImage(updateUserDto.getFile()).getData().toString();

        String ps= Md5Util.md5(updateUserDto.getPassword());
        User updateUser=User.builder()
                .username(updateUserDto.getUsername())
                .password(ps)
                .nickname(updateUserDto.getNickname())
                .picture(picture)
                .description(updateUserDto.getDescription())
                .phone(updateUserDto.getPhone())
                .email(updateUserDto.getEmail())
                .build();
        userMapper.updateById(updateUser);
        return R.Success("用户 "+username+" 信息更新成功");
    }


}
