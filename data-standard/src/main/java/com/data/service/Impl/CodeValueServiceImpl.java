package com.data.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.data.dto.CodeValue.AddCodeValueDto;
import com.data.dto.CodeValue.DeleteCodeValueDto;
import com.data.entity.CodeValue;
import com.data.mapper.CodeValueMapper;
import com.data.service.CodeValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 码值表 服务实现类
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-15
 */
@Service
public class CodeValueServiceImpl extends ServiceImpl<CodeValueMapper, CodeValue> implements CodeValueService {
    private Logger logger= LoggerFactory.getLogger(getClass());
    @Autowired
    private CodeValueMapper codeValueMapper;

    /**
     * 码值新增
     * @param addCodeValueDto
     * @return
     */
    @Override
    public R addCodeValue(AddCodeValueDto addCodeValueDto) {
        logger.info("正在处理新增码值请求");
        //判断码值取值是否重复
        LambdaQueryWrapper<CodeValue> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //输入查询条件
        lambdaQueryWrapper
                .eq(CodeValue::getCodeTableNumber,addCodeValueDto.getCodeTableNumber())
                .eq(CodeValue::getCodeValueValue,addCodeValueDto.getCodeValueValue());
        CodeValue codeValueValue = getOne(lambdaQueryWrapper);
        //检查查询结果是否为空
        if(!ObjectUtils.isEmpty(codeValueValue)){
            logger.info("该码值取值已存在");
            return R.BAD_REQUEST( "该码值取值已存在");
        }

        //判断码值名称是否重复
        LambdaQueryWrapper<CodeValue> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        //输入查询条件
        lambdaQueryWrapper1
                .eq(CodeValue::getCodeTableNumber,addCodeValueDto.getCodeTableNumber())
                .eq(CodeValue::getCodeValueName,addCodeValueDto.getCodeValueName());
        CodeValue codeValueName = getOne(lambdaQueryWrapper1);
        //检查查询结果是否为空
        if(!ObjectUtils.isEmpty(codeValueName)){
            return R.BAD_REQUEST("该码值名称已存在");
        }

        //保存不重复的数据
        CodeValue codeValue = new CodeValue();
        //将 addCodeValueDto 对象中的属性复制到 codeValue 对象中
        BeanUtils.copyProperties(addCodeValueDto,codeValue);
        //将 codeValue 对象插入到数据库中
        this.baseMapper.insert(codeValue);
        return R.Success(codeValue);
    }

//    /**
//     * 码值删除
//     * @param deleteCodeValueDto
//     * @return
//     */
//    @Override
//    public boolean deleteCodeValue(DeleteCodeValueDto deleteCodeValueDto) {
//        logger.info("正在处理码值删除请求");
//        LambdaQueryWrapper<CodeValue> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        //输入查询条件
//        lambdaQueryWrapper
//                .eq(CodeValue::getCodeTableNumber,deleteCodeValueDto.getCodeTableNumber());
//        //查询条件可能会返回多条记录
//        List<CodeValue> codeValueList = list(lambdaQueryWrapper);
//        if(codeValueList!=null){
//            //删除数据
//            int success = codeValueMapper.delete(lambdaQueryWrapper);
//            return success>0;
//        }
//        return false;
//    }

    /**
     * 码值删除（逻辑删除）
     * @param deleteCodeValueDto
     * @return
     */
    @Override
    public boolean deleteCodeValue(DeleteCodeValueDto deleteCodeValueDto) {
        logger.info("正在处理码值删除请求");
        QueryWrapper<CodeValue> queryWrapper = new QueryWrapper<>();
        //输入查询条件
        queryWrapper
                .eq("code_table_number",deleteCodeValueDto.getCodeTableNumber());
        //查询条件可能会返回多条记录
        List<CodeValue> codeValueList = list(queryWrapper);
        if (codeValueList!=null){
            CodeValue codeValue=CodeValue.builder()
                    .deleteFlag(1)
                    .build();
            int count=codeValueMapper.update(codeValue,queryWrapper);
            return count>0;
        }
        return false;
    }
}
