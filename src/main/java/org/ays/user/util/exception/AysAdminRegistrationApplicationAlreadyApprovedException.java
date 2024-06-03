package org.ays.user.util.exception;

import org.ays.common.util.exception.AysAlreadyException;
import org.ays.user.model.enums.AdminRegistrationApplicationStatus;

/**
 * Exception indicating that an admin registration application is already approved.
 * This exception is a subclass of AysAlreadyException, which is typically used to indicate that an entity or
 * resource already exists with expected behaviour.
 * Typically, this exception is thrown when an approval operation is performed on an admin register application
 * entity to an already approved or rejected admin register application.
 */
public class AysAdminRegistrationApplicationAlreadyApprovedException extends AysAlreadyException {
    /**
     * Constructs a new AysAlreadyException with the specified detail message.
     *
     * @param message the detail message.
     */
    public AysAdminRegistrationApplicationAlreadyApprovedException(final String id,
                                                                   final AdminRegistrationApplicationStatus applicationStatus) {
        super("ADMIN REGISTRATION APPLICATION WAS ALREADY APPROVED OR REJECTED WITH id: " + id + ", status:" + applicationStatus);
    }
}
