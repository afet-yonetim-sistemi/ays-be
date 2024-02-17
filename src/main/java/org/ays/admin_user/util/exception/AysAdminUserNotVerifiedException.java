package org.ays.admin_user.util.exception;

import java.io.Serial;

/**
 * Exception indicating that an admin user is not verified.
 * This exception is a subclass of RuntimeException, which is typically used to indicate that an unexpected error
 * has occurred.
 * Typically, this exception is thrown when an operation or query is performed on an admin user entity using a
 * username that corresponds to an existing admin user, but the user has not been verified or authenticated.
 */
public class AysAdminUserNotVerifiedException extends RuntimeException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 7664416170459803790L;

    /**
     * Constructs a new AysAdminUserNotVerifiedException with the specified username.
     *
     * @param username The username of the admin user that is not verified.
     */
    public AysAdminUserNotVerifiedException(String username) {
        super("ADMIN USER IS NOT VERIFIED! username:" + username);
    }

}
