package com.ecnu.meethere.news.manager;

import com.ecnu.meethere.common.annotation.Manager;
import com.ecnu.meethere.common.utils.CacheUtils;
import com.ecnu.meethere.news.dao.NewsDao;
import com.ecnu.meethere.news.dto.NewsDTO;
import com.ecnu.meethere.news.dto.NewsDigestDTO;
import com.ecnu.meethere.paging.PageParam;
import com.ecnu.meethere.redis.codec.ProtoStuffCodec;
import com.ecnu.meethere.redis.core.RedisExpires;
import com.ecnu.meethere.redis.core.RedisUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * News的缓存层
 */
@Manager
public class NewsManager {
    @Autowired
    private NewsDao newsDao;

    @Autowired
    @Qualifier("newsRedisExpires")
    private RedisExpires newsRedisExpires;

    @Autowired
    @Qualifier("newsPageRedisExpires")
    private RedisExpires newsPageRedisExpires;

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
                (i, c) -> redisUtils.opsForHash().putObject(newsRedisKey, c, newsRedisExpires)
        );
    }

    private static class LongListWrapper extends ProtoStuffCodec.ListWrapper<Long> {
    }

    public List<NewsDigestDTO> listNewsDigests(PageParam pageParam) {
        String redisKey = getNewsPageRedisKey(pageParam);
        List<Long> cache = Optional.ofNullable(redisUtils.opsForValue().get(redisKey,
                LongListWrapper.class)).orElse(new LongListWrapper()).getList();

        //越后面的页数过期时间越长
        newsPageRedisExpires.resetExpiresAndRandomRange( newsPageRedisExpires.getExpires() * pageParam.getPageNum(), 0L);

        return listNewsDigestsByNewsIds(CacheUtils.handleCache(
                cache,
                pageParam,
                newsDao::listNewsDigestsIds,
                Function.identity(),
                (pp, c) -> redisUtils.opsForValue().set(redisKey, c, newsPageRedisExpires)
        ));
    }

    List<NewsDigestDTO> listNewsDigestsByNewsIds(List<Long> newsIds) {
        List<String> redisKeys =
                newsIds.stream().map(this::getNewsRedisKey).collect(Collectors.toList());
        List<NewsDigestDTO> cache = redisUtils.opsForHash().multiGetObject(redisKeys,
                NewsDigestDTO.class);
        return CacheUtils.handleBatchCache(
                cache,
                newsIds,
                newsDao::listNews,
                newsDTOS -> newsDTOS.stream().map(this::convertTo).collect(Collectors.toList()),
                (missCache, missData) -> {
                    List<String> missNewsRedisKeys =
                            missData.stream().map(d -> this.getNewsRedisKey(d.getId())).collect(Collectors.toList());
                    redisUtils.opsForHash().multiPutObject(missNewsRedisKeys, missData,
                            newsRedisExpires);
                }
        );
    }

    private NewsDigestDTO convertTo(NewsDTO newsDTO) {
        NewsDigestDTO newsDigestDTO = new NewsDigestDTO();
        BeanUtils.copyProperties(newsDTO, newsDigestDTO);
        return newsDigestDTO;
    }

    private String getNewsPageRedisKey(PageParam pageParam) {
        return String.format("news:page:%d", pageParam.getPageNum());
    }

    private String getNewsRedisKey(Long id) {
        return String.format("news:%d", id);
    }
}
