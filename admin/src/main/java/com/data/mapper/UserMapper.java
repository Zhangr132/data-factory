package com.data.mapper;

import com.data.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author zhangr132
 * @since 2024-03-14
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    //通过userName获取数据
    @Select("select * from user where username = #{username}")
    User selectByUserName(@Param("username") String username);
}
