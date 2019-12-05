package com.ecnu.meethere.news.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class NewsCommentDTO {
    private Long id;

    private Long userId;

    private String content;

    private LocalDateTime timeCreate;
}
