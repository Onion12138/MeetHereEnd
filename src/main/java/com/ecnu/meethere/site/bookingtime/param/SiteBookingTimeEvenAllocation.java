package com.ecnu.meethere.site.bookingtime.param;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class SiteBookingTimeEvenAllocation {
    private LocalTime start;

    private LocalTime end;

    private int duration;

    private int interval;
}
