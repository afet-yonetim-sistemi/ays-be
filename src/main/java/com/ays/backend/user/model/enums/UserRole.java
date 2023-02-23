package com.ays.backend.user.model.enums;

/**
 * Enumeration keeping user userRoles.
 */
public enum UserRole {
    ROLE_VOLUNTEER,
    ROLE_WORKER,
    ROLE_ADMIN;

    public static UserRole getById(int userTypeId) {
        return UserRole.values()[userTypeId];
    }
}
