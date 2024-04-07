package com.data.controller;


import com.data.dto.DataAssetField.SelectDataAssetFieldDto;
import com.data.service.DataAssetFieldService;
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
 * 数据资产字段管理
 * <p>
 * 数据资产字段表 前端控制器
 * </p>
 * @module 数据工厂数据资产字段管理
 * @author zhangr132
 * @since 2024-03-29
 */
@RestController
@Api("数据资产字段管理")
@RequestMapping("/data-asset-field")
public class DataAssetFieldController {
    private Logger logger= LoggerFactory.getLogger(getClass());
    @Autowired
    private DataAssetFieldService dataAssetFieldService;

    @ApiOperation("数据资产字段查询")
    @PostMapping("/selectDataAssetField")
    public R selectDataAssetField(@Valid @RequestBody SelectDataAssetFieldDto selectDataAssetFieldDto){
        logger.info("正在查询数据标准信息");
        R result=dataAssetFieldService.selectDataAssetField(selectDataAssetFieldDto);
        return result;
    }

}
