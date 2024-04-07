package com.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 数据资产分类表
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("data_asset_relation_category")
@ApiModel(value = "DataAssetRelationCategory对象", description = "数据资产分类表")
public class DataAssetRelationCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("数据资产编码")
    private String dataAssetCode;

    @ApiModelProperty("分类编码")
    private String categoryCode;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;


}
