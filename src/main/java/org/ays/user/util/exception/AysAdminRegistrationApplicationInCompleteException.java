package org.ays.user.util.exception;

import java.io.Serial;

import org.ays.common.util.exception.AysInvalidStatusException;
import org.ays.user.model.enums.AdminRegistrationApplicationStatus;

/**
 * Exception indicating that an admin registration application is not completed to be rejected.
 * This exception is a subclass of AysProcessException, which is typically used to indicate that an entity or
 * resource was incorrectly used during the process.
 * Typically, this exception is thrown when a rejection operation is performed on an admin register application
 * entity to an incomplete application, where its status is not set to 'COMPLETE'.
 */
public class AysAdminRegistrationApplicationInCompleteException extends AysInvalidStatusException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 8056706849244878245L;

    /**
     * Constructs a new {@link AysAdminRegistrationApplicationInCompleteException} with the specified applicationId and applicationStatus.
     *
     * @param id                the applicationId of admin register application.
     * @param applicationStatus the applicationStatus of admin register application.
     */
    public AysAdminRegistrationApplicationInCompleteException(final String id,
                                                              final AdminRegistrationApplicationStatus applicationStatus) {

        super("ADMIN REGISTRATION APPLICATION IS NOT COMPLETE WITH id: " + id + ", status:" + applicationStatus);
    }

}
