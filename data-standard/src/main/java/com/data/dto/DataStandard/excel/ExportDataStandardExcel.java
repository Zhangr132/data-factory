package com.data.dto.DataStandard.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.data.dto.CodeTable.excel.ExportCodeTableExcel;
import com.data.dto.CodeValue.excel.ExportCodeValueExcel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author zhangr132
 * @Date 2024/3/28 11:50
 * @注释
 */
@Data
public class ExportDataStandardExcel {
    @Excel(name = "数据标准id",width = 20, needMerge = true)
    private Integer id;

    @Excel(name = "标准编号",width = 20, needMerge = true)
    private String dataStandardCode;

    @Excel(name = "中文名称",width = 20, needMerge = true)
    private String dataStandardCnName;

    @Excel(name = "英文名称",width = 20, needMerge = true)
    private String dataStandardEnName;

    @Excel(name = "标准说明",width = 20, needMerge = true)
    private String dataStandardExplain;

    @Excel(name = "来源机构编码",width = 20, needMerge = true)
    private String dataStandardSourceOrganization;

    @Excel(name = "数据类型",width = 20, needMerge = true)
    private String dataStandardType;

    @Excel(name = "数据长度",width = 20, needMerge = true)
    private Long dataStandardLength;

    @Excel(name = "数据精度",width = 20, needMerge = true)
    private Integer dataStandardAccuracy;

    @Excel(name = "默认值",width = 20, needMerge = true)
    private String dataStandardDefaultValue;

    @Excel(name = "取值范围-最大值",width = 20, needMerge = true)
    private String dataStandardValueMax;

    @Excel(name = "取值范围-最小值",width = 20, needMerge = true)
    private String dataStandardValueMin;

    @Excel(name = "枚举范围",width = 20, needMerge = true)
    private String dataStandardEnumerationRange;

    @Excel(name = "数据标准状态",width = 20, needMerge = true,replace = {"未发布_0", "已发布_1","已停用_2"})
    private Integer dataStandardState;

    @Excel(name = "是否可为空",width = 20, needMerge = true,replace = {"不可为空_0", "可为空_1"})
    private Integer dataStandardIsBlank;

    @Excel(name = "删除标识符",width = 20, needMerge = true,replace = {"未删除_0", "已删除_1"})
    private Integer deleteFlag;

    @Excel(name = "创建时间",width = 20, needMerge = true,format = "yyyy-MM-dd")
    private Date createTime;

    @Excel(name = "修改时间",width = 20, needMerge = true,format = "yyyy-MM-dd")
    private Date updateTime;

    @ExcelCollection(name = "码表列表")
    private List<ExportCodeTableExcel> exportCodeTableExcelList;
}
