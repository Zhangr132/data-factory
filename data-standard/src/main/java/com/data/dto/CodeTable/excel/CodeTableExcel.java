package com.data.dto.CodeTable.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import com.data.dto.CodeValue.excel.CodeValueExcel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 码表excel模板 数据传输对象
 * @Author zhangr132
 * @Date 2024/3/26 9:14
 * @注释
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CodeTableExcel {

    @Excel(name = "码表名称", width = 20, needMerge = true)
    @NotBlank(message = "码表名称不能为空(包含空格)")
    @Length(max = 20, message = "码表名称最长为20")
    @Pattern(regexp = "^[\u4e00-\u9fa5_a-zA-Z]+$",message = "码表名称只能包含中文和字母")
    private String codeTableName;

    @Excel(name = "码表描述", width = 28, needMerge = true)
    private String codeTableDesc;

    @ExcelCollection(name = "码值列表")
    private List<CodeValueExcel> codeValueExcelLists;
}
