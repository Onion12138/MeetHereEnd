package com.ecnu.meethere.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserSessionInfo {
    private Long id;

    private String username;

    private String avatar;

    private Boolean isAdministrator;
}
