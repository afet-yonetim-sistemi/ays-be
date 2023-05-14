package com.ays.auth.util.exception;

import com.ays.common.util.exception.AysAuthException;

import java.io.Serial;

/**
 * Exception to be thrown when a password is not valid.
 */
public class PasswordNotValidException extends AysAuthException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -6170966118655522879L;

    /**
     * Constructs a new PasswordNotValidException with a default error message.
     */
    public PasswordNotValidException() {
        super("PASSWORD IS NOT VALID!");
    }

}
