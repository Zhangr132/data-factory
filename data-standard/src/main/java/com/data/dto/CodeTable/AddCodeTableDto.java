package com.data.dto.CodeTable;

import com.data.dto.CodeValue.AddCodeValueDto;
import com.data.entity.CodeValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 新增码表 数据传输对象
 * @Author zhangr132
 * @Date 2024/3/18 10:29
 * @注释
 */
@Data
public class AddCodeTableDto {

//    @ApiModelProperty("码表编号")
//    private String codeTableNumber;

    @ApiModelProperty("码表名称")
    @NotBlank(message = "码表名称不能为空(包含空格)")
    @Length(max = 10, message = "码表名称最长为10")
    @Pattern(regexp = "^[\u4e00-\u9fa5_a-zA-Z]+$",message = "码表名称只能包含中文和字母")
    private String codeTableName;

    @ApiModelProperty("码表描述")
    private String codeTableDesc;

    @ApiModelProperty("码值列表")
    private List<AddCodeValueDto> items;

}
