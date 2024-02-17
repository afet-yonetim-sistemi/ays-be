package org.ays.user.util.exception;

import org.ays.common.util.exception.AysNotExistException;

import java.io.Serial;

/**
 * Exception to be thrown when a user with a given ID does not exist.
 */
public class AysUserNotExistByIdException extends AysNotExistException {

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
        super("USER NOT EXIST! id:" + id);
    }

}
