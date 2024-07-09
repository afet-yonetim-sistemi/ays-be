package org.ays.auth.util.exception;

import org.ays.common.util.exception.AysNotExistException;

import java.io.Serial;

/**
 * Exception to be thrown when a user has an invalid status.
 */
public final class AysInvalidUserStatusException extends AysNotExistException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 8511628526768842769L;

    /**
     * Constructs a new {@link AysInvalidUserStatusException} with the specified user id.
     *
     * @param id the id of the user with the invalid status.
     */
    public AysInvalidUserStatusException(String id) {
        super("User with ID " + id + " has an invalid status: user is not active or passive.");
    }

}
