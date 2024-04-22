package com.data.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author zhangr132
 * @Date 2024/4/18 11:05
 * @注释
 */
@Data
public class ClassifyScriptDto {
    @ApiModelProperty("脚本分类")
    @NotBlank(message = "脚本分类不能为空")
    private String scriptCategory;
}
