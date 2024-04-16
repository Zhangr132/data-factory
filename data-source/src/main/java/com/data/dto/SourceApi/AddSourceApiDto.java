package com.data.dto.SourceApi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.List;

/**
 * @Author zhangr132
 * @Date 2024/4/7 9:40
 * @注释
 */
@Data
public class AddSourceApiDto {
    @ApiModelProperty("接口名称")
    @NotBlank(message = "接口名称不能为空(包含空格)")
    @Length(max = 100, message = "接口名称最长为100")
    @Pattern(regexp = "^[^\\s]+$",message = "接口名称允许使用中英文，大小写和特殊字符，不允许使用空格")
    private String apiName;

    @ApiModelProperty("接口来源")
    @NotBlank(message = "接口来源不能为空(包含空格)")
    private String apiOrigin;

    @ApiModelProperty("接口描述")
    @Length(max = 1000, message = "接口描述最长为1000")
    private String apiDesc;

    @ApiModelProperty("接口分类编码")
    @NotBlank(message = "接口分类编码不能为空(包含空格)")
    private String apiCategoryCode;

    @ApiModelProperty("接口协议（0：http，1：https）")
    @NotNull(message = "接口协议不能为空(包含空格)")
    private Integer apiProtocol;

    @ApiModelProperty("接口端口")
    @NotBlank(message = "接口端口不能为空(包含空格)")
    @Pattern(regexp = "^((?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))(:(\\d{1,5})?)$",message = "IP端口通用规则")
    private String apiPort;

    @ApiModelProperty("接口路径")
    @NotBlank(message = "接口路径不能为空(包含空格)")
    private String apiPath;

    @ApiModelProperty("接口请求方式（0：GET，1：POST）")
    @NotNull(message = "接口请求方式不能为空(包含空格)")
    private Integer apiRequestMethod;

    @ApiModelProperty("接口超时时间")
    @Min(value = 1,message = "必须为大于0的整数")
    @Max(value = 1799,message = "必须为小于1800的整数")
    @NotNull(message = "接口超时时间不能为空(包含空格)")
    private Integer apiTimeoutTime;

    @ApiModelProperty("输入参数")
    private List<RequestParamsDto> apiRequestParamsList;

    @ApiModelProperty("请求body")
    private List<RequestBodyDto> apiRequestBodyList;

    @ApiModelProperty("返回参数")
    private List<ResponseDto> apiResponseList;
}
