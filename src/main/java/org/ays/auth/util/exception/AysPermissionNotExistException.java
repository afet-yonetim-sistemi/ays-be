package org.ays.auth.util.exception;

import org.ays.common.util.exception.AysNotExistException;

import java.io.Serial;
import java.util.List;

/**
 * Exception to be thrown when a permission with a given ID does not exist.
 */
public final class AysPermissionNotExistException extends AysNotExistException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = 1022212670473308130L;

    /**
     * Constructs a new AysPermissionNotExistException with the specified permission IDs.
     *
     * @param ids the IDs of the permissions don't exist
     */
    public AysPermissionNotExistException(List<String> ids) {
        super("the following permissions were not found! permissionIds:" + ids);
    }

}
