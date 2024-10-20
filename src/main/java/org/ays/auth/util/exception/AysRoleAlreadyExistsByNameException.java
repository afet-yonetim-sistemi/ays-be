package org.ays.auth.util.exception;

import org.ays.common.exception.AysConflictException;

import java.io.Serial;

/**
 * Exception to be thrown when a role with a given name already exists.
 */
public final class AysRoleAlreadyExistsByNameException extends AysConflictException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -8192753469109678221L;

    /**
     * Constructs a new AysRoleAlreadyExistsByNameException with the specified role name.
     *
     * @param name the name of the role that already exists
     */
    public AysRoleAlreadyExistsByNameException(String name) {
        super("role already exist! name:" + name);
    }

}
