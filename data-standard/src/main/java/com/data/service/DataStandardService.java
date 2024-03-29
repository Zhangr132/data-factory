package com.data.service;

import com.data.dto.CodeTable.StateCodeTableDto;
import com.data.dto.CodeTable.excel.ExportCodeTableExcel;
import com.data.dto.DataStandard.*;
import com.data.dto.DataStandard.excel.DataStandardExcel;
import com.data.entity.DataStandard;
import com.baomidou.mybatisplus.extension.service.IService;
import com.data.utils.R;

import java.util.List;

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

    List selectDataStandardEnum(SelectEnumDto selectEnumDto);

    boolean updateDataStandard(UpdateDataStandardDto updateDataStandardDto);

    boolean stateDataStandard(StateDataStandardDto stateDataStandardDto);

    boolean deleteDataStandard(DeleteDataStandardDto deleteDataStandardDto);

    boolean batchPublishDataStandard(List<StateDataStandardDto> stateDataStandardDtos);

    boolean batchStopDataStanadard(List<StateDataStandardDto> stateDataStandardDtos);

    List<ExportCodeTableExcel> selectCodeTableExcel(String dataStandardEnumerationRange);

    R saveDataStandardExcels(DataStandardExcel newDataStandardExcel);
}
