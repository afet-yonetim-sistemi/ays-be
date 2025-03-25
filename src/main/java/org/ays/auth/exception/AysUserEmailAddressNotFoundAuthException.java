package org.ays.auth.exception;

import org.ays.common.exception.AysAuthException;
import org.ays.common.util.AysSensitiveMaskingCategory;

import java.io.Serial;

/**
 * An exception that is thrown when a emailAddress does not found.
 * Extends {@link AysAuthException}.
 */
public final class AysUserEmailAddressNotFoundAuthException extends AysAuthException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -7384151891321633254L;

    /**
     * Constructs a new {@link AysUserEmailAddressNotFoundAuthException} with a default message.
     */
    public AysUserEmailAddressNotFoundAuthException(final String emailAddress) {
        super("email address does not found! emailAddress: " + AysSensitiveMaskingCategory.EMAIL_ADDRESS.mask(emailAddress));
    }

}
