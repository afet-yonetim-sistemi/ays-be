package org.ays.auth.util.exception;

import org.ays.auth.model.enums.AysUserStatus;
import org.ays.common.util.exception.AysNotExistException;

import java.io.Serial;

/**
 * Exception thrown when a user is not in a passive state.
 * This exception extends {@link AysNotExistException}.
 */
public final class AysUserIsNotPassiveException extends AysNotExistException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = 3508025652421021710L;

    /**
     * Constructs a new {@link AysNotExistException} with the specified detail message.
     *
     * @param status the detail message.
     */
    public AysUserIsNotPassiveException(AysUserStatus status) {
        super("user status is not with " + status.toString().toLowerCase() + "!");
    }
}
