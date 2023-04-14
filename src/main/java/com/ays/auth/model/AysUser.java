package com.ays.auth.model;

import com.ays.auth.model.enums.AysUserType;
import com.ays.user.model.enums.UserRole;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class AysUser {

    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private AysUserType type;
    private Set<UserRole> roles;

}
