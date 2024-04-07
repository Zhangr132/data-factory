package com.data.dto.SourceApi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 请求参数
 * @Author zhangr132
 * @Date 2024/4/3 18:01
 * @注释
 */
@Data
public class RequestParams {
    @ApiModelProperty("参数名称")
    private String requestParamsName;

    @ApiModelProperty("参数位置")
    private String requestParamsPosition;

    @ApiModelProperty("数据类型")
    private String requestParamsType;

    @ApiModelProperty("是否为空")
    private Integer requestParamsIsBlank;

    @ApiModelProperty("默认值")
    private String requestParamsDefaultValue;

    @ApiModelProperty("参数描述")
    private String requestParamsDesc;

}
