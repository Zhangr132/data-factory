package com.data.dto.CodeTable;

import com.data.entity.CodeTable;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhangr132
 * @Date 2024/3/18 9:38
 * @注释
 */
@Data
public class CodeTablePageDto extends CodeTable {
    @ApiModelProperty("每页显示条数")
    private Integer pageSize=20;
    @ApiModelProperty("页码")
    private Integer pageNumber=1;
}
