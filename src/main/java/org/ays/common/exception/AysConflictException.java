package org.ays.common.exception;

import java.io.Serial;

/**
 * Represents an exception that occurs when a conflict is detected in the AYS (Are You Sure) system.
 * This is an abstract base class for specific conflict-related exceptions.
 * <p>
 * This exception extends {@link RuntimeException}, making it an unchecked exception.
 * Subclasses should provide more specific details about the nature of the conflict.
 * </p>
 */
public abstract class AysConflictException extends RuntimeException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -6212586832670981639L;

    /**
     * Constructs a new {@link AysConflictException} with the specified detail message.
     *
     * @param message the detail message.
     */
    protected AysConflictException(final String message) {
        super(message);
    }

}
