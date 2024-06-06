package org.ays.common.util.exception;

import java.io.Serial;

/**
 * A base class for exceptions that occur due to invalid entity status.
 */
public abstract class AysInvalidStatusException extends RuntimeException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 7261195193622257525L;

    /**
     * Constructs a new {@code AysInvalidStatusException} with the specified detail message.
     *
     * @param message the detail message.
     */
    protected AysInvalidStatusException(final String message) {
        super(message);
    }

}
