package com.data.dto.DataStandard.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

/**
 * @Author zhangr132
 * @Date 2024/3/28 11:49
 * @注释
 */
@Data
public class DataStandardExcel {
    @Excel(name = "中文名称",width = 20)
    @NotBlank(message = "中文名称不能为空(包含空格)")
    @Length(max = 10, message = "中文名称最长为10")
    @Pattern(regexp = "^[\u4e00-\u9fa5_a-zA-Z]+$",message = "码表名称只能包含中文和字母")
    private String dataStandardCnName;

    @Excel(name = "英文名称",width = 20)
    @NotBlank(message = "英文名称不能为空(包含空格)")
    @Length(max = 10, message = "英文名称最长为10")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]*$", message = "只支持英文大小写、数字及下划线且只能英文开头")
    private String dataStandardEnName;

    @Excel(name = "标准说明",width = 20)
    private String dataStandardExplain;

    @Excel(name = "来源机构编码",width = 20)
    @NotBlank(message = "来源机构不能为空(包含空格)")
    private String dataStandardSourceOrganization;

    @Excel(name = "数据类型",width = 20)
    @NotBlank(message = "数据类型不能为空(包含空格)")
    private String dataStandardType;

    @Excel(name = "数据长度",width = 20)
    private Long dataStandardLength;

    @Excel(name = "数据精度",width = 20)
    private Integer dataStandardAccuracy;

    @Excel(name = "默认值",width = 20)
    private String dataStandardDefaultValue;

    @Excel(name = "取值范围-最大值",width = 20)
    private String dataStandardValueMax;

    @Excel(name = "取值范围-最小值",width = 20)
    private String dataStandardValueMin;

    @Excel(name = "枚举范围",width = 20)
    private String dataStandardEnumerationRange;

    @Excel(name = "是否可为空（选填：可为空/不可为空）",width = 20,replace = {"0_不可为空", "1_可为空"})
    private Integer dataStandardIsBlank;
}
