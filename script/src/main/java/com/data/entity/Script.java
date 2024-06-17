package com.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 脚本管理
 * </p>
 *
 * @author zhangr132
 * @since 2024-04-17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Script对象", description = "脚本管理")
public class Script implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "Id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("脚本名称")
    private String scriptName;

    @ApiModelProperty("脚本描述")
    private String scriptDesc;

    @ApiModelProperty("脚本分类")
    private String scriptCategory;

    @ApiModelProperty("脚本状态（（0：未发布，1：已发布，2：已停用））")
    private Integer scriptState;

    @ApiModelProperty("文件")
    private String scriptFiles;

    @ApiModelProperty("类名")
    private String className;

    @ApiModelProperty("函数名")
    private String functionName;

    @ApiModelProperty("请求参数")
    private String scriptRequestParams;

    @ApiModelProperty("请求体")
    private String scriptRequestBody;

    @ApiModelProperty("返回参数")
    private String scriptResponseParams;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;


}
