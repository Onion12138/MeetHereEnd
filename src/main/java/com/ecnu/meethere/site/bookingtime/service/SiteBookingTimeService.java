package com.ecnu.meethere.site.bookingtime.service;

import com.ecnu.meethere.common.idgenerator.IdGenerator;
import com.ecnu.meethere.site.bookingtime.dao.SiteBookedTimeDao;
import com.ecnu.meethere.site.bookingtime.dto.SiteBookedTimeDTO;
import com.ecnu.meethere.site.bookingtime.entity.SiteBookedTimeDO;
import com.ecnu.meethere.site.bookingtime.manager.SiteBookedTimeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SiteBookingTimeService {
    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SiteBookedTimeDao siteBookedTimeDao;

    @Autowired
    private SiteBookedTimeManager siteBookedTimeManager;

    public List<SiteBookedTimeDTO> listSiteBookedTimeByDay(Long siteId, LocalDate date) {
        return siteBookedTimeManager.listSiteBookedTimeByDay(siteId, date);
    }

    @Transactional
    public boolean tryBooking(Long siteId, LocalDateTime startTime, LocalDateTime endTime) {
        if (siteBookedTimeDao.hasConflict(siteId, startTime, endTime).orElse(false))
            return false;
        siteBookedTimeDao.insert(convertToSiteBookedTimeDO(siteId, startTime, endTime));
        siteBookedTimeManager.disableSiteBookedTimeCacheByDay(siteId, startTime.toLocalDate());
        return true;
    }

    private SiteBookedTimeDO convertToSiteBookedTimeDO(Long siteId, LocalDateTime startTime,
                                                       LocalDateTime endTime) {
        SiteBookedTimeDO siteBookedTimeDO = new SiteBookedTimeDO();
        siteBookedTimeDO.setId(idGenerator.nextId());
        siteBookedTimeDO.setSiteId(siteId);
        siteBookedTimeDO.setStartTime(startTime);
        siteBookedTimeDO.setEndTime(endTime);
        return siteBookedTimeDO;
    }

    public void cancelBooked(Long siteId, LocalDateTime startTime) {
        siteBookedTimeDao.deleteByStartTime(siteId, startTime);
        siteBookedTimeManager.disableSiteBookedTimeCacheByDay(siteId, startTime.toLocalDate());
    }
}
