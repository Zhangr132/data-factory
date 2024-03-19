package com.data.dto.CodeTable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author zhangr132
 * @Date 2024/3/19 16:28
 * @注释
 */
@Data
public class StateCodeTableDto {
    @ApiModelProperty("码表编号")
    @NotEmpty(message = "码表编号不能为空")
    private String codeTableNumber;

    @ApiModelProperty("码表状态（0：未发布，1：已发布，2：已停用）")
    @NotNull(message = "码表状态不能为空")
    private Integer codeTableState;
}
