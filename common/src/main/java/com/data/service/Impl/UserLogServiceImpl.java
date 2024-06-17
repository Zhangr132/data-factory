package com.data.service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.dto.AddCategoryInfoDto;
import com.data.dto.DeleteCategoryInfoDto;
import com.data.dto.SelectCategoryInfoDto;
import com.data.dto.UpdateCategoryInfoDto;
import com.data.entity.CategoryInfo;
import com.data.entity.UserLog;
import com.data.mapper.CategoryInfoMapper;
import com.data.mapper.UserLogMapper;
import com.data.service.CategoryInfoService;
import com.data.service.UserLogService;
import com.data.utils.DateUtils;
import com.data.utils.IpUtil;
import com.data.utils.R;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户日志 服务实现类
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-29
 */
@Slf4j
@Service
public class UserLogServiceImpl extends ServiceImpl<UserLogMapper, UserLog> implements UserLogService {
    @Autowired
    private UserLogMapper userLogMapper;

    @Override
    public UserLog getUserLoginLog(HttpServletRequest request) throws Exception {
        String agent=request.getHeader("User-Agent");
        //解析agent字符串
        UserAgent userAgent = UserAgent.parseUserAgentString(agent);
        //获取操作系统对象
        String os = userAgent.getOperatingSystem().getName();
        //获取浏览器对象
        String browser = userAgent.getBrowser().getName();
        //获取IP地址
        String ip = IpUtil.getIpAddr(request);
        //ip归属地
        String ipAttribution=IpUtil.getCityInfo(ip);

        UserLog userLog=UserLog.builder()
                .ip(ip)
                .ipAttribution(ipAttribution)
                .os(os)
                .browser(browser)
                .loginTime(DateUtils.getCurrentTime())
                .build();
        return userLog;
    }
}
