package com.data.dto.DataAssetRelationCategory;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
 * @Author zhangr132
 * @Date 2024/4/1 16:43
 * @注释
 */
@Data
public class AddDataAssetRelationCategoryDto {
    @ApiModelProperty("分类编码")
    @NotBlank(message = "分类编码不能为空(包含空格)")
    private String categoryCode;
}
