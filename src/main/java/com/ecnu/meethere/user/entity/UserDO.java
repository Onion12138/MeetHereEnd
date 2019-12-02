package com.ecnu.meethere.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDO {
    private Long id;

    private String username;

    private String password;

    private String avatar;

    private Boolean isAdministrator;

    private LocalDateTime timeCreate;

    private LocalDateTime timeModified;
}