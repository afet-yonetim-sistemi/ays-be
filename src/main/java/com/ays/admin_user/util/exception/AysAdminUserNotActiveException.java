package com.ays.admin_user.util.exception;

import java.io.Serial;

/**
 * Exception indicating that an admin user is not active and cannot perform the requested action.
 * This exception is a subclass of RuntimeException, which means it is an unchecked exception and does not need to
 * be declared in a throws clause or caught explicitly.
 * Typically, this exception is thrown when an admin user tries to perform an action that is only allowed for active
 * users, but the user is currently inactive.
 */
public class AysAdminUserNotActiveException extends RuntimeException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = 1008678165854009528L;

    /**
     * Constructs a new AysAdminUserNotActiveException with the specified username.
     *
     * @param username The username of the inactive admin user.
     */
    public AysAdminUserNotActiveException(String username) {
        super("USER IS NOT ACTIVE! username:" + username);
    }

}
