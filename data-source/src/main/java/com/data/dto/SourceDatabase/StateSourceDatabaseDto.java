package com.data.dto.SourceDatabase;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author zhangr132
 * @Date 2024/4/10 13:04
 * @注释
 */
@Data
public class StateSourceDatabaseDto {
    @ApiModelProperty("数据库数据源ID")
    @NotBlank
    private Integer id;

    @ApiModelProperty("数据库数据源状态（（0：未发布，1：已发布，2：已停用））")
    private Integer databaseSourceState;
}
