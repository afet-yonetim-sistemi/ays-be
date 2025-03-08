package org.ays.auth.exception;

import java.io.Serial;
import org.ays.common.exception.AysConflictException;

/**
 * Exception thrown when a user is not in a passive state.
 * This exception extends {@link AysConflictException}.
 */
public final class AysUserNotPassiveException extends AysConflictException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = 2243994749508397732L;

    /**
     * Constructs a new {@link AysUserNotPassiveException} with the specified detail message.
     *
     */
    public AysUserNotPassiveException() {
        super("user is not passive!");
    }
}
