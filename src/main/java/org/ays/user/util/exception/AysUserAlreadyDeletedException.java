package org.ays.user.util.exception;

import org.ays.common.util.exception.AysAlreadyException;

import java.io.Serial;

/**
 * Exception thrown when a user is deleted and attempting to perform an action that requires a deleted user.
 */
public class AysUserAlreadyDeletedException extends AysAlreadyException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = 2187171739927065907L;

    /**
     * Constructs a new {@code AysUserAlreadyDeletedException} with the specified id.
     *
     * @param id the id of the deleted user
     */
    public AysUserAlreadyDeletedException(String id) {
        super("user is already deleted! id:" + id);
    }

}
