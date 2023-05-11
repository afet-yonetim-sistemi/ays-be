package com.ays.common.util.exception;

import java.io.Serial;

/**
 * Class for exceptions that occur during an unexpected argument execution in the AYS system.
 */
public class AysUnexpectedArgumentException extends RuntimeException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -7275175844050733904L;

    /**
     * Constructs a new {@code AysUnexpectedArgumentException} with the specified unexpected object.
     *
     * @param object the unexpected object.
     */
    public AysUnexpectedArgumentException(final Object object) {
        super("Unexpected argument: " + object.toString() + " of type: " + object.getClass().getName());
    }

}
