package org.ays.auth.util.exception;

import org.ays.common.util.exception.AysAuthException;

import java.io.Serial;

/**
 * Exception to be thrown when a password is not valid.
 */
public final class PasswordNotValidException extends AysAuthException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -6170966118655522879L;

    /**
     * Constructs a new PasswordNotValidException with a default error message.
     */
    public PasswordNotValidException() {
        super("password is not valid!");
    }

}
