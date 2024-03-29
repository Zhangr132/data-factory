package com.data.dto.DataStandard;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

/**
 * 数据标准编辑 数据传输对象
 * @Author zhangr132
 * @Date 2024/3/28 9:17
 * @注释
 */
@Data
public class UpdateDataStandardDto {

    @ApiModelProperty("标准编号")
    private String dataStandardCode;

    @ApiModelProperty("中文名称")
    @NotBlank(message = "中文名称不能为空(包含空格)")
    @Length(max = 10, message = "中文名称最长为10")
    @Pattern(regexp = "^[\u4e00-\u9fa5_a-zA-Z]+$",message = "码表名称只能包含中文和字母")
    private String dataStandardCnName;

    @ApiModelProperty("英文名称")
    @NotBlank(message = "英文名称不能为空(包含空格)")
    @Length(max = 10, message = "英文名称最长为10")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]*$", message = "只支持英文大小写、数字及下划线且只能英文开头")
    private String dataStandardEnName;

    @ApiModelProperty("标准说明")
    private String dataStandardExplain;

    @ApiModelProperty("来源机构编码")
    @NotBlank(message = "来源机构不能为空(包含空格)")
    private String dataStandardSourceOrganization;

    @ApiModelProperty("数据类型：类型字典项编码")
    @NotBlank(message = "数据类型不能为空(包含空格)")
    private String dataStandardType;

    @ApiModelProperty("数据长度")
    private Long dataStandardLength;

    @ApiModelProperty("数据精度")
    private Integer dataStandardAccuracy;

    @ApiModelProperty("默认值")
    private String dataStandardDefaultValue;

    @ApiModelProperty("取值范围-最大值")
    private String dataStandardValueMax;

    @ApiModelProperty("取值范围-最小值")
    private String dataStandardValueMin;

    @ApiModelProperty("枚举范围：字典组编码")
    private String dataStandardEnumerationRange;

    @ApiModelProperty("是否可为空：0不可为空，1可为空")
    private Integer dataStandardIsBlank;
}
