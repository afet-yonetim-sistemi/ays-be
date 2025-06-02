package org.ays.auth.exception;

import org.ays.common.exception.AysConflictException;

import java.io.Serial;

/**
 * Exception thrown when a user is not in a passive state.
 * This exception extends {@link AysConflictException}.
 */
public final class AysUserNotPassiveException extends AysConflictException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = 159529363134095650L;

    /**
     * Constructs a new {@link AysUserNotPassiveException} with the specified detail message and given userId.
     *
     * @param userId the id of the user that is not passive.
     */
    public AysUserNotPassiveException(final String userId) {
        super("user is not passive! userId:" + userId);
    }

}
