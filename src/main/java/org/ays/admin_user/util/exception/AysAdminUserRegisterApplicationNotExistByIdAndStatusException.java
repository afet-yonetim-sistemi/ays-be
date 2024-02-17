package org.ays.admin_user.util.exception;

import org.ays.admin_user.model.enums.AdminUserRegisterApplicationStatus;
import org.ays.common.util.exception.AysNotExistException;

import java.io.Serial;

/**
 * Exception indicating that an admin user register application does not exist with the specified ID.
 * This exception is a subclass of AysNotExistException, which is typically used to indicate that an entity or
 * resource does not exist.
 * Typically, this exception is thrown when an operation or query is performed on an admin user register application
 * entity using an ID that does not correspond to an existing admin user register application.
 */
public class AysAdminUserRegisterApplicationNotExistByIdAndStatusException extends AysNotExistException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 8056706849244878245L;

    /**
     * Constructs a new AysAdminUserRegisterApplicationNotExistByIdAndStatusException with the specified applicationId and applicationStatus.
     *
     * @param applicationId     the applicationId of admin user register application.
     * @param applicationStatus the applicationStatus of admin user register application.
     */
    public AysAdminUserRegisterApplicationNotExistByIdAndStatusException(String applicationId, AdminUserRegisterApplicationStatus applicationStatus) {
        super("ADMIN USER REGISTER APPLICATION NOT EXIST! id:" + applicationId + ", status:" + applicationStatus);
    }
}
