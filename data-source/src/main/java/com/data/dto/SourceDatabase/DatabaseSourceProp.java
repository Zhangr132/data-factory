package com.data.dto.SourceDatabase;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhangr132
 * @Date 2024/4/7 22:59
 * @注释
 */
@Data
public class DatabaseSourceProp {
    @ApiModelProperty("用户名")
    private String databaseName;


    @ApiModelProperty("密码")
    private String databasePassword;

}
