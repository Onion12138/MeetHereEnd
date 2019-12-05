package com.ecnu.meethere.news.comment.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsCommentPostParam {
    @Positive
    private Long newsId;

    @Min(1)
    @Max(65536)
    private String content;
}
