package com.data.utils.tree;

import java.util.List;
/**
 *  * 树节点父类，所有需要使用{@linkplain BaseTreeHelper}工具类形成树形结构等操作的节点都需要实现该接口
 *  *
 *  * @param <T> 节点id类型
 * @Author zhangr132
 * @Date 2024/4/28 15:23
 * @注释
 */

public interface TreeNode<T, E> {
    /**
     * 获取节点id
     *
     * @return 树节点id
     */
    T getId();

    /**
     * 获取该节点的父节点id
     *
     * @return 父节点id
     */
    T getPid();

    /**
     * 是否是根节点
     *
     * @return true：根节点
     */
    Boolean root();

    /**
     * 设置节点的子节点列表
     *
     * @param children 子节点
     */
    void setChildren(List<E> children);

    /**
     * 获取所有子节点
     *
     * @return 子节点列表
     */
    List<E> getChildren();
}
