package com.data.service;

import com.data.dto.AddCategoryInfoDto;
import com.data.dto.DeleteCategoryInfoDto;
import com.data.dto.SelectCategoryInfoDto;
import com.data.dto.UpdateCategoryInfoDto;
import com.data.entity.CategoryInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.data.utils.R;

/**
 * <p>
 * 分类信息表(导航目录) 服务类
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-29
 */
public interface CategoryInfoService extends IService<CategoryInfo> {

    R addCategoryInfo(AddCategoryInfoDto addCategoryInfoDto);

    R updateCategoryInfo(UpdateCategoryInfoDto updateCategoryInfoDto);

    R deleteCategoryInfo(DeleteCategoryInfoDto deleteCategoryInfoDto);

    R selectCategoryInfo(SelectCategoryInfoDto selectCategoryInfoDto);

    R listCategoryInfo(String categoryCode);
}
