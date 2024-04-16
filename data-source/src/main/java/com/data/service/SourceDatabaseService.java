package com.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.data.dto.SourceDatabase.*;
import com.data.entity.SourceDatabase;
import com.data.utils.R;

import java.util.List;

/**
 * <p>
 * 数据库数据源 服务类
 * </p>
 *
 * @author zhangr132
 * @since 2024-04-02
 */
public interface SourceDatabaseService extends IService<SourceDatabase> {

    R selectSourceDatabase(SourceDatabasePageDto sourceDatabasePageDto);

    R addSourceDatabase(AddSourceDatabaseDto addSourceDatabaseDto);

    R testDatabaseConnection(ConnectionDatabaseDto connectionDatabaseDto);

    R updateSourceDatabase(UpdateSourceDatabaseDto updateSourceDatabaseDto);

    R deleteSourceDatabase(DeleteSourceDatabaseDto deleteSourceDatabaseDto);

    R stateSourceDatabase(StateSourceDatabaseDto stateSourceDatabaseDto);

    R batchPublishSourceDatabase(List<DeleteSourceDatabaseDto> deleteSourceDatabaseDtos);

    R batchStopSourceDatabase(List<DeleteSourceDatabaseDto> deleteSourceDatabaseDtos);
}
