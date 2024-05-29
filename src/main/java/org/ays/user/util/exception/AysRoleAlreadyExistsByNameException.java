package org.ays.user.util.exception;

import org.ays.common.util.exception.AysAlreadyException;

import java.io.Serial;


/**
 * Exception to be thrown when a role with a given name already exists.
 */
public class AysRoleAlreadyExistsByNameException extends AysAlreadyException {

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
        super("ROLE ALREADY EXIST! name:" + name);
    }

}
