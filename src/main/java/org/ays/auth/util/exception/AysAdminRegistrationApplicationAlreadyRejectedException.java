package org.ays.auth.util.exception;

import org.ays.auth.model.enums.AdminRegistrationApplicationStatus;
import org.ays.common.util.exception.AysAlreadyException;

import java.io.Serial;

/**
 * Exception indicating that an admin registration application is already rejected.
 * This exception is a subclass of AysAlreadyException, which is typically used to indicate that an entity or
 * resource already exists with expected behaviour.
 * Typically, this exception is thrown when a rejection operation is performed on an admin register application
 * entity to an already rejected admin register application.
 */
public class AysAdminRegistrationApplicationAlreadyRejectedException extends AysAlreadyException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 8056706849244878245L;

    /**
     * Constructs a new {@link AysAdminRegistrationApplicationAlreadyRejectedException} with the specified applicationId and applicationStatus.
     *
     * @param id                the applicationId of admin register application.
     * @param applicationStatus the applicationStatus of admin register application.
     */
    public AysAdminRegistrationApplicationAlreadyRejectedException(final String id,
                                                                   final AdminRegistrationApplicationStatus applicationStatus) {

        super("admin registration application was already rejected! id: " + id + ", status:" + applicationStatus);
    }

}
