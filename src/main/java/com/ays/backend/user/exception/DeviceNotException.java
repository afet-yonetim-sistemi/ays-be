package com.ays.backend.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DeviceNotException extends RuntimeException {
    public DeviceNotException(String msg) {
        super(msg);
    }
}
