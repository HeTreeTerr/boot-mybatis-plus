package com.hss.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@MapperScan("com.hss.mapper")
public class MyBatisPlusConfig {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        logger.debug("注册分页插件");
        return new PaginationInterceptor();
    }

    @Bean
    //@Profile({"test"})// 设置 dev test 环境开启
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }

    /**
     * 逻辑删除用，3.1.1之后的版本可不需要配置该bean，
     * 但项目这里用的是3.1.0的
     * @return
     */
    @Bean
    public ISqlInjector sqlInjector() {
        return new LogicSqlInjector();
    }
}
