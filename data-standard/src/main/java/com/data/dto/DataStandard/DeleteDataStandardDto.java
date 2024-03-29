package com.data.dto.DataStandard;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据标准删除 数据传输对象
 * @Author zhangr132
 * @Date 2024/3/28 10:31
 * @注释
 */
@Data
public class DeleteDataStandardDto {
    @ApiModelProperty("标准编号")
    private String dataStandardCode;
}
