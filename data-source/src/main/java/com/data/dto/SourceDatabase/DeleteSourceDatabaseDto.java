package com.data.dto.SourceDatabase;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author zhangr132
 * @Date 2024/4/10 13:05
 * @注释
 */
@Data
public class DeleteSourceDatabaseDto {
    @ApiModelProperty("数据库数据源ID")
    @NotNull
    private Integer id;
}
