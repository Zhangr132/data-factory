package com.data.service;

import com.data.dto.DataAsset.*;
import com.data.dto.DataAssetField.SelectDataAssetFieldDto;
import com.data.entity.DataAsset;
import com.baomidou.mybatisplus.extension.service.IService;
import com.data.utils.R;

import java.util.List;

/**
 * <p>
 * 数据资产表 服务类
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-29
 */
public interface DataAssetService extends IService<DataAsset> {

    R selectDataAsset(DataAssetPageDto dataAssetPageDto);

    void selectDataAssetVo(SelectDataAssetFieldDto selectDataAssetFieldDto);

    R addDataAsset(AddDataAssetDto addDataAssetDto);

    R updateDataAsset(UpdateDataAssetDto updateDataAssetDto);

    R deleteDataAsset(DeleteDataAssetDto deleteDataAssetDto);

    R stateDataAsset(StateDataAssetDto stateDataAssetDto);

    R batchPublishDataAsset(List<DeleteDataAssetDto> deleteDataAssetDtoList);

    R batchStopDataAsset(List<DeleteDataAssetDto> deleteDataAssetDtoList);
}
