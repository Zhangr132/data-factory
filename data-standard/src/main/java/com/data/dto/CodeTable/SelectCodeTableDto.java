package com.data.dto.CodeTable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhangr132
 * @Date 2024/3/21 10:59
 * @注释
 */
@Data
public class SelectCodeTableDto {
    @ApiModelProperty("码表名称")
    private String codeTableName;

    @ApiModelProperty("码表状态（0：未发布，1：已发布，2：已停用）")
    private Integer codeTableState;
}
