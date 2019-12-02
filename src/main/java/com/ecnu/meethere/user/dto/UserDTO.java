package com.ecnu.meethere.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;

    private String username;

    private String password;

    private String avatar;

    private Boolean isAdministrator;
}