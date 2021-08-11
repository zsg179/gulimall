package com.atguigu.gulimall.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther :朱树广
 * @Date :2021/3/22
 * @Description :com.atguigu.gulimall.product.config
 * @Version :1.0
 */
@Configuration
public class MyRedissonConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://8.135.40.127:6379");
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }
}
