package com.data.controller;


import com.data.dto.DataAsset.*;
import com.data.service.DataAssetService;
import com.data.service.DataStandardService;
import com.data.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
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
 * 数据资产管理
 * <p>
 * 数据资产表 前端控制器
 * </p>
 * @module 数据工厂数据资产管理
 * @author zhangr132
 * @since 2024-03-29
 */
@RestController
@Api("数据资产管理")
@RequestMapping("/data-asset")
public class DataAssetController {
    private Logger logger= LoggerFactory.getLogger(getClass());
    @Autowired
    private DataAssetService dataAssetService;

    @ApiOperation("数据资产查询")
    @PostMapping("/selectDataAsset")
    public R selectDataAsset(@Valid @RequestBody DataAssetPageDto dataAssetPageDto){
        logger.info("正在查询码表信息");
        R result=dataAssetService.selectDataAsset(dataAssetPageDto);
        return result;
    }

    @ApiOperation("数据资产新增")
    @PostMapping("/addDataAsset")
    public R addDataAsset(@Valid @RequestBody AddDataAssetDto addDataAssetDto){
        logger.info("正在新增数据资产");
        R result= dataAssetService.addDataAsset(addDataAssetDto);
        return result;
    }

    @ApiOperation("数据资产编辑")
    @PostMapping("/updateDataAsset")
    public R updateDataAsset(@Valid @RequestBody UpdateDataAssetDto updateDataAssetDto){
        logger.info("正在编辑数据资产");
        R result=dataAssetService.updateDataAsset(updateDataAssetDto);
        return  result;
    }

    @ApiOperation("数据资产删除")
    @PostMapping("/deleteDataAsset")
    public R deleteDataAsset(@Valid @RequestBody DeleteDataAssetDto deleteDataAssetDto){
        logger.info("正在删除数据资产");
        R result=dataAssetService.deleteDataAsset(deleteDataAssetDto);
        return result;
    }

    @ApiOperation("数据资产状态更改")
    @PostMapping("/stateDataAsset")
    public R stateDataAsset(@Valid @RequestBody StateDataAssetDto stateDataAssetDto){
        logger.info("正在更改数据资产状态");
        R r=dataAssetService.stateDataAsset(stateDataAssetDto);
        return r;
    }

    @ApiOperation("数据资产批量发布")
    @PostMapping("/batchPublishDataAsset")
    public R batchPublishDataAsset(@Valid @RequestBody List<DeleteDataAssetDto> deleteDataAssetDtoList){
        logger.info("正在批量发布数据资产");
        R result=dataAssetService.batchPublishDataAsset(deleteDataAssetDtoList);
        return result;
    }

    @ApiOperation("数据资产批量停用")
    @PostMapping("/batchStopDataAsset")
    public R batchStopDataAsset(@Valid @RequestBody List<DeleteDataAssetDto> deleteDataAssetDtoList){
        logger.info("正在批量停用数据资产");
        R result=dataAssetService.batchStopDataAsset(deleteDataAssetDtoList);
        return result;
    }
}
