package com.data.service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.data.dto.DataAssetField.AddDataAssetFieldDto;
import com.data.dto.DataAssetField.SelectDataAssetFieldDto;
import com.data.dto.DataAssetField.UpdateDataAssetFieldDto;
import com.data.entity.*;
import com.data.mapper.DataAssetFieldMapper;
import com.data.mapper.DataAssetMapper;
import com.data.service.DataAssetFieldService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.utils.R;
import com.data.vo.DataAssetFieldVo;
import com.data.vo.DataAssetVo;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * <p>
 * 数据资产字段表 服务实现类
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-29
 */
@Service
public class DataAssetFieldServiceImpl extends ServiceImpl<DataAssetFieldMapper, DataAssetField> implements DataAssetFieldService {
    private Logger logger= LoggerFactory.getLogger(getClass());
    @Autowired
    private DataAssetFieldMapper dataAssetFieldMapper;
    @Autowired
    private DataAssetMapper dataAssetMapper;

    /**
     * 数据资产详情查询
     * @param selectDataAssetFieldDto
     * @return
     */
    @Override
    public R selectDataAssetField(SelectDataAssetFieldDto selectDataAssetFieldDto) {
        logger.info("正在处理数据资产详情查询请求");

        //查询 DataAssetVo 中的字段
        logger.info("正在处理DataAssetVo查询请求");
        dataAssetMapper.selectLeftJoinByDataAssetCode(selectDataAssetFieldDto.getDataAssetCode());
        DataAssetVo dataAssetVo= DataAssetVo.builder()
                .assetNameCn(dataAssetMapper.selectLeftJoinByDataAssetCode(selectDataAssetFieldDto.getDataAssetCode()).getAssetNameCn())
                .assetNameEn(dataAssetMapper.selectLeftJoinByDataAssetCode(selectDataAssetFieldDto.getDataAssetCode()).getAssetNameEn())
                .parentCategoryName(dataAssetMapper.selectLeftJoinByDataAssetCode(selectDataAssetFieldDto.getDataAssetCode()).getParentCategoryName())
                .categoryName(dataAssetMapper.selectLeftJoinByDataAssetCode(selectDataAssetFieldDto.getDataAssetCode()).getCategoryName())
                .build();

        System.out.println("dataAssetVo："+dataAssetVo);

        //查询 DataAssetFieldVo 中的字段
        MPJLambdaWrapper<DataAssetFieldVo> queryWrapper = new MPJLambdaWrapper<>();
        queryWrapper
                .select(DataAsset::getAssetNameCn,DataAsset::getAssetNameEn)
                .select(CategoryInfo::getCategoryName)
                .select(DataAssetField::getFieldNameCn,DataAssetField::getFieldNameEn,DataAssetField::getFieldDesc)
                .select(DataStandard::getDataStandardType,DataStandard::getDataStandardLength,DataStandard::getDataStandardAccuracy,DataStandard::getDataStandardDefaultValue
                        ,DataStandard::getDataStandardValueMax,DataStandard::getDataStandardValueMin)
                .leftJoin(DataAsset.class,DataAsset::getDataAssetCode,DataAssetField::getDataAssetCode)
                .leftJoin(DataStandard.class,DataStandard::getDataStandardCode,DataAssetField::getDataStandardCode)
                .leftJoin(DataAssetRelationCategory.class,DataAssetRelationCategory::getDataAssetCode,DataAssetField::getDataAssetCode)
                .leftJoin(CategoryInfo.class,CategoryInfo::getCategoryCode,DataAssetRelationCategory::getCategoryCode)
                .eq(DataAssetField::getDataAssetCode,selectDataAssetFieldDto.getDataAssetCode());

        List<DataAssetFieldVo> dataAssetFieldVoList = dataAssetFieldMapper.selectJoinList(DataAssetFieldVo.class, queryWrapper);
        System.out.println("dataAssetFieldVoList："+dataAssetFieldVoList);

        //将 DataAssetFieldVo 作为list赋值给 DataAssetVo 中的 dataAssetFieldVoLists
        dataAssetVo.setDataAssetFieldVoLists(dataAssetFieldVoList);

        logger.info("处理数据资产详情查询请求完成");
        return R.Success(dataAssetVo);
    }

    /**
     * 数据字段新增
     * @param addDataAssetFieldDto
     * @param p
     */
    @Override
    public void addDataAssetField(AddDataAssetFieldDto addDataAssetFieldDto,String p) {
        logger.info("正在处理数据字段新增请求");
        try {
            //判断数据资产字段名称是否重复
            //构建查询条件
            MPJLambdaWrapper<DataAssetField> lambdaQueryWrapperCn = new MPJLambdaWrapper<>();
            //输入查询条件——中文名称判空
            lambdaQueryWrapperCn
                    .selectAll(DataAssetField.class)
                    .eq(DataAssetField::getFieldNameCn,addDataAssetFieldDto.getFieldNameCn());
            logger.info("检验中文名称是否为空");
            DataAssetField dataAssetFieldCnName = dataAssetFieldMapper.selectOne(lambdaQueryWrapperCn);
            //检查查询结果是否为空
            if(!ObjectUtils.isEmpty(dataAssetFieldCnName)){
                throw new RuntimeException("该中文名称已存在");
            }

            //输入查询条件——英文名称判空
            MPJLambdaWrapper<DataAssetField> lambdaQueryWrapperEn = new MPJLambdaWrapper<>();
            lambdaQueryWrapperEn
                    .selectAll(DataAssetField.class)
                    .eq(DataAssetField::getFieldNameEn,addDataAssetFieldDto.getFieldNameEn());
            DataAssetField dataAssetFieldEnName = dataAssetFieldMapper.selectOne(lambdaQueryWrapperEn);
            logger.info("检查查询结果是否为空");
            //检查查询结果是否为空
            if(!ObjectUtils.isEmpty(dataAssetFieldEnName)){
                throw new RuntimeException("该英文名称已存在");
            }

            //存数据到 DataAssetField
            DataAssetField dataAssetField = DataAssetField.builder()
                    .dataAssetCode(p)
                    .fieldNameCn(addDataAssetFieldDto.getFieldNameCn())
                    .fieldNameEn(addDataAssetFieldDto.getFieldNameEn())
                    .fieldDesc(addDataAssetFieldDto.getFieldDesc())
                    .dataStandardCode(addDataAssetFieldDto.getDataStandardCode())
                    .build();
            //进行新增码表操作
            dataAssetFieldMapper.insert(dataAssetField);
            logger.info("处理数据字段新增请求完成");
        } catch (Exception e) {
            // 发生异常时回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    /**
     * 数据资产字段编辑
     * @param updateDataAssetFieldDto
     * @param p
     */
    @Override
    public void updateDataAssetField(UpdateDataAssetFieldDto updateDataAssetFieldDto, String p) {
        logger.info("正在处理数据资产字段编辑请求");
        try {
            MPJLambdaWrapper<DataAssetField> wrapper=new MPJLambdaWrapper<>();
            wrapper.selectAll(DataAssetField.class)
                    .eq(DataAssetField::getDataAssetCode,p);
            DataAssetField dataAssetField=dataAssetFieldMapper.selectOne(wrapper);
            if (ObjectUtils.isEmpty(dataAssetField)){
                throw new RuntimeException("数据资产字段查询为空");
            }
            DataAssetField dataAssetField1= DataAssetField.builder()
                    .fieldNameCn(updateDataAssetFieldDto.getFieldNameCn())
                    .fieldNameEn(updateDataAssetFieldDto.getFieldNameEn())
                    .fieldDesc(updateDataAssetFieldDto.getFieldDesc())
                    .dataStandardCode(updateDataAssetFieldDto.getDataStandardCode())
                    .build();

            UpdateWrapper<DataAssetField> updateWrapper=new UpdateWrapper<>();
            updateWrapper.eq(!ObjectUtils.isEmpty(p),"data_asset_code",p);
            dataAssetFieldMapper.update(dataAssetField1,updateWrapper);

            logger.info("处理数据资产字段编辑请求完成");
        } catch (Exception e) {
            // 发生异常时回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }


}
