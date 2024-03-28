package com.data.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.data.dto.DataStandard.AddDataStandardDto;
import com.data.dto.DataStandard.DataStandardPageDto;
import com.data.vo.DataStandardVo;
import com.data.vo.EnumVo;
import com.data.dto.DataStandard.SelectEnumDto;
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
    public R selectDataStandardEnum(SelectEnumDto selectEnumDto) {
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
                .eq(!ObjectUtils.isEmpty(selectEnumDto.getCodeTableNumber()),"code_table_number",selectEnumDto.getCodeTableNumber());
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
        return R.Success(enumVoList);
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


}
