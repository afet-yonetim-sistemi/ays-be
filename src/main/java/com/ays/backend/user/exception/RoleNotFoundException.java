package com.ays.backend.user.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
public class RoleNotFoundException extends RuntimeException {
    private final String message;

    public RoleNotFoundException(String message) {
        this.message = message;
    }

}