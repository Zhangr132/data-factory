package com.data.dto.DataStandard;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 枚举信息查询 数据传输对象
 * @Author zhangr132
 * @Date 2024/3/27 10:40
 * @注释
 */
@Data
public class SelectEnumDto {
    @ApiModelProperty("码表编号")
    @NotBlank(message = "码表编号不能为空(包含空格)")
    private String codeTableNumber;
}
