package org.ays.auth.exception;

import org.ays.common.exception.AysNotExistException;

import java.io.Serial;

/**
 * Exception indicating that an admin registration application does not exist with the specified ID.
 * This exception is a subclass of {@link AysNotExistException}, which is typically used to indicate that an entity or
 * resource does not exist.
 * Typically, this exception is thrown when an operation or query is performed on an admin register application
 * entity using an ID that does not correspond to an existing admin register application.
 */
public final class AysAdminRegistrationApplicationNotExistByIdException extends AysNotExistException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 8416712253227498925L;

    /**
     * Constructs a new {@link AysAdminRegistrationApplicationNotExistByIdException} with the specified ID.
     *
     * @param id The ID of the admin registration application that does not exist.
     */
    public AysAdminRegistrationApplicationNotExistByIdException(String id) {
        super("admin registration application not exist! id:" + id);
    }

}
