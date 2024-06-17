package com.data.controller;


import com.data.dto.*;
import com.data.service.ScriptService;
import com.data.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 脚本管理 前端控制器
 * </p>
 *
 * @author zhangr132
 * @since 2024-04-17
 */
@Api( "脚本管理")
@Slf4j
@RestController
@RequestMapping("/script")
public class ScriptController {
    @Autowired
    private ScriptService scriptService;

    @ApiOperation("脚本分页查询")
    @PostMapping("selectScriptPage")
    public R selectScriptPage(@Valid @RequestBody ScriptPageDto scriptPageDto) {
        log.info("正在分页查询脚本信息");
        R result = scriptService.selectScriptPage(scriptPageDto);
        return result;
    }

    @ApiOperation("脚本新增")
    @PostMapping("addScript")
    public R addScript(@Valid @RequestBody AddScriptDto addScriptDto){
        log.info("正在新增脚本信息");
        R result = scriptService.addScript(addScriptDto);
        return result;
    }

    @ApiOperation("脚本编辑")
    @PostMapping("updateScript")
    public R updateScript(@Valid @RequestBody UpdateScriptDto updateScriptDto){
        log.info("正在编辑脚本信息");
        R result = scriptService.updateScript(updateScriptDto);
        return result;
    }

    @ApiOperation("脚本删除")
    @PostMapping("deleteScript")
    public R deleteScript(@Valid @RequestBody DeleteScriptDto deleteScriptDto){
        log.info("正在删除脚本信息");
        R result = scriptService.deleteScript(deleteScriptDto);
        return result;
    }

    @ApiOperation("脚本状态更改")
    @PostMapping("stateScript")
    public R stateScript(@Valid @RequestBody StateScriptDto stateScriptDto){
        log.info("正在更改脚本状态");
        R result = scriptService.stateScript(stateScriptDto);
        return result;
    }

    @ApiOperation("脚本批量发布")
    @PostMapping("batchPublishScript")
    public R batchPublishScript(@Valid @RequestBody List<DeleteScriptDto> deleteScriptDtos){
        log.info("正在批量发布脚本");
        R result = scriptService.batchPublishScript(deleteScriptDtos);
        return result;
    }

    @ApiOperation("脚本批量停用")
    @PostMapping("batchStopScript")
    public R batchStopScript(@Valid @RequestBody List<DeleteScriptDto> deleteScriptDtos){
        log.info("正在批量停用脚本");
        R result = scriptService.batchStopScript(deleteScriptDtos);
        return result;
    }

    @ApiOperation("脚本批量分类")
    @PostMapping("batchClassifyScript")
    public R batchClassifyScript(@Valid @RequestBody List<ClassifyScriptDto> classifyScriptDtos) {
        log.info("正在批量分类脚本");
        R result = scriptService.batchClassifyScript(classifyScriptDtos);
        return result;
    }

}
