package com.ays.backend.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DeviceNotFoundException  extends RuntimeException {
    public DeviceNotFoundException (String msg) {
        super(msg);
    }
}
