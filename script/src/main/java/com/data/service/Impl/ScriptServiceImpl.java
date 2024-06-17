package com.data.service.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.data.dto.*;
import com.data.entity.Script;
import com.data.mapper.ScriptMapper;
import com.data.service.ScriptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.utils.R;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 脚本管理 服务实现类
 * </p>
 *
 * @author zhangr132
 * @since 2024-04-17
 */
@Slf4j
@Service
public class ScriptServiceImpl extends ServiceImpl<ScriptMapper, Script> implements ScriptService {
    @Autowired
    private ScriptMapper scriptMapper;

    /**
     * 脚本信息分页查询
     * @param scriptPageDto
     * @return
     */
    @Override
    public R selectScriptPage(ScriptPageDto scriptPageDto) {
        log.info("正在处理脚本查询请求");
        //将 pageNumber 和 pageSize 存到Page中
        Page<Script> page = new Page<>(scriptPageDto.getPageNumber(), scriptPageDto.getPageSize());

        MPJLambdaWrapper<Script> wrapper = new MPJLambdaWrapper<>();
        wrapper
                .selectAll(Script.class)
                .eq(ObjectUtils.isNotEmpty(scriptPageDto.getScriptState()),Script::getScriptState, scriptPageDto.getScriptState())
                .like(ObjectUtils.isNotEmpty(scriptPageDto.getScriptName()),Script::getScriptName, scriptPageDto.getScriptName())
                .orderByAsc(Script::getScriptState)
                .orderByDesc(Script::getUpdateTime);

        IPage<Script> scriptIPage= scriptMapper.selectPage(page, wrapper);
        List<Script> records=scriptIPage.getRecords();
        Map responseData=new HashMap<>();
        responseData.put("data", records);
        responseData.put("total", scriptIPage.getTotal()); // 总记录数
        responseData.put("pageSize", scriptIPage.getSize()); // 每页显示数量
        responseData.put("pageNumber", scriptIPage.getCurrent()); // 当前页码
//        responseData.put("orders", scriptIPage.orders()); // 排序信息
//        responseData.put("optimizeCountSql", scriptIPage.optimizeCountSql()); // 是否优化count语句
        responseData.put("pages", scriptIPage.getPages()); // 总页数
        return R.Success(responseData);
    }

    @Override
    public R addScript(AddScriptDto addScriptDto) {
        log.info("正在处理脚本新增请求");
        return R.NOT_IMPLEMENTED();
    }

    @Override
    public R updateScript(UpdateScriptDto updateScriptDto) {
        return R.NOT_IMPLEMENTED();
    }

    @Override
    public R deleteScript(DeleteScriptDto deleteScriptDto) {
        return R.NOT_IMPLEMENTED();
    }

    @Override
    public R stateScript(StateScriptDto stateScriptDto) {
        return R.NOT_IMPLEMENTED();
    }

    @Override
    public R batchPublishScript(List<DeleteScriptDto> deleteScriptDtos) {
        return R.NOT_IMPLEMENTED();
    }

    @Override
    public R batchStopScript(List<DeleteScriptDto> deleteScriptDtos) {
        return R.NOT_IMPLEMENTED();
    }

    @Override
    public R batchClassifyScript(List<ClassifyScriptDto> classifyScriptDtos) {
        return R.NOT_IMPLEMENTED();
    }

}
