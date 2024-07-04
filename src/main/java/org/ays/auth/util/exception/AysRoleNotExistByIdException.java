package org.ays.auth.util.exception;

import org.ays.common.util.exception.AysNotExistException;

import java.io.Serial;

/**
 * Exception to be thrown when a role with a given ID does not exist.
 */
public final class AysRoleNotExistByIdException extends AysNotExistException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = -1647644049969376455L;

    /**
     * Constructs a new {@link AysRoleNotExistByIdException} with the specified role ID.
     *
     * @param id the ID of the role that does not exist
     */
    public AysRoleNotExistByIdException(String id) {
        super("role does not exist! id:" + id);
    }

}
