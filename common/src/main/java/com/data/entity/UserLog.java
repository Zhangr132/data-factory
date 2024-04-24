package com.data.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户日志实体类
 * @Author zhangr132
 * @Date 2024/4/24 18:02
 * @注释
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "用户日志", description = "用户日志实体类")
public class UserLog {
    /**
     * 登陆人姓名
     */
    private String userName;

    /**
     * 登录ip
     */
    private String ip;

    /**
     * ip归属地信息
     */
    private String ipAttribution;

    /**
     * 登录时间
     */
    private String loginTime;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 浏览器
     */
    private String browser;


}
