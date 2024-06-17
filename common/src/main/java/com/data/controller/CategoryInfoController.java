package com.data.controller;


import com.data.dto.AddCategoryInfoDto;
import com.data.dto.DeleteCategoryInfoDto;
import com.data.dto.SelectCategoryInfoDto;
import com.data.dto.UpdateCategoryInfoDto;
import com.data.service.CategoryInfoService;
import com.data.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * 分类信息表(导航目录) 前端控制器
 * </p>
 * @author zhangr132
 * @since 2024-03-29
 */
@Slf4j
@Api("分类信息管理")
@RestController
@RequestMapping("/category-info")
public class CategoryInfoController {
    @Autowired
    private CategoryInfoService categoryInfoService;

    @ApiOperation("分类信息列表")
    @RequestMapping("/listCategoryInfo")
    public R listCategoryInfo(String categoryCode) {
        log.info("正在获取分类信息列表");
        R result = categoryInfoService.listCategoryInfo(categoryCode);
        return result;
    }

    @ApiOperation("分类信息查询")
    @RequestMapping("/selectCategoryInfo")
    public R selectCategoryInfo(@Valid @RequestBody SelectCategoryInfoDto selectCategoryInfoDto){
        log.info("正在查询分类信息");
        R result = categoryInfoService.selectCategoryInfo(selectCategoryInfoDto);
        return result;
    }

    @ApiOperation("分类信息新增")
    @RequestMapping("/addCategoryInfo")
    public R addCategoryInfo(@Valid @RequestBody AddCategoryInfoDto addCategoryInfoDto){
        log.info("正在新增分类信息");
        R result = categoryInfoService.addCategoryInfo(addCategoryInfoDto);
        return result;
    }

    @ApiOperation("分类信息修改")
    @PostMapping("updateCategoryInfo")
    public R updateCategoryInfo(@Valid @RequestBody UpdateCategoryInfoDto updateCategoryInfoDto) {
        log.info("正在修改分类信息");
        R result = categoryInfoService.updateCategoryInfo(updateCategoryInfoDto);
        return result;
    }

    @ApiOperation("分类信息删除")
    @PostMapping("deleteCategoryInfo")
    public R deleteCategoryInfo(@Valid @RequestBody DeleteCategoryInfoDto deleteCategoryInfoDto){
        log.info("正在删除分类信息");
        R result = categoryInfoService.deleteCategoryInfo(deleteCategoryInfoDto);
        return result;
    }
}
