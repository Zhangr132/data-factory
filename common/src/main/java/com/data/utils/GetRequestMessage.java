package com.data.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义方法类
 * @Author zhangr132
 * @Date 2024/4/22 20:04
 * @注释
 */
public class GetRequestMessage {

    /**
     * 获取请求头中的token，并解析出用户名
     * @param request
     * @return
     */
    public static String getUsername(HttpServletRequest request) {
        String token=request.getHeader("Authorization");
        String username=JwtTokenUtil.getUsername(token);
        return username;
    }
}
