package com.ays.backend.user.model.enums;

import java.util.Arrays;

import com.ays.backend.user.exception.RoleNotFoundException;

/**
 * Enumeration keeping user roles.
 */
public enum UserRole {
    ROLE_VOLUNTEER("ROLE_VOLUNTEER"),
    ROLE_WORKER("ROLE_WORKER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }

    public static UserRole getUserRoleByName(String value) {
        return Arrays.stream(values())
                .filter(userRole -> userRole.getValue().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(RoleNotFoundException::new);
    }

}
