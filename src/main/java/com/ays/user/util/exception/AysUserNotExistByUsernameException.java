package com.ays.user.util.exception;

import com.ays.common.util.exception.AysNotExistException;

public class AysUserNotExistByUsernameException extends AysNotExistException {

    public AysUserNotExistByUsernameException(String username) {
        super("USER NOT EXIST! username:{}" + username);
    }

}
