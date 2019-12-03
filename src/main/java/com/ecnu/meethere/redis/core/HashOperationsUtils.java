package com.ecnu.meethere.redis.core;

import org.joor.Reflect;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

public class HashOperationsUtils {
    private HashOperations<String, String, byte[]> hashOperations;

    private RedisUtils redisUtils;

    public HashOperationsUtils(RedisUtils redisUtils) {
        this.hashOperations = redisUtils.getRedisTemplate().opsForHash();
        this.redisUtils = redisUtils;
    }

    public void putObject(String key, Object o, RedisExpires redisExpires) {
        if (StringUtils.isEmpty(key) || o == null)
            throw new NullPointerException();
        Map<byte[], byte[]> hashes = redisUtils.getRedisCodec().encodeFieldsWithByteKeys(o);

        redisUtils.executePipelined(connection -> {
            byte[] keyBytes = StringRedisSerializer.UTF_8.serialize(key);
            connection.hMSet(keyBytes, hashes);
            connection.pExpire(keyBytes, redisExpires.getExpires());
            return null;
        });
    }

    public <T> void multiPutObject(List<String> keys, List<T> values,
                                   RedisExpires redisExpires) {
        if (CollectionUtils.isEmpty(keys))
            return;
        if (redisExpires == null)
            throw new NullPointerException();
        if (keys.size() != values.size())
            throw new IllegalArgumentException();
        redisUtils.executePipelined(connection -> {
            com.ecnu.meethere.common.utils.CollectionUtils.consume(
                    keys, values,
                    (k, v) -> {
                        byte[] kBytes = k.getBytes(Charset.defaultCharset());
                        connection.hMSet(kBytes,
                                redisUtils.getRedisCodec().encodeFieldsWithByteKeys(v));
                        connection.pExpire(kBytes, redisExpires.getExpires());
                    }
            );
            return null;
        });
    }

    public <T> T getObject(String key, Class<T> clazz) {
        if (StringUtils.isEmpty(key) || clazz == null)
            throw new NullPointerException();

        Map<String, byte[]> m = hashOperations.entries(key);
        return redisUtils.getRedisCodec().decodeFields(m, clazz);
    }

    public <T> List<T> multiGetObject(List<String> keys, Class<T> clazz) {
        if (org.springframework.util.CollectionUtils.isEmpty(keys))
            return new ArrayList<>();
        if (clazz == null)
            throw new NullPointerException();

        List<String> hashKeys =
                Arrays.stream(clazz.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());

        byte[][] hashKeyBytes =
                hashKeys.stream().map(k -> k.getBytes(Charset.defaultCharset())).toArray(byte[][]::new);

        List<Object> objects = redisUtils.executePipelined(connection -> {
            keys.forEach(key -> connection.hMGet(key.getBytes(Charset.defaultCharset()),
                    hashKeyBytes));
            return null;
        });

        return objects.stream()
                .map(hashValueBytes -> {
                    if (hashValueBytes == null || ((List<byte[]>) hashValueBytes).get(0) == null)
                        return null;
                    return com.ecnu.meethere.common.utils.CollectionUtils
                            .transTwoCollectionToMap(hashKeys, ((List<byte[]>) hashValueBytes));
                })
                .map(m -> redisUtils.getRedisCodec().decodeFields(m, clazz))
                .collect(Collectors.toList());
    }

}
