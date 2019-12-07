package com.ecnu.meethere.site.bookingtime.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteBookedTimeDTO {
    private Long id;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
