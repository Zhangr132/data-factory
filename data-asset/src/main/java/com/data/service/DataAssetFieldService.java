package com.data.service;

import com.data.dto.DataAssetField.AddDataAssetFieldDto;
import com.data.dto.DataAssetField.SelectDataAssetFieldDto;
import com.data.dto.DataAssetField.UpdateDataAssetFieldDto;
import com.data.entity.DataAssetField;
import com.baomidou.mybatisplus.extension.service.IService;
import com.data.utils.R;

/**
 * <p>
 * 数据资产字段表 服务类
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-29
 */
public interface DataAssetFieldService extends IService<DataAssetField> {

    R selectDataAssetField(SelectDataAssetFieldDto selectDataAssetFieldDto);

    void addDataAssetField(AddDataAssetFieldDto addDataAssetFieldDto,String p);

    void updateDataAssetField(UpdateDataAssetFieldDto updateDataAssetFieldDto,String p);
}
