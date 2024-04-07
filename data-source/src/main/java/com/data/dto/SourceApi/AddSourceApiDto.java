package com.data.dto.SourceApi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhangr132
 * @Date 2024/4/7 9:40
 * @注释
 */
@Data
public class AddSourceApiDto {
    @ApiModelProperty("接口分类编码")
    private String apiCategoryCode;

    @ApiModelProperty("接口来源")
    private String apiOrigin;

    @ApiModelProperty("接口名称")
    private String apiName;

    @ApiModelProperty("接口描述")
    private String apiDesc;

    @ApiModelProperty("接口协议（0：http，1：https）	")
    private Integer apiProtocol;

    @ApiModelProperty("接口端口")
    private String apiPort;

    @ApiModelProperty("接口路径")
    private String apiPath;

    @ApiModelProperty("接口请求方式（0：GET，1：POST）")
    private Integer apiRequestMethod;

    @ApiModelProperty("接口超时时间")
    private Integer apiTimeoutTime;
}
