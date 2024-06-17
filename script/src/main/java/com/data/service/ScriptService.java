package com.data.service;

import com.data.dto.*;
import com.data.entity.Script;
import com.baomidou.mybatisplus.extension.service.IService;
import com.data.utils.R;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 脚本管理 服务类
 * </p>
 *
 * @author zhangr132
 * @since 2024-04-17
 */
public interface ScriptService extends IService<Script> {

    R selectScriptPage(ScriptPageDto scriptPageDto);

    R addScript(AddScriptDto addScriptDto);

    R updateScript(UpdateScriptDto updateScriptDto);

    R deleteScript(DeleteScriptDto deleteScriptDto);

    R stateScript(StateScriptDto stateScriptDto);

    R batchPublishScript(List<DeleteScriptDto> deleteScriptDtos);

    R batchStopScript(List<DeleteScriptDto> deleteScriptDtos);

    R batchClassifyScript(List<ClassifyScriptDto> classifyScriptDtos);

}
