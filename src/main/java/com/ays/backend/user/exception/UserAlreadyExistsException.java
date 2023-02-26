package com.ays.backend.user.exception;

/**
 * Exception class defining fired when a user already exists in the database.
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String msg) {
        super(msg);
    }
}
