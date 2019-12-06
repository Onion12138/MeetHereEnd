package com.ecnu.meethere.site.manager;

import com.ecnu.meethere.common.annotation.Manager;
import com.ecnu.meethere.common.utils.CacheUtils;
import com.ecnu.meethere.paging.PageParam;
import com.ecnu.meethere.redis.codec.protobuf.LongListWrapper;
import com.ecnu.meethere.redis.core.RedisExpires;
import com.ecnu.meethere.redis.core.RedisUtils;
import com.ecnu.meethere.site.dao.SiteDao;
import com.ecnu.meethere.site.dto.SiteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Manager
public class SiteManager {
    @Autowired
    private SiteDao siteDao;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    @Qualifier("siteRedisExpires")
    private RedisExpires siteRedisExpires;

    @Autowired
    @Qualifier("sitePageRedisExpires")
    private RedisExpires sitePageRedisExpires;

    public List<SiteDTO> listSites(PageParam pageParam) {
        String redisKey = getSitePageRedisKey(pageParam);
        List<Long> cache = Optional.ofNullable(redisUtils.opsForValue().get(redisKey,
                LongListWrapper.class)).orElse(new LongListWrapper()).getList();
        return listSitesByIds(
                CacheUtils.handleCache(
                        cache,
                        pageParam,
                        siteDao::listSiteIds,
                        Function.identity(),
                        (pp, ids) -> redisUtils.opsForValue().set(redisKey,
                                new LongListWrapper(ids),
                                sitePageRedisExpires)
                )
        );
    }

    private List<SiteDTO> listSitesByIds(List<Long> siteIds) {
        List<String> redisKeys =
                siteIds.stream().map(this::getSiteRedisKey).collect(Collectors.toList());
        List<SiteDTO> cache = redisUtils.opsForValue().multiGet(redisKeys, SiteDTO.class);
        return CacheUtils.handleBatchCache(
                cache,
                siteIds,
                siteDao::listSites,
                Function.identity(),
                (missedCache, missData) ->
                {
                    List<String> missCacheRedisKeys =
                            missedCache.stream().map(s -> getSiteRedisKey(s.getId())).collect(Collectors.toList());
                    redisUtils.opsForValue().multiSet(
                            missCacheRedisKeys,
                            (missedCache),
                            siteRedisExpires
                    );
                });
    }

    public SiteDTO getSite(Long id) {
        String redisKey = getSiteRedisKey(id);
        SiteDTO cache = redisUtils.opsForValue().get(redisKey, SiteDTO.class);
        return CacheUtils.handleCache(
                cache,
                id,
                siteDao::getSite,
                Function.identity(),
                (i, s) -> redisUtils.opsForValue().set(redisKey, s, siteRedisExpires)
        );
    }

    private String getSiteRedisKey(Long siteId) {
        return String.format("site:%d", siteId);
    }

    private String getSitePageRedisKey(PageParam pageParam) {
        return String.format("site:p:%d", pageParam.getPageNum());
    }
}
