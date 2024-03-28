package com.data.mapper;

import com.data.entity.CodeValue;
import com.data.entity.DataStandard;
import com.data.vo.DataStandardVo;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 数据标准表 Mapper 接口
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-15
 */
@Mapper
public interface DataStandardMapper extends MPJBaseMapper<DataStandard> {
    //通过code_table_number查询码值信息
    @Select("select  * from code_value where code_table_number = #{codeTableNumber}")
    List<CodeValue> selectCodeValueByCodeTableNumber(String codeTableNumber);

    //通过data_standard_code查询数据标准信息
    @Select("select * from data_standard where data_standard_code=#{dataStandardCode}")
    DataStandard selectByDataStandardCode(String dataStandardCode);
}
