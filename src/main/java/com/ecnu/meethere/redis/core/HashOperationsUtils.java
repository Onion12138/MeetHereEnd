package com.ecnu.meethere.redis.core;

import com.ecnu.meethere.common.utils.CollectionUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class HashOperationsUtils {
    private HashOperations<String, String, byte[]> hashOperations;

    private RedisUtils redisUtils;

    public HashOperationsUtils(RedisUtils redisUtils) {
        this.hashOperations = redisUtils.getRedisTemplate().opsForHash();
        this.redisUtils = redisUtils;
    }

    public void putObject(String key, Object o) {
        if (StringUtils.isEmpty(key) || o == null)
            throw new NullPointerException();
        Map<String, byte[]> m = redisUtils.getRedisCodec().encodeFields(o);
        hashOperations.putAll(key, m);
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

    public void put(String key, String hashKey, Object o) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(hashKey) || o == null)
            throw new NullPointerException();
        hashOperations.put(key, hashKey, redisUtils.getRedisCodec().encode(o));
    }

    public <T> T getObject(String key, Class<T> clazz) {
        if (StringUtils.isEmpty(key) || clazz == null)
            throw new NullPointerException();
        Map<String, byte[]> m = hashOperations.entries(key);
        return redisUtils.getRedisCodec().decodeFields(m, clazz);
    }

    public <T> T getObject(String key, Class<T> clazz, List<String> hashKeys) {
        if (StringUtils.isEmpty(key) || clazz == null)
            throw new NullPointerException();
        List<byte[]> values = hashOperations.multiGet(key, hashKeys);
        return redisUtils.getRedisCodec().decodeFields(
                CollectionUtils.transTwoCollectionToMap(hashKeys, values)
                , clazz);
    }

    public <T> T get(String key, String hashKey, Class<T> clazz) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(hashKey) || clazz == null)
            throw new NullPointerException();
        return redisUtils.getRedisCodec().decode(hashOperations.get(key, hashKey), clazz);
    }

/*
    public <T> T getSimilarShapeObject(String key, Class<T> clazz) {
        if (StringUtils.isEmpty(key) || clazz == null)
            throw new NullPointerException();
        Set<String> fieldNameSet = Reflect.onClass(clazz).fields().keySet();
        List<byte[]> fieldValBytesList = hashOperations.multiGet(key, fieldNameSet);
        return redisUtils.getRedisCodec().decodeFields(convertToMap(fieldNameSet,
        fieldValBytesList), clazz);
    }*/
}
