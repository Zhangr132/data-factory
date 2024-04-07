package com.data.service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.data.dto.DataAssetRelationCategory.AddDataAssetRelationCategoryDto;
import com.data.dto.DataAssetRelationCategory.UpdateDataAssetRelationCategoryDto;
import com.data.entity.CategoryInfo;
import com.data.entity.DataAssetField;
import com.data.entity.DataAssetRelationCategory;
import com.data.mapper.CategoryInfoMapper;
import com.data.mapper.DataAssetRelationCategoryMapper;
import com.data.service.DataAssetRelationCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * <p>
 * 数据资产分类表 服务实现类
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-29
 */
@Service
public class DataAssetRelationCategoryServiceImpl extends ServiceImpl<DataAssetRelationCategoryMapper, DataAssetRelationCategory> implements DataAssetRelationCategoryService {
    private Logger logger= LoggerFactory.getLogger(getClass());
    @Autowired
    private DataAssetRelationCategoryMapper dataAssetRelationCategoryMapper;
    @Autowired
    private CategoryInfoMapper categoryInfoMapper;

    /**
     * 数据资产分类新增
     * @param addDataAssetRelationCategoryDto
     * @param p
     */
    @Override
    public void addDataAssetRelationCategory(AddDataAssetRelationCategoryDto addDataAssetRelationCategoryDto, String p) {
        logger.info("正在处理数据资产分类新增");

        try {
            //判断添加的目录是否是叶子节点目录
            //构建查询条件
            MPJLambdaWrapper<CategoryInfo> lambdaQueryWrapperCn = new MPJLambdaWrapper<>();
            //输入查询条件——中文名称判空
            lambdaQueryWrapperCn
                    .selectAll(CategoryInfo.class)
                    .eq(CategoryInfo::getParentCode,addDataAssetRelationCategoryDto.getCategoryCode());
            logger.info("检验添加的目录是否是叶子节点目录");
            CategoryInfo categoryInfo = categoryInfoMapper.selectOne(lambdaQueryWrapperCn);
            //检查查询结果是否为空
            if(!ObjectUtils.isEmpty(categoryInfo)){
                throw new RuntimeException("添加的目录不是叶子节点目录");
            }

            DataAssetRelationCategory dataAssetRelationCategory= DataAssetRelationCategory.builder()
                    .dataAssetCode(p)
                    .categoryCode(addDataAssetRelationCategoryDto.getCategoryCode())
                    .build();

            //进行数据资产分类
            dataAssetRelationCategoryMapper.insert(dataAssetRelationCategory);
            logger.info("处理数据资产分类新增完成");
        } catch (RuntimeException e) {
            // 发生异常时回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    /**
     * 数据资产分类编辑
     * @param updateDataAssetRelationCategoryDto
     * @param p
     */
    @Transactional
    @Override
    public void updateDataAssetRelationCategory(UpdateDataAssetRelationCategoryDto updateDataAssetRelationCategoryDto, String p) {
        logger.info("正在处理数据资产分类编辑");
        try {
            MPJLambdaWrapper<DataAssetRelationCategory> wrapper=new MPJLambdaWrapper<>();
            wrapper.selectAll(DataAssetRelationCategory.class)
                    .eq(DataAssetRelationCategory::getDataAssetCode,p);
            DataAssetRelationCategory dataAssetRelationCategory=dataAssetRelationCategoryMapper.selectOne(wrapper);
            if (ObjectUtils.isEmpty(dataAssetRelationCategory)){
                throw new RuntimeException("数据资产分类查询为空");
            }
            DataAssetRelationCategory dataAssetRelationCategory1= DataAssetRelationCategory.builder()
                    .categoryCode(updateDataAssetRelationCategoryDto.getCategoryCode())
                    .build();

            UpdateWrapper<DataAssetRelationCategory> updateWrapper=new UpdateWrapper<>();
            updateWrapper.eq(!ObjectUtils.isEmpty(p),"data_asset_code",p);
            dataAssetRelationCategoryMapper.update(dataAssetRelationCategory1,wrapper);
            logger.info("处理数据资产分类编辑完毕");

        } catch (Exception e) {
            // 发生异常时回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }
}
