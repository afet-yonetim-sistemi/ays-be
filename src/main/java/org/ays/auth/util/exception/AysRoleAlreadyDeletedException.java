package org.ays.auth.util.exception;

import org.ays.common.exception.AysAlreadyException;

import java.io.Serial;

/**
 * Exception thrown when a role is deleted and attempting to perform an action that requires a deleted role.
 */
public final class AysRoleAlreadyDeletedException extends AysAlreadyException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = -7631303999314005771L;

    /**
     * Constructs a new {@link AysRoleAlreadyDeletedException} with the specified detail message.
     *
     * @param id the id of the deleted user
     */
    public AysRoleAlreadyDeletedException(String id) {
        super("role is already deleted! id:" + id);
    }

}
