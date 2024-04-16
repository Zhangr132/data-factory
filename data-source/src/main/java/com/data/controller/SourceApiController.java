package com.data.controller;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.data.dto.SourceApi.*;
import com.data.service.SourceApiService;
import com.data.utils.JacksonUtil;
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
 * 接口数据源管理
 * <p>
 * 接口数据源 前端控制器
 * </p>
 * @module 数据工厂接口数据源管理
 * @author zhangr132
 * @since 2024-04-02
 */
@RestController
@Api("接口数据源管理")
@RequestMapping("/source-api")
public class SourceApiController {
    private Logger logger= LoggerFactory.getLogger(getClass());
    @Autowired
    private SourceApiService sourceApiService;

    @ApiOperation("接口数据源查询")
    @PostMapping("/selectSourceApi")
    public R selectSourceApi(@Valid @RequestBody SourceApiPageDto sourceApiPageDto){
        logger.info("正在查询接口数据源信息");
        R result=sourceApiService.selectSourceApi(sourceApiPageDto);
        return result;
    }

    @ApiOperation("接口数据源新增")
    @PostMapping("/addSourceApi")
    public R addSourceApi(@Valid @RequestBody AddSourceApiDto addSourceApiDto){
        logger.info("正在新增接口数据源");
        R result=sourceApiService.addSourceApi(addSourceApiDto);
        return result;
    }

    @ApiOperation("接口数据源修改")
    @PostMapping("/updateSourceApi")
    public R updateSourceApi(@Valid @RequestBody UpdateSourceApiDto updateSourceApiDto){
        logger.info("正在修改接口数据源");
        R result=sourceApiService.updateSourceApi(updateSourceApiDto);
        return result;
    }


    @ApiOperation("接口数据源删除")
    @PostMapping("/deleteSourceApi")
    public R deleteSourceApi(@Valid @RequestBody DeleteSourceApiDto deleteSourceApiDto) {
        logger.info("正在删除接口数据源");
        R result = sourceApiService.deleteSourceApi(deleteSourceApiDto);
        return result;
    }

    @ApiOperation("接口数据源测试")
    @PostMapping("/testSourceApi")
    public R testSourceApi(@Valid @RequestBody TestSourceApiDto testSourceApiDto) {
        logger.info("正在测试接口数据源");
        R result = sourceApiService.testSourceApi(testSourceApiDto);
        return result;
    }

    @ApiOperation("接口数据源状态更改")
    @PostMapping("/stateSourceApi")
    public R stateSourceApi(@Valid @RequestBody StateSourceApiDto stateSourceApiDto) {
        logger.info("正在更改接口数据源状态");
        R result = sourceApiService.stateSourceApi(stateSourceApiDto);
        return result;
    }

    @ApiOperation("接口数据源批量发布")
    @PostMapping("/batchPublishSourceApi")
    public R batchPublishSourceApi(@Valid @RequestBody List<DeleteSourceApiDto> deleteSourceApiDtos) {
        logger.info("正在批量发布接口数据源");
        R result = sourceApiService.batchPublishSourceApi(deleteSourceApiDtos);
        return result;
    }

    @ApiOperation("接口数据源批量停用")
    @PostMapping("/batchStopSourceApi")
    public R batchStopSourceApi(@Valid @RequestBody List<DeleteSourceApiDto> deleteSourceApiDtos) {
        logger.info("正在批量停用接口数据源");
        R result = sourceApiService.batchStopSourceApi(deleteSourceApiDtos);
        return result;
    }
}
