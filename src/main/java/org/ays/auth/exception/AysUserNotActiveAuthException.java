package org.ays.auth.exception;

import org.ays.auth.model.enums.AysUserStatus;
import org.ays.common.exception.AysAuthException;

import java.io.Serial;

/**
 * Exception thrown when attempting to authenticate a user that is not active.
 */
public final class AysUserNotActiveAuthException extends AysAuthException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -3010480609839859754L;

    /**
     * Constructs a new AysUserNotActiveAuthException with the specified userId.
     *
     * @param userId the userId of the user that is not active
     */
    public AysUserNotActiveAuthException(String userId) {
        super("user is not active! userId:" + userId);
    }

    /**
     * Constructs a new AysUserNotActiveAuthException with the specified user status.
     *
     * @param status the status of the user that is not active
     */
    public AysUserNotActiveAuthException(AysUserStatus status) {
        super("user is not active! currentStatus: " + status.name());
    }

}
