package com.ecnu.meethere.redis.core;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Redis 过期时间通用类，加上随机值避免缓存雪崩
 */
public class RedisExpires {
    private Long expires;

    private TimeUnit timeUnit = TimeUnit.MILLISECONDS;

    private Long randomRange;

    public long getExpires() {
        return expires + (randomRange > 0L ?
                ThreadLocalRandom.current().nextLong(randomRange) * 2 - randomRange
                : randomRange);
    }

    public void init() {
        assert timeUnit != null;
        resetExpiresAndRandomRange(expires, randomRange);
    }

    public void resetExpiresAndRandomRange(Long expires, Long randomRange) {
        assert expires != null;
        assert randomRange != null;
        this.expires = timeUnit.toMillis(expires);
        this.randomRange = timeUnit.toMillis(randomRange);
    }

    public void setExpires(Long expires) {
        this.expires = expires;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public void setRandomRange(Long randomRange) {
        this.randomRange = randomRange;
    }

    @Override
    public String toString() {
        return "RedisExpires{" +
                "expires=" + expires +
                ", timeUnit=" + timeUnit +
                ", randomRange=" + randomRange +
                '}';
    }
}