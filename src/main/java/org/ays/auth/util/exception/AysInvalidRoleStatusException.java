package org.ays.auth.util.exception;

import org.ays.auth.model.enums.AysRoleStatus;
import org.ays.common.util.exception.AysNotExistException;

import java.io.Serial;

/**
 * Exception to be thrown when a role with a given id does not exist.
 */
public final class AysInvalidRoleStatusException extends AysNotExistException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -933312733826008379L;

    /**
     * Constructs a new {@link AysInvalidRoleStatusException} with the specified role name.
     *
     * @param status the name of the role that that does not exist.
     */
    public AysInvalidRoleStatusException(AysRoleStatus status) {
        super("role status is not " + status.toString().toLowerCase() + "!");
    }

}
