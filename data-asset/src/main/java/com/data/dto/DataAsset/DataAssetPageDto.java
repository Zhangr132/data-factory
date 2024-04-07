package com.data.dto.DataAsset;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhangr132
 * @Date 2024/3/29 14:53
 * @注释
 */
@Data
public class DataAssetPageDto extends SelectDataAssetDto {
    @ApiModelProperty("每页显示条数")
    private Integer pageSize=20;
    @ApiModelProperty("页码")
    private Integer pageNumber=1;
}
