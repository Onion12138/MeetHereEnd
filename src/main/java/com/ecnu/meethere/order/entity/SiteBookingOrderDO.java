package com.ecnu.meethere.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class  SiteBookingOrderDO {
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
