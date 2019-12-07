package com.ecnu.meethere.order.param;

import com.ecnu.meethere.paging.PageParam;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Data
public class SiteBookingOrderListByUserParam {
    @Range(
            min = SiteBookingOrderStatus.AUDITING,
            max = SiteBookingOrderStatus.CANCELLED
    )
    private Integer status;

    @Valid
    private PageParam pageParam;
}