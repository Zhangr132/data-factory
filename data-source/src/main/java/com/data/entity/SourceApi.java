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
 * 接口数据源
 * </p>
 *
 * @author zhangr132
 * @since 2024-04-07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("source_api")
@ApiModel(value = "SourceApi对象", description = "接口数据源")
public class SourceApi implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("数据库数据源ID")
    private Integer id;

    @ApiModelProperty("接口编码")
    private String apiCode;

    @ApiModelProperty("接口名称")
    private String apiName;

    @ApiModelProperty("接口描述")
    private String apiDesc;

    @ApiModelProperty("接口分类编码")
    private String apiCategoryCode;

    @ApiModelProperty("接口来源")
    private String apiOrigin;

    @ApiModelProperty("接口协议（0：http，1：https）")
    private Integer apiProtocol;

    @ApiModelProperty("接口端口")
    private String apiPort;

    @ApiModelProperty("接口路径")
    private String apiPath;

    @ApiModelProperty("接口请求方式（0：GET，1：POST）")
    private Integer apiRequestMethod;

    @ApiModelProperty("接口超时时间")
    private Integer apiTimeoutTime;

    @ApiModelProperty("输入参数")
    private String apiRequestParams;

    @ApiModelProperty("请求body")
    private String apiRequestBody;

    @ApiModelProperty("返回参数")
    private String apiResponse;

    @ApiModelProperty("接口状态（0：未发布，1：已发布，2已停用）")
    private Integer apiState;

    @ApiModelProperty("是否删除（0：未删除；1：已删除）")
    private Boolean deleteFlag;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;


}
