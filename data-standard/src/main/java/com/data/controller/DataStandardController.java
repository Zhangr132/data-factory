package com.data.controller;


import com.data.service.DataStandardService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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

}

