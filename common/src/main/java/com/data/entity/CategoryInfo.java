package com.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 分类信息表(导航目录)
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("category_info")
@ApiModel(value = "CategoryInfo对象", description = "分类信息表(导航目录)")
public class CategoryInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("分类编码")
    private String categoryCode;

    @ApiModelProperty("父级分类编码")
    private String parentCode;

    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty("分类描述")
    private String categoryDesc;

    @ApiModelProperty("逻辑删除标识 0 未删除 1 已删除")
    private Integer deleteFlag;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;


}
