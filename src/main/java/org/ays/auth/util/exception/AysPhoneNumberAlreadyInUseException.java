package org.ays.auth.util.exception;

import org.ays.common.util.exception.AysAlreadyException;

import java.io.Serial;

/**
 * Exception indicating that a phone number is already in use.
 * This exception is a subclass of {@link AysAlreadyException}, which is typically used to indicate that an entity or
 * resource already exists with the expected behavior.
 * Typically, this exception is thrown when an attempt is made to register a phone number that is already associated
 * with an existing user account.
 */
public class AysPhoneNumberAlreadyInUseException extends AysAlreadyException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 3706511380768682738L;

    /**
     * Constructs a new AysPhoneNumberAlreadyInUseException with the specified phone number.
     *
     * @param phoneNumber the phone number that is already in use
     */
    public AysPhoneNumberAlreadyInUseException(final String phoneNumber) {
        super("phone number is already in use: " + phoneNumber + "!");
    }
}
