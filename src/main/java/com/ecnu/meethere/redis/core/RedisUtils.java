package com.ecnu.meethere.redis.core;

import com.ecnu.meethere.redis.codec.RedisCodec;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class RedisUtils implements InitializingBean {
    private RedisTemplate<String, byte[]> redisTemplate;

    private RedisCodec redisCodec;

    private ValueOperationsUtils valueOperationsUtils;

    private HashOperationsUtils hashOperationsUtils;

    public ValueOperationsUtils opsForValue() {
        return valueOperationsUtils;
    }

    public HashOperationsUtils opsForHash() {
        return hashOperationsUtils;
    }

    public void expire(String key, RedisExpires redisExpires) {
        redisTemplate.expire(key, redisExpires.getExpires(), TimeUnit.MILLISECONDS);
    }

    public void multi() {
        redisTemplate.multi();
    }

    public List<Object> exec() {
        return redisTemplate.exec();
    }

    public List<Object> executePipelined(RedisCallback redisCallback) {
        return redisTemplate.executePipelined(redisCallback);
    }

    public RedisUtils(RedisTemplate<String, byte[]> redisTemplate, RedisCodec redisCodec) {
        this.redisTemplate = redisTemplate;
        this.redisCodec = redisCodec;
    }

    public RedisTemplate<String, byte[]> getRedisTemplate() {
        return redisTemplate;
    }

    public RedisCodec getRedisCodec() {
        return redisCodec;
    }

    @Override
    public void afterPropertiesSet() {
        this.valueOperationsUtils = new ValueOperationsUtils(this);
        this.hashOperationsUtils = new HashOperationsUtils(this);
    }
}