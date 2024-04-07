package com.data.dto.DataAsset;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhangr132
 * @Date 2024/3/29 14:54
 * @注释
 */
@Data
public class SelectDataAssetDto {
    @ApiModelProperty("数据资产中文名称")
    private String assetNameCn;

    @ApiModelProperty("数据资产英文名称")
    private String assetNameEn;

    @ApiModelProperty("数据资产表状态；0：未发布；1：已发布；2：已停用")
    private Integer dataAssetState;
}
