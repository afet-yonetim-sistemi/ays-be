package com.ays.backend.user.model.enums;
/**
 * Enumeration keeping user status.
 */
public enum UserStatus {
    WAITING,
    VERIFIED,
    COMPLETED,
    PASSIVE,
    OCCUPIED;

    public static UserStatus getById(int userStatusId) {
        return UserStatus.values()[userStatusId];
    }
}
