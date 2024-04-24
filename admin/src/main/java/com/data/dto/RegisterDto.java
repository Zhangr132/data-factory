package com.data.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
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
    @Length(max = 20, message = "登录名最长为20")
    @Pattern(regexp = "^[A-Za-z0-9]+$",message = "用户名只能是数字和字母")
    private String username;

    @ApiModelProperty("密码")
    @NotEmpty(message = "登录密码不能为空")
    @Length(min = 6, max = 16, message = "密码长度为6-16")
    @Pattern(regexp = "^[A-Za-z0-9]+$",message = "密码只能是数字和字母")
    private String password;

    @ApiModelProperty("昵称")
    @NotEmpty(message = "昵称不能为空")
    @Length(max = 10, message = "昵称最长为15")
    @Pattern(regexp = "^[\u4e00-\u9fa5_A-Za-z0-9]+$",message = "昵称只能包含中文、字母和数字")
    private String nickname;

    @ApiModelProperty("头像")
    private MultipartFile file;

    @ApiModelProperty("简介")
    private String description;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

}
