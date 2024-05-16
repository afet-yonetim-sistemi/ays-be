package org.ays.admin_user.util.exception;

import org.ays.common.model.dto.request.AysPhoneNumberRequest;
import org.ays.common.util.exception.AysAlreadyException;

import java.io.Serial;

/**
 * This exception is thrown when attempting to create an user with a phone number that already exists in the system.
 */
public class AysUserAlreadyExistsByPhoneNumberException extends AysAlreadyException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 3562892866158335946L;

    /**
     * Constructs a new AysUserAlreadyExistsByPhoneNumberException with the specified phone number.
     *
     * @param phoneNumber The phone number that already exists in the system.
     */
    public AysUserAlreadyExistsByPhoneNumberException(AysPhoneNumberRequest phoneNumber) {
        super("USER ALREADY EXIST! countryCode:" + phoneNumber.getCountryCode() + " , " + "lineNumber:" + phoneNumber.getLineNumber());
    }

}
