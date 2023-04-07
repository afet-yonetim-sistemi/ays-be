package com.ays.backend.user.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Keeps error types to be handled in the error GlobalExceptionHandler.
 */
@Getter
@RequiredArgsConstructor
enum ErrorTypes {

    UNIQUE_MOBILE_NUMBER("UNIQUEMOBILENUMBER");

    private final String reason;
}
