package com.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;


/**
 * @Author zhangr132
 * @Date 2024/4/1 9:33
 * @注释
 */
@Data
public class DataAssetFieldVo {
    @ApiModelProperty("字段中文名称")
    private String fieldNameCn;

    @ApiModelProperty("字段英文名称")
    private String fieldNameEn;

    @ApiModelProperty("字段描述")
    private String fieldDesc;

    @ApiModelProperty("数据类型：类型字典项编码")
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

}
