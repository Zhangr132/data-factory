package com.data.dto.CodeValue;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 码值删除 数据传输对象
 * @Author zhangr132
 * @Date 2024/3/19 17:31
 * @注释
 */
@Data
public class DeleteCodeValueDto {
    @ApiModelProperty("码表编号(外键)")
    private String codeTableNumber;
}
