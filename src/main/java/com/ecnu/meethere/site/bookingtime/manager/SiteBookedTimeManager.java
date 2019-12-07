package com.ecnu.meethere.site.bookingtime.manager;

import com.ecnu.meethere.common.annotation.Manager;
import com.ecnu.meethere.common.utils.CacheUtils;
import com.ecnu.meethere.common.utils.Tuple;
import com.ecnu.meethere.redis.codec.protobuf.ListWrapper;
import com.ecnu.meethere.redis.core.RedisExpires;
import com.ecnu.meethere.redis.core.RedisUtils;
import com.ecnu.meethere.site.bookingtime.dao.SiteBookedTimeDao;
import com.ecnu.meethere.site.bookingtime.dto.SiteBookedTimeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Manager
public class SiteBookedTimeManager {
    @Autowired
    private SiteBookedTimeDao siteBookedTimeDao;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    @Qualifier("siteBookedTimeByDayRedisExpires")
    private RedisExpires siteBookedTimeByDayRedisExpires;

    private static class SiteBookedTimeDTOListWrapper extends ListWrapper<SiteBookedTimeDTO> {
        public SiteBookedTimeDTOListWrapper() {
        }

        public SiteBookedTimeDTOListWrapper(List<SiteBookedTimeDTO> list) {
            super(list);
        }
    }

    public List<SiteBookedTimeDTO> listSiteBookedTimeByDay(Long siteId, LocalDate date) {
        String redisKey = getSiteBookedTimeByDayRedisKey(siteId, date);
        List<SiteBookedTimeDTO> cache = Optional.ofNullable(redisUtils.opsForValue().get(redisKey,
                SiteBookedTimeDTOListWrapper.class)).orElse(new SiteBookedTimeDTOListWrapper()).getList();
        return CacheUtils.handleCache(
                cache,
                new Tuple<>(siteId, date),
                t -> siteBookedTimeDao.listByStartTime(t.getFirst(),
                        t.getSecond().atTime(LocalTime.MIN),
                        t.getSecond().atTime(LocalTime.MAX)),
                Function.identity(),
                (t, c) -> redisUtils.opsForValue().set(redisKey,
                        new SiteBookedTimeDTOListWrapper(c), siteBookedTimeByDayRedisExpires)
        );
    }

    public void disableSiteBookedTimeCacheByDay(Long siteId, LocalDate date) {
        String redisKey = getSiteBookedTimeByDayRedisKey(siteId, date);
        redisUtils.delete(redisKey);
    }

    private String getSiteBookedTimeByDayRedisKey(Long siteId, LocalDate date) {
        return String.format("site:%d:day:%s", siteId, date.toString());
    }
}
