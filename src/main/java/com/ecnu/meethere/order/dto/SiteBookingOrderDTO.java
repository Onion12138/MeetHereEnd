package com.ecnu.meethere.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SiteBookingOrderDTO {
    private Long id;

    private Long userId;

    private Long siteId;

    private String siteName;

    private String siteImage;

    private BigDecimal rent;

    private Integer status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime timeCreate;

    private LocalDateTime timeModified;
}
