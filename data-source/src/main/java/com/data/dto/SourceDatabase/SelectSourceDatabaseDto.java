package com.data.dto.SourceDatabase;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * @Author zhangr132
 * @Date 2024/4/7 17:55
 * @注释
 */
@Data
public class SelectSourceDatabaseDto {
    @ApiModelProperty("数据库数据源名称")
    private String databaseSourceName;

    @ApiModelProperty("数据库数据源状态（（0：未发布，1：已发布，2：已停用））")
    private Integer databaseSourceState;
}
