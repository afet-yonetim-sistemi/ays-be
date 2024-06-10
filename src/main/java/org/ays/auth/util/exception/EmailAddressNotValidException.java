package org.ays.auth.util.exception;

import org.ays.common.util.exception.AysAuthException;

import java.io.Serial;

/**
 * An exception that is thrown when a emailAddress is not valid.
 * Extends {@link AysAuthException}.
 */
public final class EmailAddressNotValidException extends AysAuthException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 8716775556496272471L;

    /**
     * Constructs a new {@link EmailAddressNotValidException} with a default message.
     */
    public EmailAddressNotValidException(final String emailAddress) {
        super("email address is not valid! emailAddress: " + emailAddress);
    }

}
