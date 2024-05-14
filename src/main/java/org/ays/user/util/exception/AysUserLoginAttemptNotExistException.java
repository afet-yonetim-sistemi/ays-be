package org.ays.user.util.exception;

import org.ays.common.util.exception.AysNotExistException;

import java.io.Serial;

/**
 * Exception to be thrown when a user with a given User ID does not exist.
 */
public class AysUserLoginAttemptNotExistException extends AysNotExistException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = 4835784446413089860L;

    /**
     * Constructs a new AysUserNotExistByIdException with the specified user ID.
     *
     * @param userId the User ID of the user that does not exist
     */
    public AysUserLoginAttemptNotExistException(String userId) {
        super("USER LOGIN ATTEMPT NOT EXIST! userId:" + userId);
    }

}
