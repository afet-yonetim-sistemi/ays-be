package org.ays.admin_user.util.exception;

import org.ays.common.util.exception.AysNotExistException;

import java.io.Serial;

/**
 * Exception indicating that the verification code provided during registration for an admin user is not valid.
 * This exception is a subclass of AysNotExistException, which is typically used to indicate that an entity or
 * resource does not exist.
 * Typically, this exception is thrown when a verification code provided during the registration process of an admin
 * user is not valid, meaning that it may have already been used or expired.
 */
public class AysAdminUserRegisterApplicationCodeNotValidException extends AysNotExistException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -2140764454266823885L;

    /**
     * Constructs a new AysAdminUserRegisterVerificationCodeNotValidException with the specified verification ID.
     *
     * @param verificationId The verification ID that is not valid.
     */
    public AysAdminUserRegisterApplicationCodeNotValidException(String verificationId) {
        super("VERIFICATION ID IS NOT VALID! verificationId:" + verificationId);
    }

}
