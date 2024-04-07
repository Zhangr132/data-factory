package com.data.dto.DataAsset;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author zhangr132
 * @Date 2024/4/2 9:29
 * @注释
 */
@Data
public class StateDataAssetDto {
    @ApiModelProperty("数据资产编码")
    @NotBlank(message = "数据资产编码不能为空(包含空格)")
    private String dataAssetCode;

    @ApiModelProperty("数据资产表状态；0：未发布；1：已发布；2：已停用")
    @NotBlank(message = "数据资产状态不能为空(包含空格)")
    private Integer dataAssetState;

}
