package org.ays.user.util.exception;

import org.ays.common.util.exception.AysNotExistException;

import java.io.Serial;
import java.util.Set;

/**
 * Exception to be thrown when a permission with a given ID does not exist.
 */
public class AysPermissionNotExistException extends AysNotExistException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = 1022212670473308130L;

    /**
     * Constructs a new AysPermissionNotExistException with the specified permission IDs.
     *
     * @param notFoundIds the IDs of the permissions don't exist
     */
    public AysPermissionNotExistException(Set<String> ids) {
        super("THE FOLLOWING PERMISSIONS WERE NOT FOUND! permissionIds:" + ids);
    }

}
