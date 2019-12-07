package com.ecnu.meethere.order.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiteBookingOrderCreateParam {
    @NotNull
    @Positive
    private Long siteId;

    @NotEmpty
    @Length(max = 16)
    private String siteName;

    @NotNull
    @Length(max = 128)
    private String siteImage;

    @NotNull
    @DecimalMin("0.01")
    @DecimalMax("100000.00")
    private BigDecimal rent;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;
}
