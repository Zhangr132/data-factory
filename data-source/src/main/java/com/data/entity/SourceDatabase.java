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
 * 数据库数据源
 * </p>
 *
 * @author zhangr132
 * @since 2024-04-07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("source_database")
@ApiModel(value = "SourceDatabase对象", description = "数据库数据源")
public class SourceDatabase implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("数据库数据源名称")
    private String databaseSourceName;

    @ApiModelProperty("数据库数据源类型（例如：mysql、mongodb等）")
    private String databaseSourceType;

    @ApiModelProperty("数据库数据源描述")
    private String databaseSourceDesc;

    @ApiModelProperty("数据库数据源路径")
    private String databaseSourceUrl;

    @ApiModelProperty("数据库数据源状态（（0：未发布，1：已发布，2：已停用））")
    private Integer databaseSourceState;

    @ApiModelProperty("是否删除（0：未删除；1：已删除）")
    private Boolean deleteFlag;

    @ApiModelProperty("数据源其它信息，包括地址用户名密码等，json存储，例如Kerberos文件，hbase-site.xml文件")
    private String databaseSourceProp;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;


}
