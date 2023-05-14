package com.ays.admin_user.util.exception;

import com.ays.common.util.exception.AysAlreadyException;

import java.io.Serial;

/**
 * Exception indicating that an admin user already exists with the specified username.
 * This exception is a subclass of AysAlreadyException.
 */
public class AysAdminUserAlreadyExistsByUsernameException extends AysAlreadyException {

    /**
     * Unique identifier for serialization.
     */
    @Serial
    private static final long serialVersionUID = -3186201226632256358L;

    /**
     * Constructs a new AysAdminUserAlreadyExistsByUsernameException with the specified username.
     *
     * @param username The username of the admin user that already exists.
     */
    public AysAdminUserAlreadyExistsByUsernameException(String username) {
        super("ADMIN USER ALREADY EXIST! username:" + username);
    }

}
