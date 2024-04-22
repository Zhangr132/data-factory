package com.data.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.data.dto.CodeTable.excel.ExportCodeTableExcel;
import com.data.dto.DataStandard.*;
import com.data.dto.DataStandard.excel.DataStandardExcel;
import com.data.dto.DataStandard.excel.ExportDataStandardExcel;
import com.data.entity.DataStandard;
import com.data.service.DataStandardService;
import com.data.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据标准管理
 * <p>
 * 数据标准表 前端控制器
 * </p>
 * @module 数据工厂数据标准管理
 * @author zhangr132
 * @since 2024-03-15
 */
@RestController
@RequestMapping("/dataStandard")
@Api("数据标准管理")
public class DataStandardController {
    private Logger logger= LoggerFactory.getLogger(getClass());
    @Autowired
    private DataStandardService dataStandardService;

    @ApiOperation("数据标准查询")
    @PostMapping("/selectDataStandard")
    public R selectDataStandard(@Valid @RequestBody DataStandardPageDto dataStandardPageDto){
        logger.info("正在查询数据标准信息");
        R result=dataStandardService.selectDataStandard(dataStandardPageDto);
        return result;
    }

    @ApiOperation("数据标准枚举查询")
    @PostMapping("/selectDataStandardEnum")
    public R selectDataStandardEnum(@Valid @RequestBody SelectEnumDto selectEnumDto){
        logger.info("正在查询数据标准枚举");
        List result=dataStandardService.selectDataStandardEnum(selectEnumDto);
        if (result.isEmpty()){
            return R.Failed("查询结果为空");
        }
        return R.Success(result);
    }
    @ApiOperation("数据标准新增")
    @PostMapping("/addDataStandard")
    public R addDataStandard(@Valid @RequestBody AddDataStandardDto addDataStandardDto){
        logger.info("正在新增数据标准信息");
        R result=dataStandardService.addDataStandard(addDataStandardDto);

        return result;
    }

    @ApiOperation("数据标准编辑")
    @PostMapping("/updateDataStandard")
    public R updateDataStandard(@Valid @RequestBody UpdateDataStandardDto updateDataStandardDto){
        logger.info("正在进入数据标准编辑");
        boolean row=dataStandardService.updateDataStandard(updateDataStandardDto);
        if (row){
            return R.Success("编辑成功");
        }
        return R.Failed("未选中目标或目标已发布");
    }

    @ApiOperation("数据标准状态更改")
    @PostMapping("/stateDataStandard")
    public R stateDataStandard(@Valid @RequestBody StateDataStandardDto stateDataStandardDto){
        logger.info("正在进入数据标准状态更改");
        boolean row=dataStandardService.stateDataStandard(stateDataStandardDto);
        if (row){
            return R.Success("状态更改成功");
        }
        return R.Failed("目标不存在");
    }

    @ApiOperation("数据标准删除")
    @PostMapping("/deleteDataStandard")
    public R deleteDataStandard(@Valid @RequestBody DeleteDataStandardDto deleteDataStandardDto){
        logger.info("正在进入数据标准删除");
        boolean row=dataStandardService.deleteDataStandard(deleteDataStandardDto);
        if (row){
            return R.Success("删除成功");
        }
        return R.Failed("目标不存在 或 目标不处于未发布状态");
    }

    @ApiOperation("数据标准批量发布")
    @PostMapping("/batchPublishDataStandard")
    public R batchPublishDataStandard(@Valid @RequestBody List<DeleteDataStandardDto> deleteDataStandardDtos){
        logger.info("正在进入数据标准批量发布");
        boolean result=dataStandardService.batchPublishDataStandard(deleteDataStandardDtos);
        if (result){
            return R.Success("批量发布成功");
        }
        return R.Failed("批量发布失败：只能发布未发布的数据");
    }

    @ApiOperation("数据标准批量停用")
    @PostMapping("/batchStopDataStanadard")
    public R batchStopDataStanadard(@Valid @RequestBody List<DeleteDataStandardDto> deleteDataStandardDtos){
        logger.info("正在进入码表批量停用");
        boolean result=dataStandardService.batchStopDataStanadard(deleteDataStandardDtos);
        if (result){
            return R.Success("批量停用成功");
        }
        return R.Failed("批量停用失败：只能发布停用已发布的数据");
    }

    @ApiOperation("数据标准数据导出")
    @GetMapping("/exportDataStandardExcel")
    public void  exportDataStandardExcel( ) throws IOException {
        logger.info("正在进入数据标准数据导出");
        //查询数据

        // 将 DataStandard 数据复制为 ExportDataStandardExcel 类型的数据
        List<ExportDataStandardExcel> exportList = new ArrayList<>();
        QueryWrapper queryWrapper=new QueryWrapper<>();
        queryWrapper.select("id","data_standard_code","data_standard_cn_name","data_standard_en_name","data_standard_explain",
                "data_standard_source_organization","data_standard_type","data_standard_length","data_standard_accuracy","data_standard_default_value",
                "data_standard_value_max","data_standard_value_min","data_standard_enumeration_range","data_standard_state","data_standard_is_blank",
                "delete_flag","create_time","update_time");
        List<DataStandard> dataStandardList = dataStandardService.list(queryWrapper);
        dataStandardList.forEach(
                dataStandard -> {
                    //码表
                    ExportDataStandardExcel exportDataStandardExcel = new ExportDataStandardExcel();
                    //将一个对象（dataStandard）的属性复制到另一个对象（exportDataStandardExcel）
                    BeanUtils.copyProperties(dataStandard,exportDataStandardExcel);

                    if (!ObjectUtils.isEmpty(dataStandard.getDataStandardEnumerationRange())){
                        System.out.println(dataStandard.getDataStandardEnumerationRange());
                        logger.info("查询码表数据");
                        List<ExportCodeTableExcel> exportCodeTableExcels = dataStandardService.selectCodeTableExcel(dataStandard.getDataStandardEnumerationRange());
                        //保存码值数据到 exportDataStandardExcel 的 exportCodeTableExcelList
                        exportDataStandardExcel.setExportCodeTableExcelList(exportCodeTableExcels);
                    }



                    exportList.add(exportDataStandardExcel);
                }
        );

        String fileName="./excel文件/DataStandard/导出文件/数据标准数据.xls";
        //将数据列表导出到 Excel 文件中
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), ExportDataStandardExcel.class,exportList);
        //使用了FileOutputStream类来创建一个文件输出流，将数据写入到名为fileName的文件中
        FileOutputStream fos = new FileOutputStream(fileName);
        //将 Excel 文档数据写入到文件输出流 fos 中，实现将 Excel 数据写入到文件中
        workbook.write(fos);
        fos.close();
        logger.info("数据标准excel导出成功");
    }

    @ApiOperation("数据标准模板下载")
    @GetMapping("/downloadDataStandardTemplate")
    public void  downloadDataStandardTemplate() throws IOException {
        logger.info("正在进入下载数据标准模板");

        String fileName="./excel文件/DataStandard/数据标准模板/数据标准模板.xls";
        //将数据列表导出到 Excel 文件中
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), DataStandardExcel.class, new ArrayList());
        //使用了FileOutputStream类来创建一个文件输出流，将数据写入到名为fileName的文件中
        FileOutputStream fos = new FileOutputStream(fileName);
        //将 Excel 文档数据写入到文件输出流 fos 中，实现将 Excel 数据写入到文件中
        workbook.write(fos);
        fos.close();
        logger.info("数据标准模板下载成功");

    }
    @ApiOperation("数据标准导入")
    @PostMapping("/importDataStandardExcel")
    public R importDataStandardExcel(@RequestParam("static/file") MultipartFile file ) throws Exception {
        logger.info("正在进入数据标准导入");

        if (file.isEmpty()) {
            return R.Failed("上传的文件为空");
//            throw new Exception("上传的文件为空");
        }

        // 设置EasyPOI的导入参数
        ImportParams importParams = new ImportParams();
        // 设置头部行数，通常主表头部是1行
        importParams.setHeadRows(1);
        importParams.setTitleRows(0);
        //收集返回的信息
        List results= new ArrayList<>();
        try (InputStream inputStream = file.getInputStream()) {
            // 解析Excel，得到主表和子表的数据
            List<DataStandardExcel> dataStandardExcelList = ExcelImportUtil.importExcel(inputStream, DataStandardExcel.class,
                    importParams);

            // 打印或处理解析得到的数据
            System.out.println("dataStandardExcelList：" + dataStandardExcelList);

            List<DataStandardExcel> newDataStandardExcelList = new ArrayList<>();

            for (DataStandardExcel dataStandardExcel : dataStandardExcelList) {
                DataStandardExcel newDataStandardExcel = new DataStandardExcel();
                // 将 dataStandardExcel 对象的属性赋值给 newDataStandardExcel 对象的对应属性
                newDataStandardExcel.setDataStandardCnName(dataStandardExcel.getDataStandardCnName());
                newDataStandardExcel.setDataStandardEnName(dataStandardExcel.getDataStandardEnName());
                newDataStandardExcel.setDataStandardExplain(dataStandardExcel.getDataStandardExplain());
                newDataStandardExcel.setDataStandardSourceOrganization(dataStandardExcel.getDataStandardSourceOrganization());
                newDataStandardExcel.setDataStandardType(dataStandardExcel.getDataStandardType());
                newDataStandardExcel.setDataStandardLength(dataStandardExcel.getDataStandardLength());
                newDataStandardExcel.setDataStandardAccuracy(dataStandardExcel.getDataStandardAccuracy());
                newDataStandardExcel.setDataStandardDefaultValue(dataStandardExcel.getDataStandardDefaultValue());
                newDataStandardExcel.setDataStandardValueMax(dataStandardExcel.getDataStandardValueMax());
                newDataStandardExcel.setDataStandardValueMin(dataStandardExcel.getDataStandardValueMin());
                newDataStandardExcel.setDataStandardEnumerationRange(dataStandardExcel.getDataStandardEnumerationRange());
                newDataStandardExcel.setDataStandardIsBlank(dataStandardExcel.getDataStandardIsBlank());

                // 将转换后的实体类对象添加到列表中
                newDataStandardExcelList.add(newDataStandardExcel);
                System.out.println("newDataStandardExcelList：" + newDataStandardExcelList);
                // TODO: 将解析得到的数据保存到数据库
                R result = dataStandardService.saveDataStandardExcels(newDataStandardExcel);
                results.add(result);
            }


        } catch (Exception e) {
            logger.info("数据标准导入失败");
            throw new Exception("文件导入失败", e);
        }
        logger.info("数据标准导入结束");
        return R.Success("导入返回的日志信息",results);

    }
}

