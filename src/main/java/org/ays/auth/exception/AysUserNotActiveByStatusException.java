package org.ays.auth.exception;

import org.ays.auth.model.enums.AysUserStatus;
import org.ays.common.exception.AysAuthException;

import java.io.Serial;

/**
 * Exception thrown when an operation is attempted on a user who is not active due to their current status.
 */
public final class AysUserNotActiveByStatusException extends AysAuthException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 2278892850482871645L;

    /**
     * Constructs a new AysUserNotActiveByStatusException with the specified user status.
     *
     * @param userStatus the status of the user that is not active
     */
    public AysUserNotActiveByStatusException(AysUserStatus userStatus) {
        super("user is not active! user status: " + userStatus.toString());
    }

}
