package com.data.controller;

import com.data.entity.UserLog;
import com.data.service.UserLogService;
import com.data.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author zhangr132
 * @Date 2024/4/24 18:09
 * @注释
 */
@Slf4j
@Api("用户日志管理")
@RestController
@RequestMapping("/userLog")
public class UserLogController {
    @Autowired
    private UserLogService userLogService;

    @ApiOperation("获取用户登录日志")
    @RequestMapping("/getUserLoginLog")
    public UserLog getUserLoginLog(HttpServletRequest request) throws Exception {
        UserLog userLog = userLogService.getUserLoginLog(request);
        return userLog;
    }
}
