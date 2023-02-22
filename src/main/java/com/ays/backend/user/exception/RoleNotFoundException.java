package com.ays.backend.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoleNotFoundException extends RuntimeException {
    private String message;

    public RoleNotFoundException(String message) {
        this.message = message;
    }

    public RoleNotFoundException() {
    }
}