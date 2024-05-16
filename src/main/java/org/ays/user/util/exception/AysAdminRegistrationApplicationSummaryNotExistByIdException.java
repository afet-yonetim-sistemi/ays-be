package org.ays.user.util.exception;

import org.ays.common.util.exception.AysAuthException;

import java.io.Serial;

/**
 * Exception indicating that an admin registration application does not exist with the specified ID.
 * This exception is a subclass of AysAuthException, which is typically used to indicate that an entity or
 * resource does not exist.
 * Typically, this exception is thrown when an operation or query is performed on an admin register application
 * entity using an ID that does not correspond to an existing admin register application.
 */
public class AysAdminRegistrationApplicationSummaryNotExistByIdException extends AysAuthException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 1262328240991745084L;

    /**
     * Constructs a new {@link AysAdminRegistrationApplicationSummaryNotExistByIdException} with the specified ID.
     *
     * @param id The ID of the admin registration application that does not exist.
     */
    public AysAdminRegistrationApplicationSummaryNotExistByIdException(String id) {
        super("ADMIN REGISTRATION APPLICATION NOT EXIST! id:" + id);
    }

}
