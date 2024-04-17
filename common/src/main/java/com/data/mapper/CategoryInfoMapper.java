package com.data.mapper;

import com.data.entity.CategoryInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.data.vo.CategoryInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 分类信息表(导航目录) Mapper 接口
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-29
 */
@Mapper
public interface CategoryInfoMapper extends BaseMapper<CategoryInfo> {

    //通过 categoryCode 查询数据的分类名称和父名称
    @Select("SELECT t1.category_name parentCategoryName,t.category_name " +
            "FROM category_info t LEFT JOIN category_info t1 On (t1.category_code=t.parent_code) " +
            "where t.category_code=#{categoryCode}")
    CategoryInfoVo selectCNameCode(String categoryCode);
}
