package com.ecnu.meethere.site.bookingtime.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteBookedTimeDO {
    private Long id;

    private Long siteId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime timeCreate;

    private LocalDateTime timeModified;
}
