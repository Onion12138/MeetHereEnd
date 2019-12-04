package com.ecnu.meethere.news.param;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Accessors(chain = true)
public class NewsPublishParam {
    @Max(32)
    @Min(1)
    private String title;

    @Max(128)
    private String image;

    @Min(1)
    @Max(65535)
    private String content;
}
