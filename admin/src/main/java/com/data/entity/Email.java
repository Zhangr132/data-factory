package com.data.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author zhangr132
 * @Date 2024/4/19 17:42
 * @注释
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "Email邮箱", description = "邮箱实体类")
public class Email {
    private String email;
    private String username;
    private String emailCode;
    private int times;
}
