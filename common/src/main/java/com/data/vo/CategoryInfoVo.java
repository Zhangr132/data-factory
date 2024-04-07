package com.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author zhangr132
 * @Date 2024/4/3 17:22
 * @注释
 */
@Data
public class CategoryInfoVo {
    @ApiModelProperty("分类编码")
    private String categoryCode;

    @ApiModelProperty("父级分类编码")
    private String parentCode;
    @ApiModelProperty("父级分类名称")
    private String parentCategoryName;

    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty("分类描述")
    private String categoryDesc;

    @ApiModelProperty("逻辑删除标识 0 未删除 1 已删除")
    private Integer deleteFlag;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;
}
