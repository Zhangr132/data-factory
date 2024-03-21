package com.data.dto.DataStandard;

import com.data.entity.DataStandard;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhangr132
 * @Date 2024/3/20 13:20
 * @注释
 */

@Data
public class DataStandardPageDto extends SelectDataStandardDto {
    @ApiModelProperty("每页显示条数")
    private Integer pageSize=20;
    @ApiModelProperty("页码")
    private Integer pageNumber=1;
}
