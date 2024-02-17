package org.ays.auth.util.exception;

import org.ays.common.util.exception.AysAuthException;

import java.io.Serial;

/**
 * Exception thrown when attempting to authenticate a user that is not active.
 */
public class UserNotActiveException extends AysAuthException {

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
    public UserNotActiveException(String userId) {
        super("USER IS NOT ACTIVE! userId:" + userId);
    }

}
