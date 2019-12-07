package com.ecnu.meethere.site.bookingtime.manager;

import com.ecnu.meethere.paging.PageParam;
import com.ecnu.meethere.redis.config.RedisExpiresConfig;
import com.ecnu.meethere.redis.config.RedisUtilsConfig;
import com.ecnu.meethere.redis.core.RedisUtils;
import com.ecnu.meethere.site.bookingtime.dao.SiteBookedTimeDao;
import com.ecnu.meethere.site.bookingtime.dto.SiteBookedTimeDTO;
import com.ecnu.meethere.site.dao.SiteDao;
import com.ecnu.meethere.site.dto.SiteDTO;
import com.ecnu.meethere.site.manager.SiteManager;
import com.ecnu.meethere.utils.ReflectionTestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataRedisTest(
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = SiteBookedTimeManager.class
        )
)
@Import({
        RedisUtilsConfig.class,
        RedisExpiresConfig.class,
})
@ImportAutoConfiguration(RedisAutoConfiguration.class)
class SiteBookedTimeManagerTest {
    @Autowired
    private SiteBookedTimeManager manager;

    @MockBean
    private SiteBookedTimeDao siteDao;

    @Autowired
    private RedisUtils redisUtils;

    @AfterEach
    void flushAll() {
        redisUtils.flushAll();
    }

    @Test
    void disableSiteBookedTimeCacheByDay() {
        when(siteDao.listByStartTime(anyLong(), any(), any())).thenReturn(
                List.of(
                        new SiteBookedTimeDTO(-1L, LocalDateTime.now(), LocalDateTime.now())
                ));
        List<SiteBookedTimeDTO> sites = manager.listSiteBookedTimeByDay(-1L, LocalDate.now());
        assertEquals(1, sites.size());
        assertTrue(ReflectionTestUtils.isAllFieldsNotNull(sites.get(0)));

        manager.disableSiteBookedTimeCacheByDay(-1L, LocalDate.now());

        manager.listSiteBookedTimeByDay(-1L, LocalDate.now());

        verify(siteDao, times(2)).listByStartTime(anyLong(), any(), any());
    }

    @Test
    void listSiteBookedTimeByDay() {
        when(siteDao.listByStartTime(anyLong(), any(), any())).thenReturn(
                List.of(
                        new SiteBookedTimeDTO(-1L, LocalDateTime.now(), LocalDateTime.now())
                ));

        //缓存不命中
        List<SiteBookedTimeDTO> sites = manager.listSiteBookedTimeByDay(-1L, LocalDate.now());
        assertEquals(1, sites.size());
        assertTrue(ReflectionTestUtils.isAllFieldsNotNull(sites.get(0)));

        //缓存命中
        List<SiteBookedTimeDTO> sites1 = manager.listSiteBookedTimeByDay(-1L, LocalDate.now());
        assertEquals(1, sites1.size());
        assertTrue(ReflectionTestUtils.isAllFieldsNotNull(sites1.get(0)));

        verify(siteDao, times(1)).listByStartTime(anyLong(), any(), any());
    }


}