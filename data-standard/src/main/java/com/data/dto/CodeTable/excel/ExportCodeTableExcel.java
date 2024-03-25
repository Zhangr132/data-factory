package com.data.dto.CodeTable.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import com.data.dto.CodeValue.AddCodeValueDto;
import com.data.dto.CodeValue.excel.ExportCodeValueExcel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 码表导出 excel 模板类
 * @Author zhangr132
 * @Date 2024/3/25 15:07
 * @注释
 * name：Excel中的列名；
 * width：指定列的宽度；
 * needMerge：是否需要纵向合并单元格；
 * format：当属性为时间类型时，设置时间的导出导出格式；
 * desensitizationRule：数据脱敏处理，3_4表示只显示字符串的前3位和后4位，其他为*号；
 * replace：对属性进行替换；
 * suffix：对数据添加后缀。
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExportCodeTableExcel {
    @Excel(name = "码表编号",width = 20, needMerge = true)
    private String codeTableNumber;

    @Excel(name = "码表名称", width = 20, needMerge = true)
    private String codeTableName;

    @Excel(name = "码表描述", width = 28, needMerge = true)
    private String codeTableDesc;

    @Excel(name = "码表状态", width = 10, needMerge = true, replace = {"未发布_0", "已发布_1","已停用_2"})
    private Integer codeTableState;

    @Excel(name = "是否删除", width = 10, needMerge = true, replace = {"未删除_0", "已删除_1"})
    private Integer deleteFlag;

    @Excel(name = "码表更新时间", width = 20, needMerge = true, format = "yyyy-MM-dd")
    private Date updateTime;

    @ExcelCollection(name = "码值列表")
    private List<ExportCodeValueExcel> exportCodeValueExcelList;
}
