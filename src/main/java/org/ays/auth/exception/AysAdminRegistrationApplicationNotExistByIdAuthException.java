package org.ays.auth.exception;

import org.ays.common.exception.AysAuthException;

import java.io.Serial;

/**
 * Exception indicating that an admin registration application does not exist with the specified ID.
 * It extends {@link AysAuthException}, which provides the base exception for all authentication-related issues in the application.
 * Typically, this exception is thrown when an operation or query is performed on an admin register application
 * entity using an ID that does not correspond to an existing admin register application.
 */
public final class AysAdminRegistrationApplicationNotExistByIdAuthException extends AysAuthException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -6284753696808049193L;

    /**
     * Constructs a new {@link AysAdminRegistrationApplicationNotExistByIdAuthException} with the specified ID.
     *
     * @param id The ID of the admin registration application that does not exist.
     */
    public AysAdminRegistrationApplicationNotExistByIdAuthException(String id) {
        super("admin registration application does not exist! id:" + id);
    }

}
