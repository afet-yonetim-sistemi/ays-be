package org.ays.auth.util.exception;

import org.ays.common.util.exception.AysAlreadyException;

import java.io.Serial;

/**
 * Exception indicating that an user already exists with the specified email address.
 * This exception is a subclass of {@link AysAlreadyException}.
 */
public final class AysUserAlreadyExistsByEmailAddressException extends AysAlreadyException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 5626401282048575054L;

    /**
     * Constructs a new AysUserAlreadyExistsByEmailException with the specified email address.
     *
     * @param emailAddress The email address of the user that already exists.
     */
    public AysUserAlreadyExistsByEmailAddressException(String emailAddress) {
        super("user already exist! emailAddress:" + emailAddress);
    }

}
