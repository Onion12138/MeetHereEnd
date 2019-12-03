package com.ecnu.meethere.redis.config;

import com.ecnu.meethere.redis.core.RedisExpires;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisExpiresConfig {
    @Bean(initMethod = "init")
    @ConfigurationProperties("redis.expires.news")
    public RedisExpires newsRedisExpires() {
        return new RedisExpires();
    }
}
