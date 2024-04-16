package com.data.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.data.dto.CodeTable.StateCodeTableDto;
import com.data.dto.CodeValue.AddCodeValueDto;
import com.data.dto.CodeValue.DeleteCodeValueDto;
import com.data.dto.DataAsset.*;
import com.data.dto.DataAssetField.AddDataAssetFieldDto;
import com.data.dto.DataAssetField.SelectDataAssetFieldDto;
import com.data.dto.DataAssetField.UpdateDataAssetFieldDto;
import com.data.dto.DataAssetRelationCategory.AddDataAssetRelationCategoryDto;
import com.data.dto.DataAssetRelationCategory.UpdateDataAssetRelationCategoryDto;
import com.data.entity.*;
import com.data.mapper.DataAssetMapper;
import com.data.mapper.DataAssetRelationCategoryMapper;
import com.data.service.DataAssetFieldService;
import com.data.service.DataAssetRelationCategoryService;
import com.data.service.DataAssetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.utils.R;
import com.data.vo.DataAssetVo;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据资产表 服务实现类
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-29
 */
@Service
public class DataAssetServiceImpl extends ServiceImpl<DataAssetMapper, DataAsset> implements DataAssetService {
    private Logger logger= LoggerFactory.getLogger(getClass());
    @Autowired
    private DataAssetMapper dataAssetMapper;

    @Autowired
    private DataAssetFieldService dataAssetFieldService;
    @Autowired
    private DataAssetRelationCategoryService dataAssetRelationCategoryService;

    /**
     * 数据资产查询
     * @param dataAssetPageDto
     * @return
     */
    @Override
    public R selectDataAsset(DataAssetPageDto dataAssetPageDto) {
        logger.info("正在处理数据资产查询请求");
        MPJLambdaWrapper<DataAsset> queryWrapper = new MPJLambdaWrapper<>();
        //将 pageSize 和 pageNumber 放入Page中
        Page<DataAsset> page=new Page<>(dataAssetPageDto.getPageNumber(),dataAssetPageDto.getPageSize());
        queryWrapper
                .selectAll(DataAsset.class)
                .like(!ObjectUtils.isEmpty(dataAssetPageDto.getAssetNameCn()),DataAsset::getAssetNameCn,dataAssetPageDto.getAssetNameCn())
                .like(!ObjectUtils.isEmpty(dataAssetPageDto.getAssetNameEn()),DataAsset::getAssetNameEn,dataAssetPageDto.getAssetNameEn())
                .eq(!ObjectUtils.isEmpty(dataAssetPageDto.getDataAssetState()),DataAsset::getDataAssetState,dataAssetPageDto.getDataAssetState())
                .eq(DataAsset::getIsDelete,0)
                .orderByAsc(DataAsset::getDataAssetState);
        queryWrapper.orderByDesc(DataAsset::getUpdateTime);

        IPage<DataAsset> dataAssetIPage=dataAssetMapper.selectPage(page,queryWrapper);
        List<DataAsset> records=dataAssetIPage.getRecords();
        Map responseData=new HashMap<>();
        responseData.put("data", records);
        responseData.put("total", dataAssetIPage.getTotal()); // 总记录数
        responseData.put("pageSize", dataAssetIPage.getSize()); // 每页显示数量
        responseData.put("pageNumber", dataAssetIPage.getCurrent()); // 当前页码
//        responseData.put("orders", dataAssetIPage.orders()); // 排序信息
//        responseData.put("optimizeCountSql", dataAssetIPage.optimizeCountSql()); // 是否优化count语句
        responseData.put("pages", dataAssetIPage.getPages()); // 总页数
        return R.Success(responseData);
    }

    /**
     * DataAssetVo查询
     * @param selectDataAssetFieldDto
     */
    @Override
    public void selectDataAssetVo(SelectDataAssetFieldDto selectDataAssetFieldDto) {
        logger.info("正在处理DataAssetVo查询请求");
        dataAssetMapper.selectLeftJoinByDataAssetCode(selectDataAssetFieldDto.getDataAssetCode());
        DataAssetVo dataAssetVo= DataAssetVo.builder()
                .assetNameCn(dataAssetMapper.selectLeftJoinByDataAssetCode(selectDataAssetFieldDto.getDataAssetCode()).getAssetNameCn())
                .assetNameEn(dataAssetMapper.selectLeftJoinByDataAssetCode(selectDataAssetFieldDto.getDataAssetCode()).getAssetNameEn())
                .parentCategoryName(dataAssetMapper.selectLeftJoinByDataAssetCode(selectDataAssetFieldDto.getDataAssetCode()).getParentCategoryName())
                .categoryName(dataAssetMapper.selectLeftJoinByDataAssetCode(selectDataAssetFieldDto.getDataAssetCode()).getCategoryName())
                .build();

        System.out.println("dataAssetVo："+dataAssetVo);
    }

    /**
     * 数据资产新增
     * @param addDataAssetDto
     * @return
     */
    @Override
    public R addDataAsset(AddDataAssetDto addDataAssetDto) {
        logger.info("正在处理数据资产新增请求");
        //判断数据资产名称是否重复
        //构建查询条件
        MPJLambdaWrapper<DataAsset> lambdaQueryWrapperCn = new MPJLambdaWrapper<>();
        //输入查询条件——中文名称判空
        lambdaQueryWrapperCn
                .selectAll(DataAsset.class)
                .eq(DataAsset::getAssetNameCn,addDataAssetDto.getAssetNameCn())
                .eq(DataAsset::getIsDelete,0);
        logger.info("检验中文名称是否为空");
        DataAsset dataAssetCnName = dataAssetMapper.selectOne(lambdaQueryWrapperCn);
        //检查查询结果是否为空
        if(!ObjectUtils.isEmpty(dataAssetCnName)){
            return R.BAD_REQUEST("该中文名称已存在");
        }

        //输入查询条件——英文名称判空
        MPJLambdaWrapper<DataAsset> lambdaQueryWrapperEn = new MPJLambdaWrapper<>();
        lambdaQueryWrapperEn
                .selectAll(DataAsset.class)
                .eq(DataAsset::getAssetNameEn,addDataAssetDto.getAssetNameEn())
                .eq(DataAsset::getIsDelete,0);
        DataAsset dataAssetEnName = dataAssetMapper.selectOne(lambdaQueryWrapperEn);
        logger.info("检查查询结果是否为空");
        //检查查询结果是否为空
        if(!ObjectUtils.isEmpty(dataAssetEnName)){
            return R.BAD_REQUEST("该英文名称已存在");
        }

        //生成编号
        String DAC="DAC";       //前缀
        String newDataAssetCode;
        MPJLambdaWrapper<DataAsset> lambdaQueryWrapper1 = new MPJLambdaWrapper<>();
        //查询符合条件的最后一条数据
        lambdaQueryWrapper1
                .selectAll(DataAsset.class)
                .orderByDesc(DataAsset::getDataAssetCode)
                .last("limit 1");
        logger.info("查询符合条件的最后一条数据");
        DataAsset dataAsset = dataAssetMapper.selectOne(lambdaQueryWrapper1);

        if (ObjectUtils.isEmpty(dataAsset)) {
            //如果数据库没有 0001
            newDataAssetCode =DAC + "00001";
        }else {
            //如果数据库中有数据 拿最后一条数据的序号
            //最后一条数据账号
            String lastDataAssetCode = dataAsset.getDataAssetCode();
            //截取序号部分，并将其转换为整数
            String idStr = lastDataAssetCode.substring(3, lastDataAssetCode.length());
            //将序号加一，并格式化为五位数的字符串
            Integer id = Integer.valueOf(idStr) + 1;
            String formatId = String.format("%05d", id);
            //拼接成新的 codeTableNumber，并将其设置到 addCodeTableDto 对象中
            newDataAssetCode = DAC + (formatId);
        }
        //存数据到 DataAsset
        DataAsset dataAsset1 = DataAsset.builder()
                .dataAssetCode(newDataAssetCode)
                .assetNameCn(addDataAssetDto.getAssetNameCn())
                .assetNameEn(addDataAssetDto.getAssetNameEn())
                .assetDesc(addDataAssetDto.getAssetDesc())
                .build();
        //进行新增数据资产操作
        dataAssetMapper.insert(dataAsset1);

        //进行新增数据资产字段操作
        //遍历 AddDataAssetDto 中的 addDataAssetFieldDtoLists 集合，每一个 AddDataAssetFieldDto 对象执行相应的操作
        for(AddDataAssetFieldDto addDataAssetFieldDto: addDataAssetDto.getAddDataAssetFieldDtoLists()){
            //调用 addDataAssetField 方法新增数据资产字段
            dataAssetFieldService.addDataAssetField(addDataAssetFieldDto,newDataAssetCode);
        }

        //进行新增数据资产分类操作
        //遍历 AddDataAssetDto 中的 addDataAssetRelationCategoryDtoLists 集合，每一个 AddDataAssetRelationCategoryDto 对象执行相应的操作
        for(AddDataAssetRelationCategoryDto addDataAssetRelationCategoryDto: addDataAssetDto.getAddDataAssetRelationCategoryDtoLists()){
            //调用 addDataAssetField 方法新增数据资产字段
            dataAssetRelationCategoryService.addDataAssetRelationCategory(addDataAssetRelationCategoryDto,newDataAssetCode);
        }

        System.out.println("返回新增的数据");
        return R.Success(addDataAssetDto);
    }

    /**
     * 数据资产编辑
     * @param updateDataAssetDto
     * @return
     */
    @Override
    public R updateDataAsset(UpdateDataAssetDto updateDataAssetDto) {
        logger.info("正在处理数据资产编辑请求");
        MPJLambdaWrapper<DataAsset> wrapper=new MPJLambdaWrapper<>();
        wrapper
                .selectAll(DataAsset.class)
                .eq(DataAsset::getDataAssetCode,updateDataAssetDto.getDataAssetCode());

        DataAsset dataAsset=dataAssetMapper.selectOne(wrapper);
        if (!ObjectUtils.isEmpty(dataAsset)){
            //编辑数据资产
            DataAsset dataAsset1= DataAsset.builder()
                    .assetNameCn(updateDataAssetDto.getAssetNameCn())
                    .assetNameEn(updateDataAssetDto.getAssetNameEn())
                    .assetDesc(updateDataAssetDto.getAssetDesc())
                    .build();
            UpdateWrapper<DataAsset> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq(!ObjectUtils.isEmpty(updateDataAssetDto.getDataAssetCode()),"data_asset_code",updateDataAssetDto.getDataAssetCode());
            dataAssetMapper.update(dataAsset1,updateWrapper);

            //编辑数据资产字段
            //遍历 UpdateDataAssetDto 中的 pdateDataAssetFieldDtoLists 集合，每一个 UpdateDataAssetFieldDto 对象执行相应的操作
            for(UpdateDataAssetFieldDto updateDataAssetFieldDto: updateDataAssetDto.getUpdateDataAssetFieldDtoLists()){
                //调用 updateDataAssetField 方法新增数据资产字段
                dataAssetFieldService.updateDataAssetField(updateDataAssetFieldDto, updateDataAssetDto.getDataAssetCode());
            }

            //进行编辑数据资产分类操作
            //遍历 UpdateDataAssetDto 中的 updateDataAssetRelationCategoryDtoLists 集合，每一个 UpdateDataAssetRelationCategoryDto 对象执行相应的操作
            for(UpdateDataAssetRelationCategoryDto updateDataAssetRelationCategoryDto: updateDataAssetDto.getUpdateDataAssetRelationCategoryDtoLists()){
                //调用 addDataAssetField 方法新增数据资产字段
                dataAssetRelationCategoryService.updateDataAssetRelationCategory(updateDataAssetRelationCategoryDto, updateDataAssetDto.getDataAssetCode());
            }
            logger.info("处理数据资产编辑请求完成");
            return R.Success("编辑成功");
        }
        return R.Failed("未找到该数据资产");
    }

    /**
     * 数据资产删除
     * @param deleteDataAssetDto
     * @return
     */
    @Override
    public R deleteDataAsset(DeleteDataAssetDto deleteDataAssetDto) {
        logger.info("正在处理数据资产删除请求");
        MPJLambdaWrapper<DataAsset> wrapper=new MPJLambdaWrapper<>();
        wrapper.selectAll(DataAsset.class)
                .eq(DataAsset::getDataAssetCode,deleteDataAssetDto.getDataAssetCode());
        DataAsset dataAsset=dataAssetMapper.selectOne(wrapper);
        if (!ObjectUtils.isEmpty(dataAsset)&&dataAsset.getIsDelete().equals(false)){
            //进行数据资产删除
            UpdateWrapper<DataAsset> updateWrapper =new UpdateWrapper<>();
            updateWrapper
                    .eq("data_asset_code",deleteDataAssetDto.getDataAssetCode());

            DataAsset dataAsset1=DataAsset.builder()
                    .isDelete(true)
                    .build();
            dataAssetMapper.update(dataAsset1,updateWrapper);
            logger.info("处理数据资产删除请求成功");
            return R.Success("删除成功");
        }
        logger.info("处理数据资产删除请求失败");
        return R.Failed("未找到数据存在");
    }

    /**
     * 数据资产状态更改
     * @param stateDataAssetDto
     * @return
     */
    @Override
    public R stateDataAsset(StateDataAssetDto stateDataAssetDto) {
        logger.info("正在处理数据资产状态更改");
        MPJLambdaWrapper<DataAsset> wrapper=new MPJLambdaWrapper<>();
        wrapper.selectAll(DataAsset.class)
                .eq(DataAsset::getDataAssetCode,stateDataAssetDto.getDataAssetCode());
        DataAsset dataAsset=dataAssetMapper.selectOne(wrapper);
        if (!ObjectUtils.isEmpty(dataAsset)&&dataAsset.getIsDelete().equals(false)){
            UpdateWrapper<DataAsset> updateWrapper=new UpdateWrapper<>();
            updateWrapper.eq("data_asset_code",stateDataAssetDto.getDataAssetCode());

            DataAsset dataAsset1= DataAsset.builder()
                    .dataAssetState(stateDataAssetDto.getDataAssetState())
                    .build();
            dataAssetMapper.update(dataAsset1,updateWrapper);
            return R.Success("状态更改成功");
        }
        return R.Failed("数据不存在");
    }

    /**
     * 数据资产批量发布
     * @param deleteDataAssetDtoList
     * @return
     */
    @Override
    public R batchPublishDataAsset(List<DeleteDataAssetDto> deleteDataAssetDtoList) {
        logger.info("正在处理数据资产批量发布请求");
        try {
            List<DataAsset> dataAssets = new ArrayList<>();
            //根据其编号从数据库中获取对应的 DataAsset 对象，并将其状态更新为新状态
            for (DeleteDataAssetDto deleteDataAssetDto : deleteDataAssetDtoList) {
                //通过 codeTableNumber 查询数据
                MPJLambdaWrapper<DataAsset> wrapper=new MPJLambdaWrapper<>();
                wrapper
                        .selectAll(DataAsset.class)
                        .eq(DataAsset::getDataAssetCode,deleteDataAssetDto.getDataAssetCode());
                DataAsset dataAsset = dataAssetMapper.selectOne(wrapper);
                //将 dataAssetCode 赋给 DataAsset 并保存
                if (!ObjectUtils.isEmpty(dataAsset)&&dataAsset.getDataAssetState()==0&&dataAsset.getIsDelete().equals(false)) {
                    dataAsset.setDataAssetState(1);
                    dataAssets.add(dataAsset);
                }
            }

            if (!dataAssets.isEmpty()) {
                this.updateBatchById(dataAssets);
                return R.Success("批量修改成功");
            }
        } catch (Exception e) {
            // 发生异常时回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return R.Failed("批量发布出现异常");
    }

    /**
     * 数据资产批量停用
     * @param deleteDataAssetDtoList
     * @return
     */
    @Override
    public R batchStopDataAsset(List<DeleteDataAssetDto> deleteDataAssetDtoList) {
        logger.info("正在处理数据资产批量停用请求");
        try {
            List<DataAsset> dataAssets = new ArrayList<>();
            //根据其编号从数据库中获取对应的 DataAsset 对象，并将其状态更新为新状态
            for (DeleteDataAssetDto deleteDataAssetDto : deleteDataAssetDtoList) {
                //通过 codeTableNumber 查询数据
                MPJLambdaWrapper<DataAsset> wrapper=new MPJLambdaWrapper<>();
                wrapper
                        .selectAll(DataAsset.class)
                        .eq(DataAsset::getDataAssetCode,deleteDataAssetDto.getDataAssetCode());
                DataAsset dataAsset = dataAssetMapper.selectOne(wrapper);
                //将 dataAssetCode 赋给 DataAsset 并保存
                if (!ObjectUtils.isEmpty(dataAsset)&&dataAsset.getDataAssetState()==0&&dataAsset.getIsDelete().equals(false)) {
                    dataAsset.setDataAssetState(2);
                    dataAssets.add(dataAsset);
                }
            }

            if (!dataAssets.isEmpty()) {
                this.updateBatchById(dataAssets);
                return R.Success("批量修改成功");
            }
        } catch (Exception e) {
            // 发生异常时回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return R.Failed("批量停用出现异常");
    }


}
