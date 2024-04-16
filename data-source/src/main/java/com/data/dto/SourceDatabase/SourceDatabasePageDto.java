package com.data.dto.SourceDatabase;

import com.data.dto.SourceApi.SelectSourceApiDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhangr132
 * @Date 2024/4/7 17:55
 * @注释
 */
@Data
public class SourceDatabasePageDto extends SelectSourceDatabaseDto {
    @ApiModelProperty("每页显示条数")
    private Integer pageSize=20;
    @ApiModelProperty("页码")
    private Integer pageNumber=1;
}
