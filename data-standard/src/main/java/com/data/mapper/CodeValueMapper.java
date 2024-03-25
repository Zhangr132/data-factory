package com.data.mapper;

import com.data.dto.CodeValue.excel.ExportCodeValueExcel;
import com.data.entity.CodeValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 码值表 Mapper 接口
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-15
 */
@Mapper
public interface CodeValueMapper extends BaseMapper<CodeValue> {
    @Select("select  * from code_value where code_table_number = #{codeTableNumber}")
    List<ExportCodeValueExcel> selectByCodeTableNumber(String codeTableNumber);

    /*//通过codeValueName获取数据
    @Select("select * from code_value where code_value_name = #{codeValueName}")
    CodeValue getByCodeValueName(String codeValueName);

    //查找code_value_name
    @Select("SELECT * FROM code_value")
    List<CodeValue> getExistingNames();

    @Select("SELECT code_value_name FROM code_value WHERE code_value_name IN (#{codeValueName})")
    List<CodeValue> selectNamesByList(@Param("codeValueName") List<CodeValue> codeValueName);*/
}
