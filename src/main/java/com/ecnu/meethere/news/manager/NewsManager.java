package com.ecnu.meethere.news.manager;

import com.ecnu.meethere.common.utils.CacheUtils;
import com.ecnu.meethere.news.dao.NewsDao;
import com.ecnu.meethere.news.dto.NewsDTO;
import com.ecnu.meethere.redis.core.RedisExpires;
import com.ecnu.meethere.redis.core.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * News的缓存层
 */
@Component
public class NewsManager {
    @Autowired
    private NewsDao newsDao;

    @Autowired
    @Qualifier("newsRedisExpires")
    private RedisExpires redisExpires;

    @Autowired
    private RedisUtils redisUtils;

    public NewsDTO getNews(Long id) {
        String newsRedisKey = getNewsRedisKey(id);
        NewsDTO cache = redisUtils.opsForHash().getObject(newsRedisKey, NewsDTO.class);
        return CacheUtils.handleCache(
                cache,
                id,
                newsDao::getNews,
                Function.identity(),
                (i, c) -> redisUtils.opsForHash().putObject(newsRedisKey, c, redisExpires)
        );
    }

    private String getNewsRedisKey(Long id) {
        return String.format("news:%d", id);
    }
}
