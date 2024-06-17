package com.data.utils.tree;

/**
 * @Author zhangr132
 * @Date 2024/4/28 15:18
 * @注释
 */
import cn.hutool.core.collection.CollUtil;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class BaseTreeHelper {

    /**
     * @param <T> 节点类型
     * @param <E> 节点id的类型
     * @return 树形结构列表
     * 根据所有树节点列表，生成含有所有树形结构的列表
     **/
    public static <T extends TreeNode<E, T>, E> List<T> generateTrees(List<T> data) {
        if (CollUtil.isEmpty(data)) {
            return Lists.newArrayList();
        }
        //将集合中所有数据按照父Id进行分组，放入Map中，Map<parntId, List<T>>
        Map<String, List<T>> groupByParentIdMap = data.stream().collect(Collectors.groupingBy(item -> item.root() ? "" : Objects.toString(item.getPid())));
        //将集合中所有数据以数据Id为key，放入Map中，Map<id,T>
        Map<String, T> dataMap = data.stream().collect(Collectors.toMap(item -> item.getId().toString(), t -> t));
        List<T> resp = Lists.newArrayList();
        //遍历数据，将子节点放入对应父节点Children属性中
        groupByParentIdMap.forEach((parentId, values) -> {
            if (dataMap.containsKey(parentId)) {
                List<T> child = dataMap.get(parentId).getChildren();
                if (CollUtil.isEmpty(child)) {
                    child = Lists.newArrayList();
                }
                child.addAll(values);
                dataMap.get(parentId).setChildren(child);
            } else {
                resp.addAll(values);
            }
        });
        return resp;
    }


    /**
     * @param parent 父节点
     * @param <T>    实际节点类型
     * @return 叶子节点
     * 获取指定树节点下的所有叶子节点
     **/
    public static <T extends TreeNode<E, T>, E> List<T> getLeafs(T parent) {
        List<T> leafs = new ArrayList<>();
        fillLeaf(parent, leafs);
        return leafs;
    }

    /**
     * @param parent 父节点
     * @param leafs  叶子节点列表
     * @param <T>    实际节点类型
     * 将parent的所有叶子节点填充至leafs列表中
     **/
    private static <T extends TreeNode<E, T>, E> void fillLeaf(T parent, List<T> leafs) {
        List<T> children = parent.getChildren();
        // 如果节点没有子节点则说明为叶子节点
        if (CollUtil.isEmpty(children)) {
            leafs.add(parent);
            return;
        }
        // 递归调用子节点，查找叶子节点
        for (T child : children) {
            fillLeaf(child, leafs);
        }
    }

}
