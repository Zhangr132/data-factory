package com.data.controller;


import com.data.dto.SourceDatabase.*;
import com.data.service.SourceDatabaseService;
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
import java.util.List;

/**
 * 数据库数据源管理
 * <p>
 * 数据库数据源 前端控制器
 * </p>
 * @module 数据工厂数据库数据源管理
 * @author zhangr132
 * @since 2024-04-02
 */
@RestController
@Api("数据库数据源管理")
@RequestMapping("/source-database")
public class SourceDatabaseController {
    private Logger logger= LoggerFactory.getLogger(getClass());
    @Autowired
    private SourceDatabaseService sourceDatabaseService;

    @ApiOperation("数据库数据源查询")
    @PostMapping("/selectSourceDatabase")
    public R selectSourceDatabase(@Valid @RequestBody SourceDatabasePageDto sourceDatabasePageDto){
        logger.info("正在查询数据库数据源信息");
        R result=sourceDatabaseService.selectSourceDatabase(sourceDatabasePageDto);
        return result;
    }

    @ApiOperation("数据库数据源新增")
    @PostMapping("/addSourceDatabase")
    public R addSourceDatabase(@Valid @RequestBody AddSourceDatabaseDto addSourceDatabaseDto){
        logger.info("正在新增数据库数据源信息");
        R result=sourceDatabaseService.addSourceDatabase(addSourceDatabaseDto);
        return result;
    }

    @ApiOperation("数据库连接测试")
    @PostMapping("/testDatabaseConnection")
    public R testDatabaseConnection(@Valid @RequestBody ConnectionDatabaseDto connectionDatabaseDto){
        logger.info("正在测试数据库连通性");
        R result=sourceDatabaseService.testDatabaseConnection(connectionDatabaseDto);
        return result;
    }

    @ApiOperation("数据库数据源编辑")
    @PostMapping("/updateSourceDatabase")
    public R updateSourceDatabase(@Valid @RequestBody UpdateSourceDatabaseDto updateSourceDatabaseDto){
        logger.info("正在编辑数据库数据源信息");
        R result=sourceDatabaseService.updateSourceDatabase(updateSourceDatabaseDto);
        return result;
    }

    @ApiOperation("数据库数据源删除")
    @PostMapping("/deleteSourceDatabase")
    public R deleteSourceDatabase(@Valid @RequestBody DeleteSourceDatabaseDto deleteSourceDatabaseDto){
        logger.info("正在删除数据库数据源");
        R result=sourceDatabaseService.deleteSourceDatabase(deleteSourceDatabaseDto);
        return result;
    }

    @ApiOperation("数据库数据源状态更改")
    @PostMapping("/stateSourceDatabase")
    public R stateSourceDatabase(@Valid @RequestBody StateSourceDatabaseDto stateSourceDatabaseDto){
        logger.info("正在更改数据库数据源状态");
        R result=sourceDatabaseService.stateSourceDatabase(stateSourceDatabaseDto);
        return result;
    }

    @ApiOperation("数据库数据源批量发布")
    @PostMapping("/batchPublishSourceDatabase")
    public R batchPublishSourceDatabase(@Valid @RequestBody List<DeleteSourceDatabaseDto> deleteSourceDatabaseDtos){
        logger.info("正在批量发布数据库数据源");
        R result=sourceDatabaseService.batchPublishSourceDatabase(deleteSourceDatabaseDtos);
        return result;
    }

    @ApiOperation("数据库数据源批量停用")
    @PostMapping("/batchStopSourceDatabase")
    public R batchStopSourceDatabase(@Valid @RequestBody List<DeleteSourceDatabaseDto> deleteSourceDatabaseDtos){
        logger.info("正在批量停用数据库数据源");
        R result=sourceDatabaseService.batchStopSourceDatabase(deleteSourceDatabaseDtos);
        return result;
    }
}
