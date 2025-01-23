package org.ays.auth.exception;

import org.ays.common.exception.AysConflictException;

import java.io.Serial;

/**
 * Exception thrown when a user does not in an active state.
 * This exception extends {@link AysConflictException}.
 */
public final class AysUserNotActiveException extends AysConflictException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 3508025652421021710L;

    /**
     * Constructs a new AysUserNotActiveException with the specified userId.
     *
     * @param userId the id of the user that is not active.
     */
    public AysUserNotActiveException(String userId) {
        super("user is not active! userId:" + userId);
    }

}
