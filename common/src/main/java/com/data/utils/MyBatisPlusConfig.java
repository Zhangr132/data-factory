package com.data.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author zhangr132
 * @Date 2024/3/26 0:43
 * @注释
 */
@Configuration
public class MyBatisPlusConfig {
    /**
     * 分页插件配置
     *
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 向MyBatis-Plus的过滤器链中添加分页拦截器，需要设置数据库类型（主要用于分页方言）
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

//    /**
//     * 自定义配置
//     * 名称与配置文件一致 优先级高于配置文件（这种方式会覆盖配置文件中的配置）
//     */
//    @Bean
//    public MybatisPlusJoinPropertiesConsumer mybatisPlusJoinPropertiesConsumer() {
//        return prop -> prop
//                .setBanner(true)
//                .setTableAlias("t");
//    }
}
