package org.ays.auth.util.exception;

import org.ays.common.util.exception.AysNotExistException;

import java.io.Serial;

/**
 * Exception to be thrown when a user with a given id does not exist.
 */
public final class AysUserNotExistException extends AysNotExistException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -5085235136115857968L;

    /**
     * Constructs a new {@link AysUserNotExistException} with the specified user id.
     *
     * @param id of the user that that does not exist.
     */
    public AysUserNotExistException(String id) {
        super("user does not exist! id:" + id);
    }

}
