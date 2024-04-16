package com.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.data.dto.SourceApi.*;
import com.data.entity.SourceApi;
import com.data.utils.R;

import java.util.List;

/**
 * <p>
 * 接口数据源 服务类
 * </p>
 *
 * @author zhangr132
 * @since 2024-04-02
 */
public interface SourceApiService extends IService<SourceApi> {

    R selectSourceApi(SourceApiPageDto sourceApiPageDto);

    R addSourceApi(AddSourceApiDto addSourceApiDto);

    R updateSourceApi(UpdateSourceApiDto updateSourceApiDto);

    R deleteSourceApi(DeleteSourceApiDto deleteSourceApiDto);

    R testSourceApi(TestSourceApiDto testSourceApiDto);

    R stateSourceApi(StateSourceApiDto stateSourceApiDto);

    R batchPublishSourceApi(List<DeleteSourceApiDto> deleteSourceApiDtos);

    R batchStopSourceApi(List<DeleteSourceApiDto> deleteSourceApiDtos);
}
