package com.ecnu.meethere.redis.core;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.stream.Collectors;

public class ZSetOperationsUtils {
    private ZSetOperations<String, byte[]> zSetOperations;

    private RedisUtils redisUtils;


    public ZSetOperationsUtils(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
        this.zSetOperations = redisUtils.getRedisTemplate().opsForZSet();
    }

/*    public void add(String key, Object value, double score) {
        zSetOperations.add(key, redisUtils.getRedisCodec().encode(value), score);
    }

    public <T> List<T> range(String key, long start, long end, Class<T> clazz) {
        return zSetOperations.range(key, start, end).stream()
                .map(bytes -> redisUtils.getRedisCodec().decode(bytes, clazz))
                .collect(Collectors.toList());
    }

    public void remove(String key, Object value) {
        zSetOperations.remove(key, redisUtils.getRedisCodec().encode(value));
    }*/


}
