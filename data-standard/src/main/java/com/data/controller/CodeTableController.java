package com.data.controller;


import com.data.dto.CodeTable.AddCodeTableDto;
import com.data.dto.CodeTable.CodeTablePageDto;
import com.data.dto.CodeTable.StateCodeTableDto;
import com.data.dto.CodeTable.UpdateCodeTableDto;
import com.data.service.CodeTableService;
import com.data.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

}

