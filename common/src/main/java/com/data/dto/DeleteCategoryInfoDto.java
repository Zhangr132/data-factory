package com.data.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author zhangr132
 * @Date 2024/4/16 18:14
 * @注释
 */
@Data
public class DeleteCategoryInfoDto {
    @ApiModelProperty("分类编码")
    @NotBlank(message = "分类编码不能为空")
    private String categoryCode;
}
