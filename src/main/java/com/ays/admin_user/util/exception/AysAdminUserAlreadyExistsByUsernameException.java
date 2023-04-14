package com.ays.admin_user.util.exception;

import com.ays.common.util.exception.AysAlreadyException;

/**
 * Exception class defining fired when a admin user already exists in the database.
 */
public class AysAdminUserAlreadyExistsByUsernameException extends AysAlreadyException {

    public AysAdminUserAlreadyExistsByUsernameException(String username) {
        super("ADMIN USER ALREADY EXIST! username:" + username);
    }

}
