package org.ays.auth.exception;

import org.ays.common.exception.AysConflictException;

import java.io.Serial;

/**
 * Exception thrown when a user does not in an active state.
 */
public final class AysUserNotActiveException extends AysConflictException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 3508025652421021710L;

    /**
     * Constructs a new {@link AysUserNotActiveException} with the specified detail message.
     */
    public AysUserNotActiveException() {
        super("user is not active!");
    }

}
