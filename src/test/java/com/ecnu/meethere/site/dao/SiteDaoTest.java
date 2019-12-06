package com.ecnu.meethere.site.dao;

import com.ecnu.meethere.paging.PageParam;
import com.ecnu.meethere.site.entity.SiteDO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SiteDaoTest {

    @Autowired
    private SiteDao siteDao;

    @BeforeEach
    void insertTestData() {
        LocalDateTime now = LocalDateTime.now();
        siteDao.insertSite(new SiteDO(-1L, "1", "1", "1", BigDecimal.ONE, "", now, now));
        siteDao.insertSite(new SiteDO(-2L, "1", "1", "1", BigDecimal.ONE, "", now, now));
        siteDao.insertSite(new SiteDO(-3L, "1", "1", "1", BigDecimal.ONE, "", now, now));
        siteDao.insertSite(new SiteDO(-4L, "1", "1", "1", BigDecimal.ONE, "", now, now));
    }

    @Test
    void getSite() {
        assertNotNull(siteDao.getSite(-1L));
        assertNull(siteDao.getSite(0L));
    }

    @ParameterizedTest
    @MethodSource("listSitesGen")
    void listSites(List<Long> siteIds, int wSize) {
        assertEquals(wSize, siteDao.listSites(siteIds).size());
    }

    static Stream<Arguments> listSitesGen() {
        return Stream.of(
                Arguments.of(null, 0),
                Arguments.of(List.of(), 0),
                Arguments.of(List.of(-1L), 1),
                Arguments.of(List.of(-1L,-2L,-3L), 3),
                Arguments.of(List.of(-1L,0L), 1)
        );
    }

    @ParameterizedTest
    @MethodSource("listSiteIdsGen")
    void listSiteIds(PageParam pageParam, int wSize) {
        assertEquals(wSize, siteDao.listSiteIds(pageParam).size());
    }

    static Stream<Arguments> listSiteIdsGen() {
        return Stream.of(
                Arguments.of(new PageParam(1,1), 1),
                Arguments.of(new PageParam(1,4), 4),
                Arguments.of(new PageParam(1,5), 4),
                Arguments.of(new PageParam(2,2), 2)
        );
    }
}