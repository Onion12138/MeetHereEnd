package com.ecnu.meethere.news.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class NewsDO {
    private Long id;

    private Long userId;

    private String title;

    private String image;

    private String content;

    private LocalDateTime timeCreate;

    private LocalDateTime timeModified;
}
