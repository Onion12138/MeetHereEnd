package com.ecnu.meethere.site.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteCreateParam {
    @NotNull
    @Length(min = 1, max = 16)
    private String name;

    @NotNull
    @Length(min = 1, max = 64)
    private String description;

    @NotNull
    @Length(min = 1, max = 32)
    private String location;

    @NotNull
    @DecimalMin("0.01")
    @DecimalMax("100000.00")
    private BigDecimal rent;

    @NotNull
    @Length(max = 128)
    private String image;
}