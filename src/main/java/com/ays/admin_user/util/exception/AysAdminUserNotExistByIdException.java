package com.ays.admin_user.util.exception;

import com.ays.common.util.exception.AysNotExistException;

import java.io.Serial;

/**
 * Exception indicating that an admin user does not exist with the specified ID.
 * This exception is a subclass of AysNotExistException, which is typically used to indicate that an entity or
 * resource does not exist.
 * Typically, this exception is thrown when an operation or query is performed on an admin user entity using an ID
 * that does not correspond to an existing admin user.
 */
public class AysAdminUserNotExistByIdException extends AysNotExistException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -4832610600125442954L;

    /**
     * Constructs a new AysAdminUserNotExistByIdException with the specified ID.
     *
     * @param id The ID of the admin user that does not exist.
     */
    public AysAdminUserNotExistByIdException(String id) {
        super("ADMIN USER NOT EXIST! id:" + id);
    }

}
