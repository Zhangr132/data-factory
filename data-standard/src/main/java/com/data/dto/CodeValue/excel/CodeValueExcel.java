package com.data.dto.CodeValue.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

/**
 * 码值excel模板 数据传输对象
 * @Author zhangr132
 * @Date 2024/3/26 9:20
 * @注释
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CodeValueExcel {

    @Excel(name = "码值名称", width = 20)
    @NotBlank(message = "码值名称不能为空")
    @Length(max = 10, message = "码值名称最长为10")
    @Pattern(regexp = "^[\u4e00-\u9fa5a-zA-Z]+$",message = "码值名称只能包含中文和字母")
    private String codeValueName;

    @Excel(name = "码值取值", width = 20)
    @NotEmpty(message = "码值取值不能为空")
    private String codeValueValue;

    @Excel(name = "码值含义", width = 20)
    private String codeValueDesc;

    private String codeTableNumber;

}
