package com.data.dto.CodeValue.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 码值导出 excel 模板类
 * @Author zhangr132
 * @Date 2024/3/25 15:22
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
public class ExportCodeValueExcel {
    @Excel(name = "ID", width = 10)
    private Integer id;


    @Excel(name = "码值名称", width = 20)
    private String codeValueName;

    @Excel(name = "码值取值", width = 20)
    private String codeValueValue;

    @Excel(name = "码值含义", width = 20)
    private String codeValueDesc;

    @Excel(name = "码值状态", width = 10, replace = {"未删除_0", "已删除_1"})
    private Integer deleteFlag;

    @Excel(name = "码值更新时间", width = 20, format = "yyyy-MM-dd")
    private Date updateTime;
}
