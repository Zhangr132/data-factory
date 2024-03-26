package com.data.dto.CodeTable.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import com.data.dto.CodeValue.excel.CodeValueExcel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author zhangr132
 * @Date 2024/3/26 9:14
 * @注释
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CodeTableExcel {

    @Excel(name = "码表名称", width = 20, needMerge = true)
    private String codeTableName;

    @Excel(name = "码表描述", width = 28, needMerge = true)
    private String codeTableDesc;

    @ExcelCollection(name = "码值列表")
    private List<CodeValueExcel> codeValueExcelList;
}
