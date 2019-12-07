package com.ecnu.meethere.site.bookingtime.dao;

import com.ecnu.meethere.site.bookingtime.dto.SiteBookedTimeDTO;
import com.ecnu.meethere.site.bookingtime.entity.SiteBookedTimeDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface SiteBookedTimeDao {
    int insert(SiteBookedTimeDO siteBookedTimeDO);

    Optional<Boolean> hasConflict(
            @Param("siteId") Long siteId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    List<SiteBookedTimeDTO> listByStartTime(
            @Param("siteId") Long siteId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    int deleteByStartTime(@Param("siteId") Long siteId,
                          @Param("startTime") LocalDateTime startTime);
}
