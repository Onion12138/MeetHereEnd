package com.ecnu.meethere.news.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsUpdateParam {
    @NotNull
    @Positive
    private Long id;

    @Min(1)
    @Max(32)
    private String title;

    @Max(128)
    private String image;

    @Min(1)
    @Max(65535)
    private String content;
}
