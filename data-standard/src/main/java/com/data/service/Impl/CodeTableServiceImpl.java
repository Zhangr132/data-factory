package com.data.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.data.dto.CodeTable.AddCodeTableDto;
import com.data.dto.CodeTable.CodeTablePageDto;
import com.data.dto.CodeTable.UpdateCodeTableDto;
import com.data.dto.CodeValue.AddCodeValueDto;
import com.data.entity.CodeTable;
import com.data.mapper.CodeTableMapper;
import com.data.service.CodeTableService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.service.CodeValueService;
import com.data.utils.Md5Util;
import com.data.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 码表管理 服务实现类
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-15
 */
@Service
public class CodeTableServiceImpl extends ServiceImpl<CodeTableMapper, CodeTable> implements CodeTableService {
    private Logger logger= LoggerFactory.getLogger(getClass());
    @Autowired
    private CodeTableMapper codeTableMapper;
    @Autowired
    private CodeValueService codeValueService;

    /**
     * 码表分页查询
     * @param codeTablePageDto
     * @return
     */
    @Override
    public R selectCodeTable(CodeTablePageDto codeTablePageDto) {
        logger.info("正在处理分页查询请求");
        QueryWrapper queryWrapper=new QueryWrapper<>();
        //将pageSize和pageNumber放入Page中
        Page<CodeTable> page=new Page<>(codeTablePageDto.getPageNumber(),codeTablePageDto.getPageSize());
        queryWrapper
                .select("code_table_number","code_table_name","code_table_desc","code_table_state","delete_flag","code_table.create_time",
                        "code_table.update_time")
                .like(codeTablePageDto.getCodeTableName()!=null,"code_table_name",codeTablePageDto.getCodeTableName())
                .eq(codeTablePageDto.getCodeTableState()!=null,"code_table_state",codeTablePageDto.getCodeTableState());

        IPage<CodeTable> dataDicValIPage=codeTableMapper.selectPage(page,queryWrapper);
        List<CodeTable> records=dataDicValIPage.getRecords();
        Map responseData=new HashMap<>();
        responseData.put("data", records);
        responseData.put("total", dataDicValIPage.getTotal()); // 总记录数
        responseData.put("size", dataDicValIPage.getSize()); // 每页显示数量
        responseData.put("current", dataDicValIPage.getCurrent()); // 当前页码
//        responseData.put("orders", dataDicValIPage.orders()); // 排序信息
//        responseData.put("optimizeCountSql", dataDicValIPage.optimizeCountSql()); // 是否优化count语句
        responseData.put("pages", dataDicValIPage.getPages()); // 总页数
        return R.Success(responseData);
    }

    @Override
    public R addCodeTable(AddCodeTableDto addCodeTableDto) {
        logger.info("正在处理码表新增请求");
        //判断码表名称是否重复
        LambdaQueryWrapper<CodeTable> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //输入查询条件
        lambdaQueryWrapper.eq(CodeTable::getCodeTableName,addCodeTableDto.getCodeTableName());
        CodeTable codeTableName = getOne(lambdaQueryWrapper);
        //检查查询结果是否为空
        if(!ObjectUtils.isEmpty(codeTableName)){
            return R.BAD_REQUEST("该码表名称已存在");
        }

        //生成编号
        String Mzb="MZB";       //前缀
        int i = 1;              // 初始序号
        while (i <= 99999) {
            String newCodeTableNumber = Mzb + String.format("%05d", i); // 生成新账号
            CodeTable existingName = codeTableMapper.getByCodeTableNumber(newCodeTableNumber);
            if (existingName == null) {
                //存入码表数据到CodeTable
                CodeTable codeTable = CodeTable.builder()
                        .codeTableNumber(newCodeTableNumber)
                        .codeTableName(addCodeTableDto.getCodeTableName())
                        .codeTableDesc(addCodeTableDto.getCodeTableDesc())
                        .build();
                //进行新增码表操作
                codeTableMapper.insert(codeTable);

               //新增码值
                for(AddCodeValueDto addCodeValueDto: addCodeTableDto.getItems()){
                    addCodeValueDto.setCodeTableNumber(codeTable.getCodeTableNumber());
                    codeValueService.addCodeValue(addCodeValueDto);
                }
                return R.Success(codeTableMapper.getByCodeTableNumber(newCodeTableNumber));
//                return R.Success("新增码表成功");
            }
            i++; // 序号加1
        }
        return R.Failed("新增码表失败");

    }

    @Override
    public boolean updateCodeTable(UpdateCodeTableDto updateCodeTableDto) {
        logger.info("正在处理码表编辑请求");
        CodeTable codeTable=codeTableMapper.getByCodeTableNumber(updateCodeTableDto.getCodeTableNumber());
        if (codeTable!=null){
            CodeTable codeTable1=CodeTable.builder()
                    .codeTableName(updateCodeTableDto.getCodeTableName())
                    .codeTableDesc(updateCodeTableDto.getCodeTableDesc())
                    .build();

            UpdateWrapper<CodeTable> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("code_table_number",updateCodeTableDto.getCodeTableNumber());
            int count=codeTableMapper.update(codeTable1,updateWrapper);
            return count>0;
        }
        return false;
    }
}
