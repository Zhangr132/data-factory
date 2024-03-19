package com.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 码值表
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("code_value")
@ApiModel(value = "CodeValue对象", description = "码值表")
public class CodeValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("码值编号")
    private String codeValueNumber;

    @ApiModelProperty("码值名称")
    private String codeValueName;

    @ApiModelProperty("码值取值")
    private String codeValueValue;

    @ApiModelProperty("码值含义")
    private String codeValueDesc;

    @ApiModelProperty("码表编号(外键)")
    private String codeTableNumber;

    @ApiModelProperty("0未删除，其他已删除")
    private Integer deleteFlag;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;


}
