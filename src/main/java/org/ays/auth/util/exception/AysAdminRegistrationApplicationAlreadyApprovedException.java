package org.ays.auth.util.exception;

import org.ays.auth.model.enums.AdminRegistrationApplicationStatus;
import org.ays.common.exception.AysConflictException;

import java.io.Serial;

/**
 * Exception indicating that an admin registration application is already approved.
 * This exception is a subclass of {@link AysConflictException}, which is typically used to indicate that an entity or
 * resource already exists with expected behaviour.
 * Typically, this exception is thrown when an approval operation is performed on an admin register application
 * entity to an already approved or rejected admin register application.
 */
public class AysAdminRegistrationApplicationAlreadyApprovedException extends AysConflictException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -5585343678106236152L;

    /**
     * Constructs a new AysAlreadyException with the specified detail message.
     *
     * @param id                the id of the approved application.
     * @param applicationStatus the status of the already approved application.
     */
    public AysAdminRegistrationApplicationAlreadyApprovedException(final String id,
                                                                   final AdminRegistrationApplicationStatus applicationStatus) {
        super("admin registration application was already approved or rejected! id: " + id + ", status:" + applicationStatus);
    }
}
