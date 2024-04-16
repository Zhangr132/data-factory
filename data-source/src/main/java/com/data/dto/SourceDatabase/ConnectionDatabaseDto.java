package com.data.dto.SourceDatabase;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @Author zhangr132
 * @Date 2024/4/10 11:20
 * @注释
 */
@Data
public class ConnectionDatabaseDto {
    @ApiModelProperty("数据库数据源名称")
    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9_\\u4e00-\\u9fa5]+$",message = "数据库数据源名称支持中英文大小写、数字、下划线，不支持特殊符号及空格")
    private String databaseSourceName;

    @ApiModelProperty("数据库数据源类型（例如：mysql、mongodb等）")
    @NotBlank
    private String databaseSourceType;

    @ApiModelProperty("数据库数据源路径")
    @NotBlank
    private String databaseSourceUrl;

    @ApiModelProperty("数据源其它信息，包括地址用户名密码等，json存储，例如Kerberos文件，hbase-site.xml文件")
    private DatabaseSourceProp databaseSourceProp;
}
