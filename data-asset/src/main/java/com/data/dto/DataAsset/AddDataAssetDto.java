package com.data.dto.DataAsset;

import com.data.dto.DataAssetField.AddDataAssetFieldDto;
import com.data.dto.DataAssetRelationCategory.AddDataAssetRelationCategoryDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @Author zhangr132
 * @Date 2024/4/1 15:20
 * @注释
 */
@Data
public class AddDataAssetDto {
    @ApiModelProperty("数据资产中文名称")
    @NotBlank(message = "中文名称不能为空(包含空格)")
    @Length(max = 10, message = "中文名称最长为10")
    @Pattern(regexp = "^[\u4e00-\u9fa5_a-zA-Z]+$",message = "数据资产中文名称只能包含中文和字母")
    private String assetNameCn;

    @ApiModelProperty("数据资产英文名称")
    @NotBlank(message = "英文名称不能为空(包含空格)")
    @Length(max = 20, message = "英文名称最长为20")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]*$", message = "只支持英文大小写、数字及下划线且只能英文开头")
    private String assetNameEn;

    @ApiModelProperty("数据资产表描述")
    private String assetDesc;

    @ApiModelProperty("数据字段列表")
    private List<AddDataAssetFieldDto> addDataAssetFieldDtoLists;
    @ApiModelProperty("数据资产分类列表")
    private List<AddDataAssetRelationCategoryDto> addDataAssetRelationCategoryDtoLists;
}
