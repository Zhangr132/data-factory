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
 * 数据资产表
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("data_asset")
@ApiModel(value = "DataAsset对象", description = "数据资产表")
public class DataAsset implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("数据资产编码")
    private String dataAssetCode;

    @ApiModelProperty("数据资产中文名称")
    private String assetNameCn;

    @ApiModelProperty("数据资产英文名称")
    private String assetNameEn;

    @ApiModelProperty("数据资产表描述")
    private String assetDesc;

    @ApiModelProperty("数据资产表状态；0：未发布；1：已发布；2：已停用")
    private Integer dataAssetState;

    @ApiModelProperty("是否删除（0：未删除；1：已删除）")
    private Boolean isDelete;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;


}
