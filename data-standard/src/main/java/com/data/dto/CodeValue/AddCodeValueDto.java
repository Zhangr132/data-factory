package com.data.dto.CodeValue;

import com.data.entity.CodeValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 码值新增 数据传输对象
 * @Author zhangr132
 * @Date 2024/3/18 11:34
 * @注释
 */

@Data
public class AddCodeValueDto {

    @ApiModelProperty("码值名称")
    @NotBlank(message = "码值名称不能为空")
    @Length(max = 10, message = "码值名称最长为10")
    @Pattern(regexp = "^[\u4e00-\u9fa5a-zA-Z]+$",message = "码值名称只能包含中文和字母")
    private String codeValueName;

    @ApiModelProperty("码值取值")
    @NotEmpty(message = "码值取值不能为空")
    private String codeValueValue;

    @ApiModelProperty("码值含义")
    private String codeValueDesc;

    @ApiModelProperty("码表编号(外键-自动生成)")
    private String codeTableNumber;
}
