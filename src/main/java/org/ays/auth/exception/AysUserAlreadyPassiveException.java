package org.ays.auth.exception;

import org.ays.common.exception.AysConflictException;

import java.io.Serial;

/**
 * Exception thrown when a user is already in a passive state.
 */
public final class AysUserAlreadyPassiveException extends AysConflictException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 2484662602911824448L;

    /**
     * Constructs a new {@link AysUserAlreadyPassiveException} with the specified detail message.
     */
    public AysUserAlreadyPassiveException() {
        super("user is already passive!");
    }

}
