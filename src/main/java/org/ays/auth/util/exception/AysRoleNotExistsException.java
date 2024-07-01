package org.ays.auth.util.exception;

import org.ays.common.util.exception.AysNotExistException;

import java.io.Serial;

/**
 * Exception to be thrown when a role with a given id does not exist.
 */
public final class AysRoleNotExistsException extends AysNotExistException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 8425205814091657359L;

    /**
     * Constructs a new AysRoleNotExistsException with the specified role name.
     *
     * @param id the name of the role that that does not exist.
     */
    public AysRoleNotExistsException(String id) {
        super("role does not exist! id:" + id);
    }

}