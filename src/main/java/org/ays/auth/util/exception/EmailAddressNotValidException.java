package org.ays.auth.util.exception;

import org.ays.common.util.exception.AysAuthException;

import java.io.Serial;

/**
 * An exception that is thrown when a emailAddress is not valid.
 * Extends {@link AysAuthException}.
 */
public class EmailAddressNotValidException extends AysAuthException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 8716775556496272471L;

    /**
     * Constructs a new {@code emailAddressNotValidException} with a default message.
     */
    public EmailAddressNotValidException(final String emailAddress) {
        super("EMAIL ADDRESS IS NOT VALID! emailAddress: " + emailAddress);
    }

}
