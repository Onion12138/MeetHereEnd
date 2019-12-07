package com.ecnu.meethere.site.bookingtime.service;

import com.ecnu.meethere.common.idgenerator.IdGenerator;
import com.ecnu.meethere.common.idgenerator.SnowflakeIdGenerator;
import com.ecnu.meethere.site.bookingtime.dao.SiteBookedTimeDao;
import com.ecnu.meethere.site.bookingtime.entity.SiteBookedTimeDO;
import com.ecnu.meethere.site.bookingtime.manager.SiteBookedTimeManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SiteBookingTimeServiceTest {
    @InjectMocks
    private SiteBookingTimeService service;

    @Mock
    private SiteBookedTimeDao siteBookedTimeDao;

    @Spy
    private IdGenerator idGenerator = new SnowflakeIdGenerator(0, 0);

    @Mock
    private SiteBookedTimeManager siteBookedTimeManager;


    @Test
    void listSiteBookedTimeByDay() {
        service.listSiteBookedTimeByDay(-1L, LocalDate.now());
        verify(siteBookedTimeManager).listSiteBookedTimeByDay(anyLong(), any());
    }

    @Test
    void tryBooking() {
        when(siteBookedTimeDao.hasConflict(anyLong(), any(), any())).thenReturn(Optional.of(true));
        LocalDateTime now = LocalDateTime.now();
        assertFalse(service.tryBooking(-1L, now, now));

        when(siteBookedTimeDao.hasConflict(anyLong(), any(), any())).thenReturn(Optional.ofNullable(null));

        when(siteBookedTimeDao.insert(any())).thenAnswer(invocation -> {
            SiteBookedTimeDO o = invocation.getArgument(0);
            assertNotNull(o.getId());
            assertNotNull(o.getSiteId());
            assertNotNull(o.getStartTime());
            assertNotNull(o.getEndTime());
            return 1;
        });

        assertTrue(service.tryBooking(-1L, now, now));

    }

    @Test
    void cancelBooked() {
        service.cancelBooked(-1L, LocalDateTime.now());
        verify(siteBookedTimeDao).deleteByStartTime(anyLong(), any());
        verify(siteBookedTimeManager).disableSiteBookedTimeCacheByDay(anyLong(), any());
    }
}