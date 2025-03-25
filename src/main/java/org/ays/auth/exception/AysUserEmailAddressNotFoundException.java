package org.ays.auth.exception;

import org.ays.common.exception.AysAuthException;

import java.io.Serial;

/**
 * An exception that is thrown when a emailAddress is not valid.
 * Extends {@link AysAuthException}.
 */
public final class AysUserEmailAddressNotFoundException extends AysAuthException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 741079982194537418L;

    /**
     * Constructs a new {@link AysUserEmailAddressNotFoundException} with a default message.
     */
    public AysUserEmailAddressNotFoundException(final String emailAddress) {
        super("email address is not valid! emailAddress: " + emailAddress);
    }

}
