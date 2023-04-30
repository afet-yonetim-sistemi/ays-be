package com.ays.role.exception;

import com.ays.common.util.exception.AysNotExistException;

import java.io.Serial;

/**
 * Exception to be thrown when a requested role does not exist.
 */
public class AysRoleNotExistException extends AysNotExistException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = 6116668401680664562L;

    /**
     * Constructor to create a new AysRoleNotExistException instance with the provided message.
     *
     * @param message the detail message.
     */
    public AysRoleNotExistException(String message) {
        super(message);
    }

}
