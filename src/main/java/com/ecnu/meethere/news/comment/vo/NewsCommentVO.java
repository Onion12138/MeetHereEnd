package com.ecnu.meethere.news.comment.vo;

import com.ecnu.meethere.user.vo.UserVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsCommentVO {
    private Long id;

    private UserVO commenter;

    private String content;

    private LocalDateTime timeCreate;
}
