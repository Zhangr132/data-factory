package com.data.dto.CodeValue.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author zhangr132
 * @Date 2024/3/26 9:20
 * @注释
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CodeValueExcel {

    @Excel(name = "码值名称", width = 20)
    private String codeValueName;

    @Excel(name = "码值取值", width = 20)
    private String codeValueValue;

    @Excel(name = "码值含义", width = 20)
    private String codeValueDesc;

}
