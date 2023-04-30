package com.ays.user.util.exception;

import java.io.Serial;

/**
 * Exception thrown when a user is not active and attempting to perform an action that requires an active user.
 */
public class AysUserNotActiveException extends RuntimeException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = -6280453284388968774L;

    /**
     * Constructs a new {@code AysUserNotActiveException} with the specified username.
     *
     * @param username the username of the inactive user
     */
    public AysUserNotActiveException(String username) {
        super("USER IS NOT ACTIVE! username:" + username);
    }

}
