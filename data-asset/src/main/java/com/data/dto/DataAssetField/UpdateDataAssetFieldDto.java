package com.data.dto.DataAssetField;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @Author zhangr132
 * @Date 2024/4/1 17:33
 * @注释
 */
@Data
public class UpdateDataAssetFieldDto {
    @ApiModelProperty("字段中文名称")
    @NotBlank(message = "中文名称不能为空(包含空格)")
    @Length(max = 10, message = "中文名称最长为10")
    @Pattern(regexp = "^[\u4e00-\u9fa5_a-zA-Z]+$",message = "字段中文名称只能包含中文和字母")
    private String fieldNameCn;

    @ApiModelProperty("字段英文名称")
    @NotBlank(message = "英文名称不能为空(包含空格)")
    @Length(max = 20, message = "英文名称最长为20")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]*$", message = "只支持英文大小写、数字及下划线且只能英文开头")
    private String fieldNameEn;

    @ApiModelProperty("字段描述")
    private String fieldDesc;

    @ApiModelProperty("数据标准编码")
    private String dataStandardCode;
}
