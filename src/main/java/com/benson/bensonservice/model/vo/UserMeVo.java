package com.benson.bensonservice.model.vo;

import com.benson.bensonservice.constants.Role;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserMeVo {
    private Integer id;
    private String username;
    private String name;
    private String email;
    private Role role;
}
