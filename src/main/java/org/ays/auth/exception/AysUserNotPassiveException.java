package org.ays.auth.exception;

import java.io.Serial;
import org.ays.common.exception.AysConflictException;

/**
 Exception thrown when a user does not in a passive state.
 * This exception extends {@link AysConflictException}.
 */
public final class AysUserNotPassiveException extends AysConflictException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = 3508025652421021710L;

    /**
     * Constructs a new {@link AysConflictException} with the specified detail message.
     *
     */
    public AysUserNotPassiveException() {
        super("user is already active!");
    }
}
