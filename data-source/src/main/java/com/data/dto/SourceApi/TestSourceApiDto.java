package com.data.dto.SourceApi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author zhangr132
 * @Date 2024/4/15 9:09
 * @注释
 */
@Data
public class TestSourceApiDto {
    @ApiModelProperty("输入参数")
    private List<RequestParamsDto> apiRequestParamsList;

    @ApiModelProperty("请求body")
    private List<RequestBodyDto> apiRequestBodyList;

    @ApiModelProperty("返回参数")
    private List<ResponseDto> apiResponseList;
}
