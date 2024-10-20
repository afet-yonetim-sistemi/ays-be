package org.ays.common.exception;

import java.io.Serial;

/**
 * Base exception class for cases where an entity or resource is already present.
 * Typically, used for operations that try to create or add
 * something that already exists.
 */
public abstract class AysAlreadyException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -7941152772581443440L;

    /**
     * Constructs a new AysAlreadyException with the specified detail message.
     *
     * @param message the detail message.
     */
    protected AysAlreadyException(final String message) {
        super(message);
    }

}
