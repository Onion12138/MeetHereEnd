package com.ecnu.meethere.site.bookingtime.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SiteBookingTimeDO {
    private Long id;

    private Long siteId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Boolean isInUse;

    private LocalDateTime timeCreate;

    private LocalDateTime timeModified;
}
