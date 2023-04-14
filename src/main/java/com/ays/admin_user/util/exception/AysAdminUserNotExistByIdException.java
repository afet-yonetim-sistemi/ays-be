package com.ays.admin_user.util.exception;

import com.ays.common.util.exception.AysNotExistException;

public class AysAdminUserNotExistByIdException extends AysNotExistException {

    public AysAdminUserNotExistByIdException(String id) {
        super("ADMIN USER NOT EXIST! id:" + id);
    }

}
