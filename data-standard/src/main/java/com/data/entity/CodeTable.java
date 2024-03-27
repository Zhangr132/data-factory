package com.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 码表管理
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("code_table")
//@TableName("code_table LEFT JOIN code_value on code_table.code_table_number = code_value.code_table_number")
@ApiModel(value = "CodeTable对象", description = "码表管理")
public class CodeTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("码表编号")
    private String codeTableNumber;

    @ApiModelProperty("码表名称")
    private String codeTableName;

    @ApiModelProperty("码表描述")
    private String codeTableDesc;

    @ApiModelProperty("码表状态（0：未发布，1：已发布，2：已停用）")
    private Integer codeTableState;

    @ApiModelProperty("0未删除，其他值为删除")
    private Integer deleteFlag;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;


}
