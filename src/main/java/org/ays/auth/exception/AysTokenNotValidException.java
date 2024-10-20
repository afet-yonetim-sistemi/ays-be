package org.ays.auth.exception;

import org.ays.common.exception.AysAuthException;

import java.io.Serial;

/**
 * Exception thrown when a token is not valid.
 */
public final class AysTokenNotValidException extends AysAuthException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -5404410121820902017L;

    /**
     * Constructs a new TokenNotValidException with the specified JWT and cause.
     *
     * @param jwt   The JWT (JSON Web Token) that is not valid.
     * @param cause The cause of the exception.
     */
    public AysTokenNotValidException(String jwt, Throwable cause) {
        super("token is not valid! token: " + jwt, cause);
    }

}
