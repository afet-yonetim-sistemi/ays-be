package org.ays.common.util.exception;

import java.io.Serial;

/**
 * This exception is thrown when an operation cannot be performed on an entity that is not active.
 */
public abstract class AysNotActiveException extends RuntimeException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -382050466231287818L;

    /**
     * Constructs a new AysNotActiveException with the specified error message.
     *
     * @param message the error message.
     */
    protected AysNotActiveException(final String message) {
        super(message);
    }

}
