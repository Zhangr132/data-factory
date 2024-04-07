package com.data.dto.DataAssetField;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author zhangr132
 * @Date 2024/3/29 15:48
 * @注释
 */
@Data
public class SelectDataAssetFieldDto {
    @ApiModelProperty("数据资产表编码")
    @NotBlank(message = "数据资产表编码不能为空(包含空格)")
    private String dataAssetCode;
}
