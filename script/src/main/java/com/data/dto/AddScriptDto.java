package com.data.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @Author zhangr132
 * @Date 2024/4/17 10:27
 * @注释
 */
@Data
public class AddScriptDto {
    @ApiModelProperty("脚本名称")
    @Pattern(regexp ="^[\\u4e00-\\u9fa5A-Za-z]+$",message="只支持中文、英文大小写")
    private String scriptName;

    @ApiModelProperty("脚本描述")
    private String scriptDesc;

    @ApiModelProperty("脚本分类")
    private String scriptCategory;

//    @ApiModelProperty("文件")
//    private String scriptFiles;

    @ApiModelProperty("类名")
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9]*$",message = "类名只支持英文大小写及数字，且不能以数字开头")
    private String className;

    @ApiModelProperty("函数名")
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9]*$",message = "函数名只支持英文大小写及数字，且不能以数字开头")
    private String functionName;

    @ApiModelProperty("输入参数")
    private List<RequestParamsDto> apiRequestParamsList;

    @ApiModelProperty("返回参数")
    private List<ResponseDto> apiResponseList;
}
