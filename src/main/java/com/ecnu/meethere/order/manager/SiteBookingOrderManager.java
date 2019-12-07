package com.ecnu.meethere.order.manager;

import com.ecnu.meethere.common.annotation.Manager;
import com.ecnu.meethere.common.utils.CacheUtils;
import com.ecnu.meethere.common.utils.Triple;
import com.ecnu.meethere.order.dao.SiteBookingOrderDao;
import com.ecnu.meethere.order.dto.SiteBookingOrderDTO;
import com.ecnu.meethere.paging.PageParam;
import com.ecnu.meethere.redis.codec.protobuf.LongListWrapper;
import com.ecnu.meethere.redis.core.RedisExpires;
import com.ecnu.meethere.redis.core.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Manager
public class SiteBookingOrderManager {
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private SiteBookingOrderDao siteBookingOrderDao;

    @Autowired
    @Qualifier("userOrderPageRedisExpires")
    private RedisExpires userOrderPageRedisExpires;

    @Autowired
    @Qualifier("siteOrderPageRedisExpires")
    private RedisExpires siteOrderPageRedisExpires;

    @Autowired
    @Qualifier("orderRedisExpires")
    private RedisExpires orderRedisExpires;

    public List<SiteBookingOrderDTO> listOrdersByUser(Long userId, int status,
                                                      PageParam pageParam) {
        String redisKey = getUserOrderPageRedisKey(userId, status, pageParam);
        List<Long> cache = Optional.ofNullable(redisUtils.opsForValue().get(redisKey,
                LongListWrapper.class)).orElse(new LongListWrapper()).getList();
        return listOrders(
                CacheUtils.handleCache(
                        cache,
                        new Triple<>(userId, status, pageParam),
                        t -> siteBookingOrderDao.listIdsByUser(t.getFirst(), t.getSecond()
                                , t.getThrid()),
                        Function.identity(),
                        (t, c) -> redisUtils.opsForValue().set(redisKey, new LongListWrapper(c),
                                userOrderPageRedisExpires)
                )
        );
    }

    public List<SiteBookingOrderDTO> listOrdersBySite(Long siteId, int status,
                                                      PageParam pageParam) {
        String redisKey = getSiteOrderPageRedisKey(siteId, status, pageParam);
        List<Long> cache = Optional.ofNullable(redisUtils.opsForValue().get(redisKey,
                LongListWrapper.class)).orElse(new LongListWrapper()).getList();
        return listOrders(
                CacheUtils.handleCache(
                        cache,
                        new Triple<>(siteId, status, pageParam),
                        t -> siteBookingOrderDao.listIdsBySite(t.getFirst(), t.getSecond()
                                , t.getThrid()),
                        Function.identity(),
                        (t, c) -> redisUtils.opsForValue().set(redisKey, new LongListWrapper(c),
                                siteOrderPageRedisExpires)
                )
        );
    }

    List<SiteBookingOrderDTO> listOrders(List<Long> orderIds) {
        List<String> redisKeys =
                orderIds.stream().map(this::getOrderRedisKey).collect(Collectors.toList());
        List<SiteBookingOrderDTO> cache =
                redisUtils.opsForValue().multiGet(redisKeys, SiteBookingOrderDTO.class);
        return CacheUtils.handleBatchCache(
                cache,
                orderIds,
                siteBookingOrderDao::list,
                Function.identity(),
                (missedCache, missedData) -> {
                    List<String> missedCacheRedisKeys =
                            missedCache.stream().map(c -> getOrderRedisKey(c.getId())).collect(Collectors.toList());
                    redisUtils.opsForValue().multiSet(missedCacheRedisKeys, missedCache,
                            orderRedisExpires);
                }
        );
    }

    public SiteBookingOrderDTO getOrder(Long orderId) {
        String redisKey = getOrderRedisKey(orderId);
        SiteBookingOrderDTO cache = redisUtils.opsForValue().get(redisKey,
                SiteBookingOrderDTO.class);
        return CacheUtils.handleCache(
                cache,
                orderId,
                siteBookingOrderDao::get,
                Function.identity(),
                (i, c) -> redisUtils.opsForValue().set(redisKey, c,
                        orderRedisExpires)
        );
    }

    public void diableOrderCache(Long orderId) {
        String redisKey = getOrderRedisKey(orderId);
        redisUtils.delete(redisKey);
    }

    private String getUserOrderPageRedisKey(Long userId, int status,
                                            PageParam pageParam) {
        return String.format("user:%d:status:%d:p:%d", userId, status, pageParam.getPageNum());
    }

    private String getSiteOrderPageRedisKey(Long userId, int status,
                                            PageParam pageParam) {
        return String.format("site:%d:status:%d:p:%d", userId, status, pageParam.getPageNum());
    }

    private String getOrderRedisKey(Long orderId) {
        return String.format("order:%d", orderId);
    }
}
