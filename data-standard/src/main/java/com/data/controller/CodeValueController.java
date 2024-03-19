package com.data.controller;


import com.data.dto.CodeValue.AddCodeValueDto;
import com.data.service.CodeValueService;
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
 * 码值管理
 * <p>
 * 码值表 前端控制器
 * </p>
 * @module 数据工厂码值管理
 * @author zhangr132
 * @since 2024-03-15
 */
@RestController
@RequestMapping("/codeValue")
public class CodeValueController {
    private Logger logger= LoggerFactory.getLogger(getClass());
    @Autowired
    private CodeValueService codeValueService;

    public R addCodeValue(@Valid @RequestBody AddCodeValueDto addCodeValueDto){
        logger.info("正在新增码值信息");
        R result = codeValueService.addCodeValue(addCodeValueDto);
        return result;
    }

}

