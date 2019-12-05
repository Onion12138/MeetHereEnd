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

    @Bean(initMethod = "init")
    @ConfigurationProperties("redis.expires.news-comment")
    public RedisExpires newsCommentRedisExpires() {
        return new RedisExpires();
    }

    @Bean(initMethod = "init")
    @ConfigurationProperties("redis.expires.news-page")
    public RedisExpires newsPageRedisExpires() {
        return new RedisExpires();
    }

    @Bean(initMethod = "init")
    @ConfigurationProperties("redis.expires.news-comment-page")
    public RedisExpires newsCommentPageRedisExpires() {
        return new RedisExpires();
    }

    @Bean(initMethod = "init")
    @ConfigurationProperties("redis.expires.user-vo")
    public RedisExpires userVORedisExpires() {
        return new RedisExpires();
    }
}
