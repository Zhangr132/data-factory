package com.data.dto.Email;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author zhangr132
 * @Date 2024/4/22 15:17
 * @注释
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
    @ApiModelProperty("发送邮箱列表")
    @NotEmpty
    private List<String> tos;

    @ApiModelProperty("主题")
    @NotBlank
    private String subject;

    @ApiModelProperty("内容")
    @NotBlank
    private String content;
}
