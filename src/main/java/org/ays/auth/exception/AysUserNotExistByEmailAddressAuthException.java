package org.ays.auth.exception;

import org.ays.common.exception.AysAuthException;
import org.ays.common.util.AysSensitiveMaskingCategory;

import java.io.Serial;

/**
 * An exception that is thrown when a user does not exist by email address.
 * Extends {@link AysAuthException}.
 */
public final class AysUserNotExistByEmailAddressAuthException extends AysAuthException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 1200135838236855380L;

    /**
     * Constructs a new {@link AysUserNotExistByEmailAddressAuthException} with a default message.
     */
    public AysUserNotExistByEmailAddressAuthException(final String emailAddress) {
        super("user does not found! emailAddress: " + AysSensitiveMaskingCategory.EMAIL_ADDRESS.mask(emailAddress));
    }

}
