package com.ays.common.util.exception;

import java.io.Serial;

/**
 * A base class for exceptions that occur when an entity is expected to exist, but is not found.
 */
public abstract class AysNotExistException extends RuntimeException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -3046469863777315718L;

    /**
     * Constructs a new {@code AysNotExistException} with the specified detail message.
     *
     * @param message the detail message.
     */
    protected AysNotExistException(final String message) {
        super(message);
    }

}
