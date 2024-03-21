package com.data.dto.DataStandard;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhangr132
 * @Date 2024/3/21 11:44
 * @注释
 */
@Data
public class SelectDataStandardDto {
    @ApiModelProperty("标准编号")
    private String dataStandardCode;

    @ApiModelProperty("中文名称")
    private String dataStandardCnName;

    @ApiModelProperty("英文名称")
    private String dataStandardEnName;

    @ApiModelProperty("来源机构编码")
    private String dataStandardSourceOrganization;

    @ApiModelProperty("标准状态: 状态字典项编码（（0：未发布，1：已发布，2：已停用））")
    private Integer dataStandardState;
}
