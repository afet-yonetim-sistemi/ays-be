package org.ays.auth.exception;

import org.ays.common.exception.AysConflictException;
import org.ays.common.model.AysPhoneNumber;

import java.io.Serial;

/**
 * This exception is thrown when attempting to create a user with a phone number that already exists in the system.
 */
public final class AysUserAlreadyExistsByPhoneNumberException extends AysConflictException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 259255128392056805L;

    /**
     * Constructs a new AysUserAlreadyExistsByPhoneNumberException with the specified phone number.
     *
     * @param phoneNumber The phone number that already exists in the system.
     */
    public AysUserAlreadyExistsByPhoneNumberException(AysPhoneNumber phoneNumber) {
        super("user already exist! countryCode:" + phoneNumber.getCountryCode() + " , " + "lineNumber:" + phoneNumber.getLineNumber());
    }

}
