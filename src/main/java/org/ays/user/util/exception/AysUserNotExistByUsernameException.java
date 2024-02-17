package org.ays.user.util.exception;

import org.ays.common.util.exception.AysNotExistException;

import java.io.Serial;

/**
 * An exception thrown when attempting to perform an action on a user that is not active.
 */
public class AysUserNotExistByUsernameException extends AysNotExistException {

    /**
     * Unique serial version ID.
     */
    @Serial
    private static final long serialVersionUID = 5558109482647165290L;

    /**
     * Constructs a new {@code AysUserNotActiveException} with the specified username.
     *
     * @param username the username of the inactive user.
     */
    public AysUserNotExistByUsernameException(String username) {
        super("USER NOT EXIST! username:{}" + username);
    }

}
