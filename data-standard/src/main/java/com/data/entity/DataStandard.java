package com.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 数据标准表
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("data_standard")
//@TableName("data_standard LEFT JOIN code_table on code_table.code_table_number = data_standard_enumeration_range")
@ApiModel(value = "DataStandard对象", description = "数据标准表")
public class DataStandard implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("数据标准id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("标准编号")
    private String dataStandardCode;

    @ApiModelProperty("中文名称")
    private String dataStandardCnName;

    @ApiModelProperty("英文名称")
    private String dataStandardEnName;

    @ApiModelProperty("标准说明")
    private String dataStandardExplain;

    @ApiModelProperty("来源机构编码")
    private String dataStandardSourceOrganization;

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
    @ApiModelProperty("码表名称（码表字段）")
    private String codeTableName;

    @ApiModelProperty("标准状态: 状态字典项编码（（0：未发布，1：已发布，2：已停用））")
    private Integer dataStandardState;

    @ApiModelProperty("是否可为空：0不可为空，1可为空")
    private Integer dataStandardIsBlank;

    @ApiModelProperty("删除标识符: 0未删除，时间戳已删除")
    private Integer deleteFlag;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;


}
