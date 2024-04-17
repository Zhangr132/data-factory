package com.data.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhangr132
 * @Date 2024/4/17 10:02
 * @注释
 */
@Data
public class SelectCategoryInfoDto {
    @ApiModelProperty("父级分类编码")
    private String parentCode;
}
