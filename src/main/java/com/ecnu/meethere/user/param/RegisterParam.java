package com.ecnu.meethere.user.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterParam {
    @NotNull
    @Length(min = 1, max = 16)
    private String username;

    @NotNull
    @Length(min = 8, max = 16)
    private String password;
}
