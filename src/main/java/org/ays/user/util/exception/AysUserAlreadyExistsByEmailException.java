package org.ays.user.util.exception;

import org.ays.common.util.exception.AysAlreadyException;

import java.io.Serial;

/**
 * Exception indicating that an user already exists with the specified email address.
 * This exception is a subclass of AysAlreadyException.
 */
public class AysUserAlreadyExistsByEmailException extends AysAlreadyException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -8333833736509873511L;

    /**
     * Constructs a new AysUserAlreadyExistsByEmailException with the specified email address.
     *
     * @param emailAddress The email address of the user that already exists.
     */
    public AysUserAlreadyExistsByEmailException(String emailAddress) {
        super("USER ALREADY EXIST! emailAddress:" + emailAddress);
    }

}
