package com.data.dto.DataStandard;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据标准状态更改 数据传输对象
 * @Author zhangr132
 * @Date 2024/3/28 10:16
 * @注释
 */
@Data
public class StateDataStandardDto {
    @ApiModelProperty("标准编号")
    private String dataStandardCode;

    @ApiModelProperty("标准状态: 状态字典项编码（（0：未发布，1：已发布，2：已停用））")
    private Integer dataStandardState;
}
