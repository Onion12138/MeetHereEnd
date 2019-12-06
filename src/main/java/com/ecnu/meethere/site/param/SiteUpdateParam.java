package com.ecnu.meethere.site.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteUpdateParam {
    @NotNull
    @Positive
    private Long id;

    @Length(min = 1, max = 16)
    private String name;

    @Length(min = 1, max = 64)
    private String description;

    @Length(min = 1, max = 32)
    private String location;

    @DecimalMin("0.01")
    @DecimalMax("100000.00")
    private BigDecimal rent;

    @Length(max = 128)
    private String image;
}
