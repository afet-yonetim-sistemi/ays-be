package org.ays.auth.exception;

import org.ays.common.exception.AysConflictException;

import java.io.Serial;

/**
 * Exception thrown when a user is deleted and attempting to perform an action that requires a deleted user.
 */
public final class AysUserAlreadyDeletedException extends AysConflictException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = 2187171739927065907L;

    /**
     * Constructs a new {@link AysUserAlreadyDeletedException} with the specified id.
     *
     * @param id the id of the deleted user
     */
    public AysUserAlreadyDeletedException(String id) {
        super("user is already deleted! id:" + id);
    }

}
