package com.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.data.entity.UserLog;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author zhangr132
 * @Date 2024/4/24 18:07
 * @注释
 */
public interface UserLogService extends IService<UserLog> {
    UserLog getUserLoginLog(HttpServletRequest request) throws Exception;
}
