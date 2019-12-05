package com.ecnu.meethere.news.comment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class NewsCommentDO {
    private Long id;

    private Long newsId;

    private Long userId;

    private String content;

    private LocalDateTime timeCreate;

    private LocalDateTime timeModified;
}
