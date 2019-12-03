package com.ecnu.meethere.redis.config;

import com.ecnu.meethere.redis.codec.ProtoStuffCodec;
import com.ecnu.meethere.redis.codec.RedisCodec;
import com.ecnu.meethere.redis.core.RedisUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisUtilsConfig {
    @Bean
    public RedisTemplate<String, byte[]> redisUtilsTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setEnableDefaultSerializer(false);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(byteRedisSerializer());
        redisTemplate.setHashValueSerializer(byteRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public RedisSerializer<byte[]> byteRedisSerializer() {
        return new RedisSerializer<byte[]>() {
            @Override
            public byte[] serialize(byte[] bytes) throws SerializationException {
                return bytes;
            }

            @Override
            public byte[] deserialize(byte[] bytes) throws SerializationException {
                return bytes;
            }
        };
    }

    @Bean
    public RedisCodec redisCodec() {
        return new ProtoStuffCodec();
    }

    @Bean
    public RedisUtils redisUtils(RedisTemplate<String, byte[]> redisTemplate) {
        return new RedisUtils(redisTemplate, redisCodec());
    }
}
