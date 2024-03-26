package com.data.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.data.dto.CodeTable.*;
import com.data.dto.CodeTable.excel.CodeTableExcel;
import com.data.dto.CodeTable.excel.ExportCodeTableExcel;
import com.data.dto.CodeValue.excel.ExportCodeValueExcel;
import com.data.entity.CodeTable;
import com.data.mapper.CodeValueMapper;
import com.data.service.CodeTableService;
import com.data.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 码表管理
 * <p>
 * 码表管理 前端控制器
 * </p>
 * @module 数据工厂码表管理
 * @author zhangr132
 * @since 2024-03-15
 */
@RestController
@RequestMapping("/codeTable")
@Api("码表管理模块")
public class CodeTableController {
    private Logger logger= LoggerFactory.getLogger(getClass());
    @Autowired
    private CodeTableService codeTableService;

    @Resource
    private CodeValueMapper codeValueMapper;

    @ApiOperation("码表查询")
    @PostMapping("/selectCodeTable")
    public R selectCodeTable(@Valid @RequestBody CodeTablePageDto codeTablePageDto){
        logger.info("正在查询码表信息");
        R result=codeTableService.selectCodeTable(codeTablePageDto);
        return result;
    }

    @ApiOperation("码表新增")
    @PostMapping("/addCodeTable")
    public R addCodeTable(@Valid @RequestBody AddCodeTableDto addCodeTableDto){
        logger.info("正在新增码表信息");
        R result=codeTableService.addCodeTable(addCodeTableDto);

        return result;
    }

    @ApiOperation("码表编辑")
    @PostMapping("/updateCodeTable")
    public R updateCodeTable(@Valid @RequestBody UpdateCodeTableDto updateCodeTableDto){
        logger.info("正在进入码表编辑");
        boolean row=codeTableService.updateCodeTable(updateCodeTableDto);
        if (row){
            return R.Success("编辑成功");
        }
        return R.Failed("未选中目标或目标已发布");
    }

    @ApiOperation("码表状态")
    @PostMapping("/stateCodeTable")
    public R stateCodeTable(@Valid @RequestBody StateCodeTableDto stateCodeTableDto){
        logger.info("正在进入码表状态更改");
        boolean row=codeTableService.stateCodeTable(stateCodeTableDto);
        if (row){
            return R.Success("状态更改成功");
        }
        return R.Failed("目标不存在");
    }

    @ApiOperation("码表删除")
    @PostMapping("/deleteCodeTable")
    public R deleteCodeTable(@Valid @RequestBody DeleteCodeTableDto deleteCodeTableDto){
        logger.info("正在进入码表状态更改");
        boolean row=codeTableService.deleteCodeTable(deleteCodeTableDto);
        if (row){
            return R.Success("删除成功");
        }
        return R.Failed("目标不存在 或 目标不处于未发布状态");
    }

    @ApiOperation("码表批量发布")
    @PostMapping("/batchPublish")
    public R batchPublish(@Valid @RequestBody List<StateCodeTableDto> stateCodeTableDtos){
        logger.info("正在进入码表批量发布");
        boolean result=codeTableService.batchPublish(stateCodeTableDtos);
        if (result){
            return R.Success("批量发布成功");
        }
        return R.Failed("批量发布失败：只能发布未发布的数据");
    }

    @ApiOperation("码表批量停用")
    @PostMapping("/batchStop")
    public R batchStop(@Valid @RequestBody List<StateCodeTableDto> stateCodeTableDtos){
        logger.info("正在进入码表批量停用");
        boolean result=codeTableService.batchStop(stateCodeTableDtos);
        if (result){
            return R.Success("批量停用成功");
        }
        return R.Failed("批量停用失败：只能发布停用已发布的数据");
    }

    @ApiOperation("码表数据导出")
    @GetMapping("/exportCodeTableExcel")
    public void  exportCodeTableExcel( ModelMap map, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("正在进入码表数据导出");
        //查询数据

        // 将 CodeTable 数据复制为 ExportCodeTableExcel 类型的数据
        List<ExportCodeTableExcel> exportList = new ArrayList<>();
        List<CodeTable> codeTableList = codeTableService.list(null);
        codeTableList.forEach(
                codeTable -> {
                    //码表
                    ExportCodeTableExcel exportCodeTableExcel = new ExportCodeTableExcel();
                    //将一个对象（codeTable）的属性复制到另一个对象（exportCodeTableExcel）
                    BeanUtils.copyProperties(codeTable,exportCodeTableExcel);
                    List<ExportCodeValueExcel> exportCodeValueExcels = codeValueMapper.selectByCodeTableNumber(codeTable.getCodeTableNumber());
                    //保存码值数据到ExportCodeTableExcel的exportCodeValueExcelList
                    exportCodeTableExcel.setExportCodeValueExcelList(exportCodeValueExcels);

                    exportList.add(exportCodeTableExcel);
                }
        );

        String fileName="./excel文件/导出文件/码表数据.xls";
        //将数据列表导出到 Excel 文件中
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), ExportCodeTableExcel.class,exportList);
        //使用了FileOutputStream类来创建一个文件输出流，将数据写入到名为fileName的文件中
        FileOutputStream fos = new FileOutputStream(fileName);
        //将 Excel 文档数据写入到文件输出流 fos 中，实现将 Excel 数据写入到文件中
        workbook.write(fos);
        fos.close();

    }

    @ApiOperation("码表导入")
    @PostMapping("/importCodeTableExcel")
    public void importCodeTableExcel(@RequestParam("file") MultipartFile file ) throws Exception {
        logger.info("正在进入码表导入");

        if (file.isEmpty()) {
            throw new Exception("上传的文件为空");
        }

        // 设置EasyPOI的导入参数
        ImportParams importParams = new ImportParams();
        // 设置头部行数，通常主表头部是1行
        importParams.setHeadRows(2);
        importParams.setTitleRows(0);
        try (InputStream inputStream = file.getInputStream()) {
            // 解析Excel，得到主表和子表的数据
            List<CodeTableExcel> codeTableExcelList = ExcelImportUtil.importExcel(inputStream, CodeTableExcel.class,
                    importParams);

            // 打印或处理解析得到的数据
            System.out.println("codeTableExcelList："+codeTableExcelList);

            List<CodeTableExcel> newCodeTableExcelList = new ArrayList<>();
            for (CodeTableExcel codeTableExcel : codeTableExcelList) {
                CodeTableExcel newCodeTableExcel = new CodeTableExcel();
                // 将 codeTableExcel 对象的属性赋值给 newCodeTableExcel 对象的对应属性
                newCodeTableExcel.setCodeTableName(codeTableExcel.getCodeTableName());
                newCodeTableExcel.setCodeTableDesc(codeTableExcel.getCodeTableDesc());
                newCodeTableExcel.setCodeValueExcelLists(codeTableExcel.getCodeValueExcelLists());

                // 将转换后的实体类对象添加到列表中
                newCodeTableExcelList.add(newCodeTableExcel);
                System.out.println("newCodeTableExcelList："+newCodeTableExcelList);
                // TODO: 将解析得到的数据保存到数据库
                codeTableService.saveCodeTableExcels(newCodeTableExcel);
            }


        } catch (Exception e) {
            throw new Exception("文件导入失败", e);

        }
    }

    @ApiOperation("码表模板下载")
    @GetMapping("/downloadCodeTableTemplate")
    public void  downloadCodeTableTemplate() throws IOException {
        logger.info("正在进入下载码表模板");

        String fileName="./excel文件/码表模板/码表模板.xls";
        //将数据列表导出到 Excel 文件中
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), CodeTableExcel.class, new ArrayList());
        //使用了FileOutputStream类来创建一个文件输出流，将数据写入到名为fileName的文件中
        FileOutputStream fos = new FileOutputStream(fileName);
        //将 Excel 文档数据写入到文件输出流 fos 中，实现将 Excel 数据写入到文件中
        workbook.write(fos);
        fos.close();

    }

}

