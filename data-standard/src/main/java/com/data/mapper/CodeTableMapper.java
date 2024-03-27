package com.data.mapper;

import com.data.entity.CodeTable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.data.entity.CodeValue;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 码表管理 Mapper 接口
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-15
 */
@Mapper
public interface CodeTableMapper extends MPJBaseMapper<CodeTable> {

    //通过CodeTableNumber获取数据
    @Select("select * from code_table where code_table_number = #{codeTableNumber}")
    CodeTable getByCodeTableNumber(String codeTableNumber);

}
