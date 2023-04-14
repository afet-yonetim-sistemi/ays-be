package com.ays.user.util.exception;

import com.ays.common.util.exception.AysNotExistException;

public class AysUserNotExistByIdException extends AysNotExistException {

    public AysUserNotExistByIdException(String id) {
        super("USER NOT EXIST! id:{}" + id);
    }

}
