package com.data.dto.DataAssetRelationCategory;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author zhangr132
 * @Date 2024/4/1 17:33
 * @注释
 */
@Data
public class UpdateDataAssetRelationCategoryDto {
    @ApiModelProperty("分类编码")
    @NotBlank(message = "分类编码不能为空(包含空格)")
    private String categoryCode;
}
