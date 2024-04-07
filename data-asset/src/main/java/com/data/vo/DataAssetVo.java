package com.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author zhangr132
 * @Date 2024/4/1 9:42
 * @注释
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataAssetVo {
    @ApiModelProperty("数据资产中文名称")
    private String assetNameCn;

    @ApiModelProperty("数据资产英文名称")
    private String assetNameEn;

    @ApiModelProperty("父级分类名称")
    private String parentCategoryName;

    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty("数据标准列表")
    private List<DataAssetFieldVo> dataAssetFieldVoLists;

}
