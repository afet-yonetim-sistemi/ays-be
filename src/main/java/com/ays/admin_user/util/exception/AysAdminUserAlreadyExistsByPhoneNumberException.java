package com.ays.admin_user.util.exception;

import com.ays.common.model.AysPhoneNumber;
import com.ays.common.util.exception.AysAlreadyException;

import java.io.Serial;

/**
 * This exception is thrown when attempting to create an admin user with a phone number that already exists in the system.
 */
public class AysAdminUserAlreadyExistsByPhoneNumberException extends AysAlreadyException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 3562892866158335946L;

    /**
     * Constructs a new AysAdminUserAlreadyExistsByPhoneNumberException with the specified phone number.
     *
     * @param phoneNumber The phone number that already exists in the system.
     */
    public AysAdminUserAlreadyExistsByPhoneNumberException(AysPhoneNumber phoneNumber) {
        super("ADMIN USER ALREADY EXIST! countryCode:" + phoneNumber.getCountryCode() + " , " + "lineNumber:" + phoneNumber.getLineNumber());
    }

}
