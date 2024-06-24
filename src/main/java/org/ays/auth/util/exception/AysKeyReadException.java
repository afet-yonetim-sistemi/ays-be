package org.ays.auth.util.exception;

import java.io.Serial;

/**
 * Exception indicating that a key could not be read.
 * This exception is a subclass of {@link RuntimeException}, which is typically used to indicate that an unexpected error
 * has occurred.
 * Typically, this exception is thrown when a key is attempted to be read, but an error occurs during the process,
 * resulting in the key being unable to be read or accessed.
 * This exception includes the original exception that caused the failure to read the key, which can be accessed
 * using the getCause() method.
 */
public final class AysKeyReadException extends RuntimeException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 3273349702435810049L;

    /**
     * Constructs a new KeyReadException with the specified exception.
     *
     * @param exception The original exception that caused the failure to read the key.
     */
    public AysKeyReadException(Exception exception) {
        super("key could not be read!", exception);
    }

}
