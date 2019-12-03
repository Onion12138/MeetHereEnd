package com.ecnu.meethere.news.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class NewsDigestDTO {
    private Long id;

    private Long userId;

    private String title;

    private String image;

    private LocalDateTime timeCreate;
}
