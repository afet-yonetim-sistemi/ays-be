package org.ays.auth.exception;

import org.ays.common.exception.AysConflictException;

import java.io.Serial;

/**
 * Exception thrown when a role is deleted and attempting to perform an action that requires a deleted role.
 */
public class AysRoleAssignedToUserException extends AysConflictException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = 7298457193066796371L;

    /**
     * Constructs a new {@link AysRoleAssignedToUserException} with the specified detail message.
     *
     * @param id the detail message.
     */
    public AysRoleAssignedToUserException(String id) {
        super("the role is assigned to one or more users id:" + id);
    }

}
