package com.ays.backend.user.controller;

/**
 * Keeps error types to be handled in the error GlobalExceptionHandler.
 */
enum ErrorTypes {
    UNIQUE_MOBILE_NUMBER("UNIQUEMOBILENUMBER");

    private final String reason;

    ErrorTypes(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
