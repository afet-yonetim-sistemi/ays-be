package org.ays.auth.exception;

import java.io.Serial;
import org.ays.common.exception.AysConflictException;

/**
 * Exception thrown when a user is already in active state.
 * This exception extends {@link AysConflictException}.
 */
public class AysUserAlreadyActiveException extends AysConflictException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = -5085484906788378345L;

    /**
     * Constructs a new {@link AysUserAlreadyActiveException} with the specified detail message.
     */
    public AysUserAlreadyActiveException() {
        super("user is already active!");
    }
}
