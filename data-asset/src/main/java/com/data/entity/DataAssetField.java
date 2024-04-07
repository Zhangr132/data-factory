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
 * 数据资产字段表
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("data_asset_field")
@ApiModel(value = "DataAssetField对象", description = "数据资产字段表")
public class DataAssetField implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("数据资产表编码")
    private String dataAssetCode;

    @ApiModelProperty("字段中文名称")
    private String fieldNameCn;

    @ApiModelProperty("字段英文名称")
    private String fieldNameEn;

    @ApiModelProperty("字段描述")
    private String fieldDesc;

    @ApiModelProperty("数据标准编码")
    private String dataStandardCode;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;


}
