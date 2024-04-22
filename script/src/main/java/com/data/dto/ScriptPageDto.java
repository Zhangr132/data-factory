package com.data.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhangr132
 * @Date 2024/4/18 9:34
 * @注释
 */
@Data
public class ScriptPageDto extends SelectScriptDto{
    @ApiModelProperty("每页显示条数")
    private Integer pageSize=20;
    @ApiModelProperty("页码")
    private Integer pageNumber=1;
}
