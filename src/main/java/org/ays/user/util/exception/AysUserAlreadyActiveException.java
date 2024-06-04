package org.ays.user.util.exception;

import org.ays.common.util.exception.AysAlreadyException;

import java.io.Serial;

/**
 * Exception thrown when a user is active and attempting to perform an action that requires an active user.
 */
public class AysUserAlreadyActiveException extends AysAlreadyException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = -9064818847163094755L;

    /**
     * Constructs a new {@code AysUserAlreadyDeletedException} with the specified id.
     *
     * @param id the id of the active user
     */
    public AysUserAlreadyActiveException(String id) {
        super("user is already active! id:" + id);
    }

}
