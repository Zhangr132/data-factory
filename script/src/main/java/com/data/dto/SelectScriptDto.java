package com.data.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhangr132
 * @Date 2024/4/18 9:34
 * @注释
 */
@Data
public class SelectScriptDto {
    @ApiModelProperty("脚本名称")
    private String scriptName;

    @ApiModelProperty("脚本状态（（0：未发布，1：已发布，2：已停用））")
    private Integer scriptState;
}
