package com.data.mapper;

import com.data.entity.DataAsset;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.data.vo.DataAssetVo;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 数据资产表 Mapper 接口
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-29
 */
@Mapper
public interface DataAssetMapper extends MPJBaseMapper<DataAsset> {

    //通过 dataAssetCode 查询特定表的特定字段
    @Select("SELECT t.asset_name_cn,t.asset_name_en,t3.category_name parentCategoryName,t2.category_name FROM data_asset t LEFT JOIN data_asset_relation_category t1 \n" +
            "ON (t1.data_asset_code = t.data_asset_code) LEFT JOIN category_info t2 ON (t2.category_code = t1.category_code) LEFT JOIN category_info t3 " +
            "On (t3.category_code=t2.parent_code) WHERE (t.data_asset_code = #{dataAssetCode})")
    DataAssetVo selectLeftJoinByDataAssetCode(String dataAssetCode);
}
