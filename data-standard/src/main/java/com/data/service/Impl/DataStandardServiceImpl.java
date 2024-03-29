package com.data.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.data.dto.CodeTable.StateCodeTableDto;
import com.data.dto.CodeTable.excel.ExportCodeTableExcel;
import com.data.dto.CodeValue.DeleteCodeValueDto;
import com.data.dto.CodeValue.excel.ExportCodeValueExcel;
import com.data.dto.DataStandard.*;
import com.data.dto.DataStandard.excel.DataStandardExcel;
import com.data.mapper.CodeValueMapper;
import com.data.vo.DataStandardVo;
import com.data.vo.EnumVo;
import com.data.entity.CodeTable;
import com.data.entity.CodeValue;
import com.data.entity.DataStandard;
import com.data.mapper.CodeTableMapper;
import com.data.mapper.DataStandardMapper;
import com.data.service.CodeTableService;
import com.data.service.DataStandardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.utils.R;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据标准表 服务实现类
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-15
 */
@Service
public class DataStandardServiceImpl extends ServiceImpl<DataStandardMapper, DataStandard> implements DataStandardService {
    private Logger logger= LoggerFactory.getLogger(getClass());
    @Autowired
    private DataStandardMapper dataStandardMapper;
    @Autowired
    private  CodeTableService codeTableService;
    @Autowired
    private CodeTableMapper codeTableMapper;
    @Autowired
    private CodeValueMapper codeValueMapper;


    /**
     * 数据标准表查询
     * @param dataStandardPageDto
     * @return
     */
    @Override
    public R selectDataStandard(DataStandardPageDto dataStandardPageDto) {
        logger.info("正在处理数据标准表查询请求");
        MPJLambdaWrapper<DataStandard> queryWrapper = new MPJLambdaWrapper<>();
        //将pageSize和pageNumber放入Page中
        Page<DataStandard> page=new Page<>(dataStandardPageDto.getPageNumber(),dataStandardPageDto.getPageSize());
        queryWrapper
                .select(DataStandard::getId,DataStandard::getDataStandardCode,DataStandard::getDataStandardCnName,DataStandard::getDataStandardEnName,DataStandard::getDataStandardExplain,DataStandard::getDataStandardSourceOrganization,
                        DataStandard::getDataStandardType,DataStandard::getDataStandardLength,DataStandard::getDataStandardAccuracy,DataStandard::getDataStandardDefaultValue,
                        DataStandard::getDataStandardValueMax,DataStandard::getDataStandardValueMin,DataStandard::getDataStandardEnumerationRange,DataStandard::getDataStandardState,
                        DataStandard::getDataStandardIsBlank,DataStandard::getDeleteFlag)
                .select(CodeTable::getCodeTableName)
                .leftJoin(CodeTable.class,CodeTable::getCodeTableNumber,DataStandard::getDataStandardEnumerationRange)
                .like(!ObjectUtils.isEmpty(dataStandardPageDto.getDataStandardCode()),DataStandard::getDataStandardCode,dataStandardPageDto.getDataStandardCode())
                .like(!ObjectUtils.isEmpty(dataStandardPageDto.getDataStandardCnName()),DataStandard::getDataStandardCnName,dataStandardPageDto.getDataStandardCnName())
                .like(!ObjectUtils.isEmpty(dataStandardPageDto.getDataStandardEnName()),DataStandard::getDataStandardEnName,dataStandardPageDto.getDataStandardEnName())
                .eq(!ObjectUtils.isEmpty(dataStandardPageDto.getDataStandardSourceOrganization()),DataStandard::getDataStandardSourceOrganization,dataStandardPageDto.getDataStandardSourceOrganization())
                .eq(!ObjectUtils.isEmpty(dataStandardPageDto.getDataStandardState()),DataStandard::getDataStandardState,dataStandardPageDto.getDataStandardState())
                .orderByAsc(DataStandard::getDataStandardState);
        queryWrapper.orderByDesc(DataStandard::getUpdateTime);

        IPage<DataStandard> dataStandardIPage=dataStandardMapper.selectPage(page,queryWrapper);
        List<DataStandard> records=dataStandardIPage.getRecords();
        Map responseData=new HashMap<>();
        responseData.put("data", records);
        responseData.put("total", dataStandardIPage.getTotal()); // 总记录数
        responseData.put("size", dataStandardIPage.getSize()); // 每页显示数量
        responseData.put("current", dataStandardIPage.getCurrent()); // 当前页码
//        responseData.put("orders", dataStandardIPage.orders()); // 排序信息
//        responseData.put("optimizeCountSql", dataStandardIPage.optimizeCountSql()); // 是否优化count语句
        responseData.put("pages", dataStandardIPage.getPages()); // 总页数
        return R.Success(responseData);
    }

    /**
     *数据标准枚举查询
     * @param selectEnumDto
     * @return
     */
    @Override
    public List selectDataStandardEnum(SelectEnumDto selectEnumDto) {
        logger.info("正在处理数据标准枚举查询请求");
        /*一对一列表查询输出
        MPJLambdaWrapper<CodeTable> wrapper = new MPJLambdaWrapper<CodeTable>()
//                .selectAll(CodeTable.class)//查询CodeTable表全部字段
                .select(CodeTable::getCodeTableNumber,CodeTable::getCodeTableName)
                .select(CodeValue::getCodeValueName,CodeValue::getCodeValueValue,CodeValue::getCodeValueDesc)
                .leftJoin(CodeValue.class,CodeValue::getCodeTableNumber, CodeTable::getCodeTableNumber)
                .eq(CodeTable::getCodeTableNumber,selectEnumDto.getCodeTableNumber());

        List<Enum> enumList = codeTableMapper.selectJoinList(Enum.class, wrapper);
        enumList.forEach(System.out::println);*/

        //一对多列表查询输出
        // 将 CodeTable 数据复制为 ExportCodeTableExcel 类型的数据
        List<EnumVo> enumVoList = new ArrayList<>();
        QueryWrapper queryWrapper=new QueryWrapper<>()
                .select("code_table_number","code_table_name")
                .eq("code_table_number",selectEnumDto.getCodeTableNumber());
        List<CodeTable> codeTableList = codeTableService.list(queryWrapper);
        codeTableList.forEach(
                codeTable -> {
                    //码表
                    EnumVo anEnumVo = new EnumVo();
                    //将一个对象（codeTable）的属性复制到另一个对象（anEnum）
                    BeanUtils.copyProperties(codeTable, anEnumVo);
                    List<CodeValue> codeValueList = dataStandardMapper.selectCodeValueByCodeTableNumber(codeTable.getCodeTableNumber());
                    //保存码值数据到 Enum 的 codeValueLists
                    anEnumVo.setCodeValueLists(codeValueList);

                    enumVoList.add(anEnumVo);
                });
        return enumVoList;
    }

    /**
     * 新增数据标准
     * @param addDataStandardDto
     * @return
     */
    @Override
    @Transactional
    public R addDataStandard(AddDataStandardDto addDataStandardDto) {
        logger.info("正在处理数据标准新增请求");

        try {
            //判断码表名称是否重复
            //构建查询条件
            MPJLambdaWrapper<DataStandard> lambdaQueryWrapperCn = new MPJLambdaWrapper<>();
            //输入查询条件——中文名称判空
            lambdaQueryWrapperCn
                    .select(DataStandard::getId,DataStandard::getDataStandardCode,DataStandard::getDataStandardCnName,DataStandard::getDataStandardEnName,DataStandard::getDataStandardExplain,DataStandard::getDataStandardSourceOrganization,
                            DataStandard::getDataStandardType,DataStandard::getDataStandardLength,DataStandard::getDataStandardAccuracy,DataStandard::getDataStandardDefaultValue,
                            DataStandard::getDataStandardValueMax,DataStandard::getDataStandardValueMin,DataStandard::getDataStandardEnumerationRange,DataStandard::getDataStandardState,
                            DataStandard::getDataStandardIsBlank,DataStandard::getDeleteFlag)
                    .select(CodeTable::getCodeTableName)
                    .leftJoin(CodeTable.class,CodeTable::getCodeTableNumber,DataStandard::getDataStandardEnumerationRange)
                    .eq(DataStandard::getDataStandardCnName,addDataStandardDto.getDataStandardCnName());
            logger.info("检验中文名称是否为空");
            DataStandard dataStandardCnName = dataStandardMapper.selectOne(lambdaQueryWrapperCn);
            //检查查询结果是否为空
            if(!ObjectUtils.isEmpty(dataStandardCnName)){
                return R.BAD_REQUEST("该中文名称已存在");
            }

            //输入查询条件——英文名称判空
            MPJLambdaWrapper<DataStandard> lambdaQueryWrapperEn = new MPJLambdaWrapper<>();
            lambdaQueryWrapperEn
                    .select(DataStandard::getId,DataStandard::getDataStandardCode,DataStandard::getDataStandardCnName,DataStandard::getDataStandardEnName,DataStandard::getDataStandardExplain,DataStandard::getDataStandardSourceOrganization,
                            DataStandard::getDataStandardType,DataStandard::getDataStandardLength,DataStandard::getDataStandardAccuracy,DataStandard::getDataStandardDefaultValue,
                            DataStandard::getDataStandardValueMax,DataStandard::getDataStandardValueMin,DataStandard::getDataStandardEnumerationRange,DataStandard::getDataStandardState,
                            DataStandard::getDataStandardIsBlank,DataStandard::getDeleteFlag)
                    .select(CodeTable::getCodeTableName)
                    .leftJoin(CodeTable.class,CodeTable::getCodeTableNumber,DataStandard::getDataStandardEnumerationRange)
                    .eq(DataStandard::getDataStandardEnName,addDataStandardDto.getDataStandardEnName());
            DataStandard dataStandardEnName = dataStandardMapper.selectOne(lambdaQueryWrapperEn);
            logger.info("检查查询结果是否为空");
            //检查查询结果是否为空
            if(!ObjectUtils.isEmpty(dataStandardEnName)){
                return R.BAD_REQUEST("该英文名称已存在");
            }

           //枚举范围是否为空
            if (!ObjectUtils.isEmpty(addDataStandardDto.getDataStandardEnumerationRange())){
                logger.info("查询对应码表是否存在");
                //查询对应码表是否存在
                CodeTable codeTable=codeTableMapper.getByCodeTableNumber(addDataStandardDto.getDataStandardEnumerationRange());
                //检查查询结果是否为空
                if(ObjectUtils.isEmpty(codeTable)){
                    return R.Failed("对应码表未存在");
                }else if (codeTable.getCodeTableState()!=1){
                    return R.Failed("对应码表未发布");
                }
            }

            //生成编号
            String BZ="BZ";       //前缀
            String newDataStandardCode;
            MPJLambdaWrapper<DataStandard> lambdaQueryWrapper1 = new MPJLambdaWrapper<>();
            //查询符合条件的最后一条数据
            lambdaQueryWrapper1
                    .select(DataStandard::getId,DataStandard::getDataStandardCode,DataStandard::getDataStandardCnName,DataStandard::getDataStandardEnName,DataStandard::getDataStandardExplain,DataStandard::getDataStandardSourceOrganization,
                            DataStandard::getDataStandardType,DataStandard::getDataStandardLength,DataStandard::getDataStandardAccuracy,DataStandard::getDataStandardDefaultValue,
                            DataStandard::getDataStandardValueMax,DataStandard::getDataStandardValueMin,DataStandard::getDataStandardEnumerationRange,DataStandard::getDataStandardState,
                            DataStandard::getDataStandardIsBlank,DataStandard::getDeleteFlag)
                    .orderByDesc(DataStandard::getDataStandardCode)
                    .last("limit 1");
            logger.info("查询符合条件的最后一条数据");
            DataStandard dataStandard = dataStandardMapper.selectOne(lambdaQueryWrapper1);

            if (ObjectUtils.isEmpty(dataStandard)) {
                //如果数据库没有 0001
                newDataStandardCode =BZ + "00001";
            }else {
                //如果数据库中有数据 拿最后一条数据的序号
                //最后一条数据账号
                String lastCodeTableNumber = dataStandard.getDataStandardCode();
                //截取序号部分，并将其转换为整数
                String idStr = lastCodeTableNumber.substring(3, lastCodeTableNumber.length());
                //将序号加一，并格式化为五位数的字符串
                Integer id = Integer.valueOf(idStr) + 1;
                String formatId = String.format("%05d", id);
                //拼接成新的 codeTableNumber，并将其设置到 addCodeTableDto 对象中
                newDataStandardCode = BZ + (formatId);
            }
            //存入码表数据到 DataStandard
            DataStandard dataStandard1 = DataStandard.builder()
                    .dataStandardCode(newDataStandardCode)
                    .dataStandardCnName(addDataStandardDto.getDataStandardCnName())
                    .dataStandardEnName(addDataStandardDto.getDataStandardEnName())
                    .dataStandardExplain(addDataStandardDto.getDataStandardExplain())
                    .dataStandardSourceOrganization(addDataStandardDto.getDataStandardSourceOrganization())
                    .dataStandardType(addDataStandardDto.getDataStandardType())
                    .dataStandardLength(addDataStandardDto.getDataStandardLength())
                    .dataStandardAccuracy(addDataStandardDto.getDataStandardAccuracy())
                    .dataStandardDefaultValue(addDataStandardDto.getDataStandardDefaultValue())
                    .dataStandardValueMax(addDataStandardDto.getDataStandardValueMax())
                    .dataStandardValueMin(addDataStandardDto.getDataStandardValueMin())
                    .dataStandardEnumerationRange(addDataStandardDto.getDataStandardEnumerationRange())
                    .dataStandardIsBlank(addDataStandardDto.getDataStandardIsBlank())
                    .build();
            //进行新增码表操作
            dataStandardMapper.insert(dataStandard1);
            System.out.println("返回新增的数据");
            return R.Success(dataStandardMapper.selectByDataStandardCode(newDataStandardCode));
    } catch (Exception  e) {
        // 发生异常时回滚事务
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
        return R.Failed("新增数据标准信息异常");
    }

    /**
     * 编辑数据标准
     * @param updateDataStandardDto
     * @return
     */
    @Override
    public boolean updateDataStandard(UpdateDataStandardDto updateDataStandardDto) {
        logger.info("正在处理数据标准编辑请求");
        DataStandard dataStandard=dataStandardMapper.getByCode(updateDataStandardDto.getDataStandardCode());
        if (dataStandard!=null&&dataStandard.getDataStandardState()!=1){
            DataStandard dataStandard1=DataStandard.builder()
                    .dataStandardCnName(updateDataStandardDto.getDataStandardCnName())
                    .dataStandardEnName(updateDataStandardDto.getDataStandardEnName())
                    .dataStandardExplain(updateDataStandardDto.getDataStandardExplain())
                    .dataStandardSourceOrganization(updateDataStandardDto.getDataStandardSourceOrganization())
                    .dataStandardType(updateDataStandardDto.getDataStandardType())
                    .dataStandardLength(updateDataStandardDto.getDataStandardLength())
                    .dataStandardAccuracy(updateDataStandardDto.getDataStandardAccuracy())
                    .dataStandardDefaultValue(updateDataStandardDto.getDataStandardDefaultValue())
                    .dataStandardValueMax(updateDataStandardDto.getDataStandardValueMax())
                    .dataStandardValueMin(updateDataStandardDto.getDataStandardValueMin())
                    .dataStandardEnumerationRange(updateDataStandardDto.getDataStandardEnumerationRange())
                    .dataStandardIsBlank(updateDataStandardDto.getDataStandardIsBlank())
                    .build();

            UpdateWrapper<DataStandard> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("data_standard_code",updateDataStandardDto.getDataStandardCode());
            int count=dataStandardMapper.update(dataStandard1,updateWrapper);
            return count>0;
        }
        return false;
    }

    /**
     * 更改数据标准状态
     * @param stateDataStandardDto
     * @return
     */
    @Override
    public boolean stateDataStandard(StateDataStandardDto stateDataStandardDto) {
        logger.info("正在处理数据标准状态更改请求");
        DataStandard dataStandard=dataStandardMapper.getByCode(stateDataStandardDto.getDataStandardCode());
        if (dataStandard!=null){
            DataStandard dataStandard1=DataStandard.builder()
                    .dataStandardState(stateDataStandardDto.getDataStandardState())
                    .build();

            UpdateWrapper<DataStandard> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("data_standard_code",stateDataStandardDto.getDataStandardCode());
            int count=dataStandardMapper.update(dataStandard1,updateWrapper);
            return count>0;
        }
        return false;
    }

    /**
     * 删除数据标准
     * @param deleteDataStandardDto
     * @return
     */
    @Override
    public boolean deleteDataStandard(DeleteDataStandardDto deleteDataStandardDto) {
        logger.info("正在处理数据标准删除请求");
        DataStandard dataStandard=dataStandardMapper.getByCode(deleteDataStandardDto.getDataStandardCode());
        if (dataStandard!=null&&dataStandard.getDeleteFlag()==0){
            //进行删除
            UpdateWrapper<DataStandard> updateWrapper =new UpdateWrapper<>();
            updateWrapper
                    .eq(deleteDataStandardDto.getDataStandardCode()!=null,"data_standard_code",deleteDataStandardDto.getDataStandardCode());

            DataStandard dataStandard1=DataStandard.builder()
                    .deleteFlag(1)
                    .build();
            int count=dataStandardMapper.update(dataStandard1,updateWrapper);
            return count>0;
        }
        return false;
    }

    /**
     * 数据标准批量发布
     * @param stateDataStandardDtos
     * @return
     */
    @Override
    public boolean batchPublishDataStandard(List<StateDataStandardDto> stateDataStandardDtos) {
        logger.info("正在处理数据标准批量发布请求");

        try {
            List<DataStandard> dataStandards = new ArrayList<>();
            //根据其编号从数据库中获取对应的 DataStandard 对象，并将其状态更新为新状态
            for (StateDataStandardDto stateDataStandardDto : stateDataStandardDtos) {
                //通过 dataStandardCode 查询数据
                DataStandard dataStandard = dataStandardMapper.getByCode(stateDataStandardDto.getDataStandardCode());
                //将 dataStandarState 的值赋给 dataStandard 并保存
                if (dataStandard != null&&dataStandard.getDataStandardState()==0) {
                    dataStandard.setDataStandardState(1);
                    dataStandards.add(dataStandard);
                }
            }

            if (!dataStandards.isEmpty()) {
                boolean count = this.updateBatchById(dataStandards);
                return count ;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * 数据标准批量停用
     * @param stateDataStandardDtos
     * @return
     */
    @Override
    public boolean batchStopDataStanadard(List<StateDataStandardDto> stateDataStandardDtos) {
        logger.info("正在处理数据标准批量停用请求");

        try {
            List<DataStandard> dataStandards = new ArrayList<>();
            //根据其编号从数据库中获取对应的 CodeTable 对象，并将其状态更新为新状态
            for (StateDataStandardDto stateDataStandardDto : stateDataStandardDtos) {
                //通过 dataStandardCode 查询数据
                DataStandard dataStandard = dataStandardMapper.getByCode(stateDataStandardDto.getDataStandardCode());
                //将 stateDataState 的值赋给 dataStandard 并保存
                if (dataStandard != null&&dataStandard.getDataStandardState()==1) {
                    dataStandard.setDataStandardState(2);
                    dataStandards.add(dataStandard);
                }
            }

            if (!dataStandards.isEmpty()) {
                boolean count = this.updateBatchById(dataStandards);
                return count ;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * 查询码表信息
     * 导出数据标准excel时调用此方法
     * @param number
     * @return
     */
    public List  selectCodeTableExcel( String number ) {
        logger.info("正在进入码表数据查询请求");
        //查询数据
        //添加与 DataStandard 连接的条件
        QueryWrapper queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(!ObjectUtils.isEmpty(number),"code_table_number",number);

        // 将 CodeTable 数据复制为 ExportCodeTableExcel 类型的数据
        List<ExportCodeTableExcel> exportList = new ArrayList<>();
        List<CodeTable> codeTableList = codeTableService.list(queryWrapper);
        codeTableList.forEach(
                codeTable -> {
                    //码表
                    ExportCodeTableExcel exportCodeTableExcel = new ExportCodeTableExcel();
                    //将一个对象（codeTable）的属性复制到另一个对象（exportCodeTableExcel）
                    BeanUtils.copyProperties(codeTable,exportCodeTableExcel);
                    List<ExportCodeValueExcel> exportCodeValueExcels = codeValueMapper.selectByCodeTableNumber(codeTable.getCodeTableNumber());
                    //保存码值数据到ExportCodeTableExcel的exportCodeValueExcelList
                    exportCodeTableExcel.setExportCodeValueExcelList(exportCodeValueExcels);

                    exportList.add(exportCodeTableExcel);
                }
        );
        return exportList;
    }

    /**
     * 数据标准导入
     * @param newDataStandardExcel
     * @return
     */
    @Override
    public R saveDataStandardExcels(DataStandardExcel newDataStandardExcel) {
        logger.info("正在处理数据标准导入请求");
        if (!ObjectUtils.isEmpty(newDataStandardExcel.getDataStandardCnName())){
            if(!ObjectUtils.isEmpty(newDataStandardExcel.getDataStandardEnName())){
                if (!ObjectUtils.isEmpty(newDataStandardExcel.getDataStandardSourceOrganization())){
                    if (!ObjectUtils.isEmpty(newDataStandardExcel.getDataStandardType())){
                        try {
                            //判断码表名称是否重复
                            //构建查询条件
                            MPJLambdaWrapper<DataStandard> lambdaQueryWrapperCn = new MPJLambdaWrapper<>();
                            //输入查询条件——中文名称是否重复
                            lambdaQueryWrapperCn
                                    .select(DataStandard::getId,DataStandard::getDataStandardCode,DataStandard::getDataStandardCnName,DataStandard::getDataStandardEnName,DataStandard::getDataStandardExplain,DataStandard::getDataStandardSourceOrganization,
                                            DataStandard::getDataStandardType,DataStandard::getDataStandardLength,DataStandard::getDataStandardAccuracy,DataStandard::getDataStandardDefaultValue,
                                            DataStandard::getDataStandardValueMax,DataStandard::getDataStandardValueMin,DataStandard::getDataStandardEnumerationRange,DataStandard::getDataStandardState,
                                            DataStandard::getDataStandardIsBlank,DataStandard::getDeleteFlag)
                                    .select(CodeTable::getCodeTableName)
                                    .leftJoin(CodeTable.class,CodeTable::getCodeTableNumber,DataStandard::getDataStandardEnumerationRange)
                                    .eq(DataStandard::getDataStandardCnName,newDataStandardExcel.getDataStandardCnName());
                            logger.info("检验中文名称是否重复");
                            DataStandard dataStandardCnName = dataStandardMapper.selectOne(lambdaQueryWrapperCn);
                            //检查查询结果是否为空
                            if(!ObjectUtils.isEmpty(dataStandardCnName)){
                                return R.BAD_REQUEST(dataStandardCnName+"：该中文名称已存在");
                            }

                            //输入查询条件——英文名称是否重复
                            MPJLambdaWrapper<DataStandard> lambdaQueryWrapperEn = new MPJLambdaWrapper<>();
                            lambdaQueryWrapperEn
                                    .select(DataStandard::getId,DataStandard::getDataStandardCode,DataStandard::getDataStandardCnName,DataStandard::getDataStandardEnName,DataStandard::getDataStandardExplain,DataStandard::getDataStandardSourceOrganization,
                                            DataStandard::getDataStandardType,DataStandard::getDataStandardLength,DataStandard::getDataStandardAccuracy,DataStandard::getDataStandardDefaultValue,
                                            DataStandard::getDataStandardValueMax,DataStandard::getDataStandardValueMin,DataStandard::getDataStandardEnumerationRange,DataStandard::getDataStandardState,
                                            DataStandard::getDataStandardIsBlank,DataStandard::getDeleteFlag)
                                    .select(CodeTable::getCodeTableName)
                                    .leftJoin(CodeTable.class,CodeTable::getCodeTableNumber,DataStandard::getDataStandardEnumerationRange)
                                    .eq(DataStandard::getDataStandardEnName,newDataStandardExcel.getDataStandardEnName());
                            DataStandard dataStandardEnName = dataStandardMapper.selectOne(lambdaQueryWrapperEn);
                            logger.info("检查查询结果是否重复");
                            //检查查询结果是否为空
                            if(!ObjectUtils.isEmpty(dataStandardEnName)){
                                return R.BAD_REQUEST(dataStandardEnName+"：该英文名称已存在");
                            }

                            //枚举范围是否为空
                            if (!ObjectUtils.isEmpty(newDataStandardExcel.getDataStandardEnumerationRange())){
                                logger.info("查询对应码表是否存在");
                                //查询对应码表是否存在
                                CodeTable codeTable=codeTableMapper.getByCodeTableNumber(newDataStandardExcel.getDataStandardEnumerationRange());
                                //检查查询结果是否为空
                                if(ObjectUtils.isEmpty(codeTable)){
                                    return R.Failed("对应码表未存在");
                                }else if (codeTable.getCodeTableState()!=1){
                                    return R.Failed("对应码表未发布");
                                }
                            }

                            //生成编号
                            String BZ="BZ";       //前缀
                            String newDataStandardCode;
                            MPJLambdaWrapper<DataStandard> lambdaQueryWrapper1 = new MPJLambdaWrapper<>();
                            //查询符合条件的最后一条数据
                            lambdaQueryWrapper1
                                    .select(DataStandard::getId,DataStandard::getDataStandardCode,DataStandard::getDataStandardCnName,DataStandard::getDataStandardEnName,DataStandard::getDataStandardExplain,DataStandard::getDataStandardSourceOrganization,
                                            DataStandard::getDataStandardType,DataStandard::getDataStandardLength,DataStandard::getDataStandardAccuracy,DataStandard::getDataStandardDefaultValue,
                                            DataStandard::getDataStandardValueMax,DataStandard::getDataStandardValueMin,DataStandard::getDataStandardEnumerationRange,DataStandard::getDataStandardState,
                                            DataStandard::getDataStandardIsBlank,DataStandard::getDeleteFlag)
                                    .orderByDesc(DataStandard::getDataStandardCode)
                                    .last("limit 1");
                            logger.info("查询符合条件的最后一条数据");
                            DataStandard dataStandard = dataStandardMapper.selectOne(lambdaQueryWrapper1);

                            if (ObjectUtils.isEmpty(dataStandard)) {
                                //如果数据库没有 0001
                                newDataStandardCode =BZ + "00001";
                            }else {
                                //如果数据库中有数据 拿最后一条数据的序号
                                //最后一条数据账号
                                String lastCodeTableNumber = dataStandard.getDataStandardCode();
                                //截取序号部分，并将其转换为整数
                                String idStr = lastCodeTableNumber.substring(3, lastCodeTableNumber.length());
                                //将序号加一，并格式化为五位数的字符串
                                Integer id = Integer.valueOf(idStr) + 1;
                                String formatId = String.format("%05d", id);
                                //拼接成新的 codeTableNumber，并将其设置到 addCodeTableDto 对象中
                                newDataStandardCode = BZ + (formatId);
                            }
                            //存入码表数据到 DataStandard
                            DataStandard dataStandard1 = DataStandard.builder()
                                    .dataStandardCode(newDataStandardCode)
                                    .dataStandardCnName(newDataStandardExcel.getDataStandardCnName())
                                    .dataStandardEnName(newDataStandardExcel.getDataStandardEnName())
                                    .dataStandardExplain(newDataStandardExcel.getDataStandardExplain())
                                    .dataStandardSourceOrganization(newDataStandardExcel.getDataStandardSourceOrganization())
                                    .dataStandardType(newDataStandardExcel.getDataStandardType())
                                    .dataStandardLength(newDataStandardExcel.getDataStandardLength())
                                    .dataStandardAccuracy(newDataStandardExcel.getDataStandardAccuracy())
                                    .dataStandardDefaultValue(newDataStandardExcel.getDataStandardDefaultValue())
                                    .dataStandardValueMax(newDataStandardExcel.getDataStandardValueMax())
                                    .dataStandardValueMin(newDataStandardExcel.getDataStandardValueMin())
                                    .dataStandardEnumerationRange(newDataStandardExcel.getDataStandardEnumerationRange())
                                    .dataStandardIsBlank(newDataStandardExcel.getDataStandardIsBlank())
                                    .build();
                            //进行新增码表操作
                            dataStandardMapper.insert(dataStandard1);
                            System.out.println("返回新增的数据");
                            return R.Success(dataStandardMapper.selectByDataStandardCode(newDataStandardCode));
                        } catch (Exception  e) {
                            // 发生异常时回滚事务
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        }
                    }
                    return R.Failed("数据类型为空");
                }
                return R.Failed("来源机构为空");

            }
                return R.Failed("英文文名称为空");
        }
        return R.Failed("中文名称为空");
    }


}
