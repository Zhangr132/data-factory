package com.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.data.entity.CategoryInfo;
import com.data.entity.UserLog;
import com.data.vo.CategoryInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 用户日志  Mapper 接口
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-29
 */
@Mapper
public interface UserLogMapper extends BaseMapper<UserLog> {

}
