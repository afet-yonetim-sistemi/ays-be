package org.ays.common.exception;

import java.io.Serial;

/**
 * Abstract base exception to be thrown when a forbidden or unauthorized action is attempted in the application.
 * This exception should be extended by more specific exceptions that represent different types of forbidden actions.
 */
public abstract class AysForbiddenException extends RuntimeException {

    /**
     * Unique identifier for serialization, ensuring compatibility during the serialization and deserialization process.
     */
    @Serial
    private static final long serialVersionUID = -3981774157306269765L;

    /**
     * Constructs a new {@link AysForbiddenException} with the specified detail message.
     * This constructor is intended to be called by subclasses to provide an appropriate error message.
     *
     * @param message the detail message providing more context about the exception.
     */
    protected AysForbiddenException(final String message) {
        super(message);
    }

}
