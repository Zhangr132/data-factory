package com.data.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

/**
 * @Author zhangr132
 * @Date 2024/3/14 16:42
 * @注释
 */

@Data
public class RegisterDto {
    @ApiModelProperty("用户名")
    @NotEmpty(message = "用户名不能为空")
    @Length(max = 10, message = "登录名最长为10")
    @Pattern(regexp = "^[A-Za-z0-9]+$",message = "用户名只能是数字和字母")
    private String username;

    @ApiModelProperty("密码")
    @NotEmpty(message = "登录密码不能为空")
    @Length(min = 6, max = 16, message = "密码长度为6-16")
    @Pattern(regexp = "^[A-Za-z0-9]+$",message = "密码只能是数字和字母")
    private String password;

    @ApiModelProperty("昵称")
    @NotEmpty(message = "昵称不能为空")
    @Length(max = 10, message = "昵称最长为10")
    private String nickname;

    @ApiModelProperty("简介")
    private String describtion;

//    @ApiModelProperty("是否删除（1：未删除；0：已删除）")
//    private Boolean isDelete;

}
