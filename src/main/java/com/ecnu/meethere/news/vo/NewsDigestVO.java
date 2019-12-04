package com.ecnu.meethere.news.vo;

import com.ecnu.meethere.user.vo.UserVO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewsDigestVO {
    private Long id;

    private UserVO writer;

    private String title;

    private String image;

    private LocalDateTime timeCreate;
}
