package com.ays.user.util.exception;

import com.ays.common.model.AysPhoneNumber;
import com.ays.common.util.exception.AysAlreadyException;

import java.io.Serial;

/**
 * This exception is thrown when attempting to create a user with a phone number that already exists in the system.
 */
public class AysUserAlreadyExistsByPhoneNumberException extends AysAlreadyException {

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
        super("USER ALREADY EXIST! countryCode:" + phoneNumber.getCountryCode() + " , " + "lineNumber:" + phoneNumber.getLineNumber());
    }

}
