package com.ays.admin_user.util.exception;

import com.ays.common.util.exception.AysNotExistException;

public class AysAdminUserNotExistByUsernameException extends AysNotExistException {

    public AysAdminUserNotExistByUsernameException(String username) {
        super("ADMIN USER NOT EXIST! username:" + username);
    }

}
