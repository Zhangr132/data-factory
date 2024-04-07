package com.data.dto.DataAsset;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author zhangr132
 * @Date 2024/4/2 9:14
 * @注释
 */
@Data
public class DeleteDataAssetDto {
    @ApiModelProperty("数据资产编码")
    @NotBlank(message = "数据资产编码不能为空(包含空格)")
    private String dataAssetCode;
}
