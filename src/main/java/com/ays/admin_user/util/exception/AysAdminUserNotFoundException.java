package com.ays.admin_user.util.exception;

import java.io.Serial;

/**
 * Exception indicating that an admin user is not found.
 * This exception is a subclass of RuntimeException, which is typically used to indicate that an unexpected error
 * has occurred.
 * Typically, this exception is thrown when an operation or query is performed on an admin user entity using a
 * username that corresponds to an existing admin user, but the user has not been verified or authenticated.
 */
public class AysAdminUserNotFoundException extends RuntimeException {


    @Serial
    private static final long serialVersionUID = -1495105765096738606L;

    /**
     * Constructs a new AysAdminUserNotFoundException with the specified username.
     *
     * @param username The username of the admin user that is not found.
     */
    public AysAdminUserNotFoundException(String username) {
        super("ADMIN USER IS NOT FOUND! username:" + username);
    }

}
