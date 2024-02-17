package org.ays.admin_user.util.exception;

import org.ays.common.util.exception.AysNotExistException;

import java.io.Serial;

/**
 * Exception indicating that an admin user does not exist with the specified username.
 * This exception is a subclass of AysNotExistException, which is typically used to indicate that an entity or
 * resource does not exist.
 * Typically, this exception is thrown when an operation or query is performed on an admin user entity using a
 * username that does not correspond to an existing admin user.
 */
public class AysAdminUserNotExistByUsernameException extends AysNotExistException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -3596142609379071521L;

    /**
     * Constructs a new AysAdminUserNotExistByUsernameException with the specified username.
     *
     * @param username The username of the admin user that does not exist.
     */
    public AysAdminUserNotExistByUsernameException(String username) {
        super("ADMIN USER NOT EXIST! username:" + username);
    }

}
