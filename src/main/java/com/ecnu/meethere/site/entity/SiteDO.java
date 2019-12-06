package com.ecnu.meethere.site.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteDO {
    private Long id;

    private String name;

    private String description;

    private String location;

    private BigDecimal rent;

    private String image;

    private LocalDateTime timeCreate;

    private LocalDateTime timeModified;
}
