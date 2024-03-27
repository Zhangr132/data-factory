package com.data.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.data.dto.CodeTable.excel.ExportCodeTableExcel;
import com.data.dto.CodeValue.excel.ExportCodeValueExcel;
import com.data.dto.DataStandard.AddDataStandardDto;
import com.data.dto.DataStandard.DataStandardPageDto;
import com.data.entity.Enum;
import com.data.dto.DataStandard.SelectEnumDto;
import com.data.entity.CodeTable;
import com.data.entity.CodeValue;
import com.data.entity.DataStandard;
import com.data.mapper.CodeTableMapper;
import com.data.mapper.DataStandardMapper;
import com.data.service.CodeTableService;
import com.data.service.DataStandardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.utils.Md5Util;
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
        QueryWrapper queryWrapper=new QueryWrapper<>();
        //将pageSize和pageNumber放入Page中
        Page<DataStandard> page=new Page<>(dataStandardPageDto.getPageNumber(),dataStandardPageDto.getPageSize());
        queryWrapper
                .select("data_standard_code","data_standard_cn_name","data_standard_en_name","data_standard_explain","data_standard_source_organization",
                        "data_standard_type","data_standard_length","data_standard_accuracy","data_standard_default_value","data_standard_value_max",
                        "data_standard_value_min","data_standard_enumeration_range","code_table_name","data_standard_state","data_standard_is_blank","delete_flag",
                        "create_time","update_time")
                .like(!ObjectUtils.isEmpty(dataStandardPageDto.getDataStandardCode()),"data_standard_code",dataStandardPageDto.getDataStandardCode())
                .like(!ObjectUtils.isEmpty(dataStandardPageDto.getDataStandardCnName()),"data_standard_cn_name",dataStandardPageDto.getDataStandardCnName())
                .like(!ObjectUtils.isEmpty(dataStandardPageDto.getDataStandardEnName()),"data_standard_en_name",dataStandardPageDto.getDataStandardEnName())
                .eq(!ObjectUtils.isEmpty(dataStandardPageDto.getDataStandardSourceOrganization()),"data_standard_source_organization",dataStandardPageDto.getDataStandardSourceOrganization())
                .eq(!ObjectUtils.isEmpty(dataStandardPageDto.getDataStandardState()),"data_standard_state",dataStandardPageDto.getDataStandardState())
                .orderByAsc("data_standard_state");
        queryWrapper.orderByDesc("create_time");

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
        List<Enum> enumList = new ArrayList<>();
        QueryWrapper queryWrapper=new QueryWrapper<>()
                .select("code_table_number","code_table_name")
                .eq(!ObjectUtils.isEmpty(selectEnumDto.getCodeTableNumber()),"code_table_number",selectEnumDto.getCodeTableNumber());
        List<CodeTable> codeTableList = codeTableService.list(queryWrapper);
        codeTableList.forEach(
                codeTable -> {
                    //码表
                    Enum anEnum = new Enum();
                    //将一个对象（codeTable）的属性复制到另一个对象（anEnum）
                    BeanUtils.copyProperties(codeTable,anEnum);
                    List<CodeValue> codeValueList = dataStandardMapper.selectCodeValueByCodeTableNumber(codeTable.getCodeTableNumber());
                    //保存码值数据到 Enum 的 codeValueLists
                    anEnum.setCodeValueLists(codeValueList);

                    enumList.add(anEnum);
                });
        return R.Success(enumList);
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
            LambdaQueryWrapper<DataStandard> lambdaQueryWrapperCn = new LambdaQueryWrapper<>();
            //输入查询条件——中文名称判空
            lambdaQueryWrapperCn
                    .eq(DataStandard::getDataStandardCnName,addDataStandardDto.getDataStandardCnName());
            DataStandard dataStandardCnName = getOne(lambdaQueryWrapperCn);
            logger.info("检验中文名称是否为空");
            //检查查询结果是否为空
            if(!ObjectUtils.isEmpty(dataStandardCnName)){
                return R.BAD_REQUEST("该中文名称已存在");
            }

            //输入查询条件——英文名称判空
            LambdaQueryWrapper<DataStandard> lambdaQueryWrapperEn = new LambdaQueryWrapper<>();
            lambdaQueryWrapperEn
                    .eq(DataStandard::getDataStandardEnName,addDataStandardDto.getDataStandardEnName());
            DataStandard dataStandardEnName = getOne(lambdaQueryWrapperEn);
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
            LambdaQueryWrapper<DataStandard> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            //查询符合条件的最后一条数据
            lambdaQueryWrapper1.orderByDesc(DataStandard::getDataStandardCode).last("limit 1");
            DataStandard dataStandard = getOne(lambdaQueryWrapper1);
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
