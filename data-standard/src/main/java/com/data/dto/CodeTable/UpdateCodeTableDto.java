package com.data.dto.CodeTable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 码表编辑 数据传输对象
 * @Author zhangr132
 * @Date 2024/3/18 11:56
 * @注释
 */

@Data
public class UpdateCodeTableDto {

    @ApiModelProperty("码表编号")
    @NotBlank(message = "码表编号不能为空")
    private String codeTableNumber;

    @ApiModelProperty("码表名称")
    @NotBlank(message = "码表名称不能为空")
    @Length(max = 20, message = "码表名称最长为20")
    @Pattern(regexp = "^[\u4e00-\u9fa5_a-zA-Z]+$",message = "码表名称只能包含中文和字母")
    private String codeTableName;

    @ApiModelProperty("码表描述")
    private String codeTableDesc;
}
