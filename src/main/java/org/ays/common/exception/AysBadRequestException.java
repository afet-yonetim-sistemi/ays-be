package org.ays.common.exception;

import java.io.Serial;

/**
 * Exception to be thrown when a bad request is made to the application.
 */
public abstract class AysBadRequestException extends RuntimeException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 6127207732429966431L;

    /**
     * Constructs a new {@link AysBadRequestException} with the specified detail message.
     *
     * @param message the detail message.
     */
    protected AysBadRequestException(final String message) {
        super(message);
    }

}
