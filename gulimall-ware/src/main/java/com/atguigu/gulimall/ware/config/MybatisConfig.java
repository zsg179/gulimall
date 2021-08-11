package com.atguigu.gulimall.ware.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Auther :朱树广
 * @Date :2021/3/5
 * @Description :com.atguigu.gulimall.ware.config
 * @Version :1.0
 */
@EnableTransactionManagement
@Configuration
@MapperScan("com.atguigu.gulimall.ware.dao")
public class MybatisConfig {

    //引入分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        //设置请求页大于最后页后操作，true返回首页，false继续请求，默认false
        paginationInterceptor.setOverflow(true);
        //设置最大单页限制数量，默认500，-1不受限制
        paginationInterceptor.setLimit(1000);
        return paginationInterceptor;
    }
}
