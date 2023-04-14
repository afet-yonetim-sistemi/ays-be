package com.ays.auth.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PasswordNotValidException extends RuntimeException {

    public PasswordNotValidException() {
        super("PASSWORD NOT VALID!");
    }

}
