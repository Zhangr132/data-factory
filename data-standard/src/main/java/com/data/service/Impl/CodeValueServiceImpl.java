package com.data.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.data.dto.CodeValue.AddCodeValueDto;
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

    @Override
    public R addCodeValue(AddCodeValueDto addCodeValueDto) {
        logger.info("正在处理新增码值请求");
        //判断码值取值是否重复
        LambdaQueryWrapper<CodeValue> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //输入查询条件
        lambdaQueryWrapper.eq(CodeValue::getCodeValueValue,addCodeValueDto.getCodeValueValue());
        CodeValue codeValueValue = getOne(lambdaQueryWrapper);
        //检查查询结果是否为空
        if(!ObjectUtils.isEmpty(codeValueValue)){
            logger.info("该码值取值已存在");
            return R.BAD_REQUEST( "该码值取值已存在");
        }

        //判断码值名称是否重复
        LambdaQueryWrapper<CodeValue> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        //输入查询条件
        lambdaQueryWrapper1.eq(CodeValue::getCodeValueName,addCodeValueDto.getCodeValueName());
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
}
