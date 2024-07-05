package org.ays.auth.util.exception;

import org.ays.common.util.exception.AysAlreadyException;

import java.io.Serial;

/**
 * Exception thrown when a role is deleted and attempting to perform an action that requires a deleted role.
 */
public class AysRoleAssignedToUserException extends AysAlreadyException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = 7298457193066796371L;

    /**
     * Constructs a new AysAlreadyException with the specified detail message.
     *
     * @param id the detail message.
     */
    public AysRoleAssignedToUserException(String id) {
        super("the role is assigned to one or more users id:" + id);
    }

}
