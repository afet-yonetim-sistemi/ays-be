package org.ays.auth.util.exception;

import org.ays.common.util.exception.AysNotExistException;

import java.io.Serial;

/**
 * Exception to be thrown when a user has an invalid status.
 */
public final class AysUserIsNotActiveOrPassiveException extends AysNotExistException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 2989379366947338524L;

    /**
     * Constructs a new {@link AysUserIsNotActiveOrPassiveException} with the specified user id.
     *
     * @param id the id of the user with the invalid status.
     */
    public AysUserIsNotActiveOrPassiveException(String id) {
        super("user status is not active or passive! id:" + id);
    }

}
