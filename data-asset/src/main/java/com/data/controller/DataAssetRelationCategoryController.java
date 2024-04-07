package com.data.controller;


import com.data.service.DataAssetRelationCategoryService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * 数据资产分类管理
 * <p>
 * 数据资产分类表 前端控制器
 * </p>
 * @module 数据工厂数据资产分类管理
 * @author zhangr132
 * @since 2024-03-29
 */
@RestController
@Api("数据资产分类管理")
@RequestMapping("/data-asset-relation-category")
public class DataAssetRelationCategoryController {
    private Logger logger= LoggerFactory.getLogger(getClass());
    @Autowired
    private DataAssetRelationCategoryService dataAssetRelationCategoryService;

}
