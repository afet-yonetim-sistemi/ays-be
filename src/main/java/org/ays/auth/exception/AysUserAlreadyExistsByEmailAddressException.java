package org.ays.auth.exception;

import org.ays.common.exception.AysConflictException;
import org.ays.common.util.AysSensitiveMaskingCategory;

import java.io.Serial;

/**
 * Exception indicating that a user already exists with the specified email address.
 * This exception is a subclass of {@link AysConflictException}.
 */
public final class AysUserAlreadyExistsByEmailAddressException extends AysConflictException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -5120208293303231802L;

    /**
     * Constructs a new AysUserAlreadyExistsByEmailException with the specified email address.
     *
     * @param emailAddress The email address of the user that already exists.
     */
    public AysUserAlreadyExistsByEmailAddressException(final String emailAddress) {
        super("user already exists! emailAddress: " + AysSensitiveMaskingCategory.EMAIL_ADDRESS.mask(emailAddress));
    }

}
