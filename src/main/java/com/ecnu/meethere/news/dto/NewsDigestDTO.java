package com.ecnu.meethere.news.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class NewsDigestDTO {
    private Long id;

    private Long userId;

    private String title;

    private String image;

    private LocalDateTime timeCreate;
}
