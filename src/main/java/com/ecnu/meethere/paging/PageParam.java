package com.ecnu.meethere.paging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageParam {
    @Min(1)
    private int pageNum;

    @Min(1)
    @Max(100)
    private int pageSize;
}
