package com.ecnu.meethere.site.service;

import com.ecnu.meethere.common.idgenerator.IdGenerator;
import com.ecnu.meethere.common.idgenerator.SnowflakeIdGenerator;
import com.ecnu.meethere.paging.PageParam;
import com.ecnu.meethere.site.dao.SiteDao;
import com.ecnu.meethere.site.entity.SiteDO;
import com.ecnu.meethere.site.manager.SiteManager;
import com.ecnu.meethere.site.param.SiteCreateParam;
import com.ecnu.meethere.site.param.SiteUpdateParam;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SiteServiceTest {
    @InjectMocks
    private SiteService siteService;

    @Mock
    private SiteManager siteManager;

    @Mock
    private SiteDao siteDao;

    @Spy
    private IdGenerator idGenerator = new SnowflakeIdGenerator(0, 0);

    @Test
    void listSites() {
        siteService.listSites(new PageParam());
        verify(siteManager).listSites(any());
    }

    @Test
    void getSite() {
        siteService.getSite(-1L);
        verify(siteManager).getSite(anyLong());
    }

    @Test
    void createSite() {
        when(siteDao.insertSite(any())).thenAnswer(invocation -> {
            SiteDO site = invocation.getArgument(0);
            assertNotNull(site.getId());
            assertNotNull(site.getDescription());
            assertNotNull(site.getImage());
            assertNotNull(site.getLocation());
            assertNotNull(site.getName());
            assertNotNull(site.getRent());
            return 1;
        });
        siteService.createSite(new SiteCreateParam("1", "1", "1", BigDecimal.ONE, "1"));
        verify(siteDao).insertSite(any());
    }

    @Test
    void updateSite() {
        siteService.updateSite(new SiteUpdateParam());
        verify(siteDao).updateSite(any());
    }

    @Test
    void deleteSite() {
        siteService.deleteSite(-1L);
        verify(siteDao).deleteSite(anyLong());
    }
}