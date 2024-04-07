package com.data.service;

import com.data.dto.DataAssetRelationCategory.AddDataAssetRelationCategoryDto;
import com.data.dto.DataAssetRelationCategory.UpdateDataAssetRelationCategoryDto;
import com.data.entity.DataAssetRelationCategory;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 数据资产分类表 服务类
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-29
 */
public interface DataAssetRelationCategoryService extends IService<DataAssetRelationCategory> {
    void addDataAssetRelationCategory(AddDataAssetRelationCategoryDto addDataAssetRelationCategoryDto,String p);

    void  updateDataAssetRelationCategory(UpdateDataAssetRelationCategoryDto updateDataAssetRelationCategoryDto,String p);
}
