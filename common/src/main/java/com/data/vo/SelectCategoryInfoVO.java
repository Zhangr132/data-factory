package com.data.vo;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.data.utils.tree.TreeNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author zhangr132
 * @Date 2024/4/28 14:55
 * @注释
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectCategoryInfoVO implements TreeNode<String,SelectCategoryInfoVO> {
    @ApiModelProperty("分类编码")
    private String categoryCode;

    @ApiModelProperty("父级分类编码")
    private String parentCode;

    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty("分类子级")
    private List<SelectCategoryInfoVO> children;

    /**
     * 获取节点编码
     * @return
     */
    @Override
    public String getId() {
        return categoryCode;
    }

    /**
     * 获取父节点编码
     * @return
     */
    @Override
    public String  getPid() {
        return parentCode; // 父节点编码
    }


    /**
     * 是否为根节点
     * @return
     */
    @Override
    public Boolean root() {
        if(ObjectUtils.isEmpty(parentCode)){
            return true;
        }
        return false;
    }
}
