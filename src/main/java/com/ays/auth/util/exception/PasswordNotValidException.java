package com.ays.auth.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PasswordNotValidException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6170966118655522879L;

    public PasswordNotValidException() {
        super("PASSWORD NOT VALID!");
    }

}
