package com.data.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 用户保存类
 * *保存前端发送的数据
 * @Author zhangr132
 * @Date 2024/3/14 11:30
 * @注释
 */
@Data
public class LoginDto {
    @ApiModelProperty("用户名")
    @NotEmpty(message = "用户名不能为空")
    @Length(max = 20, message = "登录名最长为20")
    @Pattern(regexp = "^[A-Za-z0-9]+$",message = "用户名只能是数字和字母")
    private String username;

    @ApiModelProperty("密码")
    @NotEmpty(message = "登录密码不能为空")
    @Length(min = 6, max = 16, message = "密码长度为6-16")
    @Pattern(regexp = "^[A-Za-z0-9]+$",message = "密码只能是数字和字母")
    private String password;
}
