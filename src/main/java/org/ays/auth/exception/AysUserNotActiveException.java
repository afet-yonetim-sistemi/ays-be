package org.ays.auth.exception;

import org.ays.auth.model.enums.AysUserStatus;
import org.ays.common.exception.AysAuthException;

import java.io.Serial;

/**
 * Exception thrown when attempting to authenticate a user that is not active.
 */
public final class AysUserNotActiveException extends AysAuthException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -5218287176856317070L;

    /**
     * Constructs a new UserNotActiveException with the specified userId.
     *
     * @param userId the userId of the user that is not active
     */
    public AysUserNotActiveException(String userId) {
        super("user is not active! userId:" + userId);
    }

    /**
     * Constructs a new UserNotActiveException with the specified user status.
     *
     * @param status the status of the user that is not active
     */
    public AysUserNotActiveException(AysUserStatus status) {
        super("user is not active! currentStatus: " + status.name());
    }

}
