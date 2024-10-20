package org.ays.auth.util.exception;

import org.ays.common.exception.AysAuthException;

import java.io.Serial;

/**
 * Exception indicating that an admin registration application does not exist with the specified ID.
 * It extends {@link AysAuthException}, which provides the base exception for all authentication-related issues in the application.
 * Typically, this exception is thrown when an operation or query is performed on an admin register application
 * entity using an ID that does not correspond to an existing admin register application.
 */
public final class AdminRegistrationApplicationNotExistException extends AysAuthException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 8875043711494901525L;

    /**
     * Constructs a new {@link AdminRegistrationApplicationNotExistException} with the specified ID.
     *
     * @param id The ID of the admin registration application that does not exist.
     */
    public AdminRegistrationApplicationNotExistException(String id) {
        super("admin registration application does not exist! id:" + id);
    }

}
