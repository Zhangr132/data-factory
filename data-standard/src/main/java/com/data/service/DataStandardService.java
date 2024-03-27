package com.data.service;

import com.data.dto.DataStandard.AddDataStandardDto;
import com.data.dto.DataStandard.DataStandardPageDto;
import com.data.dto.DataStandard.SelectEnumDto;
import com.data.entity.DataStandard;
import com.baomidou.mybatisplus.extension.service.IService;
import com.data.utils.R;

/**
 * <p>
 * 数据标准表 服务类
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-15
 */
public interface DataStandardService extends IService<DataStandard> {

    R selectDataStandard(DataStandardPageDto dataStandardPageDto);

    R addDataStandard(AddDataStandardDto addDataStandardDto);

    R selectDataStandardEnum(SelectEnumDto selectEnumDto);
}
