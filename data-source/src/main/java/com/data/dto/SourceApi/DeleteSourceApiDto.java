package com.data.dto.SourceApi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author zhangr132
 * @Date 2024/4/15 8:53
 * @注释
 */
@Data
public class DeleteSourceApiDto {
    @ApiModelProperty("接口编码")
    @NotBlank(message = "接口编码不能为空")
    private String apiCode;
}
