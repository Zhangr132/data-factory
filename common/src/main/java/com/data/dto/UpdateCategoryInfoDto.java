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
public class UpdateCategoryInfoDto {
    @ApiModelProperty("分类编码")
    @NotBlank(message = "分类编码不能为空")
    private String categoryCode;

    @ApiModelProperty("父级分类编码")
    private String parentCode;

    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty("分类描述")
    private String categoryDesc;
}
