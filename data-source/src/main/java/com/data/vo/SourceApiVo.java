package com.data.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author zhangr132
 * @Date 2024/4/3 16:08
 * @注释
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SourceApiVo {

    @ApiModelProperty("接口编码")
    private String apiCode;

    @ApiModelProperty("接口名称")
    private String apiName;

    @ApiModelProperty("接口描述")
    private String apiDesc;

    @ApiModelProperty("父级分类名称")
    private String parentCategoryName;

    @ApiModelProperty("接口分类编码")
    private String apiCategoryCode;
    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty("接口来源")
    private String apiOrigin;

    @ApiModelProperty("接口协议（0：http，1：https）	")
    private Integer apiProtocol;

    @ApiModelProperty("接口端口")
    private String apiPort;

    @ApiModelProperty("接口路径")
    private String apiPath;

    @ApiModelProperty("接口请求方式（0：GET，1：POST）")
    private Integer apiRequestMethod;

    @ApiModelProperty("接口超时时间")
    private Integer apiTimeoutTime;

    @ApiModelProperty("接口状态（0：未发布，1：已发布，2已停用）")
    private Integer apiState;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;
}
