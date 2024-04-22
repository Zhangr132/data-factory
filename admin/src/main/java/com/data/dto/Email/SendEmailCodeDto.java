package com.data.dto.Email;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @Author zhangr132
 * @Date 2024/4/22 14:45
 * @注释
 */
@Data
public class SendEmailCodeDto {
    @ApiModelProperty("邮箱地址")
    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp = "^[0-9a-zA-Z_]+([.-]?[0-9a-zA-Z_]+)*@[a-zA-Z_]+([.-]?[0-9a-zA-Z_]+)*\\.\\w{2,3}$", message = "邮箱格式不正确")
    private String email;
}
