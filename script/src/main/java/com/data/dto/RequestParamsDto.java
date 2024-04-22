package com.data.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 请求参数
 * @Author zhangr132
 * @Date 2024/4/3 18:01
 * @注释
 */
@Data
public class RequestParamsDto {
    @ApiModelProperty("参数名称")
    @NotBlank(message = "参数名称不能为空(包含空格)")
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9]*$",message = "参数名称只支持英文大小写及数字，且不能以数字开头")
    private String requestParamsName;

    @ApiModelProperty("数据类型：（支持String、Int、Float）")
    @NotBlank(message = "数据类型不能为空(包含空格)")
    private String requestParamsType;

    @ApiModelProperty("是否为空")
    @NotBlank(message = "是否为空必填")
    private Integer requestParamsIsBlank;

    @ApiModelProperty("参数描述")
    private String requestParamsDesc;

}
