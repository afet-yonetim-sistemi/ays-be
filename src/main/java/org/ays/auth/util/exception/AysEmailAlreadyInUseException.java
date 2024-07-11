package org.ays.auth.util.exception;

import org.ays.common.util.exception.AysAlreadyException;

import java.io.Serial;

/**
 * Exception indicating that an email address is already in use.
 * This exception is a subclass of {@link AysAlreadyException}, which is typically used to indicate that an entity or
 * resource already exists with the expected behavior.
 * Typically, this exception is thrown when an attempt is made to register an email address that is already associated
 * with an existing user account.
 */
public class AysEmailAlreadyInUseException extends AysAlreadyException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 5680009927256342822L;

    /**
     * Constructs a new AysEmailAlreadyInUseException with the specified email address.
     *
     * @param email the email address that is already in use
     */
    public AysEmailAlreadyInUseException(final String email) {
        super("email is already in use: " + email + "!");
    }
}
