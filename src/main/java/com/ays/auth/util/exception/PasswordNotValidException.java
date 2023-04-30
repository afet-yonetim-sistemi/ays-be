package com.ays.auth.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * Exception indicating that a password is not valid.
 * This exception is a subclass of RuntimeException, which is typically used to indicate that an unexpected error
 * has occurred.
 * Typically, this exception is thrown when a user attempts to set or change their password, but the password they
 * provide is not considered valid based on some predetermined criteria (e.g. it's too short, too simple, contains
 * disallowed characters, etc.).
 * This exception is annotated with the @ResponseStatus(HttpStatus.BAD_REQUEST) annotation, which can be used to
 * indicate that this exception should result in an HTTP 400 Bad Request response when thrown from a Spring
 * application.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PasswordNotValidException extends RuntimeException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -6170966118655522879L;

    /**
     * Constructs a new PasswordNotValidException with a default error message.
     */
    public PasswordNotValidException() {
        super("PASSWORD NOT VALID!");
    }

}
