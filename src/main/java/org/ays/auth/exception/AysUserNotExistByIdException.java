package org.ays.auth.exception;

import org.ays.common.exception.AysNotExistException;

import java.io.Serial;

/**
 * Exception to be thrown when a user with a given ID does not exist.
 */
public final class AysUserNotExistByIdException extends AysNotExistException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = 7236202847233201524L;

    /**
     * Constructs a new AysUserNotExistByIdException with the specified user ID.
     *
     * @param id the ID of the user that does not exist
     */
    public AysUserNotExistByIdException(String id) {
        super("user not exist! id:" + id);
    }

}
