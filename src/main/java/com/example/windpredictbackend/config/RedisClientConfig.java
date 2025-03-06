package com.example.windpredictbackend.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置 Redisson
 * @author Michael
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedisClientConfig {

    private String host;
    private String port;
    private int database;
    @Bean
    public RedissonClient redissonClient(){
        // 创建配置
        Config config=new Config();
        String redissonAddress=String.format("redis://%s:%s",host,port);
        config.useSingleServer().setAddress(redissonAddress).setDatabase(database);
        // 创建 redisson 实例
        return Redisson.create(config);
    }
}
