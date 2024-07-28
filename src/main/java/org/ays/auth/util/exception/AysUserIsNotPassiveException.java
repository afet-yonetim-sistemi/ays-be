package org.ays.auth.util.exception;

import org.ays.auth.model.enums.AysUserStatus;
import org.ays.common.util.exception.AysNotExistException;

import java.io.Serial;

/**
 *
 */

public final class AysUserIsNotPassiveException extends AysNotExistException {
    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = -3913212809970441837L;

    /**
     * Constructs a new {@link AysNotExistException} with the specified detail message.
     *
     * @param status the detail message.
     */
    public AysUserIsNotPassiveException(AysUserStatus status) {
        super("user status is not with " + status.toString().toLowerCase() + "!");
    }
}
