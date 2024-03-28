package com.data.controller;


import com.data.dto.CodeTable.AddCodeTableDto;
import com.data.dto.CodeTable.CodeTablePageDto;
import com.data.dto.CodeTable.UpdateCodeTableDto;
import com.data.dto.DataStandard.AddDataStandardDto;
import com.data.dto.DataStandard.DataStandardPageDto;
import com.data.dto.DataStandard.SelectEnumDto;
import com.data.service.DataStandardService;
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
@Api("数据标准管理模块")
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
        R result=dataStandardService.selectDataStandardEnum(selectEnumDto);
        return result;
    }
    @ApiOperation("数据标准新增")
    @PostMapping("/addDataStandard")
    public R addDataStandard(@Valid @RequestBody AddDataStandardDto addDataStandardDto){
        logger.info("正在新增数据标准信息");
        R result=dataStandardService.addDataStandard(addDataStandardDto);

        return result;
    }



}

