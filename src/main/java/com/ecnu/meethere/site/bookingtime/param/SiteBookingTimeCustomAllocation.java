package com.ecnu.meethere.site.bookingtime.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.Period;
import java.util.List;

@Data
public class SiteBookingTimeCustomAllocation {
    @NotNull
    @Size
    private List<SiteBookingTimeDuration> durations;
}
