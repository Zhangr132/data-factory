package com.data.dto.SourceApi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 请求体
 * @Author zhangr132
 * @Date 2024/4/3 18:02
 * @注释
 */
@Data
public class RequestBodyDto {
    @ApiModelProperty("参数名称")
    @NotBlank(message = "参数名称不能为空(包含空格)")
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9]*$",message = "参数名称只支持英文大小写及数字，且不能以数字开头")
    private String bodyName;

    @ApiModelProperty("数据类型：（支持String、Int、Float、Object、Array）")
    @NotBlank(message = "数据类型不能为空(包含空格)")
    private String bodyType;

    @ApiModelProperty("是否为空")
    @NotBlank(message = "是否为空必填")
    private Integer bodyIsBlank;

    @ApiModelProperty("默认值")
    private String bodyDefaultValue;

    @ApiModelProperty("参数描述")
    private String bodyDesc;

    @ApiModelProperty("子参数")
    private List<RequestBodyDto> bodyChildren;

}
