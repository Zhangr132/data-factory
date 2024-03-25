package com.data.dto.CodeTable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 码表删除 数据传输对象
 * @Author zhangr132
 * @Date 2024/3/19 16:50
 * @注释
 */

@Data
public class DeleteCodeTableDto {
    @ApiModelProperty("码表编号")
    @NotEmpty(message = "码表编号不能为空")
    private String codeTableNumber;

//    @ApiModelProperty("0未删除，其他值为删除")
//    @NotNull
//    private Integer deleteFlag;
}
