package org.ays.auth.exception;

import org.ays.common.exception.AysAuthException;

import java.io.Serial;

/**
 * An exception that is thrown when a emailAddress is not valid.
 * Extends {@link AysAuthException}.
 */
public final class AysEmailAddressNotValidException extends AysAuthException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 8716775556496272471L;

    /**
     * Constructs a new {@link AysEmailAddressNotValidException} with a default message.
     */
    public AysEmailAddressNotValidException(final String emailAddress) {
        super("email address is not valid! emailAddress: " + emailAddress);
    }

}
