package com.data.vo;

import com.data.entity.CodeValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author zhangr132
 * @Date 2024/3/27 10:26
 * @注释
 */
@Data
public class EnumVo {
    @ApiModelProperty("码表编号")
    private String codeTableNumber;

    @ApiModelProperty("码表名称")
    private String codeTableName;

//    @ApiModelProperty("码值名称")
//    private String codeValueName;
//
//    @ApiModelProperty("码值取值")
//    private String codeValueValue;
//
//    @ApiModelProperty("码值含义")
//    private String codeValueDesc;

    @ApiModelProperty("码值列表")
    private List<CodeValue> codeValueLists;
}
