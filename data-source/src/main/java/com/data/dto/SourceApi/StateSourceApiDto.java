package com.data.dto.SourceApi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author zhangr132
 * @Date 2024/4/15 9:11
 * @注释
 */
@Data
public class StateSourceApiDto {
    @ApiModelProperty("接口编码")
    @NotBlank(message = "接口编码不能为空")
    private String apiCode;

    @ApiModelProperty("接口状态（0：未发布，1：已发布，2已停用）")
    @NotBlank(message = "接口状态不能为空")
    private Integer apiState;
}
