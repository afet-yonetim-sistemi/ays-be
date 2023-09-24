package com.ays.location.util.exception;

import com.ays.common.util.exception.AysProcessException;

import java.io.Serial;

/**
 * This exception is thrown when a user's location cannot be updated. Reasons for not being able to update the user's
 * location may include the absence of an assigned task or the assigned task not being in progress status.
 * <p>
 * This exception is typically derived from the {@link AysProcessException} class and represents a specific process state
 * or condition.
 *
 * @see AysProcessException
 */
public class AysUserLocationCannotBeUpdatedException extends AysProcessException {

    /**
     * A special field containing version information along with the serial number.
     */
    @Serial
    private static final long serialVersionUID = 2733712280590701217L;

    /**
     * Constructs a new AysUserLocationCannotBeUpdatedException with the specified user ID.
     *
     * @param userId the ID of the user whose location cannot be updated
     */
    public AysUserLocationCannotBeUpdatedException(String userId) {
        super("USER LOCATION CANNOT BE UPDATED BECAUSE USER DOES NOT HAVE AN ASSIGNMENT IN PROGRESS! userId:" + userId);
    }

}
