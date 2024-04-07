package com.data.dto.SourceApi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhangr132
 * @Date 2024/4/3 9:15
 * @注释
 */
@Data
public class SelectSourceApiDto {
    @ApiModelProperty("接口来源")
    private String apiOrigin;

    @ApiModelProperty("接口名称")
    private String apiName;

    @ApiModelProperty("接口状态（0：未发布，1：已发布，2已停用）")
    private Integer apiState;

}
