package org.ays.auth.exception;

import org.ays.common.exception.AysNotExistException;

import java.io.Serial;
import java.util.List;

/**
 * Exception to be thrown when one or more roles with the given IDs do not exist.
 */
public final class AysRolesNotExistException extends AysNotExistException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = 4418659919645763610L;

    /**
     * Constructs a new {@link AysRolesNotExistException} with the specified role IDs.
     *
     * @param ids the IDs of the roles that do not exist.
     */
    public AysRolesNotExistException(List<String> ids) {
        super("the following roles are not found! ids:" + ids);
    }

}
