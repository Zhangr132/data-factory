package com.data.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.data.controller.CodeValueController;
import com.data.dto.CodeTable.*;
import com.data.dto.CodeTable.excel.CodeTableExcel;
import com.data.dto.CodeValue.AddCodeValueDto;
import com.data.dto.CodeValue.DeleteCodeValueDto;
import com.data.dto.CodeValue.excel.CodeValueExcel;
import com.data.entity.CodeTable;
import com.data.mapper.CodeTableMapper;
import com.data.service.CodeTableService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.service.CodeValueService;
import com.data.utils.GetRequestMessage;
import com.data.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
@Slf4j
@Service
public class CodeTableServiceImpl extends ServiceImpl<CodeTableMapper, CodeTable> implements CodeTableService {

    @Autowired
    private CodeTableMapper codeTableMapper;
    @Autowired
    private CodeValueService codeValueService;
    @Autowired
    private CodeValueController codeValueController;


    /**
     * 码表分页查询
     * @param codeTablePageDto
     * @return
     */
    @Override
    public R selectCodeTable(CodeTablePageDto codeTablePageDto, HttpServletRequest request) {
        //获取当前登录用户
        String username=GetRequestMessage.getUsername(request);
        log.info(username+"正在处理分页查询请求");

        QueryWrapper queryWrapper=new QueryWrapper<>();
        //将 pageSize 和 pageNumber 放入Page中
        Page<CodeTable> page=new Page<>(codeTablePageDto.getPageNumber(),codeTablePageDto.getPageSize());
//        Page<CodeTable> page=Page.of(codeTablePageDto.getPageNumber(),codeTablePageDto.getPageSize());
        //设置排序条件
        page.addOrder(new OrderItem("code_table.update_time",false),new OrderItem("code_table_state",true));
        queryWrapper
                .select("code_table.code_table_number","code_table_name","code_table_desc","code_table_state","code_table.delete_flag","code_table.create_time",
                        "code_table.update_time")
                .like(!ObjectUtils.isEmpty(codeTablePageDto.getCodeTableName()),"code_table_name",codeTablePageDto.getCodeTableName())
                .eq(!ObjectUtils.isEmpty(codeTablePageDto.getCodeTableState()),"code_table_state",codeTablePageDto.getCodeTableState())
                .eq("code_table.delete_flag",0);
//        queryWrapper
//                .orderByDesc("code_table.update_time");
//        queryWrapper
//                .orderByAsc("code_table_state");

        IPage<CodeTable> codeTableIPage=codeTableMapper.selectPage(page,queryWrapper);
        List<CodeTable> records=codeTableIPage.getRecords();
        Map responseData=new HashMap<>();
        responseData.put("data", records);
        responseData.put("total", codeTableIPage.getTotal()); // 总记录数
        responseData.put("pageSize", codeTableIPage.getSize()); // 每页显示数量
        responseData.put("pageNumber", codeTableIPage.getCurrent()); // 当前页码
//        responseData.put("orders", codeTableIPage.orders()); // 排序信息
//        responseData.put("optimizeCountSql", codeTableIPage.optimizeCountSql()); // 是否优化count语句
        responseData.put("pages", codeTableIPage.getPages()); // 总页数
        log.info(username+"分页查询请求处理完毕");
        return R.Success(responseData);
    }

    /**
     * 码表新增
     * @param addCodeTableDto
     * @return
     */
    @Transactional
    @Override
    public R addCodeTable(AddCodeTableDto addCodeTableDto, HttpServletRequest request) {
        String username=GetRequestMessage.getUsername(request);
        log.info(username+"正在处理码表新增请求");
        try {
            //判断码表名称是否重复
            //构建查询条件
            LambdaQueryWrapper<CodeTable> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            //输入查询条件
            lambdaQueryWrapper
                    .eq(CodeTable::getCodeTableName,addCodeTableDto.getCodeTableName())
                    .eq(CodeTable::getDeleteFlag,0);
            CodeTable codeTableName = getOne(lambdaQueryWrapper);
            //检查查询结果是否为空
            if(!ObjectUtils.isEmpty(codeTableName)){
                return R.BAD_REQUEST("该码表名称已存在");
            }

            //生成编号
            String Mzb="MZB";       //前缀
            String newCodeTableNumber;
            LambdaQueryWrapper<CodeTable> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            //查询符合条件的最后一条数据
            lambdaQueryWrapper1.orderByDesc(CodeTable::getCodeTableNumber).last("limit 1");
            CodeTable codeTable1 = getOne(lambdaQueryWrapper1);
            if (ObjectUtils.isEmpty(codeTable1)) {
                //如果数据库没有 0001
                newCodeTableNumber =Mzb + "00001";
    //            addCodeTableDto.setCodeTableNumber(Mzb + "00001");
            }else {
                //如果数据库中有数据 拿最后一条数据的序号
                //最后一条数据账号
                String lastCodeTableNumber = codeTable1.getCodeTableNumber();
                //截取序号部分，并将其转换为整数
                String idStr = lastCodeTableNumber.substring(3, lastCodeTableNumber.length());
                //将序号加一，并格式化为五位数的字符串
                Integer id = Integer.valueOf(idStr) + 1;
                String formatId = String.format("%05d", id);
                //拼接成新的 codeTableNumber，并将其设置到 addCodeTableDto 对象中
                newCodeTableNumber = Mzb + (formatId);
    //            addCodeTableDto.setCodeTableNumber(newCodeTableNumber);
            }
            //存入码表数据到CodeTable
            CodeTable codeTable = CodeTable.builder()
                    .codeTableNumber(newCodeTableNumber)
                    .codeTableName(addCodeTableDto.getCodeTableName())
                    .codeTableDesc(addCodeTableDto.getCodeTableDesc())
                    .build();
            //进行新增码表操作
            codeTableMapper.insert(codeTable);

            //新增码值
            //遍历 addCodeTableDto 中的 items 集合，每一个 AddCodeValueDto 对象执行相应的操作
            for(AddCodeValueDto addCodeValueDto: addCodeTableDto.getItems()){
                //将 codeTable .codeTableNumber ——> addCodeValueDto .codeTableNumber
                addCodeValueDto.setCodeTableNumber(codeTable.getCodeTableNumber());
                //调用addCodeValue方法新增码表
                codeValueService.addCodeValue(addCodeValueDto);

            //返回新增的数据
                log.info(username+"新增码表成功");
                return R.Success(codeTableMapper.getByCodeTableNumber(newCodeTableNumber));
            }
        } catch (Exception  e) {
            // 发生异常时回滚事务
            log.error(username+"新增码表失败",e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return R.Failed("新增码表失败");

    }

    /**
     * 码表编辑
     * @param updateCodeTableDto
     * @return
     */
    @Override
    public boolean updateCodeTable(UpdateCodeTableDto updateCodeTableDto, HttpServletRequest request) {
        String username=GetRequestMessage.getUsername(request);
        log.info(username+"正在处理码表编辑请求");
        CodeTable codeTable=codeTableMapper.getByCodeTableNumber(updateCodeTableDto.getCodeTableNumber());
        if (codeTable!=null&&codeTable.getCodeTableState()!=1){
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

    /**
     * 码表状态更改
     * @param stateCodeTableDto
     * @return
     */
    @Override
    public boolean stateCodeTable(StateCodeTableDto stateCodeTableDto, HttpServletRequest request) {
        log.info("正在处理更改码表状态请求");
        CodeTable codeTable=codeTableMapper.getByCodeTableNumber(stateCodeTableDto.getCodeTableNumber());
        if (codeTable!=null){
            CodeTable codeTable1=CodeTable.builder()
                    .codeTableState(stateCodeTableDto.getCodeTableState())
                    .build();

            UpdateWrapper<CodeTable> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("code_table_number",stateCodeTableDto.getCodeTableNumber());
            int count=codeTableMapper.update(codeTable1,updateWrapper);
            return count>0;
        }
        return false;
    }

//    /**
//     * 码表删除
//     * @param deleteCodeTableDto
//     * @return
//     *@Override
//     */
//    public boolean deleteCodeTable(DeleteCodeTableDto deleteCodeTableDto) {
//        log.info("正在处理码表删除请求");
//        CodeTable codeTable=codeTableMapper.getByCodeTableNumber(deleteCodeTableDto.getCodeTableNumber());
//        if (codeTable!=null){
//            //进行码值表删除
//            deleteCodeTableDto.setCodeTableNumber(codeTable.getCodeTableNumber());
//            codeValueController.deleteCodeValue(new DeleteCodeValueDto());
//
//            //进行码表删除
//            QueryWrapper<CodeTable> queryWrapper =new QueryWrapper<>();
//            queryWrapper
//                    .eq(deleteCodeTableDto.getCodeTableNumber()!=null,"code_table_number",deleteCodeTableDto.getCodeTableNumber());
//            int count=codeTableMapper.delete(queryWrapper);
//            return count>0;
//        }
//        return false;
//    }

    /**
     * 码表删除（逻辑删除）
     * @param deleteCodeTableDto
     * @return
     *@Override
     */
    public boolean deleteCodeTable(DeleteCodeTableDto deleteCodeTableDto, HttpServletRequest request) {
        CodeTable codeTable=codeTableMapper.getByCodeTableNumber(deleteCodeTableDto.getCodeTableNumber());
        if (codeTable!=null&&codeTable.getDeleteFlag()==0){
            //进行码值表删除
            DeleteCodeValueDto deleteCodeValueDto=new DeleteCodeValueDto();
            //将codeTableName赋值到deleteCodeValueDto
            deleteCodeValueDto.setCodeTableNumber(codeTable.getCodeTableNumber());
            codeValueController.deleteCodeValue(deleteCodeValueDto);

            log.info("正在处理码表删除请求");
            //进行码表删除
            //将codeTableName赋值到deleteCodeTableDto
            deleteCodeTableDto.setCodeTableNumber(codeTable.getCodeTableNumber());
            UpdateWrapper<CodeTable> updateWrapper =new UpdateWrapper<>();
            updateWrapper
                    .eq(deleteCodeTableDto.getCodeTableNumber()!=null,"code_table_number",deleteCodeTableDto.getCodeTableNumber());

            CodeTable codeTable1=CodeTable.builder()
                    .deleteFlag(1)
                    .build();
            int count=codeTableMapper.update(codeTable1,updateWrapper);
            return count>0;
        }
        return false;
    }

    /**
     * 码表批量发布
     * @param DeleteCodeTableDto
     * @return
     */
    @Override
    public boolean batchPublishCodeTable(List<DeleteCodeTableDto> DeleteCodeTableDto, HttpServletRequest request) {
        log.info("正在处理码表批量发布请求");

        try {
            List<CodeTable> codeTables = new ArrayList<>();
            //根据其编号从数据库中获取对应的 CodeTable 对象，并将其状态更新为新状态
            for (DeleteCodeTableDto deleteCodeTableDto : DeleteCodeTableDto) {
                //通过codeTableNumber查询数据
                CodeTable codeTable = codeTableMapper.getByCodeTableNumber(deleteCodeTableDto.getCodeTableNumber());
                //将 codeTableState 的值赋给 codeTable 并保存
                if (codeTable != null&&codeTable.getCodeTableState()==0) {
                    codeTable.setCodeTableState(1);
                    codeTables.add(codeTable);
                }
            }

            if (!codeTables.isEmpty()) {
                boolean count = this.updateBatchById(codeTables);
                return count ;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * 码表批量停用
     * @param deleteCodeTableDtos
     * @return
     */
    @Override
    public boolean batchStopCodeTable(List<DeleteCodeTableDto> deleteCodeTableDtos, HttpServletRequest request) {
        log.info("正在处理码表批量停用请求");

        try {
            List<CodeTable> codeTables = new ArrayList<>();
            //根据其编号从数据库中获取对应的 CodeTable 对象，并将其状态更新为新状态
            for (DeleteCodeTableDto deleteCodeTableDto : deleteCodeTableDtos) {
                //通过codeTableNumber查询数据
                CodeTable codeTable = codeTableMapper.getByCodeTableNumber(deleteCodeTableDto.getCodeTableNumber());
                //将codeTableState的值赋给codeTable并保存
                if (codeTable != null&&codeTable.getCodeTableState()==1) {
                    codeTable.setCodeTableState(2);
                    codeTables.add(codeTable);
                }
            }

            if (!codeTables.isEmpty()) {
                boolean count = this.updateBatchById(codeTables);
                return count ;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * 码表模板导入
     * @param newCodeTableExcel
     * @return
     */
    @Override
    public R saveCodeTableExcels(CodeTableExcel newCodeTableExcel) {
        log.info("正在处理码表导入请求");

        try {
            //判断码表名称是否重复
            //构建查询条件
            LambdaQueryWrapper<CodeTable> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            //输入查询条件
            lambdaQueryWrapper.eq(CodeTable::getCodeTableName,newCodeTableExcel.getCodeTableName());
            CodeTable codeTableName = getOne(lambdaQueryWrapper);
            //检查查询结果是否为空
            if(!ObjectUtils.isEmpty(codeTableName)){
                return R.BAD_REQUEST("该码表名称已存在");
            }

            //生成编号
            String Mzb="MZB";       //前缀
            String newCodeTableNumber;
            LambdaQueryWrapper<CodeTable> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            //查询符合条件的最后一条数据
            lambdaQueryWrapper1.orderByDesc(CodeTable::getCodeTableNumber).last("limit 1");
            CodeTable codeTable1 = getOne(lambdaQueryWrapper1);
            if (ObjectUtils.isEmpty(codeTable1)) {
                //如果数据库没有 0001
                newCodeTableNumber =Mzb + "00001";
            }else {
                //如果数据库中有数据 拿最后一条数据的序号
                //最后一条数据账号
                String lastCodeTableNumber = codeTable1.getCodeTableNumber();
                //截取序号部分，并将其转换为整数
                String idStr = lastCodeTableNumber.substring(3, lastCodeTableNumber.length());
                //将序号加一，并格式化为五位数的字符串
                Integer id = Integer.valueOf(idStr) + 1;
                String formatId = String.format("%05d", id);
                //拼接成新的 codeTableNumber，并将其设置到 addCodeTableDto 对象中
                newCodeTableNumber = Mzb + (formatId);
            }
            //存入码表数据到CodeTable
            CodeTable codeTable = CodeTable.builder()
                    .codeTableNumber(newCodeTableNumber)
                    .codeTableName(newCodeTableExcel.getCodeTableName())
                    .codeTableDesc(newCodeTableExcel.getCodeTableDesc())
                    .build();
            //进行新增码表操作
            codeTableMapper.insert(codeTable);

            //新增码值
            //遍历 newCodeTableExcel 中的 codeValueExcelLists 集合，每一个 codeValueExcel 对象执行相应的操作
            for(CodeValueExcel codeValueExcel: newCodeTableExcel.getCodeValueExcelLists()){
                //将 codeTable .codeTableNumber ——> codeValueExcel .codeTableNumber
                codeValueExcel.setCodeTableNumber(codeTable.getCodeTableNumber());
                //调用addCodeValue方法新增码表
                codeValueService.saveCodeValueExcel(codeValueExcel);

            }
            //返回新增的数据
            return R.Success(codeTableMapper.getByCodeTableNumber(newCodeTableNumber));
        } catch (Exception  e) {
            // 发生异常时回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return R.Failed("新增码表失败");

    }

}
