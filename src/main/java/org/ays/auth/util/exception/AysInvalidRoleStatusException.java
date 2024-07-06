package org.ays.auth.util.exception;

import org.ays.auth.model.enums.AysRoleStatus;
import org.ays.common.util.exception.AysNotExistException;

import java.io.Serial;

/**
 * Exception to be thrown when a role's status is invalid for the requested operation.
 */
public final class AysInvalidRoleStatusException extends AysNotExistException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -933312733826008379L;

    /**
     * Constructs a new {@link AysInvalidRoleStatusException} with the specified role status.
     *
     * @param status the invalid status of the role..
     */
    public AysInvalidRoleStatusException(AysRoleStatus status) {
        super("role status is not " + status.toString().toLowerCase() + "!");
    }

}
