package com.ecnu.meethere.site.manager;

import com.ecnu.meethere.common.annotation.Manager;
import com.ecnu.meethere.news.dto.NewsDTO;
import com.ecnu.meethere.news.dto.NewsDigestDTO;
import com.ecnu.meethere.news.manager.NewsManager;
import com.ecnu.meethere.paging.PageParam;
import com.ecnu.meethere.redis.config.RedisExpiresConfig;
import com.ecnu.meethere.redis.config.RedisUtilsConfig;
import com.ecnu.meethere.redis.core.RedisUtils;
import com.ecnu.meethere.site.dao.SiteDao;
import com.ecnu.meethere.site.dto.SiteDTO;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataRedisTest(
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = SiteManager.class
        )
)
@Import({
        RedisUtilsConfig.class,
        RedisExpiresConfig.class,
})
@ImportAutoConfiguration(RedisAutoConfiguration.class)
class SiteManagerTest {

    @Autowired
    private SiteManager siteManager;

    @MockBean
    private SiteDao siteDao;

    @Autowired
    private RedisUtils redisUtils;

    @AfterEach
    void flushAll() {
        redisUtils.flushAll();
    }

    @Test
    void listSites() {
        when(siteDao.listSites(List.of(-1L))).thenReturn(
                List.of(
                        new SiteDTO(-1L, "", "", "", BigDecimal.ONE, "", LocalDateTime.now())
                ));
        when(siteDao.listSiteIds(any())).thenReturn(List.of(-1L));

        //缓存不命中
        List<SiteDTO> sites = siteManager.listSites(new PageParam(1, 1));
        assertEquals(1, sites.size());
        assertTrue(ReflectionTestUtils.isAllFieldsNotNull(sites.get(0)));

        //缓存命中
        List<SiteDTO> sites1 = siteManager.listSites(new PageParam(1, 1));
        assertEquals(1, sites1.size());
        assertTrue(ReflectionTestUtils.isAllFieldsNotNull(sites1.get(0)));

        verify(siteDao, times(1)).listSiteIds(any());
    }

    @Test
    void getSite() {
        when(siteDao.getSite(-1L)).thenReturn(
                new SiteDTO(-1L, "", "", "", BigDecimal.ONE, "", LocalDateTime.now())
        );
        SiteDTO site = siteManager.getSite(-1L);
        assertTrue(ReflectionTestUtils.isAllFieldsNotNull(site));
        verify(siteDao, times(1)).getSite(anyLong());

        siteManager.getSite(-1L);
        assertTrue(ReflectionTestUtils.isAllFieldsNotNull(site));
        verify(siteDao, times(1)).getSite(anyLong());

    }
}