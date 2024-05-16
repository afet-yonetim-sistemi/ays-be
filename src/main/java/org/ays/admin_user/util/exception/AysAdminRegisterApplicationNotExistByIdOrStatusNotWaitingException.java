package org.ays.admin_user.util.exception;

import org.ays.admin_user.model.enums.AdminRegistrationApplicationStatus;
import org.ays.common.util.exception.AysNotExistException;

import java.io.Serial;

/**
 * Exception indicating that an admin register application does not exist with the specified ID.
 * This exception is a subclass of AysNotExistException, which is typically used to indicate that an entity or
 * resource does not exist.
 * Typically, this exception is thrown when an operation or query is performed on an admin register application
 * entity using an ID that does not correspond to an existing admin register application.
 */
public class AysAdminRegisterApplicationNotExistByIdOrStatusNotWaitingException extends AysNotExistException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 8056706849244878245L;

    /**
     * Constructs a new AysAdminUserRegisterApplicationNotExistByIdAndStatusException with the specified applicationId and applicationStatus.
     *
     * @param id                the applicationId of admin register application.
     * @param applicationStatus the applicationStatus of admin register application.
     */
    public AysAdminRegisterApplicationNotExistByIdOrStatusNotWaitingException(final String id,
                                                                              final AdminRegistrationApplicationStatus applicationStatus) {

        super("ADMIN REGISTER APPLICATION NOT EXIST OR STATUS IS NOT WAITING! id:" + id + ", status:" + applicationStatus);
    }

}
