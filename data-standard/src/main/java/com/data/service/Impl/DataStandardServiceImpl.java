package com.data.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.data.dto.DataStandard.DataStandardPageDto;
import com.data.entity.CodeTable;
import com.data.entity.DataStandard;
import com.data.mapper.DataStandardMapper;
import com.data.service.DataStandardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                        "data_standard_value_min","data_standard_enumeration_range","data_standard_state","data_standard_is_blank","delete_flag",
                        "create_time","update_time")
                .like(dataStandardPageDto.getDataStandardCode()!=null,"data_standard_code",dataStandardPageDto.getDataStandardCode())
                .like(dataStandardPageDto.getDataStandardCnName()!=null,"data_standard_cn_name",dataStandardPageDto.getDataStandardCnName())
                .like(dataStandardPageDto.getDataStandardEnName()!=null,"data_standard_en_name",dataStandardPageDto.getDataStandardEnName())
                .eq(dataStandardPageDto.getDataStandardSourceOrganization()!=null,"data_standard_source_organization",dataStandardPageDto.getDataStandardSourceOrganization())
                .eq(dataStandardPageDto.getDataStandardState()!=null,"data_standard_state",dataStandardPageDto.getDataStandardState());

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
}
